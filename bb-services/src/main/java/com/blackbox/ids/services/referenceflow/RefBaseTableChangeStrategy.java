package com.blackbox.ids.services.referenceflow;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.ids.IDSDao;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowRuleDTO;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Status;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceCategoryType;
import com.blackbox.ids.core.model.reference.SourceReference;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.blackbox.ids.core.model.referenceflow.SubjectMatterLink;
import com.blackbox.ids.core.repository.impl.referenceflow.SubjectMatterLinkRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSReferenceFlowRepository;

@Service
public class RefBaseTableChangeStrategy implements ReferenceRuleStrategy {

	@Autowired
	private IDSDao idsDao;

	@Autowired
	private IDSReferenceFlowRepository<IDSReferenceFlow> iDSReferenceFlowRepository;

	@Autowired
	private SubjectMatterLinkRepository<SubjectMatterLink> subjectMatterLinkRepository;

	@Autowired
	private IDSReferenceFlowService idsReferenceFlowService;

	private static final String DOC_TYPE_IDS = "IDS";
	private static final String DOC_TYPE_SPEC = "SPEC";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.referenceflow.ReferenceRuleStrategy#executeRule(com.blackbox.ids.core.model.reference.
	 * ReferenceBaseData, com.blackbox.ids.services.referenceflow.ReferenceChangeDesciption)
	 */
	@Override
	@Transactional
	public void executeRule(final ReferenceBaseData referenceBaseData,
			final ReferenceChangeDesciption referenceChangeDesciption) throws ApplicationException {
		if (ReferenceCategoryType.SELF_CITATION.equals(referenceBaseData.getReferenceCatogory())) {
			System.out.println(referenceChangeDesciption.toString());
			handleSelfCitation(referenceBaseData, referenceChangeDesciption);
		} else if (ReferenceCategoryType.PTO_CORRESPONDENCE.equals(referenceBaseData.getReferenceCatogory())) {

			switch (referenceBaseData.getCorrespondence().getDocumentCode().getCode()) {

			case DOC_TYPE_IDS:
				handlePTOCorrespondenceForDocTypeIDS(referenceBaseData, referenceChangeDesciption);
				break;
			case DOC_TYPE_SPEC:
				handlePTOCorrespondenceForDocTypeSPEC(referenceBaseData, referenceChangeDesciption);
				break;
			default:
				handlePTOCorrespondenceForDocTypeOther(referenceBaseData, referenceChangeDesciption);
				break;
			}
		}
	}

	/*****************
	 * Methods to handle PTO_CORRESPONDENCE reference record (DOC TYPE : IDS ) START
	 ********************/

	private void handlePTOCorrespondenceForDocTypeIDS(ReferenceBaseData referenceBaseData,
			ReferenceChangeDesciption referenceChangeDesciption) {

		switch (referenceChangeDesciption) {

		case REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_YES:
			this.ptoCorrespondenceDocTypeIDSRefRecordCreatedNewRefFlowFlagYes(referenceBaseData);
			break;
		case REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_NO:
			this.ptoCorrespondenceDocTypeIDSRefRecordCreatedDuplicateRefFlowFlagNo(referenceBaseData);
			break;
		case REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_YES:
			this.ptoCorrespondenceDocTypeIDSRefRecordCreatedDuplicateRefFlowFlagYes(referenceBaseData);
			break;
		case REF_RECORD_DROPPED:
			this.ptoCorrespondenceDocTypeIDSRefRecordDropped(referenceBaseData);
			break;
		case REF_RECORD_FLOW_FLAG_TO_CHANGE_YES_TO_NO:
			this.ptoCorrespondenceDocTypeIDSRefRecordChangeFlowFlagYesToNo(referenceBaseData);
			break;
		case REF_RECORD_FLOW_FLAG_TO_CHANGE_NO_TO_YES:
			this.ptoCorrespondenceDocTypeIDSRefRecordChangeFlowFlagNoToYes(referenceBaseData);
			break;
		default:
			break;
		}
	}

	/**
	 * Create new reference flow records
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceDocTypeIDSRefRecordCreatedNewRefFlowFlagYes(ReferenceBaseData referenceBaseData) {
		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);

		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			IDSReferenceFlow refFlow = this.createRefFlowRecord(referenceBaseData, refFlowRuleDto);
			iDSReferenceFlowRepository.save(refFlow);
		}
		// also create a self flow with status as CITED-PENDING_1449
		iDSReferenceFlowRepository.save(ptoCorrespondenceCreateSelfRefFlowForDoctTypeIDS(referenceBaseData));
	}

	/**
	 * Handles reference flow creation/update with Duplicate ref with flow flag No for Doc Type IDS
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceDocTypeIDSRefRecordCreatedDuplicateRefFlowFlagNo(
			ReferenceBaseData referenceBaseData) {

		boolean duplicate = idsReferenceFlowService.duplicateCheck(referenceBaseData,
				referenceBaseData.getApplication().getId());

		// if duplicate found then update existing flow
		if (duplicate) {
			IDSReferenceFlow refFlow = updateExistingRefFlowForDocTypeIDS(referenceBaseData);
			// save after making above changes
			iDSReferenceFlowRepository.save(refFlow);
		}

	}

	/**
	 * Apply business rule while updating the existing ref flow
	 * 
	 * @param referenceBaseData
	 * @return
	 */
	private IDSReferenceFlow updateExistingRefFlowForDocTypeIDS(ReferenceBaseData referenceBaseData) {
		IDSReferenceFlow refFlow = idsReferenceFlowService.findDuplicateRefFlow(referenceBaseData,
				referenceBaseData.getApplication().getId());

		switch (refFlow.getReferenceFlowStatus()) {

		case CITED:

			switch (refFlow.getReferenceFlowSubStatus()) {

			case PENDING_USPTO_FILING:
				refFlow.setReferenceFlowSubStatus(ReferenceFlowSubStatus.PENDING_1449);
				refFlow.setFilingDate(referenceBaseData.getMailingDate());
				refFlow.setExternalFinalIDSId(idsDao.generateExternalIDSId());
				break;

			case PENDING_1449:
				if (refFlow.getExternalFinalIDSId() == null || refFlow.getInternalFinalIDSId() == null) {
					refFlow.setFilingDate(referenceBaseData.getMailingDate());
					refFlow.setExternalFinalIDSId(idsDao.generateExternalIDSId());
				}
				break;

			case ACCEPTED:
			case REJECTED:
				// do nothing
				break;

			default:
				break;
			}
			break;

		case UNCITED:
			refFlow.setReferenceFlowStatus(ReferenceFlowStatus.CITED);
			refFlow.setReferenceFlowSubStatus(ReferenceFlowSubStatus.PENDING_1449);
			refFlow.setFilingDate(referenceBaseData.getMailingDate());
			refFlow.setExternalFinalIDSId(idsDao.generateExternalIDSId());
			break;

		default:
			break;

		}
		return refFlow;
	}

	/**
	 * Handles reference flow creation/update with Duplicate ref with flow flag Yes for Doc Type IDS
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceDocTypeIDSRefRecordCreatedDuplicateRefFlowFlagYes(
			ReferenceBaseData referenceBaseData) {

		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);
		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			boolean duplicate = idsReferenceFlowService.duplicateCheck(referenceBaseData,
					refFlowRuleDto.getTargetApplication().getDbId());

			// if no duplicate found then create a new flow record
			if (!duplicate) {
				IDSReferenceFlow refFlow = this.createRefFlowRecord(referenceBaseData, refFlowRuleDto);
				iDSReferenceFlowRepository.save(refFlow);
			} else {
				// update existing ref flow as per business rule
				IDSReferenceFlow refFlow = updateExistingRefFlowForDocTypeIDS(referenceBaseData);
				// save after making above changes
				iDSReferenceFlowRepository.save(refFlow);
			}
		}
		// also create a self flow with status as CITED-PENDING_1449
		iDSReferenceFlowRepository.save(ptoCorrespondenceCreateSelfRefFlowForDoctTypeIDS(referenceBaseData));
	}

	/**
	 * Handles reference flow creation/update/drop with change in ref with flow flag Yes to No for Doc Type IDS
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceDocTypeIDSRefRecordChangeFlowFlagYesToNo(ReferenceBaseData referenceBaseData) {
		iDSReferenceFlowRepository.dropReferenceFlow(referenceBaseData, referenceBaseData.getApplication().getId(),
				ReferenceFlowStatus.UNCITED);
	}

	/**
	 * Handles reference flow creation/update/drop with change in ref with flow flag No to Yes for Doc Type IDS
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceDocTypeIDSRefRecordChangeFlowFlagNoToYes(ReferenceBaseData referenceBaseData) {
		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);
		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			boolean duplicate = idsReferenceFlowService.duplicateCheck(referenceBaseData,
					refFlowRuleDto.getTargetApplication().getDbId());

			// if no duplicate found then create a new flow record
			if (!duplicate) {
				IDSReferenceFlow refFlow = this.createRefFlowRecord(referenceBaseData, refFlowRuleDto);
				iDSReferenceFlowRepository.save(refFlow);
			}
		}
		// also create a self flow with status as CITED-PENDING_1449
		iDSReferenceFlowRepository.save(ptoCorrespondenceCreateSelfRefFlowForDoctTypeIDS(referenceBaseData));
	}

	/**
	 * Handles reference flow drop when ref is dropped for Doc Type IDS
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceDocTypeIDSRefRecordDropped(ReferenceBaseData referenceBaseData) {
		// drop existing records from reference flow
		this.dropAllReferences(referenceBaseData);

	}

	/**
	 * Returns a self flow ref record entity (source and target application is same) : DocType : IDS
	 * 
	 * @param referenceBaseData
	 * @return
	 */
	private IDSReferenceFlow ptoCorrespondenceCreateSelfRefFlowForDoctTypeIDS(ReferenceBaseData referenceBaseData) {

		IDSReferenceFlow refFlow = new IDSReferenceFlow();
		refFlow.setReferenceBaseData(referenceBaseData);
		refFlow.setCorrespondence(referenceBaseData.getCorrespondence());
		// status is examiner cited for PTO_CORRESPONDENCE
		refFlow.setReferenceFlowStatus(ReferenceFlowStatus.CITED);
		refFlow.setReferenceFlowSubStatus(ReferenceFlowSubStatus.PENDING_1449);
		refFlow.setOriginalReferenceFlowSubStatus(ReferenceFlowSubStatus.PENDING_1449);
		refFlow.setTargetApplication(referenceBaseData.getApplication());
		refFlow.setSourceApplication(referenceBaseData.getApplication());
		refFlow.setFilingDate(referenceBaseData.getCorrespondence().getMailingDate());
		refFlow.setExternalFinalIDSId(idsDao.generateExternalIDSId());
		refFlow.setDoNotFile(false);

		return refFlow;
	}

	/***************** Methods to handle PTO_CORRESPONDENCE reference record (DOC TYPE : IDS ) END ********************/

	/*****************
	 * Methods to handle PTO_CORRESPONDENCE reference record (DOC TYPE : SPEC ) START
	 ********************/

	private void handlePTOCorrespondenceForDocTypeSPEC(ReferenceBaseData referenceBaseData,
			ReferenceChangeDesciption referenceChangeDesciption) {

		switch (referenceChangeDesciption) {

		case REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_YES:
			this.ptoCorrespondenceDocTypeSPECRefRecordCreatedNewRefFlowFlagYes(referenceBaseData);
			break;
		case REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_NO:
			// do nothing
			break;
		case REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_YES:
			this.ptoCorrespondenceDocTypeSPECRefRecordCreatedDuplicateRefFlowFlagYes(referenceBaseData);
			break;
		case REF_RECORD_DROPPED:
			this.ptoCorrespondenceDocTypeSPECRefRecordDropped(referenceBaseData);
			break;
		case REF_RECORD_FLOW_FLAG_TO_CHANGE_YES_TO_NO:
			this.ptoCorrespondenceDocTypeSPECRefRecordChangeFlowFlagYesToNo(referenceBaseData);
			break;
		case REF_RECORD_FLOW_FLAG_TO_CHANGE_NO_TO_YES:
			this.ptoCorrespondenceDocTypeSPECRefRecordChangeFlowFlagNoToYes(referenceBaseData);
			break;
		default:
			break;
		}
	}

	/**
	 * Handles reference flow creation when new ref created with flow flag Yes for Doc Type SPEC
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceDocTypeSPECRefRecordCreatedNewRefFlowFlagYes(ReferenceBaseData referenceBaseData) {

		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);
		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			boolean duplicate = idsReferenceFlowService.duplicateCheck(referenceBaseData,
					refFlowRuleDto.getTargetApplication().getDbId());
			// if no duplicate found then create new ref flow
			if (!duplicate) {
				IDSReferenceFlow refFlow = this.createRefFlowRecord(referenceBaseData, refFlowRuleDto);
				iDSReferenceFlowRepository.save(refFlow);
			}
		}
		// also create a self flow with status as UNCITED-NULL
		iDSReferenceFlowRepository.save(ptoCorrespondenceCreateSelfRefFlowForDoctTypeSPEC(referenceBaseData));
	}

	/**
	 * Handles reference flow creation/update/drop with new ref created with flow flag Yes for Doc Type SPEC
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceDocTypeSPECRefRecordCreatedDuplicateRefFlowFlagYes(
			ReferenceBaseData referenceBaseData) {

		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);

		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			boolean duplicate = idsReferenceFlowService.duplicateCheck(referenceBaseData,
					refFlowRuleDto.getTargetApplication().getDbId());

			// if no duplicate found then create a new flow record
			if (!duplicate) {
				IDSReferenceFlow refFlow = this.createRefFlowRecord(referenceBaseData, refFlowRuleDto);
				iDSReferenceFlowRepository.save(refFlow);
			}
		}
		// also create a self flow with status as UNCITED-NULL
		iDSReferenceFlowRepository.save(ptoCorrespondenceCreateSelfRefFlowForDoctTypeSPEC(referenceBaseData));
	}

	/**
	 * Drop all reference flows when reference is dropped from base table DocType:SPEC
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceDocTypeSPECRefRecordDropped(ReferenceBaseData referenceBaseData) {
		// drop all existing references
		this.dropAllReferences(referenceBaseData);
	}

	/**
	 * Handles reference flow creation/update/drop with change in ref with flow flag Yes to No for Doc Type SPEC
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceDocTypeSPECRefRecordChangeFlowFlagYesToNo(ReferenceBaseData referenceBaseData) {
		// drop existing records from reference flow
		iDSReferenceFlowRepository.dropReferenceFlow(referenceBaseData, referenceBaseData.getApplication().getId(),
				ReferenceFlowStatus.UNCITED);
	}

	/**
	 * Handles reference flow creation/update/drop with change in ref with flow flag No to Yes for Doc Type SPEC
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceDocTypeSPECRefRecordChangeFlowFlagNoToYes(ReferenceBaseData referenceBaseData) {
		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);

		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			boolean duplicate = idsReferenceFlowService.duplicateCheck(referenceBaseData,
					refFlowRuleDto.getTargetApplication().getDbId());

			// if no duplicate found then create a new flow record
			if (!duplicate) {
				IDSReferenceFlow refFlow = this.createRefFlowRecord(referenceBaseData, refFlowRuleDto);
				iDSReferenceFlowRepository.save(refFlow);
			}
		}
		// also create a self flow with status as UNCITED-NULL
		iDSReferenceFlowRepository.save(ptoCorrespondenceCreateSelfRefFlowForDoctTypeSPEC(referenceBaseData));

	}

	/**
	 * Returns a self flow ref record entity (source and target application is same) : DocType : SPEC
	 * 
	 * @param referenceBaseData
	 * @return
	 */
	private IDSReferenceFlow ptoCorrespondenceCreateSelfRefFlowForDoctTypeSPEC(ReferenceBaseData referenceBaseData) {

		IDSReferenceFlow refFlow = new IDSReferenceFlow();
		refFlow.setReferenceBaseData(referenceBaseData);
		refFlow.setCorrespondence(referenceBaseData.getCorrespondence());
		refFlow.setReferenceFlowStatus(ReferenceFlowStatus.UNCITED);
		refFlow.setReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
		refFlow.setOriginalReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
		refFlow.setTargetApplication(referenceBaseData.getApplication());
		refFlow.setSourceApplication(referenceBaseData.getApplication());
		refFlow.setDoNotFile(false);
		return refFlow;
	}

	/*****************
	 * Methods to handle PTO_CORRESPONDENCE reference record (DOC TYPE : SPEC ) END
	 ********************/

	/*****************
	 * Methods to handle PTO_CORRESPONDENCE reference record (DOC TYPE : OTHER ) START
	 ********************/

	/**
	 * Manage reference flows when reference category is PTO_CORRESPONDENCE
	 * 
	 * @param referenceChangeDesciption
	 */
	protected void handlePTOCorrespondenceForDocTypeOther(ReferenceBaseData referenceBaseData,
			ReferenceChangeDesciption referenceChangeDesciption) {

		switch (referenceChangeDesciption) {

		case REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_YES:
			this.ptoCorrespondenceRefRecordCreatedNewRefFlowFlagYes(referenceBaseData);
			break;
		case REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_NO:
			this.ptoCorrespondenceRefRecordCreatedDuplicateRefFlowFlagNo(referenceBaseData);
			break;
		case REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_YES:
			this.ptoCorrespondenceRefRecordCreatedDuplicateRefFlowFlagYes(referenceBaseData);
			break;
		case REF_RECORD_DROPPED:
			this.ptoCorrespondenceRefRecordDropped(referenceBaseData);
			break;
		case REF_RECORD_FLOW_FLAG_TO_CHANGE_YES_TO_NO:
			this.ptoCorrespondenceRefRecordChangeFlowFlagYesToNo(referenceBaseData);
			break;
		case REF_RECORD_FLOW_FLAG_TO_CHANGE_NO_TO_YES:
			this.ptoCorrespondenceRefRecordChangeFlowFlagNoToYes(referenceBaseData);
			break;
		default:
			break;
		}

	}

	/**
	 * Create new reference flow records
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceRefRecordCreatedNewRefFlowFlagYes(ReferenceBaseData referenceBaseData) {
		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);

		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			IDSReferenceFlow refFlow = this.createRefFlowRecord(referenceBaseData, refFlowRuleDto);
			iDSReferenceFlowRepository.save(refFlow);
		}
		// also create a self flow with status as examiner cited
		iDSReferenceFlowRepository.save(ptoCorrespondenceCreateSelfRefFlow(referenceBaseData));

	}

	/**
	 * Drop reference flow record with UNCITED status then create a new reference flow record
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceRefRecordCreatedDuplicateRefFlowFlagNo(ReferenceBaseData referenceBaseData) {

		// drop existing reference from ref flow
		boolean flag = iDSReferenceFlowRepository.dropReferenceFlow(referenceBaseData,
				referenceBaseData.getApplication().getId(), ReferenceFlowStatus.UNCITED);

		// if record is found and dropped then create new reference flow record with self flow
		if (flag) {
			iDSReferenceFlowRepository.save(ptoCorrespondenceCreateSelfRefFlow(referenceBaseData));
		}
	}

	/**
	 * Creates flow records based on flow rules and duplicate check
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceRefRecordCreatedDuplicateRefFlowFlagYes(ReferenceBaseData referenceBaseData) {

		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);

		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			boolean duplicate = idsReferenceFlowService.duplicateCheck(referenceBaseData,
					refFlowRuleDto.getTargetApplication().getDbId());

			// if no duplicate found then create a new flow record
			if (!duplicate) {
				IDSReferenceFlow refFlow = this.createRefFlowRecord(referenceBaseData, refFlowRuleDto);
				iDSReferenceFlowRepository.save(refFlow);
			}
		}
		// also create a self flow with status as examiner cited
		iDSReferenceFlowRepository.save(ptoCorrespondenceCreateSelfRefFlow(referenceBaseData));
	}

	/**
	 * Handles reference flow drop when ref is dropped
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceRefRecordDropped(ReferenceBaseData referenceBaseData) {
		// drop all existing references
		this.dropAllReferences(referenceBaseData);
	}

	/**
	 * Drop reference flow records where status is UNCITED
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceRefRecordChangeFlowFlagYesToNo(ReferenceBaseData referenceBaseData) {
		// drop existing records from reference flow
		iDSReferenceFlowRepository.dropReferenceFlow(referenceBaseData, referenceBaseData.getApplication().getId(),
				ReferenceFlowStatus.UNCITED);
	}

	/**
	 * 
	 * @param referenceBaseData
	 */
	private void ptoCorrespondenceRefRecordChangeFlowFlagNoToYes(ReferenceBaseData referenceBaseData) {

		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);

		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			boolean duplicate = idsReferenceFlowService.duplicateCheck(referenceBaseData,
					refFlowRuleDto.getTargetApplication().getDbId());

			// if no duplicate found then create a new flow record
			if (!duplicate) {
				IDSReferenceFlow refFlow = this.createRefFlowRecord(referenceBaseData, refFlowRuleDto);
				iDSReferenceFlowRepository.save(refFlow);
			}
		}
		// also create a self flow with status as examiner cited
		iDSReferenceFlowRepository.save(ptoCorrespondenceCreateSelfRefFlow(referenceBaseData));
	}

	/**
	 * Returns a self flow ref record entity (source and target application is same)
	 * 
	 * @param referenceBaseData
	 * @return
	 */
	private IDSReferenceFlow ptoCorrespondenceCreateSelfRefFlow(ReferenceBaseData referenceBaseData) {

		IDSReferenceFlow refFlow = new IDSReferenceFlow();
		refFlow.setReferenceBaseData(referenceBaseData);
		refFlow.setCorrespondence(referenceBaseData.getCorrespondence());
		// status is examiner cited for PTO_CORRESPONDENCE
		refFlow.setReferenceFlowStatus(ReferenceFlowStatus.EXAMINER_CITED);
		refFlow.setReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
		refFlow.setOriginalReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
		refFlow.setTargetApplication(referenceBaseData.getApplication());
		refFlow.setSourceApplication(referenceBaseData.getApplication());
		refFlow.setDoNotFile(false);

		return refFlow;
	}

	/***************** Methods to handle PTO_CORRESPONDENCE reference record END ********************/

	/***************** Methods to handle Selfcitation reference record Start ********************/

	/**
	 * Manage reference flows when reference category is SELFCITATION
	 * 
	 * @param referenceChangeDesciption
	 */
	private void handleSelfCitation(ReferenceBaseData referenceBaseData,
			ReferenceChangeDesciption referenceChangeDesciption) {

		switch (referenceChangeDesciption) {

		case REF_RECORD_CREATED_NEW_REF_FLOW_FLAG_YES:
			this.selfCitationRefRecordCreatedNewRefFlowFlagYes(referenceBaseData);
			break;
		case REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_NO:
			// Do nothing
			break;
		case REF_RECORD_CREATED_DUPLICATE_REF_FLOW_FLAG_YES:
			// do nothing - not a possible scenario
			break;
		case REF_RECORD_DROPPED:
			this.selfCitationRefRecordDropped(referenceBaseData);
			break;
		case REF_RECORD_FLOW_FLAG_TO_CHANGE_YES_TO_NO:
			this.selfCitationRefRecordChangeFlowFlagYesToNo(referenceBaseData);
			break;
		case REF_RECORD_FLOW_FLAG_TO_CHANGE_NO_TO_YES:
			this.selfCitationRefRecordChangeFlowFlagNoToYes(referenceBaseData);
			break;
		default:
			break;
		}
	}

	/**
	 * Executed when new reference created with flow flag Yes
	 * 
	 * @param referenceBaseData
	 */
	protected void selfCitationRefRecordCreatedNewRefFlowFlagYes(ReferenceBaseData referenceBaseData) {

		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);

		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			IDSReferenceFlow refFlow = this.createRefFlowRecord(referenceBaseData, refFlowRuleDto);
			iDSReferenceFlowRepository.save(refFlow);
		}

		// also create a self flow with status as examiner cited
		iDSReferenceFlowRepository.save(selfcitationCreateSelfRefFlow(referenceBaseData));

	}

	/**
	 * Drop all reference flows when reference is dropped from base table
	 * 
	 * @param referenceBaseData
	 */
	protected void selfCitationRefRecordDropped(ReferenceBaseData referenceBaseData) {
		// drop all existing references
		this.dropAllReferences(referenceBaseData);
	}

	/**
	 * Executed when reference record flow flag changes from Yes to No
	 * 
	 * @param referenceBaseData
	 */
	protected void selfCitationRefRecordChangeFlowFlagYesToNo(ReferenceBaseData referenceBaseData) {
		iDSReferenceFlowRepository.dropReferenceFlow(referenceBaseData, referenceBaseData.getApplication().getId(),
				ReferenceFlowStatus.UNCITED);
	}

	/**
	 * Executed when reference record flow flag changes from No to Yes
	 * 
	 * @param referenceBaseData
	 */
	protected void selfCitationRefRecordChangeFlowFlagNoToYes(ReferenceBaseData referenceBaseData) {

		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);
		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			boolean duplicate = idsReferenceFlowService.duplicateCheck(referenceBaseData,
					refFlowRuleDto.getTargetApplication().getDbId());

			// if no duplicate found then create a new flow record
			if (!duplicate) {
				IDSReferenceFlow refFlow = createRefFlowRecord(referenceBaseData, refFlowRuleDto);
				iDSReferenceFlowRepository.save(refFlow);
			}
		}
	}

	/**
	 * Returns a self flow ref record entity (source and target application is same)
	 * 
	 * @param referenceBaseData
	 * @return
	 */
	private IDSReferenceFlow selfcitationCreateSelfRefFlow(ReferenceBaseData referenceBaseData) {

		IDSReferenceFlow refFlow = new IDSReferenceFlow();
		refFlow.setReferenceBaseData(referenceBaseData);
		refFlow.setCorrespondence(referenceBaseData.getCorrespondence());
		// status is UNCITED for self citation
		refFlow.setReferenceFlowStatus(ReferenceFlowStatus.UNCITED);
		refFlow.setReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
		refFlow.setOriginalReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
		refFlow.setTargetApplication(referenceBaseData.getApplication());
		refFlow.setSourceApplication(referenceBaseData.getApplication());
		refFlow.setDoNotFile(false);
		return refFlow;
	}

	/***************** Methods to handle Selfcitation reference record END ********************/

	/**
	 * Generates complete list flow rules
	 * 
	 * @param referenceBaseData
	 * @return list
	 */
	protected List<ReferenceFlowRuleDTO> generateFlowRules(ReferenceBaseData referenceBaseData) {

		Long sourceApplicationId = referenceBaseData.getApplication().getId();
		String sourceFamilyId = referenceBaseData.getApplication().getFamilyId();
		List<ReferenceFlowRuleDTO> flowFamilyRuleList = iDSReferenceFlowRepository.generateFamilyLinks(sourceFamilyId,
				sourceApplicationId, null);

		List<SubjectMatterLink> smlList = subjectMatterLinkRepository.findBySourceFamilyIdAndStatusNot(sourceFamilyId,
				Status.ACTIVE);

		List<ReferenceFlowRuleDTO> completeFlowRuleList = new ArrayList<>();
		completeFlowRuleList.addAll(flowFamilyRuleList);
		for (SubjectMatterLink sml : smlList) {
			List<ReferenceFlowRuleDTO> flowSmlRuleList = iDSReferenceFlowRepository
					.generateSubjectMatterLinks(sml.getTargetFamilyId(), sourceApplicationId, null);
			completeFlowRuleList.addAll(flowSmlRuleList);
		}

		return completeFlowRuleList;
	}

	/**
	 * Returns a new flow record entity
	 * 
	 * @param referenceBaseData
	 * @param refFlowRuleDto
	 * @return
	 */
	private IDSReferenceFlow createRefFlowRecord(ReferenceBaseData referenceBaseData,
			ReferenceFlowRuleDTO refFlowRuleDto) {
		IDSReferenceFlow refFlow = new IDSReferenceFlow();
		refFlow.setReferenceBaseData(referenceBaseData);
		refFlow.setCorrespondence(referenceBaseData.getCorrespondence());
		refFlow.setReferenceFlowStatus(ReferenceFlowStatus.UNCITED);
		refFlow.setReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);
		refFlow.setOriginalReferenceFlowSubStatus(ReferenceFlowSubStatus.NULL);

		ApplicationBase targetApplication = new ApplicationBase();
		targetApplication.setId(refFlowRuleDto.getTargetApplication().getDbId());
		refFlow.setTargetApplication(targetApplication);

		refFlow.setSourceApplication(referenceBaseData.getApplication());
		return refFlow;
	}

	/**
	 * @param referenceBaseData
	 */
	private void dropAllReferences(ReferenceBaseData referenceBaseData) {
		List<ReferenceFlowStatus> statusList = new ArrayList<>();
		statusList.add(ReferenceFlowStatus.EXAMINER_CITED);
		statusList.add(ReferenceFlowStatus.UNCITED);
		iDSReferenceFlowRepository.dropReferenceFlowWithMultiplleStatus(referenceBaseData,
				referenceBaseData.getApplication().getId(), statusList);
	}

	@Override
	public void executeRule(ReferenceBaseData referenceBaseData, SourceReference sourceReference,
			ReferenceChangeDesciption referenceChangeDesciption) throws ApplicationException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void executeRule(ApplicationBase applicationBase, ReferenceChangeDesciption referenceChangeDesciption) {
		throw new UnsupportedOperationException();
	}
}
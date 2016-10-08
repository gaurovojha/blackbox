package com.blackbox.ids.services.referenceflow;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.ids.IDSDao;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowRuleDTO;
import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Status;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.SourceReference;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.IDSSourceReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.IDSSourceReferenceFlowFilingInfo;
import com.blackbox.ids.core.model.referenceflow.SourceReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.SourceReferenceFlowSubStatus;
import com.blackbox.ids.core.model.referenceflow.SubjectMatterLink;
import com.blackbox.ids.core.repository.impl.referenceflow.SubjectMatterLinkRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSReferenceFlowRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSSourceReferenceFlowFilingInfoRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSSourceReferenceFlowRepository;

@Service
public class SourceRefTableChangeStrategy implements ReferenceRuleStrategy {

	@Autowired
	private IDSDao idsDao;

	@Autowired
	private IDSReferenceFlowRepository<IDSReferenceFlow> iDSReferenceFlowRepository;

	@Autowired
	private IDSSourceReferenceFlowRepository<IDSSourceReferenceFlow> iDSSourceReferenceFlowRepository;

	@Autowired
	private IDSSourceReferenceFlowService iDSSourceReferenceFlowService;

	@Autowired
	private SubjectMatterLinkRepository<SubjectMatterLink> subjectMatterLinkRepository;

	@Autowired
	private IDSSourceReferenceFlowFilingInfoRepository<IDSSourceReferenceFlowFilingInfo> iDSSourceReferenceFlowFilingInfoRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.referenceflow.ReferenceRuleStrategy#executeRule(com.blackbox.ids.core.model.reference.
	 * SourceReference, com.blackbox.ids.services.referenceflow.ReferenceChangeDesciption)
	 */
	@Override
	@Transactional
	public void executeRule(final ReferenceBaseData referenceBaseData, final SourceReference sourceReference,
			final ReferenceChangeDesciption referenceChangeDesciption) throws ApplicationException {

		switch (referenceChangeDesciption) {

		case SOURCE_REF_RECORD_CREATED:
			this.sourceRefRecordCreated(referenceBaseData, sourceReference);
			break;
		case SOURCE_REF_RECORD_DROPPED:
			this.sourceRefRecordDropped(referenceBaseData, sourceReference);
			break;
		// this case when Document Type is IDS and NPL is found in SourceReference
		case SOURCE_REF_NPL_DUPLICATE_EXIST:
			this.sourceRefRecordDuplicateNplExist(referenceBaseData, sourceReference);
			break;
		default:
			break;
		}
	}

	/**
	 * Methods to handle source reference record creation
	 * 
	 * @param referenceBaseData
	 * @param sourceReference
	 */
	private void sourceRefRecordCreated(ReferenceBaseData referenceBaseData, SourceReference sourceReference) {
		List<ReferenceFlowRuleDTO> completeFlowRuleList = this.generateFlowRules(referenceBaseData);

		for (ReferenceFlowRuleDTO refFlowRuleDto : completeFlowRuleList) {
			boolean duplicate = iDSSourceReferenceFlowService.duplicateCheck(sourceReference,
					refFlowRuleDto.getTargetApplication().getDbId());

			// if no duplicate found then create a new flow record
			if (!duplicate) {
				IDSSourceReferenceFlow sourceRefFlow = this.createSourceRefFlowRecord(referenceBaseData,
						sourceReference, refFlowRuleDto);
				iDSSourceReferenceFlowRepository.save(sourceRefFlow);
			}
		}
		// also create a self flow with status as examiner cited
		iDSSourceReferenceFlowRepository
				.save(createSourceReferenceSelfSourceRefFlow(referenceBaseData, sourceReference));

	}

	/**
	 * Methods to handle source reference record drop
	 * 
	 * @param referenceBaseData
	 * @param sourceReference
	 */
	private void sourceRefRecordDropped(final ReferenceBaseData referenceBaseData,
			final SourceReference sourceReference) {
		iDSSourceReferenceFlowService.dropSourceReferenceFlow(sourceReference.getId(),
				referenceBaseData.getApplication().getId());
	}

	/**
	 * Methods to handle source reference record in case of DocType : IDS and NPL exists in source reference table
	 * 
	 * @param referenceBaseData
	 * @param sourceReference
	 */
	private void sourceRefRecordDuplicateNplExist(ReferenceBaseData referenceBaseData,
			SourceReference sourceReference) {
		IDSSourceReferenceFlow sourceRefFlow = iDSSourceReferenceFlowRepository
				.findBySourceReferenceAndTargetApplication(sourceReference, referenceBaseData.getApplication().getId());

		List<IDSSourceReferenceFlowFilingInfo> sourceRefFlowFilingInfo = iDSSourceReferenceFlowFilingInfoRepository
				.findBySourceReferenceFlowAndSourceReferenceFlowStatus(sourceRefFlow, SourceReferenceFlowStatus.CITED);

		// business rule
		IDSSourceReferenceFlowFilingInfo filingInfoWithEarliestFileGenerationDate = this
				.findFilingInfoByEarliestFileGenerationDate(sourceRefFlowFilingInfo);

		switch (filingInfoWithEarliestFileGenerationDate.getSourceReferenceFlowSubStatus()) {

		case PENDING_1449:
			if (filingInfoWithEarliestFileGenerationDate.getInternalFinalIDSId() == null
					|| filingInfoWithEarliestFileGenerationDate.getExternalFinalIDSId() == null) {
				filingInfoWithEarliestFileGenerationDate
						.setFilingDate(sourceReference.getCorrespondence().getMailingDate());
				filingInfoWithEarliestFileGenerationDate.setExternalFinalIDSId(idsDao.generateExternalIDSId());
				break;
			}
		case ACCEPTED:
		case REJECTED:
			// do nothing
			break;

		case PENDING_USPTO_FILING:
			filingInfoWithEarliestFileGenerationDate
					.setSourceReferenceFlowSubStatus(SourceReferenceFlowSubStatus.PENDING_1449);
			filingInfoWithEarliestFileGenerationDate
					.setFilingDate(sourceReference.getCorrespondence().getMailingDate());
			filingInfoWithEarliestFileGenerationDate.setExternalFinalIDSId(idsDao.generateExternalIDSId());
			break;
		default:
			break;
		}
		iDSSourceReferenceFlowFilingInfoRepository.save(filingInfoWithEarliestFileGenerationDate);
	}

	/**
	 * Find source ref flow filing info with earliest file generation date
	 * 
	 * @param sourceRefFlowFilingInfo
	 * @return
	 */
	private IDSSourceReferenceFlowFilingInfo findFilingInfoByEarliestFileGenerationDate(
			List<IDSSourceReferenceFlowFilingInfo> sourceRefFlowFilingInfo) {
		List<Long> tempIdsIdList = new ArrayList<>();
		for (IDSSourceReferenceFlowFilingInfo filingInfo : sourceRefFlowFilingInfo) {
			tempIdsIdList.add(filingInfo.getTempIDS().getId());
		}
		IDS ids = idsDao.getIDSWithEarliestFileGenerationDate(tempIdsIdList);
		return iDSSourceReferenceFlowFilingInfoRepository.findByTempIDS(ids);
	}

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
	 * Return source reference flow entity with self flow
	 * 
	 * @param referenceBaseData
	 * @param sourceReference
	 * @return
	 */
	private IDSSourceReferenceFlow createSourceReferenceSelfSourceRefFlow(ReferenceBaseData referenceBaseData,
			SourceReference sourceReference) {
		IDSSourceReferenceFlow sourceRefFlow = new IDSSourceReferenceFlow();
		sourceRefFlow.setSourceReference(sourceReference);
		sourceRefFlow.setCorrespondence(referenceBaseData.getCorrespondence());
		sourceRefFlow.setSourceReferenceFlowStatus(SourceReferenceFlowStatus.ACTIVE);
		sourceRefFlow.setTargetApplication(referenceBaseData.getApplication());
		sourceRefFlow.setSourceApplication(referenceBaseData.getApplication());
		return sourceRefFlow;
	}

	/**
	 * Returns a new source ref flow record entity
	 * 
	 * @param referenceBaseData
	 * @param refFlowRuleDto
	 * @return
	 */
	private IDSSourceReferenceFlow createSourceRefFlowRecord(ReferenceBaseData referenceBaseData,
			SourceReference sourceReference, ReferenceFlowRuleDTO refFlowRuleDto) {
		IDSSourceReferenceFlow sourceRefFlow = new IDSSourceReferenceFlow();
		sourceRefFlow.setSourceReference(sourceReference);
		sourceRefFlow.setCorrespondence(referenceBaseData.getCorrespondence());
		sourceRefFlow.setSourceReferenceFlowStatus(SourceReferenceFlowStatus.ACTIVE);
		ApplicationBase targetApplication = new ApplicationBase();
		targetApplication.setId(refFlowRuleDto.getTargetApplication().getDbId());
		sourceRefFlow.setTargetApplication(targetApplication);
		sourceRefFlow.setSourceApplication(referenceBaseData.getApplication());

		return sourceRefFlow;
	}

	@Override
	public void executeRule(ReferenceBaseData referenceBaseData, ReferenceChangeDesciption referenceChangeDesciption)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void executeRule(ApplicationBase applicationBase, ReferenceChangeDesciption referenceChangeDesciption) {
		throw new UnsupportedOperationException();
	}
}
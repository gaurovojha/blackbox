/**
 * 
 */
package com.blackbox.ids.services.impl.referenceflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dto.IDS.IDSReferenceRecordDTO;
import com.blackbox.ids.core.dto.IDS.IDSReferenceRecordStatusCountDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowDTO;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowRuleDTO;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Status;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceRuleType;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlowRuleExclusion;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.blackbox.ids.core.model.referenceflow.SubjectMatterLink;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.impl.referenceflow.IDSReferenceFlowRepositoryImpl;
import com.blackbox.ids.core.repository.impl.referenceflow.SubjectMatterLinkRepository;
import com.blackbox.ids.core.repository.reference.ReferenceBaseDataRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSReferenceFlowRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSReferenceFlowRuleExclusionRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.services.referenceflow.ReferenceFlowRuleService;
import com.mysema.query.types.Predicate;

/**
 * @author nagarro
 *
 */
@Service
public class ReferenceFlowRuleServiceImpl implements ReferenceFlowRuleService {

	private static final Logger LOGGER = Logger.getLogger(IDSReferenceFlowRepositoryImpl.class);

	@Autowired
	private IDSReferenceFlowRepository<IDSReferenceFlow> idsReferenceFlowRepository;

	@Autowired
	private SubjectMatterLinkRepository<SubjectMatterLink> subjectMatterLinkRepository;

	@Autowired
	private IDSReferenceFlowRuleExclusionRepository<IDSReferenceFlowRuleExclusion> idsReferenceFlowRuleExclusionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;

	/** The reference repository. */
	@Autowired
	private ReferenceBaseDataRepository<ReferenceBaseData> referenceBaseDataRepository;

	@Override
	@Transactional(readOnly = true)
	public Page<ReferenceFlowDTO> getAllRecords(final Predicate predicate, final Pageable pageable) {
		Page<ReferenceFlowDTO> page = idsReferenceFlowRepository.filterAllRecords(predicate, pageable);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ReferenceFlowRuleDTO> getAllNotifications(final Predicate predicate, final Pageable pageable) {
		return idsReferenceFlowRepository.filterAllNotifications(predicate, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ReferenceFlowRuleDTO> getFamilyLinks(final Predicate predicate, final Pageable pageable,
			final String familyId, final Long applicationId) {
		return idsReferenceFlowRepository.filterFamilyLinks(predicate, pageable, familyId, applicationId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ReferenceFlowRuleDTO> getSubjectMatterLinks(final Predicate predicate, final Pageable pageable,
			final String familyId, final Long applicationId) {
		return idsReferenceFlowRepository.filterSubjectMatterLinks(predicate, pageable, familyId, applicationId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MdmRecordDTO> getApplicationsByFamily(final Predicate predicate, final Pageable pageable) {
		return idsReferenceFlowRepository.findApplicationsByFamilyId(predicate, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getTargetFamilyIdList(final String sourceFamilyId) {
		List<SubjectMatterLink> smlList = subjectMatterLinkRepository.findBySourceFamilyIdAndStatusNot(sourceFamilyId,
				Status.DROPPED);
		List<String> destFamilyList = new ArrayList<>();
		for (SubjectMatterLink sml : smlList) {
			destFamilyList.add(sml.getTargetFamilyId());
		}
		return destFamilyList;

	}

	@Override
	@Transactional(readOnly = true)
	public Long getNotificationCount() {
		// TODO Auto-generated method stub
		return idsReferenceFlowRepository.fetchNotificationCount();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReferenceFlowRuleDTO> getReferenceFlowRuleExclusionList(final String sourceFamilyId) {

		LOGGER.debug("Fetch reference flow rule exclusion list");
		List<ReferenceFlowRuleDTO> completeExclusionList = new ArrayList<>();

		List<IDSReferenceFlowRuleExclusion> exclusions = idsReferenceFlowRuleExclusionRepository
				.findBySourceFamilyIdAndReferenceRuleTypeAndStatus(sourceFamilyId, ReferenceRuleType.F, Status.ACTIVE);

		for (IDSReferenceFlowRuleExclusion exclusion : exclusions) {

			// setting target application and other relevant details
			ReferenceFlowRuleDTO exclusionDTO = getReferenceFlowExclusionDTOFromEntity(exclusion);
			completeExclusionList.add(exclusionDTO);
		}
		return completeExclusionList;
	}

	/**
	 * 
	 * @param exclusion
	 * @return
	 */
	private ReferenceFlowRuleDTO getReferenceFlowExclusionDTOFromEntity(final IDSReferenceFlowRuleExclusion exclusion) {

		ReferenceFlowRuleDTO exclusionDTO = new ReferenceFlowRuleDTO();

		MdmRecordDTO sourceApp = new MdmRecordDTO();
		sourceApp.setDbId(exclusion.getSourceApplication().getId());
		sourceApp.setFamilyId(exclusion.getSourceApplication().getFamilyId());
		sourceApp.setApplicationNumber(exclusion.getSourceApplication().getApplicationNumber());
		sourceApp.setAttorneyDocket(exclusion.getSourceApplication().getAttorneyDocketNumber().getSegment());
		sourceApp.setJurisdiction(exclusion.getSourceApplication().getJurisdiction().getCode());
		exclusionDTO.setSourceApplication(sourceApp);

		MdmRecordDTO targetApp = new MdmRecordDTO();
		targetApp.setDbId(exclusion.getTargetApplication().getId());
		targetApp.setFamilyId(exclusion.getTargetApplication().getFamilyId());
		targetApp.setApplicationNumber(exclusion.getTargetApplication().getApplicationNumber());
		targetApp.setAttorneyDocket(exclusion.getTargetApplication().getAttorneyDocketNumber().getSegment());
		targetApp.setJurisdiction(exclusion.getTargetApplication().getJurisdiction().getCode());
		exclusionDTO.setTargetApplication(targetApp);

		exclusionDTO.setComments(exclusion.getComments());
		exclusionDTO.setStatus(exclusion.getStatus());

		String createBy = exclusion.getUpdatedByUser() != null
				? userRepository.getUserFullName(userRepository.getEmailId(exclusion.getCreatedByUser())) : null;
		String modifiedBy = exclusion.getUpdatedByUser() != null
				? userRepository.getUserFullName(userRepository.getEmailId(exclusion.getUpdatedByUser())) : null;

		exclusionDTO.setCreatedBy(createBy);
		exclusionDTO.setModifiedBy(modifiedBy);

		String createdDate = exclusion.getCreatedDate() != null
				? BlackboxDateUtil.dateToStr(exclusion.getCreatedDate().getTime(), TimestampFormat.MMMDDYYYY) : null;

		String modifiedDate = exclusion.getCreatedDate() != null
				? BlackboxDateUtil.dateToStr(exclusion.getUpdatedDate().getTime(), TimestampFormat.MMMDDYYYY) : null;

		exclusionDTO.setModifiedDate(modifiedDate);
		exclusionDTO.setCreatedDate(createdDate);

		return exclusionDTO;
	}

	@Override
	@Transactional
	public boolean updateReferenceFlowRuleExclusionStatus(final Long sourceAppId, final Long targetAppId,
			final String sourceFamilyId, final String targetFamilyId, final String comments) {

		boolean status = true;

		IDSReferenceFlowRuleExclusion exclusion = findExistingReferenceFlowExclusionRule(sourceAppId, targetAppId);
		if (exclusion != null) {
			updateStatus(exclusion, comments);
			idsReferenceFlowRuleExclusionRepository.save(exclusion);
		} else {
			createReferenceFlowExclusionRule(sourceAppId, targetAppId, sourceFamilyId, targetFamilyId, comments);
			status = false;
		}

		return status;
	}

	@Override
	@Transactional
	public void createReferenceFlowExclusionRule(final Long sourceAppId, final Long targetAppId,
			final String sourceFamilyId, final String targetFamilyId, final String comments) {

		LOGGER.debug("Create Rule for Family/SML");
		IDSReferenceFlowRuleExclusion exclusion = new IDSReferenceFlowRuleExclusion();
		ApplicationBase sourceAppBase = new ApplicationBase();
		sourceAppBase.setId(sourceAppId);

		ApplicationBase targetAppBase = new ApplicationBase();
		targetAppBase.setId(targetAppId);

		SubjectMatterLink sml = subjectMatterLinkRepository
				.findBySourceFamilyIdAndTargetFamilyIdAndStatus(sourceFamilyId, targetFamilyId, Status.ACTIVE);

		exclusion.setSourceApplication(sourceAppBase);
		exclusion.setTargetApplication(targetAppBase);
		exclusion.setComments(comments);
		exclusion.setStatus(Status.ACTIVE);
		exclusion.setSubjectMatterLink(sml);
		exclusion.setSourceFamilyId(sourceFamilyId);
		exclusion.setTargetFamilyId(targetFamilyId);
		exclusion.setReferenceRuleType(sml == null ? ReferenceRuleType.F : ReferenceRuleType.SML);
		idsReferenceFlowRuleExclusionRepository.save(exclusion);
	}

	/**
	 * @param sourceAppId
	 * @param targetAppId
	 * @return
	 */
	private IDSReferenceFlowRuleExclusion findExistingReferenceFlowExclusionRule(final Long sourceAppId,
			final Long targetAppId) {
		ApplicationBase sourceAppBase = new ApplicationBase();
		sourceAppBase.setId(sourceAppId);

		ApplicationBase targetAppBase = new ApplicationBase();
		targetAppBase.setId(targetAppId);

		IDSReferenceFlowRuleExclusion exclusion = idsReferenceFlowRuleExclusionRepository
				.findBySourceApplicationAndTargetApplication(sourceAppBase, targetAppBase);
		return exclusion;
	}

	/**
	 * 
	 * @param exclusion
	 */
	private void updateStatus(final IDSReferenceFlowRuleExclusion exclusion, final String comments) {

		if (exclusion.getStatus().equals(Status.ACTIVE)) {
			exclusion.setStatus(Status.INACTIVE);
		} else {
			exclusion.setStatus(Status.ACTIVE);
		}
		exclusion.setComments(comments);
	}

	@Override
	@Transactional
	public void deleteSML(final String sourceFamilyId, final String targetFamilyId) {
		subjectMatterLinkRepository.removeSML(sourceFamilyId, targetFamilyId, Status.DROPPED);
	}

	@Override
	@Transactional
	public boolean updateReferenceForReview(final Long referenceBaseDataId) {
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public ReferenceBaseDTO getReferencesForReview(final Long correspondenceId) {
		return null;
	}

	/**
	 * This method will create the exclusion rule for the families selected from the new rule page.
	 */
	@Override
	@Transactional
	public void createReferenceFlowSmlExclusionNewRule(final String sourceFamilyId, final String targetFamilyId,
			final Long sourceAppId, final Long targetAppId, final boolean isSourceLinkage,
			final boolean isTargetLinkage) {

		List<Long> sourceFamilyMemberList = applicationBaseDAO.findAllApplicationIdListOfFamily(sourceFamilyId,
				sourceAppId);
		List<Long> targetFamilyMemberList = applicationBaseDAO.findAllApplicationIdListOfFamily(targetFamilyId,
				targetAppId);

		SubjectMatterLink sourceToTargetSML = null;
		SubjectMatterLink targetToSourceSML = null;

		sourceToTargetSML = subjectMatterLinkRepository.findBySourceFamilyIdAndTargetFamilyIdAndStatus(sourceFamilyId,
				targetFamilyId, Status.ACTIVE);
		if (null == sourceToTargetSML || sourceToTargetSML.equals(null)) {
			sourceToTargetSML = new SubjectMatterLink();
			sourceToTargetSML.setSourceFamilyId(sourceFamilyId);
			sourceToTargetSML.setTargetFamilyId(targetFamilyId);
			sourceToTargetSML.setStatus(Status.ACTIVE);
		}

		targetToSourceSML = subjectMatterLinkRepository.findBySourceFamilyIdAndTargetFamilyIdAndStatus(targetFamilyId,
				sourceFamilyId, Status.ACTIVE);
		if (null == targetToSourceSML || targetToSourceSML.equals(null)) {
			targetToSourceSML = new SubjectMatterLink();
			targetToSourceSML.setSourceFamilyId(targetFamilyId);
			targetToSourceSML.setTargetFamilyId(sourceFamilyId);
			targetToSourceSML.setStatus(Status.ACTIVE);
		}

		if (isSourceLinkage && isTargetLinkage) {
			subjectMatterLinkRepository.save(sourceToTargetSML);
			subjectMatterLinkRepository.save(targetToSourceSML);
		} else if (isSourceLinkage && !isTargetLinkage) {
			this.createExclusionForSelectedFamilyWithAllOtherFamilyMembersOfTarget(sourceFamilyId, targetFamilyId,
					sourceAppId, targetFamilyMemberList, sourceFamilyMemberList, sourceToTargetSML, targetToSourceSML);
		} else if (!isSourceLinkage && isTargetLinkage) {
			this.createExclusionForOtherMembersOfSelectedSourceFamilyWithAllTargetFamilies(sourceFamilyId,
					targetFamilyId, targetAppId, targetFamilyMemberList, sourceFamilyMemberList, sourceToTargetSML,
					targetToSourceSML);
		} else {
			this.createExclusionForAllNonSelectedSourceFamilyMembersWithAllOtherTargetFamilyMembers(sourceFamilyId,
					targetFamilyId, targetAppId, targetFamilyMemberList, sourceFamilyMemberList, sourceToTargetSML,
					targetToSourceSML);
		}

	}

	/**
	 * This method will be called to create the rule between the other family members of the source family with the
	 * other members of the target family. This method will be called for the scenario when both the link are not
	 * selected.
	 * 
	 * In this method, first time it will check if there is any link available and have active status, than it will make
	 * it Inactive and again create the rule.
	 * 
	 * @param sourceFamilyId
	 * @param targetFamilyId
	 * @param targetAppId
	 * @param targetFamilyMemberList
	 * @param sourceFamilyMemberList
	 * @param sourceToTargetSML
	 * @param targetToSourceSML
	 */
	private void createExclusionForAllNonSelectedSourceFamilyMembersWithAllOtherTargetFamilyMembers(
			String sourceFamilyId, String targetFamilyId, Long targetAppId, List<Long> targetFamilyMemberList,
			List<Long> sourceFamilyMemberList, SubjectMatterLink sourceToTargetSML,
			SubjectMatterLink targetToSourceSML) {
		ApplicationBase sourceAppBase = new ApplicationBase();
		ApplicationBase targetAppBase = new ApplicationBase();

		subjectMatterLinkRepository.save(sourceToTargetSML);
		subjectMatterLinkRepository.save(targetToSourceSML);

		// Here we are first checking the available link and than inactivating it.
		for (Long sourceId : sourceFamilyMemberList) {
			for (Long targetId : targetFamilyMemberList) {
				IDSReferenceFlowRuleExclusion exclusion = findExistingReferenceFlowExclusionRule(sourceId, targetId);
				if (exclusion != null) {
					inactivateReferenceFlowRuleExclusionStatus(sourceId, targetId, exclusion);
				} else {
					exclusion = new IDSReferenceFlowRuleExclusion();
					this.saveExclusionData(sourceFamilyId, targetFamilyId, sourceToTargetSML, sourceAppBase,
							targetAppBase, sourceId, targetId, exclusion);
				}

				exclusion = findExistingReferenceFlowExclusionRule(targetId, sourceId);
				if (exclusion != null) {
					inactivateReferenceFlowRuleExclusionStatus(targetId, sourceId, exclusion);
				} else {
					exclusion = new IDSReferenceFlowRuleExclusion();
					this.saveExclusionData(targetFamilyId, sourceFamilyId, targetToSourceSML, sourceAppBase,
							targetAppBase, targetId, sourceId, exclusion);
				}

			}
		}
	}

	/**
	 * This method will be called to create the exclusion rule between the other family members of the source family
	 * with the target families. This method will be called for the scenario when the source link is unchecked and the
	 * target link is selected.
	 * 
	 * In this method, first time it will check if there is any link available and have active status, than it will make
	 * it Inactive and again create the rule.
	 * 
	 * @param sourceFamilyId
	 * @param targetFamilyId
	 * @param targetAppId
	 * @param targetFamilyMemberList
	 * @param sourceFamilyMemberList
	 * @param sourceToTargetSML
	 * @param targetToSourceSML
	 */
	private void createExclusionForOtherMembersOfSelectedSourceFamilyWithAllTargetFamilies(String sourceFamilyId,
			String targetFamilyId, Long targetAppId, List<Long> targetFamilyMemberList,
			List<Long> sourceFamilyMemberList, SubjectMatterLink sourceToTargetSML,
			SubjectMatterLink targetToSourceSML) {
		// TODO Auto-generated method stub
		List<Long> targetFamilyList = new ArrayList<>(targetFamilyMemberList);
		targetFamilyList.add(targetAppId);

		ApplicationBase sourceAppBase = new ApplicationBase();
		ApplicationBase targetAppBase = new ApplicationBase();

		subjectMatterLinkRepository.save(sourceToTargetSML);
		subjectMatterLinkRepository.save(targetToSourceSML);

		// Here we are first checking the available link and than inactivating it.
		for (Long sourceId : sourceFamilyMemberList) {
			for (Long targetId : targetFamilyList) {
				IDSReferenceFlowRuleExclusion exclusion = findExistingReferenceFlowExclusionRule(sourceId, targetId);
				if (exclusion != null) {
					inactivateReferenceFlowRuleExclusionStatus(sourceId, targetId, exclusion);
				} else {
					exclusion = new IDSReferenceFlowRuleExclusion();
					this.saveExclusionData(sourceFamilyId, targetFamilyId, sourceToTargetSML, sourceAppBase,
							targetAppBase, sourceId, targetId, exclusion);
				}

				exclusion = findExistingReferenceFlowExclusionRule(targetId, sourceId);
				if (exclusion != null) {
					inactivateReferenceFlowRuleExclusionStatus(targetId, sourceId, exclusion);
				} else {
					exclusion = new IDSReferenceFlowRuleExclusion();
					this.saveExclusionData(targetFamilyId, sourceFamilyId, targetToSourceSML, sourceAppBase,
							targetAppBase, targetId, sourceId, exclusion);
				}
			}
		}
	}

	/**
	 * This method will be called to create the exclusion rule between the source families with the all other family
	 * members of target. This method will be called for the scenario when the source link is checked and the target
	 * link is unchecked.
	 * 
	 * In this method, first time it will check if there is any link available and have active status, than it will make
	 * it Inactive and again create the rule.
	 * 
	 * @param sourceFamilyId
	 * @param targetFamilyId
	 * @param targetAppId
	 * @param targetFamilyMemberList
	 * @param sourceFamilyMemberList
	 * @param sourceToTargetSML
	 * @param targetToSourceSML
	 */
	private void createExclusionForSelectedFamilyWithAllOtherFamilyMembersOfTarget(String sourceFamilyId,
			String targetFamilyId, Long sourceAppId, List<Long> targetFamilyMemberList,
			List<Long> sourceFamilyMemberList, SubjectMatterLink sourceToTargetSML,
			SubjectMatterLink targetToSourceSML) {
		LOGGER.debug("Create New Rule for SML");

		List<Long> sourceFamilyList = new ArrayList<>(sourceFamilyMemberList);
		sourceFamilyList.add(sourceAppId);

		subjectMatterLinkRepository.save(sourceToTargetSML);
		subjectMatterLinkRepository.save(targetToSourceSML);

		ApplicationBase sourceAppBase = new ApplicationBase();
		ApplicationBase targetAppBase = new ApplicationBase();

		for (Long sourceId : sourceFamilyList) {
			for (Long targetId : targetFamilyMemberList) {
				IDSReferenceFlowRuleExclusion exclusion = findExistingReferenceFlowExclusionRule(sourceId, targetId);
				if (exclusion != null) {
					inactivateReferenceFlowRuleExclusionStatus(sourceId, targetId, exclusion);
				} else {
					exclusion = new IDSReferenceFlowRuleExclusion();
					this.saveExclusionData(sourceFamilyId, targetFamilyId, sourceToTargetSML, sourceAppBase,
							targetAppBase, sourceId, targetId, exclusion);
				}

				exclusion = findExistingReferenceFlowExclusionRule(targetId, sourceId);
				if (exclusion != null) {
					inactivateReferenceFlowRuleExclusionStatus(targetId, sourceId, exclusion);
				} else {
					exclusion = new IDSReferenceFlowRuleExclusion();
					this.saveExclusionData(targetFamilyId, sourceFamilyId, targetToSourceSML, sourceAppBase,
							targetAppBase, targetId, sourceId, exclusion);
				}
			}
		}
	}

	/**
	 * This is the extracted common method to save the details to the exclusion table.
	 * 
	 * @param sourceFamilyId
	 * @param targetFamilyId
	 * @param sourceToTargetSML
	 * @param sourceAppBase
	 * @param targetAppBase
	 * @param sourceId
	 * @param targetId
	 * @param exclusion
	 */
	private void saveExclusionData(String sourceFamilyId, String targetFamilyId, SubjectMatterLink sourceToTargetSML,
			ApplicationBase sourceAppBase, ApplicationBase targetAppBase, Long sourceId, Long targetId,
			IDSReferenceFlowRuleExclusion exclusion) {
		sourceAppBase.setId(sourceId);
		targetAppBase.setId(targetId);
		exclusion.setSourceApplication(sourceAppBase);
		exclusion.setTargetApplication(targetAppBase);
		exclusion.setComments("");
		exclusion.setSubjectMatterLink(sourceToTargetSML);
		exclusion.setSourceFamilyId(sourceFamilyId);
		exclusion.setTargetFamilyId(targetFamilyId);
		exclusion.setStatus(Status.ACTIVE);
		exclusion.setReferenceRuleType(ReferenceRuleType.SML);
		idsReferenceFlowRuleExclusionRepository.save(exclusion);
	}

	/**
	 * This method will check the status for the existing exclusion in exclusion table. And if it active, than it will
	 * make it as inactive.
	 * 
	 * @param sourceAppId
	 * @param targetAppId
	 * @param exclusion
	 * @return
	 */
	public boolean inactivateReferenceFlowRuleExclusionStatus(final Long sourceAppId, final Long targetAppId,
			IDSReferenceFlowRuleExclusion exclusion) {
		boolean status = false;
		if (exclusion != null) {
			if (exclusion.getStatus().equals(Status.ACTIVE)) {
				// do nothing
			} else {
				exclusion.setStatus(Status.ACTIVE);
				exclusion.setComments("");
				idsReferenceFlowRuleExclusionRepository.save(exclusion);
			}
			status = true;
		}
		return status;
	}

	@Override
	@Transactional
	public Page<IDSReferenceRecordDTO> getAllIDSPatentReferenceRecords(final Predicate predicate,
			final Pageable pageable) {
		return referenceBaseDataRepository.fetchPatentReferenceRecords(predicate, pageable);
	}

	@Override
	@Transactional
	public Page<IDSReferenceRecordDTO> getAllIDSNPLReferenceRecords(final Predicate predicate,
			final Pageable pageable) {
		return referenceBaseDataRepository.fetchNPLReferenceRecords(predicate, pageable);
	}

	@Override
	@Transactional
	public IDSReferenceRecordStatusCountDTO getStatusTabCount(final Predicate predicate) {
		return referenceBaseDataRepository.fetchAllTabStatusCount(predicate);

	}

	@Override
	@Transactional
	public Map<String, Long> getDistinctDateAndCount() {
		return referenceBaseDataRepository.fetchDistinctFilingDateAndCount();
	}

	@Override
	@Transactional
	public IDSReferenceRecordDTO getPatentAndNplCounts() {
		return referenceBaseDataRepository.fetchNplAndPatentCount();
	}

	@Override
	@Transactional
	public List<ReferenceFlowSubStatus> getChangeSubStatus() {
		return referenceBaseDataRepository.fetchChangeSubStatus();
	}

	@Override
	@Transactional
	public IDSReferenceRecordDTO getSourceDocumentData(Long refFlowId) {
		return referenceBaseDataRepository.fetchSourceDocumentData(refFlowId);
	}	
}
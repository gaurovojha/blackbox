/**
 * 
 */
package com.blackbox.ids.services.referenceflow;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dto.IDS.IDSReferenceRecordDTO;
import com.blackbox.ids.core.dto.IDS.IDSReferenceRecordStatusCountDTO;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowDTO;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowRuleDTO;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.mysema.query.types.Predicate;

/**
 * @author nagarro
 *
 */
public interface ReferenceFlowRuleService {

	public Page<ReferenceFlowDTO> getAllRecords(final Predicate predicate, final Pageable pageable);

	public Page<ReferenceFlowRuleDTO> getAllNotifications(final Predicate predicate, final Pageable pageable);

	public Page<ReferenceFlowRuleDTO> getFamilyLinks(final Predicate predicate, final Pageable pageable,
			final String familyId, final Long applicationId);

	public Page<ReferenceFlowRuleDTO> getSubjectMatterLinks(final Predicate predicate, final Pageable pageable,
			final String familyId, final Long applicationId);

	// TODO: unused - may be removed after verification
	public Page<MdmRecordDTO> getApplicationsByFamily(final Predicate predicate, final Pageable pageable);

	public List<String> getTargetFamilyIdList(final String sourceFamily);

	public Long getNotificationCount();

	public List<ReferenceFlowRuleDTO> getReferenceFlowRuleExclusionList(final String familyId);

	public boolean updateReferenceFlowRuleExclusionStatus(final Long sourceAppId, final Long targetAppId,
			final String sourceFamilyId, final String targetFamilyId, final String comments);

	public void createReferenceFlowExclusionRule(final Long sourceAppId, final Long targetAppId,
			final String sourceFamilyId, final String targetFamilyId, final String comments);

	public void deleteSML(final String sourceFamilyId, final String targetFamilyId);

	public void createReferenceFlowSmlExclusionNewRule(final String sourceFamilyId, final String targetFamilyId,
			final Long sourceAppId, final Long targetAppId, final boolean isSourceLinkage,
			final boolean isTargetLinkage);

	public boolean updateReferenceForReview(final Long referenceBaseDataId);

	public ReferenceBaseDTO getReferencesForReview(final Long correspondenceId);

	public IDSReferenceRecordStatusCountDTO getStatusTabCount(final Predicate predicate);

	public Page<IDSReferenceRecordDTO> getAllIDSPatentReferenceRecords(final Predicate predicate,
			final Pageable pageable);

	public Page<IDSReferenceRecordDTO> getAllIDSNPLReferenceRecords(final Predicate predicate, final Pageable pageable);

	public Map<String, Long> getDistinctDateAndCount();

	public IDSReferenceRecordDTO getPatentAndNplCounts();


	public List<ReferenceFlowSubStatus> getChangeSubStatus();

	public IDSReferenceRecordDTO getSourceDocumentData(final Long refFlowId);

}

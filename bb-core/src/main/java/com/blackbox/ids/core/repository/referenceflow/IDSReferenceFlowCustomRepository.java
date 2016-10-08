/**
 *
 */
package com.blackbox.ids.core.repository.referenceflow;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dto.IDS.IDSFeeCalculationDetails;
import com.blackbox.ids.core.dto.IDS.ReferenceRecordDTO;
import com.blackbox.ids.core.dto.IDS.ReferenceRecordsFilter;
import com.blackbox.ids.core.dto.IDS.dashboard.IDS1449ReferenceDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.UpdateRefStatusDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowDTO;
import com.blackbox.ids.core.dto.referenceflow.ReferenceFlowRuleDTO;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.mysema.query.types.Predicate;

/**
 * IDSReferenceFlowCustomRepository with custom methods
 *
 * @author nagarro
 *
 */
public interface IDSReferenceFlowCustomRepository<IDSReferenceFlow> {

	public Page<ReferenceFlowDTO> filterAllRecords(final Predicate predicate, final Pageable pageable)
			throws ApplicationException;

	public Page<ReferenceFlowRuleDTO> filterAllNotifications(final Predicate predicate, final Pageable pageable)
			throws ApplicationException;

	public Page<ReferenceFlowRuleDTO> filterFamilyLinks(final Predicate predicate, final Pageable pageable,
			final String familyId, Long applicationId) throws ApplicationException;

	public Page<ReferenceFlowRuleDTO> filterSubjectMatterLinks(final Predicate predicate, final Pageable pageable,
			final String targetFamilyId, Long sourceApplicationId) throws ApplicationException;

	public Page<MdmRecordDTO> findApplicationsByFamilyId(final Predicate predicate, final Pageable pageable)
			throws ApplicationException;

	public Long fetchNotificationCount();

	public List<ReferenceFlowRuleDTO> generateFamilyLinks(final String familyId, final Long sourceApplicationId,
			final Pageable pageable);

	public List<ReferenceFlowRuleDTO> generateSubjectMatterLinks(final String targetFamilyId,
			final Long sourceApplicationId, final Pageable pageable);

	public long claimRefFlowsForIDS(final long targetApplication, final Long idsId);

	public long releaseRefFlowsFromIDS(final long ids, long releasedBy);

	public SearchResult<ReferenceRecordDTO> fetchUSPatents(ReferenceRecordsFilter filter, PaginationInfo pageInfo);

	public SearchResult<ReferenceRecordDTO> fetchForeignData(ReferenceRecordsFilter filter, PaginationInfo pageInfo);

	public SearchResult<ReferenceRecordDTO> fetchNPLData(ReferenceRecordsFilter filter, PaginationInfo pageInfo);

	public IDSFeeCalculationDetails fetchIDSFilingFeeParams(final long idsID);

	public Map<ReferenceType, Long> countReferenceRecords(ReferenceRecordsFilter filter);

	public Map<ReferenceType, Long> countNewReferenceRecords(ReferenceRecordsFilter refFilter);

	public long doNotFileAction(Long filter);

	public long dropRefFlowFromIDS(Long refFlowId);

	public List<Long> findNewUncitedRef(final long appId);

	public long includeRefInCurrentIDS(final long iDSId, final List<Long> refFlowsIdsList);

	public boolean dropReferenceFlow(final ReferenceBaseData referenceBaseData, final Long targetApplicationId,
			final ReferenceFlowStatus status);

	public boolean duplicateCheck(final ReferenceBaseData referenceBaseData, final Long targetApplicationId);

	public SearchResult<ReferenceRecordDTO> fetchSourceRefData(ReferenceRecordsFilter filter, PaginationInfo pageInfo);

	public int insertSourceRefFillingRecords(Long[] selectedSourceRefId, long ids);

	public SearchResult<ReferenceRecordDTO> fetchSourceFilingRefData(ReferenceRecordsFilter filter,
			PaginationInfo pageInfo);

	public List<IDSReferenceFlow> findDuplicateRefFlow(final ReferenceBaseData referenceBaseData,
			final Long applicationId);

	public long updateReferenceStatus(final UpdateRefStatusDTO refStatusDTO);

	public Long getNewReferencesCount(Long appId);

	public void updateNotifyAttorneyFlag(String appId);

	public void updateNewReferences(Long applicationId, Long iDSId);

	public boolean dropReferenceFlowWithMultiplleStatus(final ReferenceBaseData referenceBaseData,
			final Long targetApplicationId, final List<ReferenceFlowStatus> statusList);

	public List<IDS1449ReferenceDTO> fetch1449USPatents(final ReferenceType type, final String finalIdsId);

	public List<IDS1449ReferenceDTO> fetch1449foreignData(final ReferenceType type, final String finalIdsId);

	public List<IDS1449ReferenceDTO> fetch1449NplData(final ReferenceType type, final String finalIdsId);

	public long updateReferenceRecords(final Map<Long, String> idStatusMap);

	public boolean checkForCitedInParentEligibility(final ReferenceBaseData referenceBaseData,
			final ApplicationBase targetAppBase);

	public List<IDSReferenceFlow> findBySourceFamilyIdAndReferenceFlowStatus(final String sourceFamily,
			final ReferenceFlowStatus status);

	public boolean dropReferenceFlow(Long refFlowId);
}

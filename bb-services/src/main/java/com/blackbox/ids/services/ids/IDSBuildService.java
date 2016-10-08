/**
 *
 */
package com.blackbox.ids.services.ids;

import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.dto.IDS.ApplicationDetailsDTO;
import com.blackbox.ids.core.dto.IDS.ReferenceRecordDTO;
import com.blackbox.ids.core.dto.IDS.ReferenceRecordsFilter;
import com.blackbox.ids.core.dto.IDS.UserDetails;
import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.model.IDS.CertificationStatement;
import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.reference.ReferenceType;

/**
 * @author ajay2258
 *
 */
public interface IDSBuildService {

	String BEAN_NAME = "idsBuildService";

	/*- -------------------------------------------------------------------------------------------- IDS Build -- */
	/**
	 * The method does all the processing required to initiate an IDS preparation flow. An IDS instance is created with
	 * a unique build Id. Also same is stamped on reference flow records.
	 *
	 * @param targetApplication
	 *            Database ID for application whose IDS is being initiated
	 * @return IDS instance created.
	 */
	public IDS initiateIDS(Long targetApplication);

	public boolean discardIDS(final long ids, long discardedBy);

	public Long raiseIDSApprovalRequest(long ids, long attorney, Long approvalNotificationId);

	public long saveCertificationStatement(final long idsID, final CertificationStatement certificate);

	public void processIDSApproval(long idsID, long attorney);

	/*- ------------------------------------------------------------------------------------- End of IDS Build -- */

	public SearchResult<ReferenceRecordDTO> fetchReferenceRecords(ReferenceRecordsFilter filter,
			PaginationInfo pageInfo);

	public ApplicationDetailsDTO fetchApplicationInfo(final long appId);

	public SearchResult<NotificationBaseDTO> getNotificationsOfApp(final long applicationId,final PaginationInfo pageInfo,final List<Long> skipNotificationList);

	public SearchResult<NotificationBaseDTO> getNotificationsOfFamily(final long applicationId,final PaginationInfo pageInfo,
			final List<Long> skipNotificationList);

	public Map<String,Long> countNotifications(final long applicationId,final List<Long> skipAppNotificationList,final List<Long> skipFamilyNotificationList);


	public Long getApplicationIdForIDSId(long idsId);

	public Long findIdsInProgressForApplication(final long applicationID);

	public Double calculateIDSFilingFee(final long ids);

	public Map<ReferenceType, Long> countReferenceRecords(final ReferenceRecordsFilter filter);
	
	public Map<ReferenceType, Long> countNewReferenceRecords(ReferenceRecordsFilter refFilter);

	public List<UserDetails> fetchAttorneyUsersDetails(final List<Long> userIds);

	public Long dropReferenceRecords(final Long refFlowId);

	public Long dropReferenceFromIDS(final Long refFlowId);

	public ApplicationDetailsDTO fetchAppInfoForNotification(final long appId);

	public SearchResult<ReferenceRecordDTO> fetchSourceRefRecords(final ReferenceRecordsFilter filter,
			final PaginationInfo pageInfo);

	public int insertSourceRefFillingInfo(Long[] selectedIndex, long idsID);

	public SearchResult<ReferenceRecordDTO> fetchNPLRecordsForPreview(final ReferenceRecordsFilter filter,
			final PaginationInfo pageInfo);
	public long includeRefInCurrentIDS(final long iDSId, final List<Long> refFlowsIdsList);

	public CertificationStatement fetchCertificationStatement(final long idsID);

	public Map<String, Object> getValipPopUpMap(final long appId,final long referenceAge,final String prosecutionStatus);

	public Map<ReferenceType, Boolean> checkReferencesCountPopUp(final ReferenceRecordsFilter refFilter);

	public Long getNewReferenceCount(Long appId);

	public void updateNotifyAttorneyFlag(String appId);

	public void updateNewReferences(Long applicationId, Long iDSId);

	public Long findIdsInProgressOrApprovedForApplication(long applicationId);

}

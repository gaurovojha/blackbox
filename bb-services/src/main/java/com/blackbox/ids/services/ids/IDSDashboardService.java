package com.blackbox.ids.services.ids;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.blackbox.ids.core.dto.IDS.dashboard.FilingInProgressDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.FilingReadyDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDS1449ReferenceDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSAttorneyApprovalDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSDrillDownInfoDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSFilingPackageDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSManuallyFiledDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSPendingApprovalDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSPrivatePairKeyDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSReferenceDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.InitiateIDSRecordDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.N1449DetailDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.N1449PendingDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.UpdateRefStatusDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.ValidateRefStatusDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.dashboard.ActiveRecordsFilter;
import com.blackbox.ids.core.model.IDS.IDS.Status;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.reference.ReferenceType;

public interface IDSDashboardService {

	SearchResult<InitiateIDSRecordDTO> getUrgentIDSRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<InitiateIDSRecordDTO> getInitiateIDSRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	List<IDSPendingApprovalDTO> getPendingApprovalRecords();

	List<IDSPendingApprovalDTO> getPendingResponseRecords();
	
	Long getPendingApprovalRecordsCount();

	Long getPendingResponseRecordsCount();
	
	Long getIDSPendingApprovalRecordsCount();

	List<FilingReadyDTO> getFilingReadyRecords();

	List<FilingInProgressDTO> getFilingInProgressRecords();

	SearchResult<InitiateIDSRecordDTO> getAllFamilyApplications(ActiveRecordsFilter filter, String familyId, long appId);

	SearchResult<IDSAttorneyApprovalDTO> getIDSAttorneyApprovalRecords(ActiveRecordsFilter filter,
			PaginationInfo pageInfo);

	List<IDSFilingPackageDTO> getIDSFilingPackageRecords();

	Status idsStatusChangeByUser(Long idsId , String selectedStatus);
	
	boolean createValidateRefStatusNotification(Set<Long> roleIds, Long idsId,
			EntityName entityName,
			NotificationProcessType notificationProcessType , Status selectedStatus, ApplicationBase appn);
	
	List<IDSManuallyFiledDTO> getManuallyFiledIdsRecords();
	
	Map<String, Long> getIDSReferenceCount(Long id);
	List<IDSReferenceDTO> getIDSReferenceDetails(ReferenceType refType, Long filingInfoId);

	List<N1449PendingDTO> get1449PendingIDS();
	
	List<ValidateRefStatusDTO> getValidateRefStatusRecords();
	
	/**IDS drill down header section
	 *
	 * @param application's db id
	 * @return IDSDrillDownInfoDTO
	 **/
	IDSDrillDownInfoDTO getIDSDrillDownInfo(Long dbId);

	List<InitiateIDSRecordDTO> getUrgentIDSRecords();
	
	/**
	 * IDS drill down filing date section
	 * 
	 * @param application id
	 * @return List<IDSDrillDownInfoDTO>
	 */
	List<IDSDrillDownInfoDTO> getIDSDrillDownFilingDates(Long applicationId);

	/** IDS Drill down ids reference type section info 
	 * 
	 *@param applicationId 
	 *@param IDSId
	 *@return IDSDrillDownInfoDTO List
	 **/
	List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceTypes(Long applicationId, Long IDSId);
	
	/** IDS Drill down ids reference type counts
	 * 
	 *@param applicationId 
	 *@param IDSId
	 *@return IDSDrillDownInfoDTO List
	 **/
	List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceTypeCounts(Long applicationId, Long IDSId);

	/** IDS Drill down ids reference source info 
	 * 
	 *@param referenceFlowId 
	 *@return IDSDrillDownInfoDTO List
	 **/
	List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceSource(Long referenceFlowId);
	
	/** IDS Drill down ids reference source other references info 
	 * 
	 *@param corrId
	 *@return IDSDrillDownInfoDTO List
	 **/
	List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceSourceOthers(Long corrId);
	

	boolean updateReferenceStatus(UpdateRefStatusDTO updateRefDTO);
	
	List<N1449DetailDTO> get1449Details(Long idsId);

	List<IDS1449ReferenceDTO> get1449ReferenceRecords(String referenceType, String finalIdsId);

	Map<String, Long> getIDSReferenceCount(String finalIdsId);

	long updateReferenceRecords(Map<Long, String> idStatusMap);
	
	boolean submitEmailResponse(Long idsId, Long notificationProcessId, String comment);

	SearchResult<IDSAttorneyApprovalDTO> getIDSAttorneyApprovalAllFamilyRecords(String familyID, Long applicationID,
			Long notificationId);
	List<IDSPrivatePairKeyDTO> getPrivatePairKeys();
	
	boolean rejectUploadManualIDS(Long notificationId);
	
	boolean downloadIDSUserAction(Long idsId);
	boolean downloadIDSPackageUserAction(Long idsID);
}

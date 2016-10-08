package com.blackbox.ids.core.dao.ids;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.dto.IDS.dashboard.FilingInProgressDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.FilingReadyDTO;
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
import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.dashboard.ActiveRecordsFilter;
import com.blackbox.ids.core.model.IDS.CertificationStatement;
import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.IDS.IDS.Status;
import com.blackbox.ids.core.model.IDS.IDS.SubStatus;
import com.blackbox.ids.core.model.reference.ReferenceType;

public interface IDSDao extends BaseDAO<IDS, Long> {

	String generateSequenceId(final IDS.Sequence sequence);

	/* IDS : Initiate IDS */
	SearchResult<InitiateIDSRecordDTO> getInitiateRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo,
			String tabType);


	/* IDS pending approval tab */
		/******content*/
	List<IDSPendingApprovalDTO> getPendingApprovalRecords();
	List<IDSPendingApprovalDTO> getPendingResponseRecords();
	
		/******count*/
	Long getPendingApprovalRecordsCount();
	Long getPendingResponseRecordsCount();
	

	/* IDS Ready for filing tab */
	List<FilingReadyDTO> getFilingReadyRecords();

	SearchResult<InitiateIDSRecordDTO> allFamilyApplications(ActiveRecordsFilter filter, String familyId, long appId);

	/* IDS WIP USPTO Filing */
	List<FilingInProgressDTO> getFilingInProgressRecords(Calendar dateCheck);
	List<IDSManuallyFiledDTO> getManuallyFiledIDSRecords();
	List<ValidateRefStatusDTO> getValidateRefStatusIDSRecords();

	boolean updateIDSStatus(long idsID, Status newStatus, long updatedBy);

	/* IDS Attorney approval records */
	SearchResult<IDSAttorneyApprovalDTO> getAttorneyApprovalRecords(ActiveRecordsFilter filter,
			PaginationInfo pageInfo);

	/** Get Application ID for a particular IDS */
	Long getApplicationIdForIDSId(Long idsId);

	List<Long> findWithStatusEq(long applicationID, Status status);
	
	List<Long> findWithStatusEq(long applicationID, Status status1, Status status2);

	List<IDSFilingPackageDTO> getIDSFilingPackageRecords();
	
	SearchResult<NotificationBaseDTO> getFamilyNotifications(long applicationId, PaginationInfo pageInfo,
			List<Long> skipNotificationList);

	Map<String,Long> countNotifications(long applicationId, List<Long> skipAppNFList, List<Long> skipFamilyNFList);
	
	SearchResult<NotificationBaseDTO> getAppNotifications(long applicationId, PaginationInfo pageInfo, List<Long> skipNotificationList);

	List<N1449PendingDTO> get1449PendingIDS();
	boolean updateIDSForApproval(long ids, long attorney);
	
	/*****************Update Reference Status**********************/
	List<IDSReferenceDTO> getIDSReferencesForRefType(ReferenceType refType, Long idsId);
	Map<ReferenceType, Long> getIDSReferenceCount(Long filingInfoId);

	List<N1449DetailDTO> get1449Details(Long idsId);

	/**
	 * IDS Drill down header section info
	 * 
	 *@param applicationId 
	 *@return IDSDrillDownInfoDTO
	 **/
	IDSDrillDownInfoDTO getIDSDrillDownInfo(Long applicationId);

	

	List<InitiateIDSRecordDTO> getUrgentIDSRecords(final String urgent);

	boolean updateIDSSubStatus(long idsID, SubStatus newStatus);

	/**
	 * IDS Drill down ids filed dates section info
	 *
	 *@param applicationId
	 *@return IDSDrillDownInfoDTO
	 **/
	List<IDSDrillDownInfoDTO> getIDSDrillDownFilingDates(Long applicationId);

	CertificationStatement findCertificationStatement(long idsID);
	
	boolean updateIDSReferenceStatus(UpdateRefStatusDTO updateRefs);

	/**
	 * IDS Drill down ids reference type section info
	 * 
	 *@param applicationId 
	 *@param IDSId
	 *@return IDSDrillDownInfoDTO List
	 **/
	List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceTypes(Long applicationId, Long IDSId);
	
	/**
	 * IDS Drill down ids reference type counts
	 * 
	 *@param applicationId 
	 *@param IDSId
	 *@return IDSDrillDownInfoDTO List
	 **/
	List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceTypeCounts(Long applicationId, Long IDSId);

	/**
	 * IDS Drill down ids reference source info
	 * 
	 * @param referenceFlowId
	 * @return IDSDrillDownInfoDTO List
	 **/
	List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceSource(Long referenceFlowId);

	/**
	 * IDS Drill down ids reference source other references info
	 * 
	 * @param corrId
	 * @return IDSDrillDownInfoDTO List
	 **/
	List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceSourceOthers(Long corrId);

	Map<ReferenceType, Long> getIDSReferenceCount(String finalIdsId);
	
	boolean submitEmailResponse(IDS idsId,Long updatedByUser, String comment);

	SearchResult<IDSAttorneyApprovalDTO> getAttorneyApprovalAllFamilyRecords(String familyID, Long applicationID, Long notificationId);
	
	public IDS getIDSWithEarliestFileGenerationDate(final List<Long> idIdsList);

	public String generateInternalIDSId();
	
	public String generateExternalIDSId();

	public String getCurrentExternalIDSId();
	
	public String getCurrentInternalIDSId();
	
	
	public List<IDSPrivatePairKeyDTO> getPrivatePairKeys();

}

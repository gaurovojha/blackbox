package com.blackbox.ids.services.ids.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dao.ids.IDSDao;
import com.blackbox.ids.core.dao.ids.IDashboardDAO;
import com.blackbox.ids.core.dao.webcrawler.ITrackIDSFilingQueueDao;
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
import com.blackbox.ids.core.model.IDS.CertificationStatement;
import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.IDS.IDS.Status;
import com.blackbox.ids.core.model.IDS.IDSFilingInfo;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.webcrawler.TrackIDSFilingQueue;
import com.blackbox.ids.core.model.webcrawler.TrackIDSFilingQueue.RequestType;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.referenceflow.IDSReferenceFlowRepository;
import com.blackbox.ids.services.ids.IDSDashboardService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.usermanagement.UserService;


@Service("idsDashboardService")
public class IDSDashboardServiceImpl implements IDSDashboardService {

	private static final Logger LOGGER = Logger.getLogger(IDSDashboardServiceImpl.class);

	private static final String URGENT = "URGENT";
	private static final String ALL = "ALL";

	@Autowired
	private IDashboardDAO dashboardDAO;

	@Autowired
	private IDSDao idsDAO;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	NotificationProcessService notificationProcessService;
	
	@Autowired
	private IDSReferenceFlowRepository<IDSReferenceFlow> iDSReferenceFlowRepository;
	
	@Autowired
	ITrackIDSFilingQueueDao iTrackIDSFilingQueueDao;


	@Override
	public SearchResult<InitiateIDSRecordDTO> getUrgentIDSRecords(ActiveRecordsFilter filter,PaginationInfo pageInfo) {


		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}
		return idsDAO.getInitiateRecords(filter, pageInfo, URGENT);
	}

	@Override
	public SearchResult<InitiateIDSRecordDTO> getInitiateIDSRecords(ActiveRecordsFilter filter,PaginationInfo pageInfo)
	{
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}
		return idsDAO.getInitiateRecords(filter, pageInfo, ALL);
	}

	@Override
	public SearchResult<InitiateIDSRecordDTO> getAllFamilyApplications(ActiveRecordsFilter filter,String familyId,long appId)
	{
		return idsDAO.allFamilyApplications(filter, familyId, appId);
	}
	@Override
	public List<IDSPendingApprovalDTO> getPendingApprovalRecords() {

		List<IDSPendingApprovalDTO> IDSPendingApprovalDTOs = idsDAO.getPendingApprovalRecords();
		return IDSPendingApprovalDTOs;
	}

	@Override
	public List<IDSPendingApprovalDTO> getPendingResponseRecords() {
		List<IDSPendingApprovalDTO> IDSPendingApprovalDTOs = idsDAO.getPendingResponseRecords();
		return IDSPendingApprovalDTOs;
	}

	@Override
	public List<FilingReadyDTO> getFilingReadyRecords() {

		List<FilingReadyDTO> filingReadyDTOs = idsDAO.getFilingReadyRecords();
		return filingReadyDTOs;
	}

	@Override
	public SearchResult<IDSAttorneyApprovalDTO> getIDSAttorneyApprovalRecords(ActiveRecordsFilter filter,
			PaginationInfo pageInfo) {

		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}
		return idsDAO.getAttorneyApprovalRecords(filter, pageInfo);
	}

	@Override
	public List<FilingInProgressDTO> getFilingInProgressRecords() {
		Calendar dateCheck = Calendar.getInstance();
		dateCheck.add(Calendar.DATE, 60);
		List<FilingInProgressDTO> filingInProgressDTOs = idsDAO.getFilingInProgressRecords(dateCheck);
		return filingInProgressDTOs;
	}

	@Override
	public List<N1449PendingDTO> get1449PendingIDS(){
		return idsDAO.get1449PendingIDS();
	}

	@Override
	public List<N1449DetailDTO> get1449Details(Long idsId)
	{
		if(idsId == null)
		{
			throw new IllegalArgumentException("IDS:  ids Id not found.");
		}
		return idsDAO.get1449Details(idsId);
			
	}
	
	@Override
	public Map<String, Long> getIDSReferenceCount(String finalIdsId) {
		// TODO Auto-generated method stub
		Map<String, Long> refCount = new HashMap<>();
		Map<ReferenceType, Long> refs = idsDAO.getIDSReferenceCount(finalIdsId);
		refCount = refs.entrySet().stream().collect(Collectors.toMap(e-> e.getKey().name(), e-> e.getValue()));
		return refCount;
	}
	
	@Override
	public List<IDS1449ReferenceDTO> get1449ReferenceRecords(String referenceType,String finalIdsId)
	{
		if(referenceType == null || finalIdsId ==null)
		{
			throw new IllegalArgumentException("Fetch Reference Records: Missing mandatory parameters.");
		}
		List<IDS1449ReferenceDTO> items = null;
		switch (ReferenceType.fromString(referenceType)) {
		case PUS:
			items = iDSReferenceFlowRepository.fetch1449USPatents(ReferenceType.PUS, finalIdsId);
			break;
		case US_PUBLICATION:
			items = iDSReferenceFlowRepository.fetch1449USPatents(ReferenceType.US_PUBLICATION, finalIdsId);
			break;

		case FP:
			items = iDSReferenceFlowRepository.fetch1449foreignData(ReferenceType.FP, finalIdsId);
			break;

		case NPL:
			items = iDSReferenceFlowRepository.fetch1449NplData(ReferenceType.NPL, finalIdsId);
			break;

		default:
			throw new IllegalArgumentException(
					String.format("Fetch Reference Records: Unhandled reference type {0}.", referenceType));
		}
		return items;
	}
	
	@Override
	public long updateReferenceRecords(Map<Long,String> idStatusMap)
	{
		if(idStatusMap == null)
		{
			throw new IllegalArgumentException("Update Reference Records: Missing mandatory parameters.");
		}
		//close the notification too
		return iDSReferenceFlowRepository.updateReferenceRecords(idStatusMap);
	}
	
	@Override
	public List<IDSFilingPackageDTO> getIDSFilingPackageRecords()
	{
		return idsDAO.getIDSFilingPackageRecords();
	}

	@Override
	@Transactional
	public Status idsStatusChangeByUser(Long idsId , String selectedStatus) {
		Map<String , String> userMap = new HashMap<>();
		IDS idsRecord = null;
		idsRecord = idsDAO.find(idsId);
		switch(selectedStatus) {

		case "DO_NOT_FILE" :
			
			/* If the user input is, ‘I have decided NOT to file’, the system will update status of IDS ID to ‘IDS WITHDRAWN’.*/
			idsDAO.updateIDSStatus(idsId.longValue(), Status.WITHDRAWN, BlackboxSecurityContextHolder.getUserId());
			
			/*
			 * When user has selected I have decided NOT to file’ STEP 1 : Send An email will be sent to the paralegal
			 * who had given the filing instructions/requested by & approving attorney that the same has been done. DO
			 * NOT RELEASE TEMP IDS ID AT THIS STAGE.
			 */
			userMap.put("parallegal", userRepository.findOne(idsRecord.getApprovalRequestedBy()).getLoginId());
			userMap.put("attorney", userRepository.findOne(idsRecord.getApprovedBy()).getLoginId());

			//fetch email Ids to send email
			//TODO UPDATE W.R.T templates that client will provide
			userService.sendEmail(userMap.get("parallegal"), "", "");
			userService.sendEmail(userMap.get("attorney"), "", "");

			/*STEP 2 : The system will also send Validate Reference Status Notification for this IDS.*/
			Set<Long> roleIds = BlackboxSecurityContextHolder.getUserDetails().getRoleIds();
			//for each filing package
			for(IDSFilingInfo idsFilingInfo : idsRecord.getFilingInfos()) {

				createValidateRefStatusNotification(roleIds, idsId , EntityName.IDS, NotificationProcessType.VALIDATE_REFERENCE_STATUS, Status.FILE_PACKAGE_GENERATED, idsRecord.getApplication());
			}
			break;

		case "FILED_IT" :
			/*When user has selected I have decided NOT to file*/
			/*Step   : the system will then send populate the IDS Filing Tracking Queue. */

			/*Step 2 : Status on the UI will remain Filing package generated.*/
			idsDAO.updateIDSStatus(idsId.longValue(), idsRecord.getStatus(), BlackboxSecurityContextHolder.getUserId());

			/* Step 3 : Populate IDS Filing Tracking Queue – Request 1 */
											
			TrackIDSFilingQueue trackingQueue = populateTrackingQueue(idsRecord, BlackboxSecurityContextHolder.getUserId(), RequestType.SUBSEQUENT_RUN);
			iTrackIDSFilingQueueDao.persist(trackingQueue);
			break;

		default :
			break;
		}

		IDS ids = idsDAO.find(idsId);
		return ids.getStatus();
	}

	@Override
	public boolean createValidateRefStatusNotification(Set<Long> roleIds, Long idsId, EntityName entityName,
			NotificationProcessType notificationProcessType , Status selectedStatus, ApplicationBase appn) {
		notificationProcessService.createNotification(appn, null, null, idsId, entityName, notificationProcessType, 
				null, null);
		return false;
	}

	@Override
	public List<IDSManuallyFiledDTO> getManuallyFiledIdsRecords() {
		// TODO Auto-generated method stub
		List<IDSManuallyFiledDTO> items= idsDAO.getManuallyFiledIDSRecords();
		return items;
	}

	@Override
	public List<ValidateRefStatusDTO> getValidateRefStatusRecords() {
		// TODO Auto-generated method stub
		List<ValidateRefStatusDTO> items= idsDAO.getValidateRefStatusIDSRecords();
		return items;
	}

	@Override
	public List<IDSReferenceDTO> getIDSReferenceDetails(ReferenceType refType, Long idsId) {
		// TODO Auto-generated method stub
		return idsDAO.getIDSReferencesForRefType(refType, idsId);
	}

	@Override
	public Map<String, Long> getIDSReferenceCount(Long filingInfoId) {
		// TODO Auto-generated method stub
		Map<String, Long> refCount = new HashMap<>();

		Map<ReferenceType, Long> refs = idsDAO.getIDSReferenceCount(filingInfoId);
		refCount = refs.entrySet().stream().collect(Collectors.toMap(e-> e.getKey().name(), e-> e.getValue()));

		return refCount;
	}
	/**
	 * IDS drill down header section
	 *
	 * @param application id
	 * @return IDSDrillDownInfoDTO
	 */
	@Override
	public IDSDrillDownInfoDTO getIDSDrillDownInfo(Long applicationId){
		return idsDAO.getIDSDrillDownInfo(applicationId);
	}

	@Override
	public List<InitiateIDSRecordDTO> getUrgentIDSRecords() {
		return idsDAO.getUrgentIDSRecords(URGENT);
	}

	/**
	 * IDS drill down filing date section
	 *
	 * @param application id
	 * @return IDSDrillDownInfoDTO
	 */
	@Override
	public List<IDSDrillDownInfoDTO> getIDSDrillDownFilingDates(Long applicationId){
		return idsDAO.getIDSDrillDownFilingDates(applicationId);
	}

	
	
	/** IDS Drill down ids reference type section info 
	 * 
	 *@param applicationId 
	 *@param IDSId
	 *@return IDSDrillDownInfoDTO List
	 **/
	@Override
	public List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceTypes(Long applicationId, Long IDSId){
		return idsDAO.getIDSDrillDownReferenceTypes(applicationId, IDSId);
	}
	
	/** IDS Drill down ids reference type counts
	 * 
	 *@param applicationId 
	 *@param IDSId
	 *@return IDSDrillDownInfoDTO List
	 **/
	@Override
	public List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceTypeCounts(Long applicationId, Long IDSId){
		return idsDAO.getIDSDrillDownReferenceTypeCounts(applicationId, IDSId);
	}
	
	/** IDS Drill down ids reference source info 
	 * 
	 *@param referenceFlowId 
	 *@return IDSDrillDownInfoDTO List
	 **/
	@Override
	public List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceSource(Long referenceFlowId){
		return idsDAO.getIDSDrillDownReferenceSource(referenceFlowId);
	}
	
	/** IDS Drill down ids reference source other references info 
	 * 
	 *@param corrId
	 *@return IDSDrillDownInfoDTO List
	 **/
	@Override
	public List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceSourceOthers(Long corrId){
		return idsDAO.getIDSDrillDownReferenceSourceOthers(corrId);
	}
	
	
	@Override
	@Transactional
	public boolean updateReferenceStatus(UpdateRefStatusDTO updateRefDTO) {
		//update reference status and remove temp ids
		Long userId = BlackboxSecurityContextHolder.getUserId();
		iDSReferenceFlowRepository.updateReferenceStatus(updateRefDTO);
		
		//Release temp ids
		for(Long idsId : updateRefDTO.getNotCitedRef()){
			iDSReferenceFlowRepository.releaseRefFlowsFromIDS(idsId, userId);
		}
		
		//close this notification
		notificationProcessService.completeTask(updateRefDTO.getNotificationProcessId(), NotificationStatus.COMPLETED);
		return false;
	}
	
	private TrackIDSFilingQueue populateTrackingQueue(IDS ids, Long userId, RequestType reqType) {
		TrackIDSFilingQueue trackingQueue =new TrackIDSFilingQueue();
		ApplicationBase application = ids.getApplication();
		trackingQueue.setApplicationId(application.getId());
		trackingQueue.setApplicationNumberFormatted(ids.getApplication().getApplicationNumber());
		trackingQueue.setApprovedBy(ids.getApprovedBy());
		trackingQueue.setCreatedByUser(userId);
		trackingQueue.setCustomerNumber(application.getCustomer().getCustomerNumber());
		trackingQueue.setFiledBy(ids.getApprovalRequestedBy());
		//application filing date
		trackingQueue.setFilingDate(application.getAppDetails().getFilingDate());
		trackingQueue.setIdsBuildId(ids.getIdsBuildId());
		trackingQueue.setIdsDate(ids.getCreatedDate());
		//Current date
		trackingQueue.setJurisdictionCode(application.getJurisdiction().getCode());
		trackingQueue.setNextRunDate(Calendar.getInstance());
		//set as default
		//trackingQueue.setStatus(status);
		trackingQueue.setUpdatedByUser(userId);
		trackingQueue.setUpdatedDate(Calendar.getInstance());
		//SUBSEQUENT_RUN
		trackingQueue.setRequestType(reqType);
		return trackingQueue ;
	}

	@Override
	@Transactional
	public boolean submitEmailResponse(Long idsId, Long notificationProcessId, String comment) {
		//STEP 1 : add parallegal comments to IDS
		IDS ids = idsDAO.find(idsId); 
		CertificationStatement certificate = ids.getCertificate();
		certificate.setComment(comment);
		ids.setCertificate(certificate);
		ids.setUpdatedDate(Calendar.getInstance());
		ids.setUpdatedByUser(BlackboxSecurityContextHolder.getUserId());
		idsDAO.persist(ids);
		//idsDAO.submitEmailResponse(ids,updatedByUser,comment);
		
		//STEP 2 : Send notification back to attorney
	   //notificationProcessService.responseNotificationInfo(notificationProcessId);
		
		return false;
	}

	@Override
	public SearchResult<IDSAttorneyApprovalDTO> getIDSAttorneyApprovalAllFamilyRecords(String familyID,
			Long applicationID, Long notificationId) {
		// TODO Auto-generated method stub
		return idsDAO.getAttorneyApprovalAllFamilyRecords(familyID, applicationID,notificationId);
	}
	
	@Override
	public List<IDSPrivatePairKeyDTO> getPrivatePairKeys() {
		return idsDAO.getPrivatePairKeys();
	}

	@Override
	public boolean rejectUploadManualIDS(Long notificationId) {
		notificationProcessService.completeTask(notificationId, NotificationStatus.REJECTED);
		return false;
	}

	@Override
	public Long getPendingApprovalRecordsCount() {
		
		return idsDAO.getPendingApprovalRecordsCount();
	}

	@Override
	public Long getPendingResponseRecordsCount() {
		return idsDAO.getPendingResponseRecordsCount();
	}

	@Override
	public Long getIDSPendingApprovalRecordsCount() {
		return getPendingApprovalRecordsCount() + getPendingResponseRecordsCount();
	}

	@Override
	public boolean downloadIDSUserAction(Long idsID) {
		// TODO Auto-generated method stub
		IDS idsRecord = idsDAO.find(idsID);
		/*Step 1 : Make an entry in TRACKING QUEUE*/
		TrackIDSFilingQueue trackingQueue = populateTrackingQueue(idsRecord, BlackboxSecurityContextHolder.getUserId(), RequestType.SUBSEQUENT_RUN);
		iTrackIDSFilingQueueDao.persist(trackingQueue);
		
		/*STEP 2 : Download IDS File*/ 
		return false;
	}

	@Override
	public boolean downloadIDSPackageUserAction(Long idsID) {
		// TODO Auto-generated method stub
		return false;
	}
}

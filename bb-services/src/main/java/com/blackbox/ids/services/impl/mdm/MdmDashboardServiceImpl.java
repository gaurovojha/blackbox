package com.blackbox.ids.services.impl.mdm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.ApplicationStageDAO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.ChangeRequestDTO;
import com.blackbox.ids.core.dto.mdm.CreateReqApplicationDTO;
import com.blackbox.ids.core.dto.mdm.CreateReqFamilyDTO;
import com.blackbox.ids.core.dto.mdm.DraftDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqApplicationDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqAssigneeDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqFamilyDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.ActiveRecordsFilter;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.model.enums.ApplicationNumberStatus;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.ExclusionList;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.repository.ExclusionListRepository;
import com.blackbox.ids.services.mdm.ApplicationService;
import com.blackbox.ids.services.mdm.MdmDashboardService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;

@Service("mdmDashboardService")
public class MdmDashboardServiceImpl implements MdmDashboardService {

	private Logger log = Logger.getLogger(MdmDashboardService.class);

	@Autowired
	private ApplicationStageDAO mdmStageDAO;

	@Autowired
	private ExclusionListRepository exclusionListRepository;

	@Autowired
	private ApplicationBaseDAO mdmBaseDAO;

	@Autowired
	private NotificationProcessService notificationProcessService;

	@Autowired
	private ApplicationService applicationService;

	@Override
	public List<DraftDTO> getCurrentUserDrafts() {
		return mdmStageDAO.getUserApplicationDrafts(BlackboxSecurityContextHolder.getUserId());
	}

	@Override
	public SearchResult<CreateReqApplicationDTO> getCreateAppRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}
		return mdmBaseDAO.getCreateAppRecords(filter, pageInfo);
	}

	@Override
	public SearchResult<CreateReqFamilyDTO> getCreateFamilyRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}
		return mdmBaseDAO.getCreateFamilyRecords(filter, pageInfo);
	}

	@Override
	public SearchResult<MdmRecordDTO> filterActiveRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}

		return mdmBaseDAO.filterActiveRecords(filter, pageInfo);
	}

	@Override
	@Transactional
	public void deleteDraft(final long dbId) {
		log.info("Deleting draft " + dbId);
		mdmStageDAO.delete(dbId);
	}

	@Override
	public SearchResult<UpdateReqApplicationDTO	> getUpdateReqApplicationRecords(ActiveRecordsFilter filter,PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}
		return mdmBaseDAO.getUpdateReqApplicationRecords(filter,pageInfo);
	}

	@Override
	public SearchResult<UpdateReqAssigneeDTO> getUpdateReqAssigneeRecords(ActiveRecordsFilter filter,PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}
		return mdmBaseDAO.getUpdateReqAssigneeRecords(filter,pageInfo);
	}

	@Override
	public SearchResult<UpdateReqFamilyDTO> getUpdateReqFamilyRecords(ActiveRecordsFilter filter,PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}
		return mdmBaseDAO.getUpdateReqFamilyRecords(filter, pageInfo);
	}

	@Override
	public SearchResult<ChangeRequestDTO> getChangeRequestRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}

		return mdmBaseDAO.getChangeRequestRecords(filter, pageInfo);
	}

	@Override
	public SearchResult<MdmRecordDTO> filterInActiveRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo) {
		if (filter == null || pageInfo == null) {
			throw new IllegalArgumentException("Filter active records: Filter and pagination details are must.");
		}

		return mdmBaseDAO.filterInActiveRecords(filter, pageInfo);
	}

	@Override
	@Transactional
	public MdmRecordDTO getMdmRecordDTO(Long id) {
		ApplicationBase applicationBase = mdmBaseDAO.getApplicationBase(id);
		Long recordId = applicationBase.getId();
		String jurisdiction = applicationBase.getJurisdiction().getCode();
		String appNumber = applicationBase.getApplicationNumber();
		String familyId = applicationBase.getFamilyId();
		String docketNumber = applicationBase.getAttorneyDocketNumber().getSegment();
		String assignee = applicationBase.getAssignee().getName();
		MDMRecordStatus status = applicationBase.getRecordStatus();
		ApplicationType applicationType = applicationBase.getAppDetails().getChildApplicationType();
		return new MdmRecordDTO(recordId, jurisdiction, appNumber, familyId, docketNumber, assignee, applicationType, status);
	}

	@Override
	@Transactional
	public boolean updateRecordStatusToTransfer(MdmRecordDTO mdmRecordDTO, String recordId,
			String familyId, MDMRecordStatus mDMRecordStatus) {
		if(!mdmRecordDTO.isFamilyMembers() && StringUtils.isNumeric(recordId)){
			return mdmBaseDAO.updateRecordStatus(Long.valueOf(recordId), mdmRecordDTO.getUserComment(), mDMRecordStatus);
		}
		else if(mdmRecordDTO.isFamilyMembers()){
			return mdmBaseDAO.updateFamilyRecordStatus(familyId, mdmRecordDTO.getUserComment(), mDMRecordStatus);
		}
		return false;
	}

	@Override
	@Transactional
	public boolean updateRecordStatusToActive(MdmRecordDTO mdmRecord, String recordId, MDMRecordStatus mDMRecordStatus) {
		MdmRecordDTO mdmRecordDTO = getMdmRecordDTO(Long.valueOf(recordId));
		if(StringUtils.isNumeric(recordId)){
			if(mdmRecordDTO.getStatus() == MDMRecordStatus.DEACTIVATED){
				ExclusionList exclusionList = exclusionListRepository.findByJuridictionAndApplication(mdmRecordDTO.getJurisdiction(), mdmRecordDTO.getApplicationNumber());
				if(exclusionList!=null && exclusionList.getApplications()!=null){
					mdmBaseDAO.updateExclusionListStatus(exclusionList, ApplicationNumberStatus.INACTIVE);
				}
			}
			return mdmBaseDAO.updateRecordStatus(Long.valueOf(recordId), mdmRecord.getUserComment(), mDMRecordStatus);
		}
		return false;
	}

	@Override
	@Transactional
	public boolean updateRecordStatusToAbandon(MdmRecordDTO mdmRecordDTO, String recordId,
			String familyId, MDMRecordStatus mDMRecordStatus) {
		if(!mdmRecordDTO.isFamilyMembers() && StringUtils.isNumeric(recordId)){
			return mdmBaseDAO.updateRecordStatus(Long.valueOf(recordId), mdmRecordDTO.getUserComment(), mDMRecordStatus);
		}
		else if(mdmRecordDTO.isFamilyMembers()){
			return mdmBaseDAO.updateFamilyRecordStatus(familyId, mdmRecordDTO.getUserComment(), mDMRecordStatus);
		}
		return false;
	}

	@Override
	@Transactional
	public boolean updateRecordStatusToDeactivate(String notificationProcessId, String recordId, MDMRecordStatus currentRecordStatus, MDMRecordStatus newRecordStatus) {
		if(StringUtils.isNumeric(recordId) && StringUtils.isNumeric(notificationProcessId)){
			boolean updateStatusToDeactivate = mdmBaseDAO.updateStatusToDeactivate(Long.valueOf(recordId), currentRecordStatus);
			boolean updateNewRecordStatus = mdmBaseDAO.updateDeactivateNewRecordStatus(Long.valueOf(recordId), newRecordStatus);
			ExclusionList exclusionList = getExclusionList(Long.valueOf(recordId));
			if(exclusionList!=null && exclusionList.getApplications()!=null){
				exclusionList.getApplications().setStatus(ApplicationNumberStatus.ACTIVE);
				mdmBaseDAO.saveDeactivatedRecordToExclusionList(exclusionList);
			}
			notificationProcessService.completeTask(Long.valueOf(notificationProcessId), NotificationStatus.COMPLETED);
			if(updateStatusToDeactivate && updateNewRecordStatus){
				return true;
			}
		}
		return false;
	}


	@Override
	@Transactional
	public boolean updateRecordStatusToDelete(String notificationProcessId, String recordId, MDMRecordStatus currentRecordStatus, MDMRecordStatus newRecordStatus) {
		if(StringUtils.isNumeric(recordId) && StringUtils.isNumeric(notificationProcessId)){
			List<Long> recordIdSet = getRecordIdList(Long.valueOf(recordId));
			boolean updateStatusToDeactivate = mdmBaseDAO.updateStatusToDropped(recordIdSet, currentRecordStatus);
			boolean updateNewRecordStatus = mdmBaseDAO.updateDroppedNewRecordStatus(recordIdSet, newRecordStatus);
			notificationProcessService.completeTask(Long.valueOf(notificationProcessId), NotificationStatus.COMPLETED);
			if(updateStatusToDeactivate && updateNewRecordStatus){
				return true;
			}
		}
		return false;
	}


	@Override
	@Transactional
	public boolean createDroppedApprovalNotification(Set<Long> roleIds, Long recordId,
			EntityName entityName,
			NotificationProcessType notificationProcessType, MdmRecordDTO mdmRecordDTO, MDMRecordStatus mdmRecordStatus)
					throws ApplicationException {
		notificationProcessService.createNotification(mdmBaseDAO.getApplicationBase(recordId), null, null, recordId, entityName, notificationProcessType, null, null);
		List<Long> recordIdList = getRecordIdList(recordId);
		boolean updateStatus = mdmBaseDAO.updateNewRecordStatus(recordIdList, mdmRecordDTO.getUserComment(), mdmRecordStatus);
		return updateStatus;
	}

	@Override
	@Transactional
	public boolean createDeactivateApprovalNotification(Set<Long> roleIds, Long recordId, EntityName entityName,
			NotificationProcessType notificationProcessType, MdmRecordDTO mdmRecordDTO, MDMRecordStatus mdmRecordStatus) throws ApplicationException{
		notificationProcessService.createNotification(null, null, null, recordId, entityName, notificationProcessType, null, null);
		return mdmBaseDAO.updateNewRecordStatus(Arrays.asList(recordId), mdmRecordDTO.getUserComment(), mdmRecordStatus);
	}

	@Override
	public SearchResult<MdmRecordDTO> getAllFamilyApplications(ActiveRecordsFilter filter , String familyId, long appId, String viewName) {
		return mdmBaseDAO.allFamilyApplications(filter , familyId, appId, viewName);
	}

	@Override
	@Transactional
	public MdmRecordDTO getDeactivateAffectedTransactions(Long recordId) {
		MdmRecordDTO mdmRecordDTO = getMdmRecordDTO(recordId);
		int countAffectedTransactions = mdmBaseDAO.countDeactivateAffectedTransactions(recordId);
		mdmRecordDTO.setCountAffectedTransaction(countAffectedTransactions);
		return mdmRecordDTO;
	}

	@Override
	@Transactional
	public MdmRecordDTO getDeleteAffectedTransactions(Long recordId) {
		List<Long> recordIdList = getRecordIdList(recordId);
		MdmRecordDTO mdmRecordDTO = getMdmRecordDTO(recordId);
		if(recordIdList.size()>1){
			mdmRecordDTO.setFamilyMembers(true);
		}

		mdmRecordDTO.setCountAffectedTransaction(countDeleteAffectedTransactions(recordIdList));
		return mdmRecordDTO;
	}

	private int countDeleteAffectedTransactions(List<Long> recordIdSet){
		int countAffectedTransactions = mdmBaseDAO.countDeleteAffectedTransactions(recordIdSet);
		return countAffectedTransactions;
	}


	@Override
	@Transactional
	public boolean rejectDeactivateStatusRequest(String notificationProcessId, String recordId, MDMRecordStatus recordStatus){
		if(StringUtils.isNumeric(recordId) && StringUtils.isNumeric(notificationProcessId)){
			notificationProcessService.completeTask(Long.valueOf(notificationProcessId), NotificationStatus.COMPLETED);
			return mdmBaseDAO.updateDeactivateNewRecordStatus(Long.valueOf(recordId), recordStatus);
		}
		return false;
	}

	@Override
	@Transactional
	public boolean rejectDroppedStatusRequest(String notificationProcessId, String recordId, MDMRecordStatus recordStatus){
		if(StringUtils.isNumeric(recordId) && StringUtils.isNumeric(notificationProcessId)){
			notificationProcessService.completeTask(Long.valueOf(notificationProcessId), NotificationStatus.COMPLETED);
			List<Long> recordIdList = getRecordIdList(Long.valueOf(recordId));
			return mdmBaseDAO.updateDroppedNewRecordStatus(recordIdList, recordStatus);
		}
		return false;
	}

	private List<Long> getRecordIdList(long recordId){
		List<Long> recordIdList = new ArrayList<Long>();
		MdmRecordDTO mdmRecordDTO = getMdmRecordDTO(recordId);
		recordIdList.add(mdmRecordDTO.getDbId());
		if(mdmRecordDTO.getApplicationType() == ApplicationType.FIRST_FILING || mdmRecordDTO.getApplicationType() == ApplicationType.PCT_US_FIRST_FILING){
			List<MdmRecordDTO> mdmRecordDTOList = applicationService.fetchApplicationByFamilyAndRecordStatus(recordId, mdmRecordDTO.getFamilyId(), MDMRecordStatus.ACTIVE);
			for(MdmRecordDTO mdmRecord : mdmRecordDTOList){
				recordIdList.add(mdmRecord.getDbId());
			}
		}
		return recordIdList;
	}

	private ExclusionList getExclusionList(Long recordId){
		ApplicationBase applicationBase = mdmBaseDAO.getApplicationBase(recordId);
		ExclusionList exclusionList = new ExclusionList();
		exclusionList.setUpdatedByUser(applicationBase.getUpdatedByUser());
		exclusionList.setUpdatedDate(applicationBase.getUpdatedDate());
		exclusionList.getApplications().setAppNumber(applicationBase.getApplicationNumber());
		exclusionList.getApplications().setAppNumberRaw(applicationBase.getAppDetails().getApplicationNumberRaw());
		exclusionList.getApplications().setCustomerNumber(applicationBase.getCustomer().getCustomerNumber());
		exclusionList.getApplications().setJurisdiction(applicationBase.getJurisdiction().getCode());
		return exclusionList;

	}

	@Override
	public long countChangeRequestRecords() {
		return mdmBaseDAO.countChangeRequestRecords();
	}

	@Override
	public long countUpdatRequestsRecords() {
		long updatRequestsRecordsCount = mdmBaseDAO.countUpdateReqApplicationRecords() + mdmBaseDAO.countUpdateReqAssigneeRecords() + mdmBaseDAO.countUpdateReqFamilyRecords();
		return updatRequestsRecordsCount;
	}

	@Override
	public long countCreateRequestRecords() {
		long createRequestRecordsCount = mdmBaseDAO.countCreateAppRecords() + mdmBaseDAO.countCreateFamilyRecords();
		return createRequestRecordsCount;
	}

	@Override
	public long countActionItems() {
		long actionItemsCount = countCreateRequestRecords() + countUpdatRequestsRecords() + countChangeRequestRecords();
		return actionItemsCount;
	}

	@Override
	public MdmRecordDTO openDraft(long dbId) {
		log.info("Opening draft " + dbId);
		ApplicationStage applicationStaged = mdmStageDAO.find(dbId);
		MdmRecordDTO application = new MdmRecordDTO(applicationStaged);
		return application;
	}

}

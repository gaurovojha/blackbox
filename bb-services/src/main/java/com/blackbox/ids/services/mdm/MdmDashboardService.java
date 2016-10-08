package com.blackbox.ids.services.mdm;

import java.util.List;
import java.util.Set;

import com.blackbox.ids.core.ApplicationException;
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
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;

public interface MdmDashboardService {


	List<DraftDTO> getCurrentUserDrafts();

	void deleteDraft(long dbId);

	SearchResult<CreateReqApplicationDTO> getCreateAppRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<CreateReqFamilyDTO> getCreateFamilyRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<MdmRecordDTO> filterActiveRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<MdmRecordDTO> filterInActiveRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<ChangeRequestDTO> getChangeRequestRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<UpdateReqApplicationDTO> getUpdateReqApplicationRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<UpdateReqAssigneeDTO> getUpdateReqAssigneeRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<UpdateReqFamilyDTO> getUpdateReqFamilyRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	MdmRecordDTO getMdmRecordDTO(Long id);

	boolean updateRecordStatusToTransfer(MdmRecordDTO mdmRecordDTO, String recordId, String familyId, MDMRecordStatus mDMRecordStatus);

	boolean updateRecordStatusToActive(MdmRecordDTO mdmRecordDTO, String recordId, MDMRecordStatus mDMRecordStatus);

	boolean updateRecordStatusToAbandon(MdmRecordDTO mdmRecordDTO, String recordId, String familyId, MDMRecordStatus mDMRecordStatus);

	boolean updateRecordStatusToDeactivate(String notificationProcessId, String recordId, MDMRecordStatus currentRecordStatus, MDMRecordStatus mdmRecordStatus);

	boolean updateRecordStatusToDelete(String notificationProcessId, String recordId, MDMRecordStatus currentRecordStatus, MDMRecordStatus mdmRecordStatus);

	SearchResult<MdmRecordDTO> getAllFamilyApplications(ActiveRecordsFilter filter , String familyId, long appid, String viewName);

	boolean createDeactivateApprovalNotification(Set<Long> roleIds, Long recordId, EntityName entityName,
			NotificationProcessType notificationProcessType, MdmRecordDTO mdmRecordDTO, MDMRecordStatus mdmRecordStatus) throws ApplicationException;

	boolean createDroppedApprovalNotification(Set<Long> roleIds, Long recordId, EntityName entityName,
			NotificationProcessType notificationProcessType, MdmRecordDTO mdmRecordDTO, MDMRecordStatus mdmRecordStatus) throws ApplicationException;

	MdmRecordDTO getDeactivateAffectedTransactions(Long recordId);

	MdmRecordDTO getDeleteAffectedTransactions(Long recordId);

	boolean rejectDeactivateStatusRequest(String notificationProcessId, String recordId, MDMRecordStatus recordStatus);

	boolean rejectDroppedStatusRequest(String notificationProcessId, String recordId, MDMRecordStatus recordStatus);

	long countChangeRequestRecords();

	long countUpdatRequestsRecords();

	long countCreateRequestRecords();

	long countActionItems();

	MdmRecordDTO openDraft(long dbId);
}
/**
 *
 */
package com.blackbox.ids.core.dao;

import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.dto.IDS.ApplicationDetailsDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.ActionsBaseDto;
import com.blackbox.ids.core.dto.mdm.ChangeRequestDTO;
import com.blackbox.ids.core.dto.mdm.CreateReqApplicationDTO;
import com.blackbox.ids.core.dto.mdm.CreateReqFamilyDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqApplicationDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqAssigneeDTO;
import com.blackbox.ids.core.dto.mdm.UpdateReqFamilyDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.ActiveRecordsFilter;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilyDetailDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilySearchFilter;
import com.blackbox.ids.core.model.enums.ApplicationNumberStatus;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ExclusionList;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.AssigneeAttorneyDocketNo;
import com.blackbox.ids.core.model.mstr.AttorneyDocketFormat;
import com.blackbox.ids.core.model.mstr.ProsecutionStatus;
import com.blackbox.ids.core.model.notification.process.EntityName;

/**
 * The {@code ApplicationBaseDAO} exposes APIs to perform database operations on entity class {@link ApplicationBase}.
 *
 * @author ajay2258
 */
public interface ApplicationBaseDAO extends BaseDAO<ApplicationBase, Long> {

	String generateFamilyId() throws ApplicationException;

	List<FamilyDetailDTO> getFamilyDetails(FamilySearchFilter searchFilter) throws ApplicationException;

	SearchResult<MdmRecordDTO> filterActiveRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<MdmRecordDTO> filterInActiveRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<CreateReqApplicationDTO> getCreateAppRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo) throws ApplicationException;

	SearchResult<CreateReqFamilyDTO> getCreateFamilyRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo) throws ApplicationException;

	SearchResult<ChangeRequestDTO> getChangeRequestRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo) throws ApplicationException;

	SearchResult<UpdateReqApplicationDTO> getUpdateReqApplicationRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<UpdateReqAssigneeDTO> getUpdateReqAssigneeRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<UpdateReqFamilyDTO> getUpdateReqFamilyRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo);

	int getCreateRequestCount();

	int getUpdateRequestCount();

	int getChangeRequestCount();

	ApplicationBase getApplicationBase(Long id);

	boolean updateRecordStatus(Long id, String comment, MDMRecordStatus mDMRecordStatus);

	boolean updateFamilyRecordStatus(String familyId, String comment, MDMRecordStatus mDMRecordStatus);

	boolean updateStatusToDeactivate(Long recordId, MDMRecordStatus currentRecordStatus);

	boolean updateStatusToDropped(List<Long> recordIdSet, MDMRecordStatus currentRecordStatus);

	boolean updateDeactivateNewRecordStatus(Long recordId, MDMRecordStatus mdmRecordStatus);

	boolean updateDroppedNewRecordStatus(List<Long> recordIdSet, MDMRecordStatus mdmRecordStatus);

	ApplicationBase findByApplicationNoAndJurisdictionCode(String jurisdiction, String applicationNo);

	MdmRecordDTO fetchApplicationDetails(final long applicationId);

	List<MdmRecordDTO> findByApplicationNoAndJurisdiction(final String jurisdiction, final String applicationNo);

	List<MdmRecordDTO> findByFamily(final String family);

	List<MdmRecordDTO> findByAttorneyDocket(final String attorneyDocket);

	List<MdmRecordDTO> findByFamilyAndRecordStatus(long recordId, String familyId, MDMRecordStatus mdmRecordStatus);

	long updateAssignee(String familyId, String strAssignee);

	List<ActionsBaseDto> getActionsBaseDTOfromBase()throws ApplicationException;

	ApplicationBase findActiveAppByApplicationNo(String jurisdiction, String applicationNo);

	SearchResult<MdmRecordDTO> allFamilyApplications(ActiveRecordsFilter filter , final String familyId, final long appId, final String viewName);

	int countDeactivateAffectedTransactions(Long recorId);

	int countDeleteAffectedTransactions(List<Long> recorId);

	Long findByPublicationNumberAndJurisdiction(String publicationNumber, String jurisdictionCode);

	boolean exists(String jurisdiction, String applicationNo);

	void saveDeactivatedRecordToExclusionList(ExclusionList exclusionList);

	boolean updateExclusionListStatus(ExclusionList exclusionList, ApplicationNumberStatus status);

	MdmRecordDTO fetchApplicationData(final Long applicationId);

	boolean updateNewRecordStatus(List<Long> id, String comment, MDMRecordStatus mdmRecordStatus);

	long rejectCreateAppRequest(long entityId, EntityName entityName);

	long countChangeRequestRecords();

	long countUpdateReqApplicationRecords();

	long countUpdateReqAssigneeRecords();

	long countUpdateReqFamilyRecords();

	long countCreateAppRecords();

	long countCreateFamilyRecords();

	long rejectUpdateFamilyRequest(long entityId, EntityName entityName);

	AttorneyDocketFormat getAttorneyDocketFormat();

	String getAttorneyDocketSeperator();

	long acceptCreateAppRequest(Long notificationId);

	Long canAccessApplication(final Long appId);

	long countFamilyApplications(String familyId);

	List<ApplicationBase> getDefaultAssigneeApplications();

	long checkIfNotificationExists(long entityId);

	List<AssigneeAttorneyDocketNo> getAssigneeAttorneyDocketNo(String segmentValue);

	long acceptCreateFamilyRequest(Long notificationId);

	ApplicationDetailsDTO fetchAppInfoForIDS(long appId);

	Assignee findAssignee(final long applicationId);
	
	List<ApplicationBase> getApplicationsByProsecutionStatus();

	String findFamilyOfApplication(long applicationId);

	List<Long> findAllApplicationIdListOfFamily(String family, Long applicationId);
	
	List<Long> findFamilyMembers(final long applicationId,final String familyId);

	ApplicationDetailsDTO fetchAppInfoForNotification(final long appId);

	String findFamilyByApplication(String jurisdiction, String applicationNo);

}
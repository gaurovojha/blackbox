package com.blackbox.ids.services.mdm;

import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.dto.IDS.ApplicationDetailsDTO;
import com.blackbox.ids.core.dto.mdm.DraftIdentityDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilyDetailDTO;
import com.blackbox.ids.core.dto.mdm.family.FamilySearchFilter;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationValidationStatus;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.dto.mdm.ApplicationValidationDTO;
import com.blackbox.ids.dto.mdm.DocketNoValidationStatus;

public interface ApplicationService {

	/*- --------------------------------- Validation APIs for Application No. -- */
	ApplicationValidationStatus validateApplicationNo(final ApplicationValidationDTO appValidationData);

	DocketNoValidationStatus validateDocketNo(final String docketNo, final String assignee);

	boolean isUnique(String jurisdiction, String applicationNo);

	String convertApplicationNo(final ApplicationValidationDTO appValidationData);

	boolean isExcluded(String jurisdiction, String applicationNo);
	/*- --------------------------------- End of validation APIs for Application No. -- */

	List<FamilyDetailDTO> fetchFamilyDetail(final FamilySearchFilter searchFilter);

	FamilyDetailDTO fetchFamilyDetails(final String familyId);

	ApplicationBase findByApplicationNo(final String jurisdiction, final String convertedApplicationNo);

	MdmRecordDTO fetchApplicationDetails(final long applicationId);

	List<MdmRecordDTO> fetchApplicationByApplicationNoAndJurisdiction(final String jurisdiction, final String applicationNo);

	List<MdmRecordDTO> fetchApplicationByFamily(final String family);

	List<MdmRecordDTO> fetchApplicationByFamilyAndRecordStatus(long recordId, final String family, MDMRecordStatus mdmRecordStatus);

	List<MdmRecordDTO> fetchApplicationByAttorneyDocket(final String attorneyDocket);

	void saveManualApplications(final List<ApplicationBase> applications, Long notification);

	MdmRecordDTO fetchApplicationDataForUpdate(final long applicationId);

	void updateApplication(final ApplicationBase application , Long notificationId);

	long rejectCreateAppRequest(long entityId, String entityName, long notificationId);

	long rejectUpdateFamilyRequest(long entityId, String entityName, long notificationId);

	void saveApplicationDrafts(List<ApplicationStage> drafts);

	void deleteDrafts(List<DraftIdentityDTO> draftIds);

	long countFamilyApplications(final String familyId);

	long sendUpdateAssigneeNotification();

	Assignee getAssigneeFromDocketNo(String attorneyDocketNumber);

	Assignee getApplicationAssignee(final long applicationId);

	void updateApplication(final ApplicationDetailsDTO appDetails);

	boolean validateDocketNumber(String docketNumber);

	void saveImportedApplications(ApplicationStage firstFilingCase,
			Map<Boolean, List<ApplicationStage>> validatedApplicationMap);

	void saveApplicationStage(List<ApplicationStage> applicationList);

	void updateApplicationStageStatus(List<Long> applicationIds, QueueStatus newStatus,
			ApplicationValidationStatus validationStatus, long updatedBy);

	/*boolean assigneeDocketNoMappingExist(String attorneyDocketNumber) ;*/

}

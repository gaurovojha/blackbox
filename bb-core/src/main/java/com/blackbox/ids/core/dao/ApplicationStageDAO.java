/**
 *
 */
package com.blackbox.ids.core.dao;

import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.dto.mdm.ActionsBaseDto;
import com.blackbox.ids.core.dto.mdm.DraftDTO;
import com.blackbox.ids.core.dto.mdm.DraftIdentityDTO;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationValidationStatus;

/**
 * The {@code ApplicationStageDAO} exposes APIs to perform database operations on entity class {@link ApplicationStage}.
 *
 * @author ajay2258
 */
public interface ApplicationStageDAO extends BaseDAO<ApplicationStage, Long> {

	List<DraftDTO> getUserApplicationDrafts(final Long createdBy) throws ApplicationException;

	List<ActionsBaseDto> getActionBaseDTOStaging()  throws ApplicationException ;

	List<ApplicationStage> findApplicationStageByNumberJurisdiction(String jurisdiction, String applicationNo)
			throws ApplicationException;

	Map<DraftIdentityDTO, Long> mapApplicationDrafts(List<DraftIdentityDTO> draftIds);
	
	
	long delete(List<DraftIdentityDTO> draftIds);

	
	/**
	 * Get Application nos for given customers for stage table
	 * @param customerNos
	 * @return
	 * @throws ApplicationException
	 */
	
	List<ApplicationStage> getApplicationStageForCustomers(final List<String> customerNos) throws ApplicationException;

	Map<String, List<ApplicationStage>> getApplicationsToImport();

	List<ApplicationStage> getPendingApplicationsByStatus(QueueStatus status, int noOfDays);

	void updateApplicationStatus(List<Long> applicationIds, QueueStatus newStatus,
			ApplicationValidationStatus validationStatus, long updatedBy);
}

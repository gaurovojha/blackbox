/**
 *
 */
package com.blackbox.ids.workflows.ids.task;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.log4j.Logger;

import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.services.ids.IDSBuildService;
import com.blackbox.ids.workflows.ids.common.IDSBuildConst;

/**
 * @author ajay2258
 *
 */
public class NotifyAttorney extends IDSTask {

	private final Logger log = Logger.getLogger(NotifyAttorney.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		/*-
		 * - Update database state
		 * - Create/resume notification for IDS approval to attorney
		 * - Sends an email to attorney
		 */
		Map<String, Object> processData = execution.getVariables();
		Long ids = (Long) processData.get(IDSBuildConst.IDS_dbID);
		Long attorney = (Long) processData.get(IDSBuildConst.ATTORNEY_USER);
		Long approvalNotificationId = null;

		boolean resumeNotification = false;
		if (processData.containsKey(IDSBuildConst.CHANGES_REQUESTED)) {
			resumeNotification = (Boolean) processData.get(IDSBuildConst.CHANGES_REQUESTED);
		}
		if (resumeNotification) {
			approvalNotificationId = (Long) processData.get(IDSBuildConst.APPROVAL_NOTIFICATION_ID);
		}

		log.info(String.format("Submitting IDS {0} for approval to Attorney {1}.", ids, attorney));
		IDSBuildService idsBuildService = (IDSBuildService) this.idsBuildService.getValue(execution);
		approvalNotificationId = idsBuildService.raiseIDSApprovalRequest(ids, attorney, approvalNotificationId);

		Map<String, Object> variables = new HashMap<>(2);
		variables.put(IDSBuildConst.APPROVAL_NOTIFICATION_ID, approvalNotificationId);
		variables.put(IDSBuildConst.IDS_STATUS, IDS.Status.PENDING_APPROVAL);
		variables.put(IDSBuildConst.CHANGES_REQUESTED, false);
		execution.setVariables(variables);
	}

}

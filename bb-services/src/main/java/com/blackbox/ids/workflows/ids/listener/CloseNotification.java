/**
 *
 */
package com.blackbox.ids.workflows.ids.listener;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.log4j.Logger;

import com.blackbox.ids.core.common.BbxApplicationContextUtil;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.workflows.ids.common.ControlFlags;
import com.blackbox.ids.workflows.ids.common.ControlFlags.AttorneyResponse;
import com.blackbox.ids.workflows.ids.common.IDSBuildConst;

/**
 * @author ajay2258
 *
 */
public class CloseNotification implements TaskListener {

	/** The serial version UID. */
	private static final long serialVersionUID = 414025818945924373L;

	private final Logger log = Logger.getLogger(CloseNotification.class);

	@Override
	public void notify(DelegateTask delegateTask) {
		/*- Close/resume the approval request notification raised to the attorney. */
		Map<String, Object> processData = delegateTask.getVariables();
		Long ids = (Long) processData.get(IDSBuildConst.IDS_dbID);
		Long attorney = (Long) processData.get(IDSBuildConst.ATTORNEY_USER);


		log.info(String.format("[PROCESSING ATTORNEY RESPONSE] IDS: {0}, Response: {1}.", ids, attorney));

		NotificationProcessService notificationServcie = (NotificationProcessService) BbxApplicationContextUtil
				.getBean(NotificationProcessService.BEAN_NAME);

		AttorneyResponse response = AttorneyResponse
				.fromString((String) processData.get(ControlFlags.AttorneyResponse.KEY));
		Long approvalNotificationId = (Long) processData.get(IDSBuildConst.APPROVAL_NOTIFICATION_ID);

		switch (response) {
		case APPROVE:
			// Close notification
			notificationServcie.completeTask(approvalNotificationId, NotificationStatus.COMPLETED);
			break;

		case DISCARD_IDS:
			// Close notification
			notificationServcie.completeTask(approvalNotificationId, NotificationStatus.REJECTED);
			break;

		case REQUEST_CHANGE:
			// Assign notification back to the paralegal user
			notificationServcie.requestNotificationInfo(approvalNotificationId);
			break;

		default:
			throw new ActivitiIllegalArgumentException(String.format("Unhandled attorney response {0}.", response));
		}

		Map<String, Object> variables = new HashMap<>();
		variables.put(IDSBuildConst.LAST_UPDATED_BY, attorney);
		delegateTask.setVariables(variables);
	}

}

package com.blackbox.ids.services.notification.process;

import java.util.List;
import java.util.Set;

import org.activiti.engine.task.Task;

import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcess;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.workflows.notification.NotificationAuditData;

/**
 * The Interface WorkflowProcessService.
 */
public interface NotificationProcessService {

	/** The bean name. */
	String BEAN_NAME = "notificationProcessService";

	/**
	 * Complete task.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @param status
	 *            the status
	 */
	void completeTask(Long notificationProcessId, NotificationStatus status);

	/**
	 * Assign notificatioin to user.
	 *
	 * @param userId
	 *            the user id
	 * @param notificationProcessId
	 *            the notification process id
	 */
	void assignNotificationToUser(Long userId, Long notificationProcessId);

	/**
	 * Unassign notificatioin.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 */
	void unassignNotificatioin(Long notificationProcessId);

	/**
	 * Complete notification.
	 *
	 * @param notificationActivitiId
	 *            the notification activiti id
	 * @param status
	 *            the status
	 */
	void completeNotification(String notificationActivitiId, NotificationStatus status);

	/**
	 * Delete notification.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @param reason
	 *            the reason
	 */
	void deleteNotification(Long notificationProcessId, String reason);

	/**
	 * Gets the tasks by user.
	 *
	 * @param userId
	 *            the user id
	 * @return the tasks by user
	 */
	List<Task> getTasksByUser(String userId);

	/**
	 * Gets the task by id.
	 *
	 * @param taskId
	 *            the task id
	 * @return the task by id
	 */
	Task getTaskById(String taskId);

	/**
	 * Gets the notification process id by activiti id.
	 *
	 * @param activitiProcessId
	 *            the activiti process id
	 * @return the notification process id by activiti id
	 */
	long getNotificationProcessIdByActivitiId(String activitiProcessId);

	/**
	 * Gets the activiti id by notification process id.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @return the activiti id by notification process id
	 */
	String getActivitiIdByNotificationProcessId(Long notificationProcessId);

	/**
	 * Complete notification level.
	 *
	 * @param notificationProcessId            the notification process id
	 * @param roleIds            the role ids
	 * @param currentLevel            the current level
	 * @param isDelegate the is delegate
	 * @param status the status
	 * @return the long
	 */
	long completeNotificationLevel(Long notificationProcessId, Set<Long> roleIds,
			int currentLevel, Boolean isDelegate, NotificationStatus status);

	/**
	 * Delete notification for app id.
	 *
	 * @param applicationId
	 *            the application id
	 * @param reason
	 *            the reason
	 */
	void deleteNotificationForAppId(Long applicationId, String reason);

	/**
	 * Delete notification for correspondence id.
	 *
	 * @param correspondenceId
	 *            the correspondence id
	 * @param reason
	 *            the reason
	 */
	void deleteNotificationForCorrespondenceId(Long correspondenceId, String reason);

	/**
	 * Delete notification for reference id.
	 *
	 * @param referenceId
	 *            the reference id
	 * @param reason
	 *            the reason
	 */
	void deleteNotificationForReferenceId(Long referenceId, String reason);

	/**
	 * Gets the entity id by notification id.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @return the entity id by notification id
	 */
	Long getEntityIdByNotificationId(Long notificationProcessId);

	/**
	 * Fetches the users, eligible to receive notifications of given type,
	 * assuming currently logged in user as the notification sender. Parameter
	 * applicationId is to used to filter users based on users' data access
	 * rights.
	 *
	 * @param notificationType
	 *            Identification type for the notification type
	 * @param applicationId
	 *            Database identifier for application Id
	 * @return List with user Id as value.
	 */
	List<Long> getReceiverUsers(final NotificationProcessType notificationType, final Long applicationId);

	/**
	 * Creates the notification.
	 *
	 * @param application
	 *            the application
	 * @param referenceId
	 *            the reference id
	 * @param correspondenceId
	 *            the correspondence id
	 * @param entityId
	 *            the entity id
	 * @param entityName
	 *            the entity name
	 * @param notificationProcessType
	 *            the notification process type
	 * @param userId
	 *            the user id
	 * @return the long
	 */
	Long createNotification(ApplicationBase application, Long referenceId, Long correspondenceId, Long entityId,
			EntityName entityName, NotificationProcessType notificationProcessType, List<Long> userId
			, List<String> filePaths);

	/**
	 * Delegate notifications.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @param roleId
	 *            the role id
	 */
	void delegateNotifications(List<Long> notificationProcessId, Long roleId);

	/**
	 * Restart notification.
	 *
	 * @param notificationProcessId            the notification process id
	 * @param userIds the user ids
	 */
	void restartNotification(Long notificationProcessId, List<Long> userIds);

	/**
	 * Resubmit notification.
	 *
	 * @param notificationProcessId            the notification process id
	 * @param userIds the user ids
	 */
	void resubmitNotification(Long notificationProcessId, List<Long> userIds);

	/**
	 * Request notification info.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 */
	void requestNotificationInfo(Long notificationProcessId);
	
	/**
	 * Response notification info.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 */
	void responseNotificationInfo(Long notificationProcessId);

	/**
	 * Gets the history.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @return the history
	 */
	void getHistory(Long notificationProcessId);

	/**
	 * Sets the notification audit data.
	 *
	 * @param notificationProcess
	 *            the notification process
	 * @param notificationStatus
	 *            the notification status
	 */
	void setNotificationAuditData(NotificationProcess notificationProcess, NotificationStatus notificationStatus);

	/**
	 * Removes the deleted role ids.
	 *
	 * @param receiverRoleIds
	 *            the receiver role ids
	 */
	void removeDeletedRoleIds(Set<Long> receiverRoleIds);
	
	/**
	 * Check and update role ids for delegation rule.
	 *
	 * @param receiverRoleIds
	 *            the receiver role ids
	 * @return the boolean
	 */
	Boolean checkAndUpdateRoleIdsForDelegationRule(Set<Long> receiverRoleIds);
	
	/**
	 * Gets the notification audit data.
	 *
	 * @param notificationProcessId the notification process id
	 * @return the notification audit data
	 */
	List<NotificationAuditData> getNotificationAuditData(Long notificationProcessId);
	
	/**
	 * Checks if is notification eligible for delegation.
	 *
	 * @param notificationProcessId the notification process id
	 * @return true, if is notification eligible for delegation
	 */
	boolean isNotificationEligibleForDelegation(Long notificationProcessId);
}

package com.blackbox.ids.workflows.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;

/**
 * The Class NotificationAuditData.
 */
public class NotificationAuditData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4278858378601860004L;

	/** The level. */
	int level;

	/** The notification process type. */
	NotificationProcessType notificationProcessType;

	/** The sub notification process type. */
	NotificationProcessType subNotificationProcessType;

	/** The current status. */
	NotificationStatus currentStatus;

	/** The previous status. */
	NotificationStatus previousStatus;

	/** The is restarted. */
	Boolean isRestarted;

	/** The is delegated. */
	Boolean isDelegated;

	/** The notification sent date. */
	Calendar notificationSentDate;

	/** The notification sent by. */
	Long notificationSentBy;

	/** The action taken date. */
	Calendar actionTakenDate;

	/** The action taken by. */
	Long actionTakenBy;

	/** The current assigned roles. */
	List<Long> currentAssignedRoles;

	/** The current assigned users. */
	List<Long> currentAssignedUsers;
	
	/** The level complete by. */
	Long levelCompleteBy;
	
	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 *
	 * @param level
	 *            the new level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Gets the notification process type.
	 *
	 * @return the notification process type
	 */
	public NotificationProcessType getNotificationProcessType() {
		return notificationProcessType;
	}

	/**
	 * Sets the notification process type.
	 *
	 * @param notificationProcessType
	 *            the new notification process type
	 */
	public void setNotificationProcessType(NotificationProcessType notificationProcessType) {
		this.notificationProcessType = notificationProcessType;
	}

	/**
	 * Gets the sub notification process type.
	 *
	 * @return the sub notification process type
	 */
	public NotificationProcessType getSubNotificationProcessType() {
		return subNotificationProcessType;
	}

	/**
	 * Sets the sub notification process type.
	 *
	 * @param subNotificationProcessType
	 *            the new sub notification process type
	 */
	public void setSubNotificationProcessType(NotificationProcessType subNotificationProcessType) {
		this.subNotificationProcessType = subNotificationProcessType;
	}

	/**
	 * Gets the current status.
	 *
	 * @return the current status
	 */
	public NotificationStatus getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * Sets the current status.
	 *
	 * @param currentStatus
	 *            the new current status
	 */
	public void setCurrentStatus(NotificationStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	/**
	 * Gets the previous status.
	 *
	 * @return the previous status
	 */
	public NotificationStatus getPreviousStatus() {
		return previousStatus;
	}

	/**
	 * Sets the previous status.
	 *
	 * @param previousStatus
	 *            the new previous status
	 */
	public void setPreviousStatus(NotificationStatus previousStatus) {
		this.previousStatus = previousStatus;
	}

	/**
	 * Gets the checks if is restarted.
	 *
	 * @return the checks if is restarted
	 */
	public Boolean getIsRestarted() {
		return isRestarted;
	}

	/**
	 * Sets the checks if is restarted.
	 *
	 * @param isRestarted
	 *            the new checks if is restarted
	 */
	public void setIsRestarted(Boolean isRestarted) {
		this.isRestarted = isRestarted;
	}

	/**
	 * Gets the checks if is delegated.
	 *
	 * @return the checks if is delegated
	 */
	public Boolean getIsDelegated() {
		return isDelegated;
	}

	/**
	 * Sets the checks if is delegated.
	 *
	 * @param isDelegated
	 *            the new checks if is delegated
	 */
	public void setIsDelegated(Boolean isDelegated) {
		this.isDelegated = isDelegated;
	}

	/**
	 * Gets the notification sent date.
	 *
	 * @return the notification sent date
	 */
	public Calendar getNotificationSentDate() {
		return notificationSentDate;
	}

	/**
	 * Sets the notification sent date.
	 *
	 * @param notificationSentDate
	 *            the new notification sent date
	 */
	public void setNotificationSentDate(Calendar notificationSentDate) {
		this.notificationSentDate = notificationSentDate;
	}

	/**
	 * Gets the notification sent by.
	 *
	 * @return the notification sent by
	 */
	public Long getNotificationSentBy() {
		return notificationSentBy;
	}

	/**
	 * Sets the notification sent by.
	 *
	 * @param notificationSentBy
	 *            the new notification sent by
	 */
	public void setNotificationSentBy(Long notificationSentBy) {
		this.notificationSentBy = notificationSentBy;
	}

	/**
	 * Gets the action taken date.
	 *
	 * @return the action taken date
	 */
	public Calendar getActionTakenDate() {
		return actionTakenDate;
	}

	/**
	 * Sets the action taken date.
	 *
	 * @param actionTakenDate
	 *            the new action taken date
	 */
	public void setActionTakenDate(Calendar actionTakenDate) {
		this.actionTakenDate = actionTakenDate;
	}

	/**
	 * Gets the action taken by.
	 *
	 * @return the action taken by
	 */
	public Long getActionTakenBy() {
		return actionTakenBy;
	}

	/**
	 * Sets the action taken by.
	 *
	 * @param actionTakenBy
	 *            the new action taken by
	 */
	public void setActionTakenBy(Long actionTakenBy) {
		this.actionTakenBy = actionTakenBy;
	}

	/**
	 * Gets the current assigned roles.
	 *
	 * @return the current assigned roles
	 */
	public List<Long> getCurrentAssignedRoles() {
		if (currentAssignedRoles == null) {
			currentAssignedRoles = new ArrayList<>();
		}
		return currentAssignedRoles;
	}

	/**
	 * Sets the current assigned roles.
	 *
	 * @param currentAssignedRoles
	 *            the new current assigned roles
	 */
	public void setCurrentAssignedRoles(List<Long> currentAssignedRoles) {
		this.currentAssignedRoles = currentAssignedRoles;
	}

	/**
	 * Gets the current assigned users.
	 *
	 * @return the current assigned users
	 */
	public List<Long> getCurrentAssignedUsers() {
		if (currentAssignedUsers == null) {
			currentAssignedUsers = new ArrayList<>();
		}
		return currentAssignedUsers;
	}

	/**
	 * Sets the current assigned users.
	 *
	 * @param currentAssignedUsers
	 *            the new current assigned users
	 */
	public void setCurrentAssignedUsers(List<Long> currentAssignedUsers) {
		this.currentAssignedUsers = currentAssignedUsers;
	}

	public Long getLevelCompleteBy() {
		return levelCompleteBy;
	}

	public void setLevelCompleteBy(Long levelCompleteBy) {
		this.levelCompleteBy = levelCompleteBy;
	}
}

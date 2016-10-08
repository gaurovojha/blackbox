package com.blackbox.ids.dto.notification;

import java.util.Calendar;
import java.util.List;

/**
 * The Class NotificationBusinessRuleDTO.
 */
public class NotificationBusinessRuleDTO {

	/** The notification business rule id. */
	private Long notificationBusinessRuleId;

	/** The business level dto. */
	private List<NotificationBusinessRuleLevelDTO> businessLevelDto;

	/** The updated on. */
	private Calendar updatedOn;

	/** The created on. */
	private Calendar createdOn;

	/** The created by. */
	private long createdBy;

	/** The updated by. */
	private long updatedBy;

	/** The Notification id. */
	private long notificationId;

	/** The is system initiated. */
	private boolean isSystemInitiated = false;

	/** The updated by usename. */
	private String updatedByUsename;

	/**
	 * Gets the notification business rule id.
	 *
	 * @return the notification business rule id
	 */
	public Long getNotificationBusinessRuleId() {
		return notificationBusinessRuleId;
	}

	/**
	 * Sets the notification business rule id.
	 *
	 * @param notificationBusinessRuleId
	 *            the new notification business rule id
	 */
	public void setNotificationBusinessRuleId(Long notificationBusinessRuleId) {
		this.notificationBusinessRuleId = notificationBusinessRuleId;
	}

	/**
	 * Gets the business level dto.
	 *
	 * @return the business level dto
	 */
	public List<NotificationBusinessRuleLevelDTO> getBusinessLevelDto() {
		return businessLevelDto;
	}

	/**
	 * Sets the business level dto.
	 *
	 * @param businessLevelDto
	 *            the new business level dto
	 */
	public void setBusinessLevelDto(List<NotificationBusinessRuleLevelDTO> businessLevelDto) {
		this.businessLevelDto = businessLevelDto;
	}

	/**
	 * Gets the updated on.
	 *
	 * @return the updated on
	 */
	public Calendar getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * Sets the updated on.
	 *
	 * @param updatedOn
	 *            the new updated on
	 */
	public void setUpdatedOn(Calendar updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * Gets the notification id.
	 *
	 * @return the notification id
	 */
	public long getNotificationId() {
		return notificationId;
	}

	/**
	 * Sets the notification id.
	 *
	 * @param notificationId
	 *            the new notification id
	 */
	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * Gets the created on.
	 *
	 * @return the created on
	 */
	public Calendar getCreatedOn() {
		return createdOn;
	}

	/**
	 * Sets the created on.
	 *
	 * @param createdOn
	 *            the new created on
	 */
	public void setCreatedOn(Calendar createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Gets the created by.
	 *
	 * @return the created by
	 */
	public long getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy
	 *            the new created by
	 */
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets the updated by.
	 *
	 * @return the updated by
	 */
	public long getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * Sets the updated by.
	 *
	 * @param updatedBy
	 *            the new updated by
	 */
	public void setUpdatedBy(long updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * Checks if is system initiated.
	 *
	 * @return true, if is system initiated
	 */
	public boolean isSystemInitiated() {
		return isSystemInitiated;
	}

	/**
	 * Sets the system initiated.
	 *
	 * @param isSystemInitiated
	 *            the new system initiated
	 */
	public void setSystemInitiated(boolean isSystemInitiated) {
		this.isSystemInitiated = isSystemInitiated;
	}

	/**
	 * Gets the updated by usename.
	 *
	 * @return the updated by usename
	 */
	public String getUpdatedByUsename() {
		return updatedByUsename;
	}

	/**
	 * Sets the updated by usename.
	 *
	 * @param updatedByUsename
	 *            the new updated by usename
	 */
	public void setUpdatedByUsename(String updatedByUsename) {
		this.updatedByUsename = updatedByUsename;
	}
}

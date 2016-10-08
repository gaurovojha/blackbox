package com.blackbox.ids.dto.notification;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.blackbox.ids.dto.RoleDTO;

/**
 * The Class NotificationDTO.
 */
public class NotificationDTO {

	/** The notification name. */
	private String notificationName;

	/** The notification type. */
	private String notificationType;

	/** The display on ids review. */
	private String displayOnIDSReview;

	/** The email notification. */
	private String emailNotification;

	/** The reminder. */
	private String reminder;

	/** The escalation. */
	private String escalation;

	/** The no of business rules. */
	private int noOfBusinessRules;

	/** The notification id. */
	private Long notificationId;

	/** The notification subject. */
	private String notificationSubject;

	/** The notification message. */
	private String notificationMessage;

	/** The notification level no. */
	private int notificationLevelNo;

	/** The notificationcode. */
	private String notificationcode;

	/** The no ofreminders. */
	private int noOfreminders;

	/** The frequency of sending remindes. */
	private int frequencyOfSendingRemindes;

	/** The no of past due days. */
	private int noOfPastDueDays;

	/** The dispaly while preparing ids. */
	private String dispalyWhilePreparingIDS;

	/** The business rule dtos. */
	private Set<NotificationBusinessRuleDTO> businessRuleDtos;

	/** The updated by. */
	private Long createdBy;

	/** The updated on. */
	private Calendar createdOn;

	/** The updated by. */
	private Long updatedBy;

	/** The updated on. */
	private Calendar updatedOn;

	/** The updated by usename. */
	private String updatedByUsename;

	/** The role dto. */
	private List<RoleDTO> roleDTOs;

	/** The escalation roles. */
	private Set<NotificationEscalationRoleDTO> escalationRoles;

	/** The escalation roles. */
	private Set<NotificationEscalationRoleDTO> defaultRoles;

	/** The escalation selected roles. */
	private String escalationSelectedRoles;

	/**
	 * Gets the escalation selected roles.
	 *
	 * @return the escalation selected roles
	 */
	public String getEscalationSelectedRoles() {
		return escalationSelectedRoles;
	}

	/**
	 * Sets the escalation selected roles.
	 *
	 * @param escalationSelectedRoles
	 *            the new escalation selected roles
	 */
	public void setEscalationSelectedRoles(String escalationSelectedRoles) {
		this.escalationSelectedRoles = escalationSelectedRoles;
	}

	/**
	 * Gets the role dt os.
	 *
	 * @return the role dt os
	 */
	public List<RoleDTO> getRoleDTOs() {
		return roleDTOs;
	}

	/**
	 * Sets the role dt os.
	 *
	 * @param roleDTOs
	 *            the new role dt os
	 */
	public void setRoleDTOs(List<RoleDTO> roleDTOs) {
		this.roleDTOs = roleDTOs;
	}

	/**
	 * Gets the updated by.
	 *
	 * @return the updated by
	 */
	public Long getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * Sets the updated by.
	 *
	 * @param updatedBy
	 *            the new updated by
	 */
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
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
	 * Gets the frequency of sending remindes.
	 *
	 * @return the frequency of sending remindes
	 */
	public int getFrequencyOfSendingRemindes() {
		return frequencyOfSendingRemindes;
	}

	/**
	 * Sets the frequency of sending remindes.
	 *
	 * @param frequencyOfSendingRemindes
	 *            the new frequency of sending remindes
	 */
	public void setFrequencyOfSendingRemindes(int frequencyOfSendingRemindes) {
		this.frequencyOfSendingRemindes = frequencyOfSendingRemindes;
	}

	/**
	 * Gets the notification id.
	 *
	 * @return the notification id
	 */
	public Long getNotificationId() {
		return notificationId;
	}

	/**
	 * Gets the notification level no.
	 *
	 * @return the notification level no
	 */
	public int getNotificationLevelNo() {
		return notificationLevelNo;
	}

	/**
	 * Sets the notification level no.
	 *
	 * @param notificationLevelNo
	 *            the new notification level no
	 */
	public void setNotificationLevelNo(int notificationLevelNo) {
		this.notificationLevelNo = notificationLevelNo;
	}

	/**
	 * Sets the notification id.
	 *
	 * @param notificationId
	 *            the new notification id
	 */
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * Gets the no ofreminders.
	 *
	 * @return the no ofreminders
	 */
	public int getNoOfreminders() {
		return noOfreminders;
	}

	/**
	 * Sets the no ofreminders.
	 *
	 * @param noOfreminders
	 *            the new no ofreminders
	 */
	public void setNoOfreminders(int noOfreminders) {
		this.noOfreminders = noOfreminders;
	}

	/**
	 * Gets the notificationcode.
	 *
	 * @return the notificationcode
	 */
	public String getNotificationcode() {
		return notificationcode;
	}

	/**
	 * Sets the notificationcode.
	 *
	 * @param notificationcode
	 *            the new notificationcode
	 */
	public void setNotificationcode(String notificationcode) {
		this.notificationcode = notificationcode;
	}

	/**
	 * Gets the notification name.
	 *
	 * @return the notification name
	 */
	public String getNotificationName() {
		return notificationName;
	}

	/**
	 * Sets the notification name.
	 *
	 * @param notificationName
	 *            the new notification name
	 */
	public void setNotificationName(String notificationName) {
		this.notificationName = notificationName;
	}

	/**
	 * Gets the notification type.
	 *
	 * @return the notification type
	 */
	public String getNotificationType() {
		return notificationType;
	}

	/**
	 * Sets the notification type.
	 *
	 * @param notificationType
	 *            the new notification type
	 */
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	/**
	 * Gets the display on ids review.
	 *
	 * @return the display on ids review
	 */
	public String getDisplayOnIDSReview() {
		return displayOnIDSReview;
	}

	/**
	 * Sets the display on ids review.
	 *
	 * @param displayOnIDSReview
	 *            the new display on ids review
	 */
	public void setDisplayOnIDSReview(String displayOnIDSReview) {
		this.displayOnIDSReview = displayOnIDSReview;
	}

	/**
	 * Gets the email notification.
	 *
	 * @return the email notification
	 */
	public String getEmailNotification() {
		return emailNotification;
	}

	/**
	 * Sets the email notification.
	 *
	 * @param emailNotification
	 *            the new email notification
	 */
	public void setEmailNotification(String emailNotification) {
		this.emailNotification = emailNotification;
	}

	/**
	 * Gets the reminder.
	 *
	 * @return the reminder
	 */
	public String getReminder() {
		return reminder;
	}

	/**
	 * Sets the reminder.
	 *
	 * @param reminder
	 *            the new reminder
	 */
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

	/**
	 * Gets the escalation.
	 *
	 * @return the escalation
	 */
	public String getEscalation() {
		return escalation;
	}

	/**
	 * Sets the escalation.
	 *
	 * @param escalation
	 *            the new escalation
	 */
	public void setEscalation(String escalation) {
		this.escalation = escalation;
	}

	/**
	 * Gets the no of business rules.
	 *
	 * @return the no of business rules
	 */
	public int getNoOfBusinessRules() {
		return noOfBusinessRules;
	}

	/**
	 * Sets the no of business rules.
	 *
	 * @param noOfBusinessRules
	 *            the new no of business rules
	 */
	public void setNoOfBusinessRules(int noOfBusinessRules) {
		this.noOfBusinessRules = noOfBusinessRules;
	}

	/**
	 * Gets the notification subject.
	 *
	 * @return the notification subject
	 */
	public String getNotificationSubject() {
		return notificationSubject;
	}

	/**
	 * Sets the notification subject.
	 *
	 * @param notificationSubject
	 *            the new notification subject
	 */
	public void setNotificationSubject(String notificationSubject) {
		this.notificationSubject = notificationSubject;
	}

	/**
	 * Gets the notification message.
	 *
	 * @return the notification message
	 */
	public String getNotificationMessage() {
		return notificationMessage;
	}

	/**
	 * Sets the notification message.
	 *
	 * @param notificationMessage
	 *            the new notification message
	 */
	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	/**
	 * Gets the dispaly while preparing ids.
	 *
	 * @return the dispaly while preparing ids
	 */
	public String getDispalyWhilePreparingIDS() {
		return dispalyWhilePreparingIDS;
	}

	/**
	 * Sets the dispaly while preparing ids.
	 *
	 * @param dispalyWhilePreparingIDS
	 *            the new dispaly while preparing ids
	 */
	public void setDispalyWhilePreparingIDS(String dispalyWhilePreparingIDS) {
		this.dispalyWhilePreparingIDS = dispalyWhilePreparingIDS;
	}

	/**
	 * Gets the no of past due days.
	 *
	 * @return the no of past due days
	 */
	public int getNoOfPastDueDays() {
		return noOfPastDueDays;
	}

	/**
	 * Sets the no of past due days.
	 *
	 * @param noOfPastDueDays
	 *            the new no of past due days
	 */
	public void setNoOfPastDueDays(int noOfPastDueDays) {
		this.noOfPastDueDays = noOfPastDueDays;
	}

	/**
	 * Gets the business rule dtos.
	 *
	 * @return the business rule dtos
	 */
	public Set<NotificationBusinessRuleDTO> getBusinessRuleDtos() {
		return businessRuleDtos;
	}

	/**
	 * Sets the business rule dtos.
	 *
	 * @param businessRuleDtos
	 *            the new business rule dtos
	 */
	public void setBusinessRuleDtos(Set<NotificationBusinessRuleDTO> businessRuleDtos) {
		this.businessRuleDtos = businessRuleDtos;
	}

	/**
	 * Gets the escalation roles.
	 *
	 * @return the escalation roles
	 */
	public Set<NotificationEscalationRoleDTO> getEscalationRoles() {
		if (escalationRoles == null) {
			escalationRoles = new LinkedHashSet<>();
		}

		return escalationRoles;
	}

	/**
	 * Sets the escalation roles.
	 *
	 * @param escalationRoles
	 *            the new escalation roles
	 */
	public void setEscalationRoles(Set<NotificationEscalationRoleDTO> escalationRoles) {
		this.escalationRoles = escalationRoles;
	}

	/**
	 * Gets the created by.
	 *
	 * @return the created by
	 */
	public Long getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy
	 *            the new created by
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
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

	/**
	 * Gets the default roles.
	 *
	 * @return the default roles
	 */
	public Set<NotificationEscalationRoleDTO> getDefaultRoles() {
		if (defaultRoles == null) {
			defaultRoles = new LinkedHashSet<>();
		}
		
		return defaultRoles;
	}

	/**
	 * Sets the default roles.
	 *
	 * @param defaultRoles
	 *            the new default roles
	 */
	public void setDefaultRoles(Set<NotificationEscalationRoleDTO> defaultRoles) {
		this.defaultRoles = defaultRoles;
	}

	/*
	 * public Set<NotificationEscalationRoleDTO> getDefaultRoles() { if (defaultRoles == null) { defaultRoles = new
	 * LinkedHashSet<>(); }
	 * 
	 * return defaultRoles; }
	 * 
	 * public void setDefaultRoles(Set<NotificationEscalationRoleDTO> defaultRoles) { this.defaultRoles = defaultRoles;
	 * }
	 */
}

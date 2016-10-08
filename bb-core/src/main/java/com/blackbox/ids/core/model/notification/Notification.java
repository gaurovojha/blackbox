package com.blackbox.ids.core.model.notification;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;

/**
 * The Class Notification.
 */
@Entity
@Table(name = "BB_NOTIFICATION")
public class Notification extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -357445599887828320L;

	/**
	 * Instantiates a new notification.
	 */
	public Notification() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new notification.
	 *
	 * @param notificationId
	 *            the notification id
	 */
	public Notification(Long notificationId) {
		this.notificationId = notificationId;
	}

	/** The notification id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_NOTIFICATION_ID", unique = true, nullable = false)
	private Long notificationId;

	/** The name. */
	@Column(name = "NAME", nullable = false, length = 500)
	private String name;

	/** The type. */
	@Column(name = "TYPE", nullable = false)
	private String type;

	/** The notification level. */
	@Column(name = "NOTIFICATION_LEVEL", nullable = false)
	private Integer notificationLevel;

	/** The process name. */
	@Column(name = "PROCESS_NAME", nullable = false)
	private String processName;

	/** The code. */
	@Column(name = "CODE")
	private String code;

	/** The Subject. */
	@Column(name = "SUBJECT")
	private String Subject;

	/** The message. */
	@Column(name = "MESSAGE")
	private String message;

	/** The ids review display. */
	@Column(name = "IDS_REVIEW_DISPLAY")
	private boolean idsReviewDisplay;

	/** The email notification. */
	@Column(name = "EMAIL_NOTIFICATION")
	private boolean emailNotification;

	/** The reminder. */
	@Column(name = "REMINDER")
	private boolean reminder;

	/** The escalation. */
	@Column(name = "ESCALATION")
	private boolean escalation;

	/** The no of past due days. */
	@Column(name = "NO_OF_PAST_DUE_DAYS")
	private Integer noOfPastDueDays;

	/** The no of reminders. */
	@Column(name = "NO_OF_REMINDERS")
	private Integer noOfReminders;

	/** The frequency of sending reminders. */
	@Column(name = "FREQUENCY_OF_SENDING_REMINDERS")
	private Integer frequencyOfSendingReminders;

	/** The active. */
	@Column(name = "IS_ACTIVE", nullable = false)
	private boolean active = true;

	/** The parent notification. */
	@Column(name = "PARENT")
	private Long parentId;

	/** The notification rules. */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "BB_NOTIFICATION_ID", referencedColumnName = "BB_NOTIFICATION_ID")
	@OrderBy("notificationBusinessRuleId DESC")
	private Set<NotificationBusinessRule> notificationRules;

	/** The escalation roles. */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "BB_NOTIFICATION_ID", referencedColumnName = "BB_NOTIFICATION_ID")
	private Set<NotificationEscalationRole> escalationRoles;

	/** The escalation roles. */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "BB_NOTIFICATION_ID", referencedColumnName = "BB_NOTIFICATION_ID")
	private Set<NotificationDefaultRole> defaultRoles;

	/** The notification process type. */
	@Column(name = "NOTIFICATION_PROCESS_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationProcessType notificationProcessType;

	/** The Assigned to role. */
	@Column(name = "ASSIGNED_TO_ROLE", nullable = false)
	private Boolean assignedToRole = true;

	/**
	 * Gets the no of reminders.
	 *
	 * @return the no of reminders
	 */
	public Integer getNoOfReminders() {
		return noOfReminders;
	}

	/**
	 * Sets the no of reminders.
	 *
	 * @param noOfReminders
	 *            the new no of reminders
	 */
	public void setNoOfReminders(Integer noOfReminders) {
		this.noOfReminders = noOfReminders;
	}

	/**
	 * Gets the frequency of sending reminders.
	 *
	 * @return the frequency of sending reminders
	 */
	public Integer getFrequencyOfSendingReminders() {
		return frequencyOfSendingReminders;
	}

	/**
	 * Sets the frequency of sending reminders.
	 *
	 * @param frequencyOfSendingReminders
	 *            the new frequency of sending reminders
	 */
	public void setFrequencyOfSendingReminders(Integer frequencyOfSendingReminders) {
		this.frequencyOfSendingReminders = frequencyOfSendingReminders;
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
	 * Sets the notification id.
	 *
	 * @param notificationId
	 *            the new notification id
	 */
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the notification level.
	 *
	 * @return the notification level
	 */
	public Integer getNotificationLevel() {
		return notificationLevel;
	}

	/**
	 * Sets the notification level.
	 *
	 * @param notificationLevel
	 *            the new notification level
	 */
	public void setNotificationLevel(Integer notificationLevel) {
		this.notificationLevel = notificationLevel;
	}

	/**
	 * Gets the process name.
	 *
	 * @return the process name
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * Sets the process name.
	 *
	 * @param processName
	 *            the new process name
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return Subject;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject
	 *            the new subject
	 */
	public void setSubject(String subject) {
		Subject = subject;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Checks if is ids review display.
	 *
	 * @return true, if is ids review display
	 */
	public boolean isIdsReviewDisplay() {
		return idsReviewDisplay;
	}

	/**
	 * Sets the ids review display.
	 *
	 * @param idsReviewDisplay
	 *            the new ids review display
	 */
	public void setIdsReviewDisplay(boolean idsReviewDisplay) {
		this.idsReviewDisplay = idsReviewDisplay;
	}

	/**
	 * Checks if is email notification.
	 *
	 * @return true, if is email notification
	 */
	public boolean isEmailNotification() {
		return emailNotification;
	}

	/**
	 * Sets the email notification.
	 *
	 * @param emailNotification
	 *            the new email notification
	 */
	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	/**
	 * Checks if is reminder.
	 *
	 * @return true, if is reminder
	 */
	public boolean isReminder() {
		return reminder;
	}

	/**
	 * Sets the reminder.
	 *
	 * @param reminder
	 *            the new reminder
	 */
	public void setReminder(boolean reminder) {
		this.reminder = reminder;
	}

	/**
	 * Checks if is escalation.
	 *
	 * @return true, if is escalation
	 */
	public boolean isEscalation() {
		return escalation;
	}

	/**
	 * Sets the escalation.
	 *
	 * @param escalation
	 *            the new escalation
	 */
	public void setEscalation(boolean escalation) {
		this.escalation = escalation;
	}

	/**
	 * Gets the no of past due days.
	 *
	 * @return the no of past due days
	 */
	public Integer getNoOfPastDueDays() {
		return noOfPastDueDays;
	}

	/**
	 * Sets the no of past due days.
	 *
	 * @param noOfPastDueDays
	 *            the new no of past due days
	 */
	public void setNoOfPastDueDays(Integer noOfPastDueDays) {
		this.noOfPastDueDays = noOfPastDueDays;
	}

	/**
	 * Gets the notification rules.
	 *
	 * @return the notification rules
	 */
	public Set<NotificationBusinessRule> getNotificationRules() {
		if (notificationRules == null) {
			notificationRules = new LinkedHashSet<>();
		}

		return notificationRules;
	}

	/**
	 * Sets the notification rules.
	 *
	 * @param notificationRules
	 *            the new notification rules
	 */
	public void setNotificationRules(Set<NotificationBusinessRule> notificationRules) {
		this.notificationRules = notificationRules;
	}

	/**
	 * Gets the escalation roles.
	 *
	 * @return the escalation roles
	 */
	public Set<NotificationEscalationRole> getEscalationRoles() {
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
	public void setEscalationRoles(Set<NotificationEscalationRole> escalationRoles) {
		this.escalationRoles = escalationRoles;
	}

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active
	 *            the new active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets the default roles.
	 *
	 * @return the default roles
	 */
	public Set<NotificationDefaultRole> getDefaultRoles() {
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
	public void setDefaultRoles(Set<NotificationDefaultRole> defaultRoles) {
		this.defaultRoles = defaultRoles;
	}

	/**
	 * Gets the parent id.
	 *
	 * @return the parent id
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * Sets the parent id.
	 *
	 * @param parentId
	 *            the new parent id
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
	 * Gets the assigned to role.
	 *
	 * @return the assigned to role
	 */
	public Boolean getAssignedToRole() {
		return assignedToRole;
	}

	/**
	 * Sets the assigned to role.
	 *
	 * @param assignedToRole
	 *            the new assigned to role
	 */
	public void setAssignedToRole(Boolean assignedToRole) {
		this.assignedToRole = assignedToRole;
	}
}

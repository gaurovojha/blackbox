
package com.blackbox.ids.core.model.notification.process;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.notification.Notification;

/**
 * The Class NotificationProcess.
 */
@Entity
@Table(name = "BB_NOTIFICATION_PROCESS")
public class NotificationProcess extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2500899514491383287L;

	/**
	 * Instantiates a new notification process.
	 */
	public NotificationProcess() {
		super();
	}

	/**
	 * Instantiates a new notification process.
	 *
	 * @param entityId
	 *            the entity id
	 * @param lockedByUser
	 *            the locked by user
	 * @param createdDate
	 *            the created date
	 */
	public NotificationProcess(Long entityId, Long lockedByUser, Calendar createdDate) {
		super();
		this.entityId = entityId;
		this.lockedByUser = new User(lockedByUser);
		this.setCreatedDate(createdDate);
	}

	/**
	 * Instantiates a new notification process.
	 *
	 * @param activitiProcessId
	 *            the activiti process id
	 * @param notifictionProcessType
	 *            the notifiction process type
	 * @param notification
	 *            the notification
	 * @param entityId
	 *            the entity id
	 * @param active
	 *            the active
	 * @param subNotifictionProcessType
	 *            the sub notifiction process type
	 * @param entityName
	 *            the entity name
	 */
	public NotificationProcess(String activitiProcessId, NotificationProcessType notifictionProcessType,
			Notification notification, Long entityId, boolean active, NotificationProcessType subNotifictionProcessType,
			EntityName entityName) {
		this.activitiProcessId = activitiProcessId;
		this.notificationProcessType = notifictionProcessType;
		this.notification = notification;
		this.entityId = entityId;
		this.active = active;
		this.subNotificationProcessType = subNotifictionProcessType;
		this.entityName = entityName;
		this.setUpdatedDate(Calendar.getInstance());
		this.notifiedDate = Calendar.getInstance();
		this.setUpdatedByUser(BlackboxSecurityContextHolder.getUserId());
	}

	/**
	 * Instantiates a new notification process.
	 *
	 * @param activitiProcessId
	 *            the activiti process id
	 * @param notifictionProcessType
	 *            the notifiction process type
	 * @param notification
	 *            the notification
	 * @param entityId
	 *            the entity id
	 * @param active
	 *            the active
	 * @param status
	 *            the status
	 * @param entityName
	 *            the entity name
	 * @param applicationId
	 *            the application id
	 * @param correspondenceId
	 *            the correspondence id
	 * @param referenceId
	 *            the reference id
	 */
	public NotificationProcess(String activitiProcessId, NotificationProcessType notifictionProcessType,
			Notification notification, Long entityId, boolean active, NotificationStatus status, EntityName entityName,
			Long applicationId, Long correspondenceId, Long referenceId) {
		this.activitiProcessId = activitiProcessId;
		this.notificationProcessType = notifictionProcessType;
		this.notification = notification;
		this.entityId = entityId;
		this.active = active;
		this.subNotificationProcessType = notifictionProcessType;
		this.entityName = entityName;
		this.setUpdatedDate(Calendar.getInstance());
		this.notifiedDate = Calendar.getInstance();
		this.setUpdatedByUser(BlackboxSecurityContextHolder.getUserId());
		this.applicationId = applicationId;
		this.correspondenceId = correspondenceId;
		this.referenceId = referenceId;
		this.status = status;
	}

	/** The notification process id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_NOTIFICATION_PROCESS_ID", unique = true, length = 50, nullable = false)
	private Long notificationProcessId;

	/** The activiti process id. */
	@Column(name = "ACTIVITI_PROCESS_ID", nullable = false)
	private String activitiProcessId;

	/** The notification process type. */
	@Column(name = "NOTIFICATION_PROCESS_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationProcessType notificationProcessType;

	/** The sub notification process type. */
	@Column(name = "SUB_NOTIFICATION_PROCESS_TYPE", nullable = true)
	@Enumerated(EnumType.STRING)
	private NotificationProcessType subNotificationProcessType;

	/** The notification. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NOTIFICATION_ID", nullable = false)
	private Notification notification;

	/** The entity name. */
	@Column(name = "ENTITY_NAME")
	@Enumerated(EnumType.STRING)
	private EntityName entityName;

	/** The entity id. */
	@Column(name = "ENTITY_ID")
	private Long entityId;

	/** The status. */
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private NotificationStatus status = NotificationStatus.PENDING;

	/** The locked by user. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCKED_BY_USER", nullable = true)
	private User lockedByUser;

	/** The roles. */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "BB_NOTIFICATION_PROCESS_ROLE", joinColumns = {
			@JoinColumn(name = "BB_NOTIFICATION_PROCESS_ID", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "BB_ROLE_ID", nullable = false) })
	private List<Role> roles;

	/** The users. */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "BB_NOTIFICATION_PROCESS_USER", joinColumns = {
			@JoinColumn(name = "BB_NOTIFICATION_PROCESS_ID", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "BB_USER_ID", nullable = false) })
	private List<User> users;

	/** The active. */
	@Column(name = "ACTIVE", nullable = false)
	private Boolean active = true;

	/** The notified date. */
	@Column(name = "NOTIFIED_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar notifiedDate;

	/** The application id. */
	@Column(name = "BB_APPLICATION_ID")
	private Long applicationId;

	/** The correspondence id. */
	@Column(name = "BB_CORRESPONDENCE_ID")
	private Long correspondenceId;

	/** The reference id. */
	@Column(name = "BB_REFERENCE_BASE_DATA_ID")
	private Long referenceId;

	/** The current level. */
	@Column(name = "CURRENT_LEVEL", nullable = false)
	private Integer currentLevel = 1;

	/** The Delegated. */
	@Column(name = "DELEGATED", nullable = false)
	private Boolean delegated = false;

	/**
	 * Gets the notification process id.
	 *
	 * @return the notification process id
	 */
	public Long getNotificationProcessId() {
		return notificationProcessId;
	}

	/**
	 * Sets the notification process id.
	 *
	 * @param notificationProcessId
	 *            the new notification process id
	 */
	public void setNotificationProcessId(Long notificationProcessId) {
		this.notificationProcessId = notificationProcessId;
	}

	/**
	 * Gets the activiti process id.
	 *
	 * @return the activiti process id
	 */
	public String getActivitiProcessId() {
		return activitiProcessId;
	}

	/**
	 * Sets the activiti process id.
	 *
	 * @param activitIProcessId
	 *            the new activiti process id
	 */
	public void setActivitiProcessId(String activitIProcessId) {
		this.activitiProcessId = activitIProcessId;
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
	 * Gets the entity id.
	 *
	 * @return the entity id
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * Sets the entity id.
	 *
	 * @param entityId
	 *            the new entity id
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public Boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active
	 *            the new active
	 */
	public void setActive(Boolean active) {
		this.active = active;
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
	 * Gets the entity name.
	 *
	 * @return the entity name
	 */
	public EntityName getEntityName() {
		return entityName;
	}

	/**
	 * Sets the entity name.
	 *
	 * @param entityName
	 *            the new entity name
	 */
	public void setEntityName(EntityName entityName) {
		this.entityName = entityName;
	}

	/**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
	public List<Role> getRoles() {
		if (roles == null) {
			roles = new ArrayList<>();
		}
		return roles;
	}

	/**
	 * Sets the roles.
	 *
	 * @param roles
	 *            the new roles
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public NotificationStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(NotificationStatus status) {
		this.status = status;
	}

	/**
	 * Gets the locked by user.
	 *
	 * @return the locked by user
	 */
	public User getLockedByUser() {
		return lockedByUser;
	}

	/**
	 * Sets the locked by user.
	 *
	 * @param lockedByUser
	 *            the new locked by user
	 */
	public void setLockedByUser(User lockedByUser) {
		this.lockedByUser = lockedByUser;
	}

	/**
	 * Gets the notification.
	 *
	 * @return the notification
	 */
	public Notification getNotification() {
		return notification;
	}

	/**
	 * Sets the notification.
	 *
	 * @param notification
	 *            the new notification
	 */
	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	/**
	 * Gets the notified date.
	 *
	 * @return the notified date
	 */
	public Calendar getNotifiedDate() {
		return notifiedDate;
	}

	/**
	 * Sets the notified date.
	 *
	 * @param notifiedDate
	 *            the new notified date
	 */
	public void setNotifiedDate(Calendar notifiedDate) {
		this.notifiedDate = notifiedDate;
	}

	/**
	 * Gets the application id.
	 *
	 * @return the application id
	 */
	public Long getApplicationId() {
		return applicationId;
	}

	/**
	 * Sets the application id.
	 *
	 * @param applicationId
	 *            the new application id
	 */
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * Gets the correspondence id.
	 *
	 * @return the correspondence id
	 */
	public Long getCorrespondenceId() {
		return correspondenceId;
	}

	/**
	 * Sets the correspondence id.
	 *
	 * @param correspondenceId
	 *            the new correspondence id
	 */
	public void setCorrespondenceId(Long correspondenceId) {
		this.correspondenceId = correspondenceId;
	}

	/**
	 * Gets the reference id.
	 *
	 * @return the reference id
	 */
	public Long getReferenceId() {
		return referenceId;
	}

	/**
	 * Sets the reference id.
	 *
	 * @param referenceId
	 *            the new reference id
	 */
	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	/**
	 * Gets the users.
	 *
	 * @return the users
	 */
	public List<User> getUsers() {
		if (users == null) {
			users = new ArrayList<>();
		}
		return users;
	}

	/**
	 * Sets the users.
	 *
	 * @param users
	 *            the new users
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * Gets the current level.
	 *
	 * @return the current level
	 */
	public Integer getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * Sets the current level.
	 *
	 * @param currentLevel
	 *            the new current level
	 */
	public void setCurrentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
	}

	/**
	 * Gets the delegated.
	 *
	 * @return the delegated
	 */
	public Boolean getDelegated() {
		return delegated;
	}

	/**
	 * Sets the delegated.
	 *
	 * @param delegated
	 *            the new delegated
	 */
	public void setDelegated(Boolean delegated) {
		this.delegated = delegated;
	}

	/**
	 * Gets the active.
	 *
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}
}

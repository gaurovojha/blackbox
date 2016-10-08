package com.blackbox.ids.core.model.notification;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.blackbox.ids.core.model.Role;

/**
 * The Class NotificationEscalationRole.
 */
@Entity
@Table(name = "BB_NOTIFICATION_DEFAULT_ROLE")
public class NotificationDefaultRole implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6594049906374106329L;

	/** The notification default role id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_NOTIFICATION_DEFAULT_ROLE_ID", unique = true, nullable = false)
	private Long notificationDefaultRoleId;

	/** The notification Id. */
	@Column(name = "BB_NOTIFICATION_ID", nullable = true)
	private Long notificationId;

	/** The role. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_ROLE_ID")
	private Role role;

	/**
	 * Gets the notification escalation role id.
	 *
	 * @return the notification escalation role id
	 */
	public Long getNotificationDefaultRoleId() {
		return notificationDefaultRoleId;
	}

	/**
	 * Sets the notification escalation role id.
	 *
	 * @param notificationEscalationRoleId
	 *            the new notification escalation role id
	 */
	public void setNotificationDefaultRoleId(Long notificationEscalationRoleId) {
		this.notificationDefaultRoleId = notificationEscalationRoleId;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 *
	 * @param role
	 *            the new role
	 */
	public void setRole(Role role) {
		this.role = role;
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
}

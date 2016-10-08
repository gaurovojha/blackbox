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
 * The Class NotificationLevelRole.
 */
@Entity
@Table(name = "BB_NOTIFICATION_LEVEL_ROLE")
public class NotificationLevelRole implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8239415808698733634L;

	/** The notification level role id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_NOTIFICATION_LEVEL_ROLE_ID", nullable = false, unique = true)
	private Long notificationLevelRoleId;

	/** The user role. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BB_ROLE_ID")
	private Role userRole;

	/** The type. */
	@Column(name = "TYPE")
	private String type;

	/** The notification level. */
	@Column(name = "BB_NOTIFICATION_BUSINESS_RULE_LEVEL_ID", nullable = true)
	private Long notificationLevelId;

	/**
	 * Gets the notification level role id.
	 *
	 * @return the notification level role id
	 */
	public Long getNotificationLevelRoleId() {
		return notificationLevelRoleId;
	}

	/**
	 * Sets the notification level role id.
	 *
	 * @param notificationLevelRoleId
	 *            the new notification level role id
	 */
	public void setNotificationLevelRoleId(Long notificationLevelRoleId) {
		this.notificationLevelRoleId = notificationLevelRoleId;
	}

	/**
	 * Gets the user role.
	 *
	 * @return the user role
	 */
	public Role getUserRole() {
		return userRole;
	}

	/**
	 * Sets the user role.
	 *
	 * @param userRole
	 *            the new user role
	 */
	public void setUserRole(Role userRole) {
		this.userRole = userRole;
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
	 * Gets the notification level id.
	 *
	 * @return the notification level id
	 */
	public Long getNotificationLevelId() {
		return notificationLevelId;
	}

	/**
	 * Sets the notification level id.
	 *
	 * @param notificationLevelId
	 *            the new notification level id
	 */
	public void setNotificationLevelId(Long notificationLevelId) {
		this.notificationLevelId = notificationLevelId;
	}
}

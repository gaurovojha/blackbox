package com.blackbox.ids.core.model.notification;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * The Class NotificationBusinessRuleLevel.
 */
@Entity
@Table(name = "BB_NOTIFICATION_BUSINESS_RULE_LEVEL")
public class NotificationBusinessRuleLevel implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3410280789144599271L;

	/** The notification business rule level id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_NOTIFICATION_BUSINESS_RULE_LEVEL_ID", unique = true, nullable = false)
	private Long notificationBusinessRuleLevelId;

	/** The notification business rule. */
	@Column(name = "BB_NOTIFICATION_BUSINESS_RULE_ID", nullable = true)
	private Long notificationBusinessRuleId;

	/** The level number. */
	@Column(name = "LEVEL_NUMBER")
	private Integer levelNumber;

	/** The notification roles. */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "BB_NOTIFICATION_BUSINESS_RULE_LEVEL_ID", referencedColumnName = "BB_NOTIFICATION_BUSINESS_RULE_LEVEL_ID")
	@OrderBy("BB_NOTIFICATION_LEVEL_ROLE_ID ASC")
	private Set<NotificationLevelRole> notificationRoles;

	/**
	 * Gets the notification business rule level id.
	 *
	 * @return the notification business rule level id
	 */
	public Long getNotificationBusinessRuleLevelId() {
		return notificationBusinessRuleLevelId;
	}

	/**
	 * Sets the notification business rule level id.
	 *
	 * @param notificationBusinessRuleLevelId
	 *            the new notification business rule level id
	 */
	public void setNotificationBusinessRuleLevelId(long notificationBusinessRuleLevelId) {
		this.notificationBusinessRuleLevelId = notificationBusinessRuleLevelId;
	}

	/**
	 * Gets the level number.
	 *
	 * @return the level number
	 */
	public Integer getLevelNumber() {
		return levelNumber;
	}

	/**
	 * Sets the level number.
	 *
	 * @param levelNumber
	 *            the new level number
	 */
	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}

	/**
	 * Gets the notification roles.
	 *
	 * @return the notification roles
	 */
	public Set<NotificationLevelRole> getNotificationRoles() {
		if (notificationRoles == null) {
			notificationRoles = new LinkedHashSet<>();
		}

		return notificationRoles;
	}

	/**
	 * Sets the notification roles.
	 *
	 * @param notificationRoles
	 *            the new notification roles
	 */
	public void setNotificationRoles(Set<NotificationLevelRole> notificationRoles) {
		this.notificationRoles = notificationRoles;
	}

	/**
	 * Sets the notification business rule level id.
	 *
	 * @param notificationBusinessRuleLevelId
	 *            the new notification business rule level id
	 */
	public void setNotificationBusinessRuleLevelId(Long notificationBusinessRuleLevelId) {
		this.notificationBusinessRuleLevelId = notificationBusinessRuleLevelId;
	}

	/**
	 * Sets the level number.
	 *
	 * @param levelNumber
	 *            the new level number
	 */
	public void setLevelNumber(Integer levelNumber) {
		this.levelNumber = levelNumber;
	}

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
}

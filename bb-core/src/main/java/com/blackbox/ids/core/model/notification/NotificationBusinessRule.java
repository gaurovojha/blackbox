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
 * The Class NotificationBusinessRule.
 */
@Entity
@Table(name = "BB_NOTIFICATION_BUSINESS_RULE")
public class NotificationBusinessRule implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7099177723237600383L;

	/** The notification business rule id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_NOTIFICATION_BUSINESS_RULE_ID", unique = true, nullable = false)
	private Long notificationBusinessRuleId;

	/** The notification. */
	@Column(name = "BB_NOTIFICATION_ID", nullable = true)
	private Long notificationId;

	/** The notification business rule levels. */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "BB_NOTIFICATION_BUSINESS_RULE_ID", referencedColumnName = "BB_NOTIFICATION_BUSINESS_RULE_ID")
	@OrderBy("levelNumber ASC")
	private Set<NotificationBusinessRuleLevel> notificationBusinessRuleLevels;

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
	 * Gets the notification business rule levels.
	 *
	 * @return the notification business rule levels
	 */
	public Set<NotificationBusinessRuleLevel> getNotificationBusinessRuleLevels() {
		if (notificationBusinessRuleLevels == null) {
			notificationBusinessRuleLevels = new LinkedHashSet<>();
		}

		return notificationBusinessRuleLevels;
	}

	/**
	 * Sets the notification business rule levels.
	 *
	 * @param notificationBusinessRuleLevels
	 *            the new notification business rule levels
	 */
	public void setNotificationBusinessRuleLevels(Set<NotificationBusinessRuleLevel> notificationBusinessRuleLevels) {
		this.notificationBusinessRuleLevels = notificationBusinessRuleLevels;
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

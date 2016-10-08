package com.blackbox.ids.core.model.notification.process;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.base.BaseEntity;

/**
 * The Class DelegationRule.
 */
@Entity
@Table(name = "BB_DELEGATION_RULE")
public class DelegationRule extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9019960578519979133L;

	/** The delegation rule id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_DELEGATION_RULE_ID", nullable = false, unique = true, length = 50)
	private Long delegationRuleId;

	/** The role. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_ROLE_ID", nullable = false)
	private Role role;

	/** The delegated role. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DELEGATE_ROLE_ID", nullable = false)
	private Role delegateRole;

	/** The start date. */
	@Column(name = "START_DATE")
	@Temporal(TemporalType.DATE)
	private Calendar startDate;

	/** The end date. */
	@Column(name = "END_DATE")
	@Temporal(TemporalType.DATE)
	private Calendar endDate;

	/** The active. */
	@Column(name = "ACTIVE")
	private Boolean active = true;

	/**
	 * Gets the delegation rule id.
	 *
	 * @return the delegation rule id
	 */
	public Long getDelegationRuleId() {
		return delegationRuleId;
	}

	/**
	 * Sets the delegation rule id.
	 *
	 * @param delegationRuleId
	 *            the new delegation rule id
	 */
	public void setDelegationRuleId(Long delegationRuleId) {
		this.delegationRuleId = delegationRuleId;
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
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDate
	 *            the new start date
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate
	 *            the new end date
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * Gets the active.
	 *
	 * @return the active
	 */
	public Boolean getActive() {
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
	 * Gets the delegate role.
	 *
	 * @return the delegate role
	 */
	public Role getDelegateRole() {
		return delegateRole;
	}

	/**
	 * Sets the delegate role.
	 *
	 * @param delegateRole
	 *            the new delegate role
	 */
	public void setDelegateRole(Role delegateRole) {
		this.delegateRole = delegateRole;
	}
}

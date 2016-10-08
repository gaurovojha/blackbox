package com.blackbox.ids.dto.notification;

import java.util.List;

/**
 * The Class NotificationLevelRole.
 */
public class NotificationLevelRoleDTO {

	/** The level role id. */
	private long levelRoleId;

	/** The role id. */
	private long roleId;

	/** The role name. */
	private String roleName;

	/** The type. */
	private String type;

	/** The assignees. */
	private List<RoleTransactionDTO> assignees;

	/** The jurisdictions. */
	private List<RoleTransactionDTO> jurisdictions;

	/** The customer nos. */
	private List<RoleTransactionDTO> customerNos;

	/** The tech groups. */
	private List<RoleTransactionDTO> techGroups;

	/** The organisations. */
	private List<RoleTransactionDTO> organisations;

	/**
	 * Gets the level role id.
	 *
	 * @return the level role id
	 */
	public long getLevelRoleId() {
		return levelRoleId;
	}

	/**
	 * Sets the level role id.
	 *
	 * @param levelRoleId
	 *            the new level role id
	 */
	public void setLevelRoleId(long levelRoleId) {
		this.levelRoleId = levelRoleId;
	}

	/**
	 * Gets the role id.
	 *
	 * @return the role id
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * Sets the role id.
	 *
	 * @param roleId
	 *            the new role id
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * Gets the role name.
	 *
	 * @return the role name
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * Sets the role name.
	 *
	 * @param roleName
	 *            the new role name
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	 * Gets the assignees.
	 *
	 * @return the assignees
	 */
	public List<RoleTransactionDTO> getAssignees() {
		return assignees;
	}

	/**
	 * Sets the assignees.
	 *
	 * @param assignees
	 *            the new assignees
	 */
	public void setAssignees(List<RoleTransactionDTO> assignees) {
		this.assignees = assignees;
	}

	/**
	 * Gets the jurisdictions.
	 *
	 * @return the jurisdictions
	 */
	public List<RoleTransactionDTO> getJurisdictions() {
		return jurisdictions;
	}

	/**
	 * Sets the jurisdictions.
	 *
	 * @param jurisdictions
	 *            the new jurisdictions
	 */
	public void setJurisdictions(List<RoleTransactionDTO> jurisdictions) {
		this.jurisdictions = jurisdictions;
	}

	/**
	 * Gets the customer nos.
	 *
	 * @return the customer nos
	 */
	public List<RoleTransactionDTO> getCustomerNos() {
		return customerNos;
	}

	/**
	 * Sets the customer nos.
	 *
	 * @param customerNos
	 *            the new customer nos
	 */
	public void setCustomerNos(List<RoleTransactionDTO> customerNos) {
		this.customerNos = customerNos;
	}

	/**
	 * Gets the tech groups.
	 *
	 * @return the tech groups
	 */
	public List<RoleTransactionDTO> getTechGroups() {
		return techGroups;
	}

	/**
	 * Sets the tech groups.
	 *
	 * @param techGroups
	 *            the new tech groups
	 */
	public void setTechGroups(List<RoleTransactionDTO> techGroups) {
		this.techGroups = techGroups;
	}

	/**
	 * Gets the organisations.
	 *
	 * @return the organisations
	 */
	public List<RoleTransactionDTO> getOrganisations() {
		return organisations;
	}

	/**
	 * Sets the organisations.
	 *
	 * @param organisations
	 *            the new organisations
	 */
	public void setOrganisations(List<RoleTransactionDTO> organisations) {
		this.organisations = organisations;
	}
}

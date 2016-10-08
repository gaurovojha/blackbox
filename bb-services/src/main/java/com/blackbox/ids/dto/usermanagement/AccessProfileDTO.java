package com.blackbox.ids.dto.usermanagement;

import java.util.ArrayList;
import java.util.List;

import com.blackbox.ids.dto.RoleDTO;

/**
 * Transports profile related information.
 *
 * @author Nagarro
 */
public class AccessProfileDTO {

	/** The id. */
	private Long id;

	/** The name. */
	private String name;

	/** The description. */
	private String description;

	/** The created by. */
	private String createdBy;

	/** The created date. */
	private String createdDate;

	/** The updated by. */
	private String updatedBy;

	/** The updated date. */
	private String updatedDate;

	/** The end date. */
	private String endDate;

	/** The user count. */
	private long userCount;

	/** The active. */
	private boolean active;

	/** The seeded. */
	private boolean seeded;

	/** The roles. */
	private List<RoleDTO> roles;

	/** The version. */
	private int version;

	/** The permission dt os. */
	private List<PermissionDTO> permissionDTOs;

	/** The permission ids. */
	private List<Long> permissionIds;

	/** The permission ids. */
	private List<Long> accessControlIds;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the created by.
	 *
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the updated by.
	 *
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * Sets the updated by.
	 *
	 * @param updatedBy
	 *            the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * Gets the updated date.
	 *
	 * @return the updatedDate
	 */
	public String getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * Sets the updated date.
	 *
	 * @param updatedDate
	 *            the updatedDate to set
	 */
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Gets the user count.
	 *
	 * @return the userCount
	 */
	public long getUserCount() {
		return userCount;
	}

	/**
	 * Sets the user count.
	 *
	 * @param userCount
	 *            the userCount to set
	 */
	public void setUserCount(long userCount) {
		this.userCount = userCount;
	}

	/**
	 * Checks if is active.
	 *
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Checks if is seeded.
	 *
	 * @return the seeded
	 */
	public boolean isSeeded() {
		return seeded;
	}

	/**
	 * Sets the seeded.
	 *
	 * @param seeded
	 *            the seeded to set
	 */
	public void setSeeded(boolean seeded) {
		this.seeded = seeded;
	}

	/**
	 * Sets the active.
	 *
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
	public List<RoleDTO> getRoles() {
		return roles;
	}

	/**
	 * Sets the roles.
	 *
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 *
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * Gets the permission dt os.
	 *
	 * @return the permission dt os
	 */
	public List<PermissionDTO> getPermissionDTOs() {
		if (permissionDTOs == null) {
			permissionDTOs = new ArrayList<>();
		}

		return permissionDTOs;
	}

	/**
	 * Sets the permission dt os.
	 *
	 * @param permissionDTOs
	 *            the new permission dt os
	 */
	public void setPermissionDTOs(List<PermissionDTO> permissionDTOs) {
		this.permissionDTOs = permissionDTOs;
	}

	/**
	 * Gets the permission ids.
	 *
	 * @return the permission ids
	 */
	public List<Long> getPermissionIds() {
		if (permissionIds == null) {
			permissionIds = new ArrayList<>();
		}

		return permissionIds;
	}

	/**
	 * Sets the permission ids.
	 *
	 * @param permissionIds
	 *            the new permission ids
	 */
	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}

	/**
	 * Gets the access control ids.
	 *
	 * @return the access control ids
	 */
	public List<Long> getAccessControlIds() {
		if (accessControlIds == null) {
			accessControlIds = new ArrayList<>();
		}

		return accessControlIds;
	}

	/**
	 * Sets the access control ids.
	 *
	 * @param accessControlIds
	 *            the new access control ids
	 */
	public void setAccessControlIds(List<Long> accessControlIds) {
		this.accessControlIds = accessControlIds;
	}
}
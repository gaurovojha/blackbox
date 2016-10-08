package com.blackbox.ids.dto;

import java.util.List;

import com.blackbox.ids.dto.usermanagement.PermissionDTO;

public class GroupDTO {

	/**
	 * system generated uid
	 */
	// private static final long serialVersionUID = -2984179424571827746L;

	private long id;
	private String name;
	private String description;
	private int type;
	private List<PermissionDTO> permissions;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the permissions
	 */
	public List<PermissionDTO> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(List<PermissionDTO> permissions) {
		this.permissions = permissions;
	}

}

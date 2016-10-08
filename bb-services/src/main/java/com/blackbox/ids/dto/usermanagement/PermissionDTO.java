package com.blackbox.ids.dto.usermanagement;

/**
 * The Class PermissionDTO.
 */
public class PermissionDTO {

	/** The permission id. */
	private Long permissionId;

	/** The description. */
	private String description;

	/** The method. */
	private String method;

	/** The url. */
	private String url;

	/** The access control dto. */
	private AccessControlDTO accessControlDto;

	/**
	 * Gets the permission id.
	 *
	 * @return the permission id
	 */
	public Long getPermissionId() {
		return permissionId;
	}

	/**
	 * Sets the permission id.
	 *
	 * @param permissionId
	 *            the new permission id
	 */
	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
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
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the method.
	 *
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the method.
	 *
	 * @param method
	 *            the new method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url
	 *            the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the access control dto.
	 *
	 * @return the access control dto
	 */
	public AccessControlDTO getAccessControlDto() {
		return accessControlDto;
	}

	/**
	 * Sets the access control dto.
	 *
	 * @param accessControlDto
	 *            the new access control dto
	 */
	public void setAccessControlDto(AccessControlDTO accessControlDto) {
		this.accessControlDto = accessControlDto;
	}
}

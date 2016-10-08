package com.blackbox.ids.dto.usermanagement;

/**
 * The Class AccessControlDTO.
 */
public class AccessControlDTO {
	
	/** The access control id. */
	private Long accessControlId;
	
	/** The action type. */
	private boolean actionType;
	
	/** The name. */
	private String name;
	
	/**
	 * Gets the access control id.
	 *
	 * @return the access control id
	 */
	public Long getAccessControlId() {
		return accessControlId;
	}
	
	/**
	 * Sets the access control id.
	 *
	 * @param accessControlId the new access control id
	 */
	public void setAccessControlId(Long accessControlId) {
		this.accessControlId = accessControlId;
	}
	
	/**
	 * Checks if is action type.
	 *
	 * @return true, if is action type
	 */
	public boolean isActionType() {
		return actionType;
	}
	
	/**
	 * Sets the action type.
	 *
	 * @param actionType the new action type
	 */
	public void setActionType(boolean actionType) {
		this.actionType = actionType;
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
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
}

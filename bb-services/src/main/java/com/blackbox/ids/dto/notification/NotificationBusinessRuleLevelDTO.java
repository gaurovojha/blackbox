package com.blackbox.ids.dto.notification;

import java.io.Serializable;
import java.util.List;

/**
 * The Class NotificationBusinessRuleLevelDTO.
 */
public class NotificationBusinessRuleLevelDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3707434134567503918L;

	public NotificationBusinessRuleLevelDTO() {
		super();
	
	}

	/** The business level dto id. */
	private long businessLevelDtoId;

	/** The current level no. */
	private int currentLevelNo;

	/** The role dtos. */
	private List<NotificationLevelRoleDTO> levelRoleDtos;

	/**
	 * Gets the business level dto id.
	 *
	 * @return the business level dto id
	 */
	public long getBusinessLevelDtoId() {
		return businessLevelDtoId;
	}

	/**
	 * Sets the business level dto id.
	 *
	 * @param businessLevelDtoId
	 *            the new business level dto id
	 */
	public void setBusinessLevelDtoId(long businessLevelDtoId) {
		this.businessLevelDtoId = businessLevelDtoId;
	}

	/**
	 * Gets the current level no.
	 *
	 * @return the current level no
	 */
	public int getCurrentLevelNo() {
		return currentLevelNo;
	}

	/**
	 * Sets the current level no.
	 *
	 * @param currentLevelNo
	 *            the new current level no
	 */
	public void setCurrentLevelNo(int currentLevelNo) {
		this.currentLevelNo = currentLevelNo;
	}

	/**
	 * Gets the level roles.
	 *
	 * @return the level roles
	 */
	public List<NotificationLevelRoleDTO> getLevelRoleDtos() {
		return levelRoleDtos;
	}

	/**
	 * Sets the level roles.
	 *
	 * @param levelRoles
	 *            the new level roles
	 */
	public void setLevelRoleDtos(List<NotificationLevelRoleDTO> levelRoles) {
		this.levelRoleDtos = levelRoles;
	}
}

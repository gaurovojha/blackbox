package com.blackbox.ids.core.model.usermanagement;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Class AccessControl.
 */
@Entity
@Table(name = "BB_ACCESS_CONTROL")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccessControl implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3596166730234774542L;

	/**
	 * Instantiates a new access control.
	 */
	public AccessControl() {
	}

	/**
	 * Instantiates a new access control.
	 *
	 * @param accessControlId
	 *            the access control id
	 */
	public AccessControl(Long accessControlId) {
		this.accessControlId = accessControlId;
	}

	/** The access control id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_ACCESS_CONTROL_ID")
	private Long accessControlId;

	/** The name. */
	@Column(name = "NAME")
	private String name;

	/** The description. */
	@Column(name = "DESCRIPTION")
	private String description;

	/** The action type. */
	@Column(name = "ACTION_TYPE")
	private boolean actionType = false;

	/** The moudle id. */
	@Column(name = "BB_MODULE_ID")
	private Long moudleId;

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
	 * @param accessControlId
	 *            the new access control id
	 */
	public void setAccessControlId(Long accessControlId) {
		this.accessControlId = accessControlId;
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
	 *            the new name
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
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @param actionType
	 *            the new action type
	 */
	public void setActionType(boolean actionType) {
		this.actionType = actionType;
	}

	/**
	 * Gets the moudle id.
	 *
	 * @return the moudle id
	 */
	public Long getMoudleId() {
		return moudleId;
	}

	/**
	 * Sets the moudle id.
	 *
	 * @param moudleId
	 *            the new moudle id
	 */
	public void setMoudleId(Long moudleId) {
		this.moudleId = moudleId;
	}
}

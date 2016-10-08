package com.blackbox.ids.core.dto.reference;

import java.util.Calendar;

import com.blackbox.ids.core.dto.user.UserDTO;

/**
 * The Class ReferenceReviewLogDTO.
 */
public class ReferenceReviewLogDTO {

	/** The id. */
	private Long id;

	/** The reviewed by. */
	private UserDTO reviewedBy;

	/** The reference base data id. */
	private Long referenceBaseDataId;

	/** The created date. */
	private Calendar createdDate;

	/** The created by user. */
	private Long createdByUser;

	/** The updated date. */
	private Calendar updatedDate;

	/** The updated by user. */
	private Long updatedByUser;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the reviewed by.
	 *
	 * @return the reviewed by
	 */
	public UserDTO getReviewedBy() {
		return reviewedBy;
	}

	/**
	 * Sets the reviewed by.
	 *
	 * @param reviewedBy
	 *            the new reviewed by
	 */
	public void setReviewedBy(UserDTO reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	/**
	 * Gets the reference base data id.
	 *
	 * @return the reference base data id
	 */
	public Long getReferenceBaseDataId() {
		return referenceBaseDataId;
	}

	/**
	 * Sets the reference base data id.
	 *
	 * @param referenceBaseDataId
	 *            the new reference base data id
	 */
	public void setReferenceBaseDataId(Long referenceBaseDataId) {
		this.referenceBaseDataId = referenceBaseDataId;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Calendar getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate
	 *            the new created date
	 */
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the created by user.
	 *
	 * @return the created by user
	 */
	public Long getCreatedByUser() {
		return createdByUser;
	}

	/**
	 * Sets the created by user.
	 *
	 * @param createdByUser
	 *            the new created by user
	 */
	public void setCreatedByUser(Long createdByUser) {
		this.createdByUser = createdByUser;
	}

	/**
	 * Gets the updated date.
	 *
	 * @return the updated date
	 */
	public Calendar getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * Sets the updated date.
	 *
	 * @param updatedDate
	 *            the new updated date
	 */
	public void setUpdatedDate(Calendar updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * Gets the updated by user.
	 *
	 * @return the updated by user
	 */
	public Long getUpdatedByUser() {
		return updatedByUser;
	}

	/**
	 * Sets the updated by user.
	 *
	 * @param updatedByUser
	 *            the new updated by user
	 */
	public void setUpdatedByUser(Long updatedByUser) {
		this.updatedByUser = updatedByUser;
	}

	/**
	 * Instantiates a new reference review log dto.
	 */
	public ReferenceReviewLogDTO() {
		super();
	}

}

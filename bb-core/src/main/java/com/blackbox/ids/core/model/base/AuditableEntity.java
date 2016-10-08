/**
 *
 */
package com.blackbox.ids.core.model.base;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;

/**
 * The <code>AuditableEntity</code> represents base class for all persistence entities which needs to be audited. It
 * contains common attributes which must be present in all auditable persistence entities.
 *
 * @author ajay2258
 */
@MappedSuperclass
public class AuditableEntity implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -4974103992349691698L;

	/** The created date. */
	@Column(name = "CREATED_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdDate;

	/** The created by user. */
	@Column(name = "CREATED_BY")
	private Long createdByUser;

	/** The updated date. */
	@Column(name = "MODIFIED_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar updatedDate;

	/** The updated by user. */
	@Column(name = "MODIFIED_BY")
	private Long updatedByUser;

	/*- ------------------------ JPA Callbacks -- */
	@PrePersist
	public void prePersist() {
		if (createdByUser == null) {
			this.createdByUser = loggedInUser();
			this.updatedByUser = this.createdByUser;
		}
		this.createdDate = Calendar.getInstance();
		this.updatedDate = this.createdDate;
	}

	@PreUpdate
	public void preUpdate() {
		if (updatedByUser == null) {
			this.updatedByUser = loggedInUser();
		}
		this.updatedDate = Calendar.getInstance();
	}

	protected Long loggedInUser() {
		return BlackboxSecurityContextHolder.getUserId();
	}

	protected String loggedInUserEmail() {
		return BlackboxSecurityContextHolder.getUserDetails().getUsername();
	}

	/*- ------------------------ getter-setters -- */
	/**
	 * Gets the created date.
	 *
	 * @return the createdDate
	 */
	public Calendar getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the created by user.
	 *
	 * @return the createdByUser
	 */
	public Long getCreatedByUser() {
		return createdByUser;
	}

	/**
	 * Sets the created by user.
	 *
	 * @param createdByUser
	 *            the createdByUser to set
	 */
	public void setCreatedByUser(Long createdByUser) {
		this.createdByUser = createdByUser;
	}

	public Calendar getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Calendar updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getUpdatedByUser() {
		return updatedByUser;
	}

	public void setUpdatedByUser(Long updatedByUser) {
		this.updatedByUser = updatedByUser;
	}

}

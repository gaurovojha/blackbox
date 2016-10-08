/**
 *
 */
package com.blackbox.ids.core.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;

/**
 * The <code>EntityUser</code> maintains the record level locks maintained in database.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_ENTITY_USER")
public class EntityUser implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -1951168738045511646L;

	public enum EntityName {
		APPLICATION_BASE;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_ENTITY_USER_ID", unique = true, nullable = false, length = 50)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "ENTITY_NAME", nullable = false)
	private EntityName entity;

	@Column(name = "RECORD_ID", nullable = false)
	private Long recordId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOCK_ACQUIRED_ON", nullable = false)
	private Calendar lockAcquiredOn;

	@Column(name = "LOCKED_BY", nullable = false)
	private Long lockedBy;

	/*- ---------------------------- Constructor -- */
	public EntityUser() {
		super();
	}

	public EntityUser(EntityName entityType, Long recordId) {
		super();
		this.entity = entityType;
		this.recordId = recordId;
	}

	/*- ----------------------------- JPA Callbacks -- */
	@PrePersist
	public void prePersist() {
		this.lockAcquiredOn = Calendar.getInstance();
		this.lockedBy = BlackboxSecurityContextHolder.getUserId();
	}

	/*- ----------------------------- getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntityName getEntity() {
		return entity;
	}

	public void setEntity(EntityName entity) {
		this.entity = entity;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Calendar getLockAcquiredOn() {
		return lockAcquiredOn;
	}

	public void setLockAcquiredOn(Calendar lockAcquiredOn) {
		this.lockAcquiredOn = lockAcquiredOn;
	}

	public Long getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(Long lockedBy) {
		this.lockedBy = lockedBy;
	}

}
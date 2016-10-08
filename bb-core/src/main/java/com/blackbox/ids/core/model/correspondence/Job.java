package com.blackbox.ids.core.model.correspondence;

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

import com.blackbox.ids.core.model.base.BaseEntity;

@Entity
@Table(name="BB_JOBS")
public class Job  extends BaseEntity{
	

	public enum Status {
		/**	New	**/
		NEW ,
		/**	Processing	**/
		PROCESSING , 
		/**	Submitted	**/
		SUBMITTED ,
		/**	Error	**/
		ERROR
	}

	public enum Type {
		/**	Pair audit	*/
		PAIR,
		/**	Bulk Upload	**/
		BULK
	}
	


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_JOB_FILES_ID", nullable = false)
	private long id ;
	
	/*Type column*/
	@Enumerated(EnumType.STRING)
	@Column(name="TYPE", nullable = false)
	private Type type ;
	
	/*	Status	*/
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS",nullable = false)
	private Status status ;
	
	/*	email	*/
	@Column(name="EMAIL")
	private String email ;
	
	/*	comments */
	@Column(name="COMMENTS")
	private String comments ;
	
	/*- ------------------------ JPA Callbacks -- */
	@PrePersist
	public void prePersist() {
		if (getCreatedByUser() == null) {
			setCreatedByUser(loggedInUser());
		}
		setUpdatedByUser(loggedInUser());
		setUpdatedDate(Calendar.getInstance());
		setCreatedDate(Calendar.getInstance());
		setEmail(loggedInUserEmail());
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	

	
}

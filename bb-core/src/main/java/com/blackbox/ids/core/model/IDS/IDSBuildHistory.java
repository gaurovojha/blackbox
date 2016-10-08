/**
 * 
 */
package com.blackbox.ids.core.model.IDS;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.base.BaseEntity;

/**
 * This class <code>IDSBuildHistory<code> contains the attributes for maintains the 
 * track of comments between Attorney User and Paralegal User.
 * 
 * @author nagarro
 */
@Entity
@Table(name = "BB_IDS_BUILD_HISTORY")
public class IDSBuildHistory extends BaseEntity{

	public enum Event {
	
		/** Do not file the IDS sent for approval to attorney */
		DO_NOT_FILE,
		
		/** References reviewed(by Paralegal) and sent for attornery approval (after the IDS) was prepared is not to be included */
		DO_NOT_INCLUDE,
		
		/** All References reviewed(by Paralegal) and sent for attornery approval (after the IDS) was prepared is to be included */
		INCLUDE_ALL(1),
		
		/** When Self - citation reference is added in IDS */
		SELF_CITATION,
		
		/** Any reference is dropped from the IDS */
		DROP_FROM_IDS,
		
		/**Any reference is not to filed in this IDS */
		DO_NOT_FILE_REF;
		
		public int numParams;
		
		private Event() {
			this.numParams = 0;
		}
		
		private Event(int numParams) {
			this.numParams = numParams;
		}

	}
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7213501366519718394L;
	
	/** Database primary key. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_IDS_BUILD_HISTORY_ID", updatable = false)
	private Long id;


	@Column(name = "IDS_ID", nullable = false)
	private Long idsID; // week reference for IDS

	@Column(name = "ASSIGNED_BY", nullable = true)
	private Long assignedBy; // attorney

	@Column(name = "ASSIGNED_TO", nullable = true)
	private Long assignedTo; // paralegal

	/** Event for which it is called */
	@Enumerated(EnumType.STRING)
	@Column(name = "EVENT")
	private Event event;

	// description message for events where user doesn't manually add comment will be kept in .properties files against the event.
	@Column(name = "DESCRIPTION_PARAMETER", nullable = true)
	String descParams; // description text parameters, split by ';'

   @Column(name = "COMMENT", nullable = true)
   private String comment; // will hold comment manually entered by the user, eg. while attorney request changes on IDS

	/*- ------------------------------------ Approval Information -- */
	/** Timestamp when the attornery had replied to paralegal*/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EVENT_DATE")
	private Calendar eventDate;

	/**
	 * Default Constructor
	 */
	public IDSBuildHistory() {
	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdsID() {
		return idsID;
	}

	public void setIdsID(Long idsID) {
		this.idsID = idsID;
	}

	public Long getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(Long assignedBy) {
		this.assignedBy = assignedBy;
	}

	public Long getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Long assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getDescParams() {
		return descParams;
	}

	public void setDescParams(String descParams) {
		this.descParams = descParams;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Calendar getEventDate() {
		return eventDate;
	}

	public void setEventDate(Calendar eventDate) {
		this.eventDate = eventDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

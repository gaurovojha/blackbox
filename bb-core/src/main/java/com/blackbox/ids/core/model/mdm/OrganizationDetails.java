/**
 *
 */
package com.blackbox.ids.core.model.mdm;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Assignee.Entity;
import com.blackbox.ids.core.model.mstr.IDSRelevantStatus;
import com.blackbox.ids.core.model.mstr.ProsecutionStatus;

/**
 * The entity class {@code OrganizationDetails} maintains the organization /assignee specific details,
 * associated with the container application.
 *
 * @author ajay2258
 */
@Embeddable
public class OrganizationDetails implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 2317917199419082821L;

	@Column(name = "PROSECUTION_STATUS")
	@Enumerated(EnumType.STRING)
	private ProsecutionStatus prosecutionStatus;

	@Column(name = "IDS_RELEVANT_STATUS")
	@Enumerated(EnumType.STRING)
	private IDSRelevantStatus idsRelevantStatus;

	@Column(name = "IDS_RELEVANT_STATUS_UPDATED_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar idsRelevantStatusUpdatedOn;

	@Column(name = "EXPORT_CONTROL")
	private boolean exportControl;

	@Column(name = "ENTITY")
	@Enumerated(EnumType.STRING)
	private Assignee.Entity entity;

	/** Office Action (OA) Response Description. */
	@Column(name = "OA_RESPONSE_DESCRIPTION")
	private String oaResponseDesc;

	/** Timestamp when office action response is received. */
	@Column(name = "OA_RESPONSE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar oaResponseDate;

	/*- -------------------------------------------------- Constructors -- */
	public OrganizationDetails() {
		super();
	}

	public OrganizationDetails(ProsecutionStatus prosecutionStatus,
			IDSRelevantStatus idsRelevantStatus, boolean exportControl,
			Entity entity) {
		this.prosecutionStatus = prosecutionStatus;
		this.idsRelevantStatus = idsRelevantStatus;
		this.exportControl = exportControl;
		this.entity = entity;
	}

	/*- -------------------------------------------------- getter-setters -- */
	public ProsecutionStatus getProsecutionStatus() {
		return prosecutionStatus;
	}

	public void setProsecutionStatus(ProsecutionStatus prosecutionStatus) {
		this.prosecutionStatus = prosecutionStatus;
	}

	public IDSRelevantStatus getIdsRelevantStatus() {
		return idsRelevantStatus;
	}

	public void setIdsRelevantStatus(IDSRelevantStatus idsRelevantStatus) {
		this.idsRelevantStatus = idsRelevantStatus;
	}

	public Calendar getIdsRelevantStatusUpdatedOn() {
		return idsRelevantStatusUpdatedOn;
	}

	public void setIdsRelevantStatusUpdatedOn(Calendar idsRelevantStatusUpdatedOn) {
		this.idsRelevantStatusUpdatedOn = idsRelevantStatusUpdatedOn;
	}

	public boolean isExportControl() {
		return exportControl;
	}

	public void setExportControl(boolean exportControl) {
		this.exportControl = exportControl;
	}

	public Assignee.Entity getEntity() {
		return entity;
	}

	public void setEntity(Assignee.Entity entity) {
		this.entity = entity;
	}

	public String getOaResponseDesc() {
		return oaResponseDesc;
	}

	public void setOaResponseDesc(String oaResponseDesc) {
		this.oaResponseDesc = oaResponseDesc;
	}

	public Calendar getOaResponseDate() {
		return oaResponseDate;
	}

	public void setOaResponseDate(Calendar oaResponseDate) {
		this.oaResponseDate = oaResponseDate;
	}

}

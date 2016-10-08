package com.blackbox.ids.core.dto.mdm.family;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.mysema.query.annotations.QueryProjection;

/**
 * Class {@code FamilyDetailDTO} holds the application family details.
 *
 * @author ajay2258
 */
public class FamilyDetailDTO {

	private long dbId;
	private String familyId;
	private String jurisdiction;
	private String applicationNo;
	private String attorneyDocket;
	private Date filedOn;
	private String assignee;
	private String applicationType;

	@QueryProjection
	public FamilyDetailDTO(Long id, String familyId, String jurisdiction, String applicationNo,
			String attorneyDocket, Calendar filingDate, String assignee, ApplicationType applicationType) {

		super();
		this.dbId = id;
		this.familyId = familyId;
		this.jurisdiction = jurisdiction;
		this.applicationNo = applicationNo;
		this.attorneyDocket = attorneyDocket;
		this.filedOn = filingDate.getTime();
		this.assignee = assignee;
		this.applicationType = applicationType.name();
	}

	/*------------------------Getter & Setters------------------*/
	public long getDbId() {
		return dbId;
	}

	public void setDbId(long dbId) {
		this.dbId = dbId;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getAttorneyDocket() {
		return attorneyDocket;
	}

	public void setAttorneyDocket(String attorneyDocket) {
		this.attorneyDocket = attorneyDocket;
	}

	public Date getFiledOn() {
		return filedOn;
	}

	public void setFiledOn(Date filedOn) {
		this.filedOn = filedOn;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

}

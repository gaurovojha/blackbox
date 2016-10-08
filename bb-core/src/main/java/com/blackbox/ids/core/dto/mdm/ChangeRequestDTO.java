package com.blackbox.ids.core.dto.mdm;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.mysema.query.annotations.QueryProjection;

public class ChangeRequestDTO extends ActionsBaseDto{

	private String familyId;

	private String currentStatus;

	private String requestedFor;

	private String requestedBy;

	private Date requestedOn;

	private String userComment;


	@QueryProjection
	public ChangeRequestDTO(String familyId, String jurisdiction, String applicationNo, MDMRecordStatus newRecordStatus, String userFirstName, String userLastName, Calendar requestedOn, MDMRecordStatus currentStatus, long notificationId , long entityId, String userComment) {
		super(jurisdiction, applicationNo, notificationId , entityId);
		this.familyId = familyId;
		this.currentStatus = currentStatus.name();
		this.requestedFor = newRecordStatus.name();
		this.requestedBy = userFirstName + " " + userLastName;
		this.requestedOn =  requestedOn.getTime();
		this.userComment = userComment;
	}

	/*- ----------------------------------- getter-setters -- */

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getRequestedFor() {
		return requestedFor;
	}

	public void setRequestedFor(String requestedFor) {
		this.requestedFor = requestedFor;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public Date getRequestedOn() {
		return requestedOn;
	}

	public void setRequestedOn(Date requestedOn) {
		this.requestedOn = requestedOn;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

}

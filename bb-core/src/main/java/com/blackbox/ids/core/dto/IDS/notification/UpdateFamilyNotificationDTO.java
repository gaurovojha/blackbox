package com.blackbox.ids.core.dto.IDS.notification;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.mysema.query.annotations.QueryProjection;


public class UpdateFamilyNotificationDTO extends NotificationBaseDTO {

	private long dbId;

	private String createdBy;

	private Date createdOn;

	private String familyId;

	private String linkedFamilyId;

	private String linkedJurisdiction;

	private String linkedApplicationNumber;

	private String source;

	@QueryProjection
	public UpdateFamilyNotificationDTO(final long notificationId, final long appdbId,final String jurisdictionCode,
			final String appNo,final Calendar notifiedDate, final String userFirstName , final String userLastName ,
			final Calendar createdOn,final String familyId,final String linkedFamilyId,final String linkedJurisdiction,final String linkedApplicationNumber, 
			final String message,final NotificationStatus status,final NotificationProcessType notificationName, final EntityName entityName){		
		
		super(notificationId,appdbId,jurisdictionCode,appNo,(notifiedDate==null ? null : notifiedDate.getTime()),message,status,notificationName,entityName);
		this.createdBy = BlackboxUtils.concat(userFirstName,Constant.SPACE_STRING,userLastName);
		this.createdOn = createdOn.getTime();
		this.familyId = familyId;
		this.linkedFamilyId = linkedFamilyId;
		this.linkedJurisdiction = linkedJurisdiction;
		this.linkedApplicationNumber = linkedApplicationNumber;
		this.source = "Private PAIR";

	}

	/*---getters - setters ---*/

	
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

	public UpdateFamilyNotificationDTO(){}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getLinkedFamilyId() {
		return linkedFamilyId;
	}

	public void setLinkedFamilyId(String linkedFamilyId) {
		this.linkedFamilyId = linkedFamilyId;
	}

	public String getLinkedJurisdiction() {
		return linkedJurisdiction;
	}

	public void setLinkedJurisdiction(String linkedJurisdiction) {
		this.linkedJurisdiction = linkedJurisdiction;
	}

	public String getLinkedApplicationNumber() {
		return linkedApplicationNumber;
	}

	public void setLinkedApplicationNumber(String linkedApplicationNumber) {
		this.linkedApplicationNumber = linkedApplicationNumber;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}



}

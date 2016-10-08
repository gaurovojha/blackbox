package com.blackbox.ids.core.dto.IDS.notification;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.mysema.query.annotations.QueryProjection;

public class StatusChangeNotificationDTO extends NotificationBaseDTO{

	private String familyId;

	private String currentStatus;

	private String requestedFor;

	private String requestedBy;

	private Date requestedOn;

	private String userComment;


	@QueryProjection
	public StatusChangeNotificationDTO(final long notificationId, final long appdbId,final String familyId,final String jurisdiction,final String appNo,final MDMRecordStatus newRecordStatus, 
			final String userFirstName,final String userLastName,final Calendar requestedOn,final MDMRecordStatus currentStatus, 
			final String userComment,final String message,final NotificationStatus status,final NotificationProcessType notificationName,final Calendar notifiedDate) {
		
		super(notificationId,appdbId,jurisdiction,appNo,(notifiedDate==null ? null : notifiedDate.getTime()),message,status,notificationName);
		this.familyId = familyId;
		this.currentStatus = currentStatus.name();
		this.requestedFor = newRecordStatus.name();
		this.requestedBy = BlackboxUtils.concat(userFirstName,Constant.SPACE_STRING,userLastName);
		this.requestedOn =  requestedOn==null ? null : requestedOn.getTime();
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

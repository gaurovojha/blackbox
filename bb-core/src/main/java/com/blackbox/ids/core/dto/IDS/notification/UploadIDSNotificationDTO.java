package com.blackbox.ids.core.dto.IDS.notification;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;

public class UploadIDSNotificationDTO extends NotificationBaseDTO{

	private long dbId;
	private Date mailingDate ;
	private String documentDesc;
	
	
	public UploadIDSNotificationDTO(final long notificationId, final long appdbId,final String jurisdiction,
			final String appNo, final String message,final NotificationStatus status,
			final NotificationProcessType notificationName,final Calendar notifiedDate,final long dbId,
			final Calendar instructedOn) {
		super(notificationId,appdbId,jurisdiction,appNo,(notifiedDate==null ? null : notifiedDate.getTime()),message,status,notificationName);
		this.mailingDate = instructedOn == null ? null : instructedOn.getTime();
		this.documentDesc = "IDS";
	}
	
	
	/**********************Getters and Setter*******************/
	
	
	public long getDbId() {
		return dbId;
	}

	public void setDbId(long dbId) {
		this.dbId = dbId;
	}
	public Date getMailingDate() {
		return mailingDate;
	}

	public void setMailingDate(Date mailingDate) {
		this.mailingDate = mailingDate;
	}
	public String getDocumentDesc() {
		return documentDesc;
	}
	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
	}

}

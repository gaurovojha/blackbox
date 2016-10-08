package com.blackbox.ids.core.dto.IDS.notification;

import java.util.Calendar;

import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;

public class ValidateRefStatusNotificationDTO extends NotificationBaseDTO{

	private long dbId;
	private String familyId;
	private Long referenceCount;

	
	public ValidateRefStatusNotificationDTO(final long notificationId, final long appdbId,final String familyId,final String jurisdiction,
			final String appNo, final String message,final NotificationStatus status,
			final NotificationProcessType notificationName,final Calendar notifiedDate,final long dbId, final Long referenceCount) {
		super(notificationId,appdbId,jurisdiction,appNo,(notifiedDate==null ? null : notifiedDate.getTime()),message,status,notificationName);
		this.familyId = familyId;
		this.dbId = dbId;
		this.referenceCount = referenceCount;
	}
	
			/*************************Getters and Setters**********************/
	
	public Long getReferenceCount() {
		return referenceCount;
	}
	public void setReferenceCount(Long referenceCount) {
		this.referenceCount = referenceCount;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public long getDbId() {
		return dbId;
	}

	public void setDbId(long dbId) {
		this.dbId = dbId;
	}
	
	

}

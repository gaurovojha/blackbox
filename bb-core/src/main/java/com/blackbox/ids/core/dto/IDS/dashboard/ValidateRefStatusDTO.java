package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.mdm.Application;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Jurisdiction;

public class ValidateRefStatusDTO extends IDSDashBoardBaseDTO{

	private Long referenceCount;
	private Date notified;
	private Long notificationProcessId;

	
	public ValidateRefStatusDTO(Long dbId, String familyId , Jurisdiction jurisdiction , String applicationNo , Long referenceCount, Calendar notified, Long notificationProcessId) {
		super(dbId , familyId, jurisdiction.getCode(), applicationNo);
		//super(dbId, familyId.getFamilyId() , null , applicationNo);
		this.referenceCount = referenceCount;
		this.notified = notified == null ? null : notified.getTime();
		this.notificationProcessId = notificationProcessId;
	}
	
			/*************************Getters and Setters**********************/
	
	public Long getReferenceCount() {
		return referenceCount;
	}
	public void setReferenceCount(Long referenceCount) {
		this.referenceCount = referenceCount;
	}
	public Date getNotified() {
		return notified;
	}
	public void setNotified(Date notified) {
		this.notified = notified;
	}

	public Long getNotificationProcessId() {
		return notificationProcessId;
	}

	public void setNotificationProcessId(Long notificationProcessId) {
		this.notificationProcessId = notificationProcessId;
	}
}

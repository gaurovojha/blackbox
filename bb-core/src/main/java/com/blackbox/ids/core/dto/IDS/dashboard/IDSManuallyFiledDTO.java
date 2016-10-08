package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.mstr.Jurisdiction;

public class IDSManuallyFiledDTO extends IDSDashBoardBaseDTO{

	private Date mailingDate ;
	private String documentDesc;
	private Date notified;
	private Long notificationId;

	
	
	public IDSManuallyFiledDTO(Long dbId , Long notificationId,  Jurisdiction jurisdiction ,String applicationNo	,Calendar instructedOn, Calendar notified) {
		super(dbId,null,jurisdiction.getCode(),applicationNo);
		this.mailingDate = instructedOn == null ? null : instructedOn.getTime();
		this.documentDesc = "IDS";
		this.notified = notified == null ? null : notified.getTime();
		this.notificationId = notificationId;
	}
	
	
	/**********************Getters and Setter*******************/
	
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
	public Date getNotified() {
		return notified;
	}
	public void setNotified(Date notified) {
		this.notified = notified;
	}
	public Long getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}
}

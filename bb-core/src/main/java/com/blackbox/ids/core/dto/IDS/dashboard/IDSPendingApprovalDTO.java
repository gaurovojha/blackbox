package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.mstr.IDSRelevantStatus;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.mysema.query.annotations.QueryProjection;

public class IDSPendingApprovalDTO extends IDSDashBoardBaseDTO {
	
	private String prosecutionStatus;	
	private Date IDSApprovalPendingSince;	
	private String pendingDays;
	private String pendingWith;	
	private Long newReferences;
	private String attorney;	
	private String attorneyComments;
	private Long notificationProcessId;
	private Long appId;
	
	public IDSPendingApprovalDTO(){
		
	}
	
	
	/*PendingApprovalRecords*/
	@QueryProjection
	public IDSPendingApprovalDTO(Long id , String familyId , Jurisdiction jurisdiction , String applicationNo , IDSRelevantStatus prosecutionStatus, Calendar iDSApprovalPendingSince, String pendingWith,
			Long newReferences) {
		super(id , familyId , jurisdiction.getCode() , applicationNo);
		if(prosecutionStatus!=null) {
			this.prosecutionStatus = prosecutionStatus.name();
		}
		IDSApprovalPendingSince = iDSApprovalPendingSince.getTime();
		IDSApprovalPendingSince =iDSApprovalPendingSince!=null ? iDSApprovalPendingSince.getTime() : null;
		this.pendingWith = pendingWith;
		this.newReferences = newReferences;
	}
	
	@QueryProjection
	public IDSPendingApprovalDTO(Long id, Long notificationProcessId,Long appId, String familyId , Jurisdiction jurisdiction , String applicationNo , IDSRelevantStatus prosecutionStatus 
			,Calendar iDSApprovalPendingSince , String attorney, String attorneyComments) {
		super(id , familyId , jurisdiction.getCode() , applicationNo);
		this.notificationProcessId =  notificationProcessId;
		this.appId = appId;
		this.prosecutionStatus = prosecutionStatus !=null ? prosecutionStatus.name() : null;
		IDSApprovalPendingSince =iDSApprovalPendingSince!=null ? iDSApprovalPendingSince.getTime() : null;
		pendingDays = IDSApprovalPendingSince !=null ? BlackboxDateUtil.dateDiffInDays(IDSApprovalPendingSince, new Date()) + " Days" : null;
		this.attorney = attorney;
		this.attorneyComments = attorneyComments;
	}
	
	
	public String getProsecutionStatus() {
		return prosecutionStatus;
	}
	public void setProsecutionStatus(String prosecutionStatus) {
		this.prosecutionStatus = prosecutionStatus;
	}
	public Date getIDSApprovalPendingSince() {
		return IDSApprovalPendingSince;
	}
	public void setIDSApprovalPendingSince(Date iDSApprovalPendingSince) {
		IDSApprovalPendingSince = iDSApprovalPendingSince;
	}
	public String getPendingWith() {
		return pendingWith;
	}
	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}
	public Long getNewReferences() {
		return newReferences;
	}
	public void setNewReferences(Long newReferences) {
		this.newReferences = newReferences;
	}
	public String getAttorney() {
		return attorney;
	}
	public void setAttorney(String attorney) {
		this.attorney = attorney;
	}
	public String getAttorneyComments() {
		return attorneyComments;
	}
	public void setAttorneyComments(String attorneyComments) {
		this.attorneyComments = attorneyComments;
	}
	public String getPendingDays() {
		return pendingDays;
	}
	public void setPendingDays(String pendingDays) {
		this.pendingDays = pendingDays;
	}
	public Long getNotificationProcessId() {
		return notificationProcessId;
	}
	public void setNotificationProcessId(Long notificationProcessId) {
		this.notificationProcessId = notificationProcessId;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
}

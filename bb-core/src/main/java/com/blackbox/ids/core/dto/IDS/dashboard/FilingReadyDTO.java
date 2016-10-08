package com.blackbox.ids.core.dto.IDS.dashboard;

import com.blackbox.ids.core.model.mstr.IDSRelevantStatus;
import com.blackbox.ids.core.model.mstr.Jurisdiction;

public class FilingReadyDTO extends IDSDashBoardBaseDTO{
	
	private String approvedBy;
	private String comments;
	private Long appId;
	
	

	public FilingReadyDTO(Long dbId,Long appId, String familyId, Jurisdiction jurisdiction, String applicationNo , IDSRelevantStatus prosecutionStatus , String approvedBy, String comments) {
		super(dbId , familyId , jurisdiction.getCode() , applicationNo , prosecutionStatus);
		this.approvedBy = approvedBy;
		this.comments = comments;
		this.appId = appId;
	}
	
	public FilingReadyDTO() {
		// TODO Auto-generated constructor stub
	}

	/************************************Getter and setters************************************/

	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
}

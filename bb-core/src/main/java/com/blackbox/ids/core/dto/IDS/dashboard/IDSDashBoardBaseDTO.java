package com.blackbox.ids.core.dto.IDS.dashboard;

import com.blackbox.ids.core.model.mstr.IDSRelevantStatus;
import com.mysema.query.annotations.QueryProjection;

public class IDSDashBoardBaseDTO {
	
	private Long dbId;
	
	private String familyId;
	
	private String jurisdiction;
	
	private String applicationNo;
	
	private String prosecutionStatus;
	
	public IDSDashBoardBaseDTO()
	{
		super();
	}
	
	@QueryProjection
	public IDSDashBoardBaseDTO(Long dbId, String familyId, String jurisdiction, String applicationNo) {
		this.dbId =  dbId;
		this.familyId = familyId;
		this.jurisdiction = jurisdiction;
		this.applicationNo = applicationNo;
	}
	
	@QueryProjection
	public IDSDashBoardBaseDTO(Long dbId , String familyId, String jurisdiction, String applicationNo , IDSRelevantStatus prosecutionStatus) {
		
		this.dbId = dbId;
		this.familyId = familyId;
		this.jurisdiction = jurisdiction;
		this.applicationNo = applicationNo;
		this.prosecutionStatus = prosecutionStatus==null ? null: prosecutionStatus.name();
	}

	
	
	public Long getDbId() {
		return dbId;
	}

	public void setDbId(Long dbId) {
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

	public String getProsecutionStatus() {
		return prosecutionStatus;
	}

	public void setProsecutionStatus(String prosecutionStatus) {
		this.prosecutionStatus = prosecutionStatus;
	}

}

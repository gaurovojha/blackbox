package com.blackbox.ids.core.dto.IDS.dashboard;

import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.mysema.query.annotations.QueryProjection;

public class IDSReferenceDTO extends IDSDashBoardBaseDTO {
	
	private Long refFlowId;
	private String publicationNo;
	private String kindCode;
	private String applicant;
	private String currentStatus;
	private String currentSubStatus;
	private String npl;
	
	@QueryProjection
	public IDSReferenceDTO(Long dbId,Long refFlowId , Jurisdiction jurisdiction, String publicationNo, String kindCode, String applicant, String currentStatus) {
		super(dbId, jurisdiction.getCode(), null , null);
		this.refFlowId = refFlowId;
		this.publicationNo = publicationNo;
		this.kindCode = kindCode;
		this.applicant = applicant;
		this.currentStatus = currentStatus;
	}
	
	@QueryProjection
	public IDSReferenceDTO(Long dbId, Long refFlowId , String publicationNo, ReferenceFlowStatus currentStatus, ReferenceFlowSubStatus currentSubStatus , String kindCode, String applicant) {
		super(dbId, null , null , null);
		this.refFlowId = refFlowId;
		this.publicationNo = publicationNo;
		this.kindCode = kindCode;
		this.applicant = applicant;
		this.currentStatus =currentStatus != null? currentStatus.name() : null;
		this.currentSubStatus = currentSubStatus != null ? currentSubStatus.name() : null;
	}
	
	@QueryProjection
	public IDSReferenceDTO(Long dbId,Long refFlowId , Jurisdiction jurisdiction , String publicationNo, ReferenceFlowStatus currentStatus, ReferenceFlowSubStatus currentSubStatus , String kindCode, String applicant) {
		super(dbId, jurisdiction.getCode() , null , null);
		this.refFlowId = refFlowId;
		this.publicationNo = publicationNo;
		this.kindCode = kindCode;
		this.applicant = applicant;
		this.currentStatus =currentStatus != null? currentStatus.name() : null;
		this.currentSubStatus = currentSubStatus != null ? currentSubStatus.name() : null;
	}
	
	@QueryProjection
	public IDSReferenceDTO(Long dbId,Long refFlowId , String npl, ReferenceFlowStatus currentStatus, ReferenceFlowSubStatus currentSubStatus) {
		super(dbId, null , null , null);
		this.refFlowId = refFlowId;
		this.npl = npl;
		this.currentStatus =currentStatus != null? currentStatus.name() : null;
		this.currentSubStatus = currentSubStatus != null ? currentSubStatus.name() : null;
	}
	
	/**********************Getters and setters************************/
	
	public String getPublicationNo() {
		return publicationNo;
	}
	public void setPublicationNo(String publicationNo) {
		this.publicationNo = publicationNo;
	}
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getCurrentSubStatus() {
		return currentSubStatus;
	}
	public void setCurrentSubStatus(String currentSubStatus) {
		this.currentSubStatus = currentSubStatus;
	}
	public String getNpl() {
		return npl;
	}
	public void setNpl(String npl) {
		this.npl = npl;
	}
	public Long getRefFlowId() {
		return refFlowId;
	}
	public void setRefFlowId(Long refFlowId) {
		this.refFlowId = refFlowId;
	}
}

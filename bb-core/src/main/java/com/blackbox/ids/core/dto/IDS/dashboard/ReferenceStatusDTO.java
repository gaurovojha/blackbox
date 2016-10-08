package com.blackbox.ids.core.dto.IDS.dashboard;

public class ReferenceStatusDTO extends IDSDashBoardBaseDTO {
	private String publicationNo;
	private String kindCode;
	private String applicantName;
	private String currentStatus;
	private String npl;
	
	

	public ReferenceStatusDTO(String publicationNo, String kindCode, String applicantName, String currentStatus,
			String npl) {
		super();
		this.publicationNo = publicationNo;
		this.kindCode = kindCode;
		this.applicantName = applicantName;
		this.currentStatus = currentStatus;
		this.npl = npl;
	}
	/*****************************Getter and Setter**********************************/
	
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
	public String getApplicantName() {
		return applicantName;
	}
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getNpl() {
		return npl;
	}
	public void setNpl(String npl) {
		this.npl = npl;
	}
}

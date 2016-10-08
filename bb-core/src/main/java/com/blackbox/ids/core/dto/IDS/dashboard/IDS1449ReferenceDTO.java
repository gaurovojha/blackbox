package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.mysema.query.annotations.QueryProjection;

public class IDS1449ReferenceDTO {
	
	private Long referenceFlowId;
	private Long citeId;
	private String kindCode;
	private String publicationNo;
	private Date publicationDate;
	private String applicantName;
	private String countryCode;
	private String refFlowStatus;
	private String refFlowSubStatus;
	private String npl;
	
	@QueryProjection
	public IDS1449ReferenceDTO(Long referenceFlowId, Long citeId, String kindCode, String publicationNo, Calendar publicationDate,
			String applicantName, String countryCode, ReferenceFlowStatus refFlowStatus, ReferenceFlowSubStatus refFlowSubStatus) {
		super();
		this.referenceFlowId = referenceFlowId;
		this.citeId = citeId;
		this.kindCode = kindCode;
		this.publicationNo = publicationNo;
		this.publicationDate = publicationDate.getTime();
		this.applicantName = applicantName;
		this.countryCode = countryCode;
		this.refFlowStatus = refFlowStatus.name();
		this.refFlowSubStatus = refFlowSubStatus.name();
	}
	
	@QueryProjection
	public IDS1449ReferenceDTO(Long referenceFlowId,Long citeId, String kindCode, String publicationNo, Calendar publicationDate,
			String applicantName,ReferenceFlowStatus refFlowStatus, ReferenceFlowSubStatus refFlowSubStatus) {
		super();
		this.referenceFlowId = referenceFlowId;
		this.citeId = citeId;
		this.kindCode = kindCode;
		this.publicationNo = publicationNo;
		this.publicationDate = publicationDate.getTime();
		this.applicantName = applicantName;
		this.refFlowStatus = refFlowStatus.name();
		this.refFlowSubStatus = refFlowSubStatus.name();
	}
	
	@QueryProjection
	public IDS1449ReferenceDTO(Long referenceFlowId,Long citeId,ReferenceFlowStatus refFlowStatus, ReferenceFlowSubStatus refFlowSubStatus,String npl) {
		super();
		this.referenceFlowId = referenceFlowId;
		this.citeId = citeId;
		this.refFlowStatus = refFlowStatus.name();
		this.refFlowSubStatus = refFlowSubStatus.name();
		this.npl = npl;
	}
	
	public Long getReferenceFlowId() {
		return referenceFlowId;
	}

	public void setReferenceFlowId(Long referenceFlowId) {
		this.referenceFlowId = referenceFlowId;
	}

	public Long getCiteId() {
		return citeId;
	}
	public void setCiteId(Long citeId) {
		this.citeId = citeId;
	}
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public String getPublicationNo() {
		return publicationNo;
	}
	public void setPublicationNo(String publicationNo) {
		this.publicationNo = publicationNo;
	}
	public Date getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getApplicantName() {
		return applicantName;
	}
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getRefFlowStatus() {
		return refFlowStatus;
	}

	public void setRefFlowStatus(String refFlowStatus) {
		this.refFlowStatus = refFlowStatus;
	}

	public String getRefFlowSubStatus() {
		return refFlowSubStatus;
	}

	public void setRefFlowSubStatus(String refFlowSubStatus) {
		this.refFlowSubStatus = refFlowSubStatus;
	}

	public String getNpl() {
		return npl;
	}
	public void setNpl(String npl) {
		this.npl = npl;
	}
	
	
	
	

}

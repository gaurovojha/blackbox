package com.blackbox.ids.core.dto.IDS;

import java.util.Date;

import com.blackbox.ids.core.model.IDS.IDS.Status;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceBaseFPData;
import com.blackbox.ids.core.model.reference.ReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.ReferenceBasePUSData;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.IDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;

public class IDSReferenceRecordDTO {
	
	private long refFlowId;

	private ReferenceType referenceType;
	
	private String jurisdiction;
	
	/** Taken as patent or publication number. */
	private String patentNo;
	
	private String patentee;
	
	private String filingDate;
	
	private ReferenceFlowSubStatus status;
	
	private Long patentsCount;

	private Long nplCount;
	
	private Long citeNo;
	
	/*- ------------ Fields specific to NPL. -- */
	private String npl;
	
	private String kindCode;
	
	private String comments;
	
	private String refEnteredBy;
	
	private String documentDesc;
	
	private String mailingDate;

	private String refDroppedBy;
	
	private boolean pdf;
	
	private String refDeletedBy;
	
	private String applicationNumber;
	
	private String familyId;
	
	private boolean hasAttachment;
	
	private Long refEnteredById;
	
	private String nplString;
	
	private Status idsStatus;

	public Status getIdsStatus() {
		return idsStatus;
	}

	public void setIdsStatus(Status idsStatus) {
		this.idsStatus = idsStatus;
	}

	public IDSReferenceRecordDTO() {
		super();
	}
	
	public IDSReferenceRecordDTO(IDSReferenceFlow refFlow) {
		this.status = refFlow.getReferenceFlowSubStatus();
		this.documentDesc = refFlow.getCorrespondence().getDocumentCode().getDescription();
		this.mailingDate = refFlow.getCorrespondence().getMailingDate()!= null ? BlackboxDateUtil.calendarToStr(refFlow.getCorrespondence().getMailingDate(),TimestampFormat.MMMDDYYYY) : null;
		this.familyId = refFlow.getReferenceBaseData().getFamilyId();
		this.jurisdiction = refFlow.getReferenceBaseData().getJurisdiction().getCode();
		this.applicationNumber = refFlow.getReferenceBaseData().getSourceApplication();
	}
	
	public IDSReferenceRecordDTO(ReferenceBaseData refBaseData, IDSReferenceFlow refFlow) {
		
		if(refBaseData instanceof ReferenceBasePUSData) {
			ReferenceBasePUSData pusData = (ReferenceBasePUSData) refBaseData;
			this.patentee = pusData.getApplicantName();
			this.kindCode = pusData.getKindCode();			
		}
		
		if(refBaseData instanceof ReferenceBaseFPData) {					
					
		}
		
		if(refBaseData instanceof ReferenceBaseNPLData) {
			ReferenceBaseNPLData nplData = (ReferenceBaseNPLData) refBaseData;
			this.nplString = nplData.getStringData();
		}
		
		this.refFlowId = refFlow.getReferenceFlowId();
		this.filingDate = refFlow.getFilingDate() != null ? BlackboxDateUtil.calendarToStr(refFlow.getFilingDate(),TimestampFormat.MMMDDYYYY) : null;
		this.patentNo = refBaseData.getPublicationNumber();
		this.jurisdiction = refFlow.getReferenceBaseData().getJurisdiction().getCode();
		this.applicationNumber = refFlow.getReferenceBaseData().getSourceApplication();
		this.status = refFlow.getReferenceFlowSubStatus();
		this.hasAttachment = refFlow.getReferenceBaseData() == null ? false : refFlow.getReferenceBaseData().isAttachment() == null ? false : refFlow.getReferenceBaseData().isAttachment(); 
		this.refEnteredById = refFlow.getReferenceBaseData().getCreatedByUser();
		this.comments = refFlow.getReferenceBaseData().getReferenceComments();
		this.idsStatus = refFlow.getIdsId().getStatus();
	}
	
	public IDSReferenceRecordDTO(Long patentCount, Long nplCount) {
		this.patentsCount = patentCount;
		this.nplCount = nplCount;
	}

	public long getRefFlowId() {
		return refFlowId;
	}

	public void setRefFlowId(long refFlowId) {
		this.refFlowId = refFlowId;
	}

	public ReferenceType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getPatentNo() {
		return patentNo;
	}

	public void setPatentNo(String patentNo) {
		this.patentNo = patentNo;
	}

	public String getPatentee() {
		return patentee;
	}

	public void setPatentee(String patentee) {
		this.patentee = patentee;
	}

	public String getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(String filingDate) {
		this.filingDate = filingDate;
	}

	public ReferenceFlowSubStatus getStatus() {
		return status;
	}

	public void setStatus(ReferenceFlowSubStatus status) {
		this.status = status;
	}

	public Long getPatentsCount() {
		return patentsCount;
	}

	public void setPatentsCount(Long patentsCount) {
		this.patentsCount = patentsCount;
	}

	public Long getNplCount() {
		return nplCount;
	}

	public void setNplCount(Long nplCount) {
		this.nplCount = nplCount;
	}

	public Long getCiteNo() {
		return citeNo;
	}

	public void setCiteNo(Long citeNo) {
		this.citeNo = citeNo;
	}

	public String getNpl() {
		return npl;
	}

	public void setNpl(String npl) {
		this.npl = npl;
	}

	public String getKindCode() {
		return kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRefEnteredBy() {
		return refEnteredBy;
	}

	public void setRefEnteredBy(String refEnteredBy) {
		this.refEnteredBy = refEnteredBy;
	}

	public String getDocumentDesc() {
		return documentDesc;
	}

	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
	}

	public String getMailingDate() {
		return mailingDate;
	}

	public void setMailingDate(String mailingDate) {
		this.mailingDate = mailingDate;
	}

	public String getRefDroppedBy() {
		return refDroppedBy;
	}

	public void setRefDroppedBy(String refDroppedBy) {
		this.refDroppedBy = refDroppedBy;
	}

	public boolean isPdf() {
		return pdf;
	}

	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}

	public String getRefDeletedBy() {
		return refDeletedBy;
	}

	public void setRefDeletedBy(String refDeletedBy) {
		this.refDeletedBy = refDeletedBy;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}
	
	public boolean isAttachment() {
		return hasAttachment;
	}

	public void setAttachment(boolean attachment) {
		this.hasAttachment = attachment;
	}
	
	public void setNplString(String nplString) {
		this.nplString = nplString;
	}

	public Long getRefEnteredById() {
		return refEnteredById;
	}

	public void setRefEnteredById(Long refEnteredById) {
		this.refEnteredById = refEnteredById;
	}
	
	public String getNplString() {
		return nplString;
	}
}

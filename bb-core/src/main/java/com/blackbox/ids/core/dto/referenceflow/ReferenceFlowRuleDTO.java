/**
 * 
 */
package com.blackbox.ids.core.dto.referenceflow;

import java.util.Calendar;

import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Status;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.mysema.query.annotations.QueryProjection;

/**
 * @author nagarro
 *
 */
public class ReferenceFlowRuleDTO {

	private String familyId;

	private Long referenceFlowRuleId;

	private MdmRecordDTO targetApplication;

	private MdmRecordDTO sourceApplication;

	private Status status;

	private String comments;

	private ReferenceFlowSubStatus referenceFlowSubStatus;

	private String createdDate;

	private String createdBy;

	private String modifiedDate;

	private String modifiedBy;

	private Calendar notifiedDate;

	private String referenceDocument;

	/**
	 * 
	 */
	public ReferenceFlowRuleDTO() {
		super();
	}

	/**
	 * 
	 */
	@QueryProjection
	public ReferenceFlowRuleDTO(ApplicationBase appBase) {
		super();

		this.familyId = appBase.getFamilyId();
		this.targetApplication = new MdmRecordDTO();
		targetApplication.setDbId(appBase.getId());
		targetApplication.setApplicationNumber(appBase.getApplicationNumber());
		targetApplication.setJurisdiction(appBase.getJurisdiction().getCode());
		targetApplication.setAttorneyDocket(appBase.getAttorneyDocketNumber().getSegment());
		targetApplication.setFamilyId(appBase.getFamilyId());
		this.status = Status.INACTIVE;
	}

	/**
	 * @param familyId
	 * @param targetApplication
	 * @param sourceApplication
	 * @param status
	 * @param comments
	 * @param createdDate
	 * @param createdBy
	 * @param modifiedDate
	 * @param modifiedBy
	 */
	@QueryProjection
	public ReferenceFlowRuleDTO(String familyId, MdmRecordDTO targetApplication, MdmRecordDTO sourceApplication,
			Status status, String comments, Calendar createdDate, String createdBy, Calendar modifiedDate,
			String modifiedBy) {
		super();
		this.familyId = familyId;
		this.targetApplication = targetApplication;
		this.sourceApplication = sourceApplication;
		this.status = status;
		this.comments = comments;
		this.createdDate = createdDate != null
				? BlackboxDateUtil.dateToStr(createdDate.getTime(), TimestampFormat.MMMDDYYYY) : null;
		this.createdBy = createdBy;
		this.modifiedDate = modifiedDate != null
				? BlackboxDateUtil.dateToStr(modifiedDate.getTime(), TimestampFormat.MMMDDYYYY) : null;
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the familyId
	 */
	public String getFamilyId() {
		return familyId;
	}

	/**
	 * @param familyId
	 *            the familyId to set
	 */
	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	/**
	 * @return the referenceFlowRuleId
	 */
	public Long getReferenceFlowRuleId() {
		return referenceFlowRuleId;
	}

	/**
	 * @param referenceFlowRuleId
	 *            the referenceFlowRuleId to set
	 */
	public void setReferenceFlowRuleId(Long referenceFlowRuleId) {
		this.referenceFlowRuleId = referenceFlowRuleId;
	}

	/**
	 * @return the targetApplication
	 */
	public MdmRecordDTO getTargetApplication() {
		return targetApplication;
	}

	/**
	 * @param targetApplication
	 *            the targetApplication to set
	 */
	public void setTargetApplication(MdmRecordDTO targetApplication) {
		this.targetApplication = targetApplication;
	}

	/**
	 * @return the sourceApplication
	 */
	public MdmRecordDTO getSourceApplication() {
		return sourceApplication;
	}

	/**
	 * @param sourceApplication
	 *            the sourceApplication to set
	 */
	public void setSourceApplication(MdmRecordDTO sourceApplication) {
		this.sourceApplication = sourceApplication;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the referenceFlowSubStatus
	 */
	public ReferenceFlowSubStatus getReferenceFlowSubStatus() {
		return referenceFlowSubStatus;
	}

	/**
	 * @param referenceFlowSubStatus
	 *            the referenceFlowSubStatus to set
	 */
	public void setReferenceFlowSubStatus(ReferenceFlowSubStatus referenceFlowSubStatus) {
		this.referenceFlowSubStatus = referenceFlowSubStatus;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the modifiedDate
	 */
	public String getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate
	 *            the modifiedDate to set
	 */
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy
	 *            the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the notifiedDate
	 */
	public Calendar getNotifiedDate() {
		return notifiedDate;
	}

	/**
	 * @param notifiedDate
	 *            the notifiedDate to set
	 */
	public void setNotifiedDate(Calendar notifiedDate) {
		this.notifiedDate = notifiedDate;
	}

	/**
	 * @return the referenceDocument
	 */
	public String getReferenceDocument() {
		return referenceDocument;
	}

	/**
	 * @param referenceDocument
	 *            the referenceDocument to set
	 */
	public void setReferenceDocument(String referenceDocument) {
		this.referenceDocument = referenceDocument;
	}

}

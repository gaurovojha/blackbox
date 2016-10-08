/**
 * 
 */
package com.blackbox.ids.core.dto.referenceflow;

import java.util.Calendar;

import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;

/**
 * @author nagarro
 *
 */
public class ReferenceFlowDTO {

	private String familyId;

	private Long referenceFlowId;

	private ReferenceBaseDTO referenceBaseDataId;

	private CorrespondenceDTO correspondenceDTO;

	private MdmRecordDTO targetApplication;

	private MdmRecordDTO sourceApplication;

	private ReferenceFlowStatus referenceFlowStatus;

	private ReferenceFlowSubStatus referenceFlowSubStatus;

	private Calendar filingDate;

	private String tempIDSId;

	private String internalFinalIDSId;

	private String externalFinalIDSId;

	private Boolean doNotFile;

	private String citeId;

	private Long familyMemberCount;

	private Long subjectMatterCount;

	private Calendar createdDate;

	private String createdBy;

	private Calendar modifiedDate;

	private String modifiedBy;

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
	 * @return the referenceFlowId
	 */
	public Long getReferenceFlowId() {
		return referenceFlowId;
	}

	/**
	 * @param referenceFlowId
	 *            the referenceFlowId to set
	 */
	public void setReferenceFlowId(Long referenceFlowId) {
		this.referenceFlowId = referenceFlowId;
	}

	/**
	 * @return the referenceBaseDataId
	 */
	public ReferenceBaseDTO getReferenceBaseDataId() {
		return referenceBaseDataId;
	}

	/**
	 * @param referenceBaseDataId
	 *            the referenceBaseDataId to set
	 */
	public void setReferenceBaseDataId(ReferenceBaseDTO referenceBaseDataId) {
		this.referenceBaseDataId = referenceBaseDataId;
	}

	/**
	 * @return the correspondenceDTO
	 */
	public CorrespondenceDTO getCorrespondenceDTO() {
		return correspondenceDTO;
	}

	/**
	 * @param correspondenceDTO
	 *            the correspondenceDTO to set
	 */
	public void setCorrespondenceDTO(CorrespondenceDTO correspondenceDTO) {
		this.correspondenceDTO = correspondenceDTO;
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
	 * @return the referenceFlowStatus
	 */
	public ReferenceFlowStatus getReferenceFlowStatus() {
		return referenceFlowStatus;
	}

	/**
	 * @param referenceFlowStatus
	 *            the referenceFlowStatus to set
	 */
	public void setReferenceFlowStatus(ReferenceFlowStatus referenceFlowStatus) {
		this.referenceFlowStatus = referenceFlowStatus;
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
	 * @return the filingDate
	 */
	public Calendar getFilingDate() {
		return filingDate;
	}

	/**
	 * @param filingDate
	 *            the filingDate to set
	 */
	public void setFilingDate(Calendar filingDate) {
		this.filingDate = filingDate;
	}

	/**
	 * @return the tempIDSId
	 */
	public String getTempIDSId() {
		return tempIDSId;
	}

	/**
	 * @param tempIDSId
	 *            the tempIDSId to set
	 */
	public void setTempIDSId(String tempIDSId) {
		this.tempIDSId = tempIDSId;
	}

	/**
	 * @return the internalFinalIDSId
	 */
	public String getInternalFinalIDSId() {
		return internalFinalIDSId;
	}

	/**
	 * @param internalFinalIDSId
	 *            the internalFinalIDSId to set
	 */
	public void setInternalFinalIDSId(String internalFinalIDSId) {
		this.internalFinalIDSId = internalFinalIDSId;
	}

	/**
	 * @return the externalFinalIDSId
	 */
	public String getExternalFinalIDSId() {
		return externalFinalIDSId;
	}

	/**
	 * @param externalFinalIDSId
	 *            the externalFinalIDSId to set
	 */
	public void setExternalFinalIDSId(String externalFinalIDSId) {
		this.externalFinalIDSId = externalFinalIDSId;
	}

	/**
	 * @return the doNotFile
	 */
	public Boolean getDoNotFile() {
		return doNotFile;
	}

	/**
	 * @param doNotFile
	 *            the doNotFile to set
	 */
	public void setDoNotFile(Boolean doNotFile) {
		this.doNotFile = doNotFile;
	}

	/**
	 * @return the citeId
	 */
	public String getCiteId() {
		return citeId;
	}

	/**
	 * @param citeId
	 *            the citeId to set
	 */
	public void setCiteId(String citeId) {
		this.citeId = citeId;
	}

	/**
	 * @return the familyMemberCount
	 */
	public Long getFamilyMemberCount() {
		return familyMemberCount;
	}

	/**
	 * @param familyMemberCount
	 *            the familyMemberCount to set
	 */
	public void setFamilyMemberCount(Long familyMemberCount) {
		this.familyMemberCount = familyMemberCount;
	}

	/**
	 * @return the subjectMatterCount
	 */
	public Long getSubjectMatterCount() {
		return subjectMatterCount;
	}

	/**
	 * @param subjectMatterCount
	 *            the subjectMatterCount to set
	 */
	public void setSubjectMatterCount(Long subjectMatterCount) {
		this.subjectMatterCount = subjectMatterCount;
	}

	/**
	 * @return the createdDate
	 */
	public Calendar getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Calendar createdDate) {
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
	public Calendar getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate
	 *            the modifiedDate to set
	 */
	public void setModifiedDate(Calendar modifiedDate) {
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

}

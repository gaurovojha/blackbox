package com.blackbox.ids.core.dto.correspondence;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

import com.blackbox.ids.core.model.reference.OcrStatus;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.mysema.query.annotations.QueryProjection;

public class CorrespondenceDTO implements Serializable{

	private static final long serialVersionUID = 6077503526048391757L;

	private Long id;

	private Long jurisdictionId;

	private String jurisdictionCode;

	private Long applicationId;

	private String applicationNumber;

	private String mailingDate;
	
	private String createdDate;
	
	private String updatedDate;

	private String documentDescription;

	private Long uploadedByUserId;

	private Long lastUpdateByUserId;

	private String uploadedBy;

	private String lastUpdateBy;

	private Long referenceCount;
	
	private String ocrStatus;
	
	private Long referenceCreatedByUser;
	
	private String referenceCreatedDate;
		
	private Map<String,String> referenceEnteredByUsers;
	
	private String referenceReviewDate;
	
	private String referenceReviewBy;
	
	private Long referenceReviewByUserId;
	
	public CorrespondenceDTO() {
		super();
	}
	
	/**
	 * @param id
	 * @param jurisdictionId
	 * @param jurisdictionCode
	 * @param applicationId
	 * @param applicationNumber
	 * @param mailingDate
	 * @param createdDate
	 * @param documentDescription
	 * @param uploadedByUserId
	 * @param lastUpdateByUserId
	 * @param ocrStatus
	 */
	@QueryProjection
	public CorrespondenceDTO(Long id, Long jurisdictionId, String jurisdictionCode, Long applicationId,
			String applicationNumber, Calendar mailingDate, Calendar createdDate, String documentDescription,
			Long uploadedByUserId, Long lastUpdateByUserId, OcrStatus ocrStatus) {
		super();
		this.id = id;
		this.jurisdictionId = jurisdictionId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationId = applicationId;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null ? BlackboxDateUtil.dateToStr(mailingDate.getTime(),TimestampFormat.MMMDDYYYY) : null;
		this.createdDate = createdDate != null ? BlackboxDateUtil.dateToStr(createdDate.getTime(),TimestampFormat.MMMDDYYYY) : null;;
		this.documentDescription = documentDescription;
		this.uploadedByUserId = uploadedByUserId;
		this.lastUpdateByUserId = lastUpdateByUserId;
		this.ocrStatus = ocrStatus.name();
	}


	/**
	 * @param id
	 * @param jurisdictionId
	 * @param jurisdictionCode
	 * @param applicationId
	 * @param applicationNumber
	 * @param mailingDate
	 * @param createdDate
	 * @param documentDescription
	 * @param uploadedByUserId
	 * @param lastUpdateByUserId
	 * @param updatedDate
	 * @param ocrStatus
	 */
	@QueryProjection
	public CorrespondenceDTO(Long id, Long jurisdictionId, String jurisdictionCode, Long applicationId,
			String applicationNumber, Calendar mailingDate, Calendar createdDate, String documentDescription,
			Long uploadedByUserId, Long lastUpdateByUserId, Calendar updatedDate, OcrStatus ocrStatus) {
		super();
		this.id = id;
		this.jurisdictionId = jurisdictionId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationId = applicationId;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null ? BlackboxDateUtil.dateToStr(mailingDate.getTime(),TimestampFormat.MMMDDYYYY) : null;
		this.createdDate = createdDate != null ? BlackboxDateUtil.dateToStr(createdDate.getTime(),TimestampFormat.MMMDDYYYY) : null;;
		this.documentDescription = documentDescription;
		this.uploadedByUserId = uploadedByUserId;
		this.lastUpdateByUserId = lastUpdateByUserId;
		this.updatedDate = updatedDate != null ? BlackboxDateUtil.dateToStr(updatedDate.getTime(),TimestampFormat.MMMDDYYYY) : null;;
		this.ocrStatus = ocrStatus.name();
	}
	
	/**
	 * @param id
	 * @param jurisdictionId
	 * @param jurisdictionCode
	 * @param applicationId
	 * @param applicationNumber
	 * @param mailingDate
	 * @param createdDate
	 * @param documentDescription
	 * @param uploadedByUserId
	 * @param lastUpdateByUserId
	 * @param referenceCount
	 */
	@QueryProjection
	public CorrespondenceDTO(Long id, Long jurisdictionId, String jurisdictionCode, Long applicationId,
			String applicationNumber, Calendar mailingDate, Calendar createdDate, String documentDescription,
			Long uploadedByUserId, Long lastUpdateByUserId, Long referenceCount) {
		super();
		this.id = id;
		this.jurisdictionId = jurisdictionId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationId = applicationId;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null ? BlackboxDateUtil.dateToStr(mailingDate.getTime(),TimestampFormat.MMMDDYYYY) : null;
		this.createdDate = createdDate != null ? BlackboxDateUtil.dateToStr(createdDate.getTime(),TimestampFormat.MMMDDYYYY) : null;;
		this.documentDescription = documentDescription;
		this.uploadedByUserId = uploadedByUserId;
		this.lastUpdateByUserId = lastUpdateByUserId;
		this.referenceCount = referenceCount;
	}

	/**
	 * @param id
	 * @param jurisdictionId
	 * @param jurisdictionCode
	 * @param applicationId
	 * @param applicationNumber
	 * @param mailingDate
	 * @param createdDate
	 * @param documentDescription
	 * @param uploadedBy
	 * @param lastUpdateBy
	 */
	@QueryProjection
	public CorrespondenceDTO(Long id, Long jurisdictionId, String jurisdictionCode, Long applicationId,
			String applicationNumber, Calendar mailingDate, Calendar createdDate, String documentDescription,
			Long uploadedByUserId, Long lastUpdateByUserId) {
		super();
		this.id = id;
		this.jurisdictionId = jurisdictionId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationId = applicationId;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null ? BlackboxDateUtil.dateToStr(mailingDate.getTime(),TimestampFormat.MMMDDYYYY) : null;
		this.createdDate = createdDate != null ? BlackboxDateUtil.dateToStr(createdDate.getTime(),TimestampFormat.MMMDDYYYY) : null;;
		this.documentDescription = documentDescription;
		this.uploadedByUserId = uploadedByUserId;
		this.lastUpdateByUserId = lastUpdateByUserId;
	}

	/**
	 * @param id
	 * @param jurisdictionId
	 * @param jurisdictionCode
	 * @param applicationId
	 * @param applicationNumber
	 * @param mailingDate
	 * @param createdDate
	 * @param documentDescription
	 * @param uploadedBy
	 * @param lastUpdateBy
	 */
	@QueryProjection
	public CorrespondenceDTO(Long id, Long jurisdictionId, String jurisdictionCode, Long applicationId,
			String applicationNumber, Calendar mailingDate, Calendar createdDate, String documentDescription,
			Long uploadedByUserId, Long lastUpdateByUserId, Long referenceCreatedByUser, Calendar referenceCreatedDate) {
		super();
		this.id = id;
		this.jurisdictionId = jurisdictionId;
		this.jurisdictionCode = jurisdictionCode;
		this.applicationId = applicationId;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate != null ? BlackboxDateUtil.dateToStr(mailingDate.getTime(),TimestampFormat.MMMDDYYYY) : null;
		this.createdDate = createdDate != null ? BlackboxDateUtil.dateToStr(createdDate.getTime(),TimestampFormat.MMMDDYYYY) : null;;
		this.documentDescription = documentDescription;
		this.uploadedByUserId = uploadedByUserId;
		this.lastUpdateByUserId = lastUpdateByUserId;
		this.referenceCreatedByUser = referenceCreatedByUser;
		this.referenceCreatedDate = referenceCreatedDate != null ? BlackboxDateUtil.dateToStr(referenceCreatedDate.getTime(),TimestampFormat.MMMDDYYYY) : null;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the jurisdictionId
	 */
	public Long getJurisdictionId() {
		return jurisdictionId;
	}

	/**
	 * @param jurisdictionId
	 *            the jurisdictionId to set
	 */
	public void setJurisdictionId(Long jurisdictionId) {
		this.jurisdictionId = jurisdictionId;
	}

	/**
	 * @return the jurisdictionCode
	 */
	public String getJurisdictionCode() {
		return jurisdictionCode;
	}

	/**
	 * @param jurisdictionCode
	 *            the jurisdictionCode to set
	 */
	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}

	/**
	 * @return the applicationId
	 */
	public Long getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId
	 *            the applicationId to set
	 */
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the applicationNumber
	 */
	public String getApplicationNumber() {
		return applicationNumber;
	}

	/**
	 * @param applicationNumber
	 *            the applicationNumber to set
	 */
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	/**
	 * @return the mailingDate
	 */
	public String getMailingDate() {
		return mailingDate;
	}

	/**
	 * @param mailingDate
	 *            the mailingDate to set
	 */
	public void setMailingDate(String mailingDate) {
		this.mailingDate = mailingDate;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the updatedDate
	 */
	public String getUpdatedDate() {
		return updatedDate;
	}


	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}


	/**
	 * @return the documentDescription
	 */
	public String getDocumentDescription() {
		return documentDescription;
	}

	/**
	 * @param documentDescription
	 *            the documentDescription to set
	 */
	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	/**
	 * @return the uploadedByUserId
	 */
	public Long getUploadedByUserId() {
		return uploadedByUserId;
	}

	/**
	 * @param uploadedByUserId
	 *            the uploadedByUserId to set
	 */
	public void setUploadedByUserId(Long uploadedByUserId) {
		this.uploadedByUserId = uploadedByUserId;
	}

	/**
	 * @return the lastUpdateByUserId
	 */
	public Long getLastUpdateByUserId() {
		return lastUpdateByUserId;
	}

	/**
	 * @param lastUpdateByUserId
	 *            the lastUpdateByUserId to set
	 */
	public void setLastUpdateByUserId(Long lastUpdateByUserId) {
		this.lastUpdateByUserId = lastUpdateByUserId;
	}

	/**
	 * @return the uploadedBy
	 */
	public String getUploadedBy() {
		return uploadedBy;
	}

	/**
	 * @param uploadedBy
	 *            the uploadedBy to set
	 */
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	/**
	 * @return the lastUpdateBy
	 */
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	/**
	 * @param lastUpdateBy
	 *            the lastUpdateBy to set
	 */
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	/**
	 * @return the referenceCount
	 */
	public Long getReferenceCount() {
		return referenceCount;
	}

	/**
	 * @param referenceCount
	 *            the referenceCount to set
	 */
	public void setReferenceCount(Long referenceCount) {
		this.referenceCount = referenceCount;
	}


	/**
	 * @return the ocrStatus
	 */
	public String getOcrStatus() {
		return ocrStatus;
	}


	/**
	 * @param ocrStatus the ocrStatus to set
	 */
	public void setOcrStatus(String ocrStatus) {
		this.ocrStatus = ocrStatus;
	}

	/**
	 * @return the referenceCreatedByUser
	 */
	public Long getReferenceCreatedByUser() {
		return referenceCreatedByUser;
	}

	/**
	 * @param referenceCreatedByUser the referenceCreatedByUser to set
	 */
	public void setReferenceCreatedByUser(Long referenceCreatedByUser) {
		this.referenceCreatedByUser = referenceCreatedByUser;
	}

	/**
	 * @return the referenceCreatedDate
	 */
	public String getReferenceCreatedDate() {
		return referenceCreatedDate;
	}

	/**
	 * @param referenceCreatedDate the referenceCreatedDate to set
	 */
	public void setReferenceCreatedDate(String referenceCreatedDate) {
		this.referenceCreatedDate = referenceCreatedDate;
	}

	/**
	 * @return the referenceEnteredByUsers
	 */
	public Map<String, String> getReferenceEnteredByUsers() {
		return referenceEnteredByUsers;
	}

	/**
	 * @param referenceEnteredByUsers the referenceEnteredByUsers to set
	 */
	public void setReferenceEnteredByUsers(Map<String, String> referenceEnteredByUsers) {
		this.referenceEnteredByUsers = referenceEnteredByUsers;
	}

	/**
	 * @return the referenceReviewDate
	 */
	public String getReferenceReviewDate() {
		return referenceReviewDate;
	}

	/**
	 * @param referenceReviewDate the referenceReviewDate to set
	 */
	public void setReferenceReviewDate(String referenceReviewDate) {
		this.referenceReviewDate = referenceReviewDate;
	}

	/**
	 * @return the referenceReviewBy
	 */
	public String getReferenceReviewBy() {
		return referenceReviewBy;
	}

	/**
	 * @param referenceReviewBy the referenceReviewBy to set
	 */
	public void setReferenceReviewBy(String referenceReviewBy) {
		this.referenceReviewBy = referenceReviewBy;
	}

	/**
	 * @return the referenceReviewByUserId
	 */
	public Long getReferenceReviewByUserId() {
		return referenceReviewByUserId;
	}

	/**
	 * @param referenceReviewByUserId the referenceReviewByUserId to set
	 */
	public void setReferenceReviewByUserId(Long referenceReviewByUserId) {
		this.referenceReviewByUserId = referenceReviewByUserId;
	}

}
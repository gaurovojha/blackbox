package com.blackbox.ids.core.dto.reference;

import java.io.Serializable;

import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;

// TODO: Auto-generated Javadoc
/**
 * The Class ReferenceRecordDTO.
 */
public class ReferenceRecordDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static long serialVersionUID = -2852723078276618881L;

	/** The jurisdiction code. */
	private String jurisdictionCode;

	/** The application number. */
	private String applicationNumber;

	/** The mailing date. */
	private String mailingDate;

	/** The document description. */
	private String documentDescription;

	/** The uploaded by. */
	private String uploadedBy;

	/** The uploaded on. */
	private String uploadedOn;

	/** The ocr status. */
	private String ocrStatus;

	/** The notified on. */
	private String notifiedOn;

	/** The locked. */
	private boolean locked;

	/** The locked by user. */
	private String lockedByUser;

	/** The document location. */
	private String documentLocation;

	/** The updated by. */
	private String updatedBy;

	/** The updated on. */
	private String updatedOn;

	/** The notification process id. */
	private Long notificationProcessId;

	/** The ocr data id. */
	private Long ocrDataId;

	/** correspondence dto */
	private CorrespondenceDTO correspondenceDto;

	private String publicationNumber;

	/** The filingDate of the application **/
	private String applicationFilingDate;

	/** The Date of issue of application **/
	private String applicationIssuedOn;

	/** The type of Publication Number **/
	private String typeOfNumber;

	/**
	 * Gets the uploaded on.
	 *
	 * @return the uploaded on
	 */
	public String getUploadedOn() {
		return uploadedOn;
	}

	/**
	 * Sets the uploaded on.
	 *
	 * @param uploadedOn
	 *            the new uploaded on
	 */
	public void setUploadedOn(String uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	/**
	 * Gets the jurisdiction code.
	 *
	 * @return the jurisdiction code
	 */
	public String getJurisdictionCode() {
		return jurisdictionCode;
	}

	/**
	 * Sets the jurisdiction code.
	 *
	 * @param jurisdictionCode
	 *            the new jurisdiction code
	 */
	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}

	/**
	 * Gets the application number.
	 *
	 * @return the application number
	 */
	public String getApplicationNumber() {
		return applicationNumber;
	}

	/**
	 * Sets the application number.
	 *
	 * @param applicationNumber
	 *            the new application number
	 */
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	/**
	 * Gets the document description.
	 *
	 * @return the document description
	 */
	public String getDocumentDescription() {
		return documentDescription;
	}

	/**
	 * Sets the document description.
	 *
	 * @param documentDescription
	 *            the new document description
	 */
	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	/**
	 * Gets the uploaded by.
	 *
	 * @return the uploaded by
	 */
	public String getUploadedBy() {
		return uploadedBy;
	}

	/**
	 * Sets the uploaded by.
	 *
	 * @param uploadedBy
	 *            the new uploaded by
	 */
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	/**
	 * Gets the ocr status.
	 *
	 * @return the ocr status
	 */
	public String getOcrStatus() {
		return ocrStatus;
	}

	/**
	 * Sets the ocr status.
	 *
	 * @param ocrStatus
	 *            the new ocr status
	 */
	public void setOcrStatus(String ocrStatus) {
		this.ocrStatus = ocrStatus;
	}

	/**
	 * Gets the mailing date.
	 *
	 * @return the mailing date
	 */
	public String getMailingDate() {
		return mailingDate;
	}

	/**
	 * Sets the mailing date.
	 *
	 * @param mailingDate
	 *            the new mailing date
	 */
	public void setMailingDate(String mailingDate) {
		this.mailingDate = mailingDate;
	}

	/**
	 * Gets the notified on.
	 *
	 * @return the notified on
	 */
	public String getNotifiedOn() {
		return notifiedOn;
	}

	/**
	 * Sets the notified on.
	 *
	 * @param notifiedOn
	 *            the new notified on
	 */
	public void setNotifiedOn(String notifiedOn) {
		this.notifiedOn = notifiedOn;
	}

	/**
	 * Checks if is locked.
	 *
	 * @return true, if is locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Sets the locked.
	 *
	 * @param locked
	 *            the new locked
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * Gets the document location.
	 *
	 * @return the document location
	 */
	public String getDocumentLocation() {
		return documentLocation;
	}

	/**
	 * Sets the document location.
	 *
	 * @param documentLocation
	 *            the new document location
	 */
	public void setDocumentLocation(String documentLocation) {
		this.documentLocation = documentLocation;
	}

	/**
	 * Gets the locked by user.
	 *
	 * @return the locked by user
	 */
	public String getLockedByUser() {
		return lockedByUser;
	}

	/**
	 * Sets the locked by user.
	 *
	 * @param lockedByUser
	 *            the new locked by user
	 */
	public void setLockedByUser(String lockedByUser) {
		this.lockedByUser = lockedByUser;
	}

	/**
	 * Gets the updated by.
	 *
	 * @return the updated by
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * Sets the updated by.
	 *
	 * @param updatedBy
	 *            the new updated by
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * Gets the updated on.
	 *
	 * @return the updated on
	 */
	public String getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * Sets the updated on.
	 *
	 * @param updatedOn
	 *            the new updated on
	 */
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * Gets the notification process id.
	 *
	 * @return the notification process id
	 */
	public Long getNotificationProcessId() {
		return notificationProcessId;
	}

	/**
	 * Sets the notification process id.
	 *
	 * @param notificationProcessId
	 *            the new notification process id
	 */
	public void setNotificationProcessId(Long notificationProcessId) {
		this.notificationProcessId = notificationProcessId;
	}

	/**
	 * Gets the ocr data id.
	 *
	 * @return the ocr data id
	 */
	public Long getOcrDataId() {
		return ocrDataId;
	}

	/**
	 * Sets the ocr data id.
	 *
	 * @param ocrDataId
	 *            the new ocr data id
	 */
	public void setOcrDataId(Long ocrDataId) {
		this.ocrDataId = ocrDataId;
	}

	/**
	 * @return the correspondenceDto
	 */
	public CorrespondenceDTO getCorrespondenceDto() {
		return correspondenceDto;
	}

	/**
	 * @param correspondenceDto
	 *            the correspondenceDto to set
	 */
	public void setCorrespondenceDto(CorrespondenceDTO correspondenceDto) {
		this.correspondenceDto = correspondenceDto;
	}

	public String getPublicationNumber() {
		return publicationNumber;
	}

	public void setPublicationNumber(String publicationNumber) {
		this.publicationNumber = publicationNumber;
	}

	/**
	 * @return the applicationFilingDate
	 */
	public String getApplicationFilingDate() {
		return applicationFilingDate;
	}

	/**
	 * @param applicationFilingDate
	 *            the applicationFilingDate to set
	 */
	public void setApplicationFilingDate(String applicationFilingDate) {
		this.applicationFilingDate = applicationFilingDate;
	}

	/**
	 * @return the applicationIssuedOn
	 */
	public String getApplicationIssuedOn() {
		return applicationIssuedOn;
	}

	/**
	 * @param applicationIssuedOn
	 *            the applicationIssuedOn to set
	 */
	public void setApplicationIssuedOn(String applicationIssuedOn) {
		this.applicationIssuedOn = applicationIssuedOn;
	}

	/**
	 * @return the typeOfNumber
	 */
	public String getTypeOfNumber() {
		return typeOfNumber;
	}

	/**
	 * @param typeOfNumber
	 *            the typeOfNumber to set
	 */
	public void setTypeOfNumber(String typeOfNumber) {
		this.typeOfNumber = typeOfNumber;
	}

}

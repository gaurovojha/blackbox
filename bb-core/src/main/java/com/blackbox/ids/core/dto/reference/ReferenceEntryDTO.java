package com.blackbox.ids.core.dto.reference;

import java.util.Calendar;

import com.blackbox.ids.core.model.reference.OcrStatus;

/**
 * The Class ReferenceEntryDTO.
 */
public class ReferenceEntryDTO {

	/** The ocr data id. */
	private Long ocrDataId;

	/** The jurisdiction code. */
	private String jurisdictionCode;

	/** The correspondence id. */
	private Long correspondenceId;

	/** The ocr status. */
	private OcrStatus ocrStatus;

	/** The application number. */
	private String applicationNumber;

	/** The mailing date. */
	private Calendar mailingDate;

	/** The document description. */
	private String documentDescription;

	/** The correspondence created date. */
	private Calendar correspondenceCreatedDate;

	/** The correspondence created by user. */
	private Long correspondenceCreatedByUser;

	/** The correspondence updated date. */
	private Calendar correspondenceUpdatedDate;

	/** The correspondence updated by user. */
	private Long correspondenceUpdatedByUser;

	/** The notification process id. */
	private Long notificationProcessId;

	/** The locked by user. */
	private Long lockedByUser;

	/** The notification date. */
	private Calendar notificationDate;

	/** The locked by user first name. */
	private String lockedByUserFirstName;

	/** The locked by user last name. */
	private String lockedByUserLastName;

	/** The filingDate of the application **/
	private String applicationFilingDate;

	/** The Date of issue of application **/
	private String applicationIssuedOn;

	/** The type of Publication Number **/
	private String typeOfNumber;


	/**
	 * Instantiates a new reference entry dto.
	 *
	 * @param ocrDataId
	 *            the ocr data id
	 * @param applicationNumber
	 *            the application number
	 * @param jurisdictionCode
	 *            the jurisdiction code
	 * @param correspondenceId
	 *            the correspondence id
	 * @param mailingDate
	 *            the mailing date
	 * @param documentDescription
	 *            the document description
	 * @param ocrStatus
	 *            the ocr status
	 * @param correspondenceCreatedByUser
	 *            the correspondence created by user
	 * @param correspondenceCreatedDate
	 *            the correspondence created date
	 * @param correspondenceUpdatedByUser
	 *            the correspondence updated by user
	 * @param correspondenceUpdatedDate
	 *            the correspondence updated date
	 */
	public ReferenceEntryDTO(Long ocrDataId, String applicationNumber, String jurisdictionCode, Long correspondenceId,
			Calendar mailingDate, String documentDescription, OcrStatus ocrStatus, Long correspondenceCreatedByUser,
			Calendar correspondenceCreatedDate, Long correspondenceUpdatedByUser, Calendar correspondenceUpdatedDate) {
		super();
		this.ocrDataId = ocrDataId;
		this.jurisdictionCode = jurisdictionCode;
		this.correspondenceId = correspondenceId;
		this.ocrStatus = ocrStatus;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate;
		this.documentDescription = documentDescription;
		this.correspondenceCreatedDate = correspondenceCreatedDate;
		this.correspondenceCreatedByUser = correspondenceCreatedByUser;
		this.correspondenceUpdatedDate = correspondenceUpdatedDate;
		this.correspondenceUpdatedByUser = correspondenceUpdatedByUser;
	}

	/**
	 * Instantiates a new reference entry dto.
	 *
	 * @param ocrDataId
	 *            the ocr data id
	 * @param applicationNumber
	 *            the application number
	 * @param jurisdictionCode
	 *            the jurisdiction code
	 * @param correspondenceId
	 *            the correspondence id
	 * @param mailingDate
	 *            the mailing date
	 * @param documentDescription
	 *            the document description
	 * @param ocrStatus
	 *            the ocr status
	 * @param correspondenceCreatedByUser
	 *            the correspondence created by user
	 * @param correspondenceCreatedDate
	 *            the correspondence created date
	 * @param correspondenceUpdatedByUser
	 *            the correspondence updated by user
	 * @param correspondenceUpdatedDate
	 *            the correspondence updated date
	 * @param notificationProcessId
	 *            the notification process id
	 * @param lockedByUser
	 *            the locked by user
	 * @param lockedByUserFirstName
	 *            the locked by user first name
	 * @param lockedByUserLastName
	 *            the locked by user last name
	 * @param notificationDate
	 *            the notification date
	 */
	public ReferenceEntryDTO(Long ocrDataId, String applicationNumber, String jurisdictionCode, Long correspondenceId,
			Calendar mailingDate, String documentDescription, OcrStatus ocrStatus, Long correspondenceCreatedByUser,
			Calendar correspondenceCreatedDate, Long correspondenceUpdatedByUser, Calendar correspondenceUpdatedDate,
			Long notificationProcessId, Long lockedByUser, String lockedByUserFirstName, String lockedByUserLastName,
			Calendar notificationDate) {
		super();
		this.ocrDataId = ocrDataId;
		this.jurisdictionCode = jurisdictionCode;
		this.correspondenceId = correspondenceId;
		this.ocrStatus = ocrStatus;
		this.applicationNumber = applicationNumber;
		this.mailingDate = mailingDate;
		this.documentDescription = documentDescription;
		this.correspondenceCreatedDate = correspondenceCreatedDate;
		this.correspondenceCreatedByUser = correspondenceCreatedByUser;
		this.correspondenceUpdatedDate = correspondenceUpdatedDate;
		this.correspondenceUpdatedByUser = correspondenceUpdatedByUser;
		this.notificationProcessId = notificationProcessId;
		this.lockedByUser = lockedByUser;
		this.lockedByUserFirstName = lockedByUserFirstName;
		this.lockedByUserLastName = lockedByUserLastName;
		this.notificationDate = notificationDate;
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
	 * Gets the correspondence id.
	 *
	 * @return the correspondence id
	 */
	public Long getCorrespondenceId() {
		return correspondenceId;
	}

	/**
	 * Sets the correspondence id.
	 *
	 * @param correspondenceId
	 *            the new correspondence id
	 */
	public void setCorrespondenceId(Long correspondenceId) {
		this.correspondenceId = correspondenceId;
	}

	/**
	 * Gets the ocr status.
	 *
	 * @return the ocr status
	 */
	public OcrStatus getOcrStatus() {
		return ocrStatus;
	}

	/**
	 * Sets the ocr status.
	 *
	 * @param ocrStatus
	 *            the new ocr status
	 */
	public void setOcrStatus(OcrStatus ocrStatus) {
		this.ocrStatus = ocrStatus;
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
	 * Gets the mailing date.
	 *
	 * @return the mailing date
	 */
	public Calendar getMailingDate() {
		return mailingDate;
	}

	/**
	 * Sets the mailing date.
	 *
	 * @param mailingDate
	 *            the new mailing date
	 */
	public void setMailingDate(Calendar mailingDate) {
		this.mailingDate = mailingDate;
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
	 * Gets the correspondence created date.
	 *
	 * @return the correspondence created date
	 */
	public Calendar getCorrespondenceCreatedDate() {
		return correspondenceCreatedDate;
	}

	/**
	 * Sets the correspondence created date.
	 *
	 * @param correspondenceCreatedDate
	 *            the new correspondence created date
	 */
	public void setCorrespondenceCreatedDate(Calendar correspondenceCreatedDate) {
		this.correspondenceCreatedDate = correspondenceCreatedDate;
	}

	/**
	 * Gets the correspondence created by user.
	 *
	 * @return the correspondence created by user
	 */
	public Long getCorrespondenceCreatedByUser() {
		return correspondenceCreatedByUser;
	}

	/**
	 * Sets the correspondence created by user.
	 *
	 * @param correspondenceCreatedByUser
	 *            the new correspondence created by user
	 */
	public void setCorrespondenceCreatedByUser(Long correspondenceCreatedByUser) {
		this.correspondenceCreatedByUser = correspondenceCreatedByUser;
	}

	/**
	 * Gets the correspondence updated date.
	 *
	 * @return the correspondence updated date
	 */
	public Calendar getCorrespondenceUpdatedDate() {
		return correspondenceUpdatedDate;
	}

	/**
	 * Sets the correspondence updated date.
	 *
	 * @param correspondenceUpdatedDate
	 *            the new correspondence updated date
	 */
	public void setCorrespondenceUpdatedDate(Calendar correspondenceUpdatedDate) {
		this.correspondenceUpdatedDate = correspondenceUpdatedDate;
	}

	/**
	 * Gets the correspondence updated by user.
	 *
	 * @return the correspondence updated by user
	 */
	public Long getCorrespondenceUpdatedByUser() {
		return correspondenceUpdatedByUser;
	}

	/**
	 * Sets the correspondence updated by user.
	 *
	 * @param correspondenceUpdatedByUser
	 *            the new correspondence updated by user
	 */
	public void setCorrespondenceUpdatedByUser(Long correspondenceUpdatedByUser) {
		this.correspondenceUpdatedByUser = correspondenceUpdatedByUser;
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
	 * Gets the locked by user.
	 *
	 * @return the locked by user
	 */
	public Long getLockedByUser() {
		return lockedByUser;
	}

	/**
	 * Sets the locked by user.
	 *
	 * @param lockedByUser
	 *            the new locked by user
	 */
	public void setLockedByUser(Long lockedByUser) {
		this.lockedByUser = lockedByUser;
	}

	/**
	 * Gets the notification date.
	 *
	 * @return the notification date
	 */
	public Calendar getNotificationDate() {
		return notificationDate;
	}

	/**
	 * Sets the notification date.
	 *
	 * @param notificationDate
	 *            the new notification date
	 */
	public void setNotificationDate(Calendar notificationDate) {
		this.notificationDate = notificationDate;
	}

	/**
	 * Gets the locked by user first name.
	 *
	 * @return the locked by user first name
	 */
	public String getLockedByUserFirstName() {
		return lockedByUserFirstName;
	}

	/**
	 * Sets the locked by user first name.
	 *
	 * @param lockedByUserFirstName
	 *            the new locked by user first name
	 */
	public void setLockedByUserFirstName(String lockedByUserFirstName) {
		this.lockedByUserFirstName = lockedByUserFirstName;
	}

	/**
	 * Gets the locked by user last name.
	 *
	 * @return the locked by user last name
	 */
	public String getLockedByUserLastName() {
		return lockedByUserLastName;
	}

	/**
	 * Sets the locked by user last name.
	 *
	 * @param lockedByUserLastName
	 *            the new locked by user last name
	 */
	public void setLockedByUserLastName(String lockedByUserLastName) {
		this.lockedByUserLastName = lockedByUserLastName;
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

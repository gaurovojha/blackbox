package com.blackbox.ids.core.dto.reference;

import java.util.Calendar;

import com.blackbox.ids.core.model.reference.ReferenceBaseNPLData;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.mysema.query.annotations.QueryProjection;

/**
 * The Class ReferenceDashboardDto.
 */
public class ReferenceDashboardDto {

	/** The ref jurisdiction code. */
	String refJurisdictionCode;

	/** The source jurisdiction code. */
	String sourceJurisdictionCode;

	/** The publication no. */
	String publicationNo;

	/** The application number. */
	String applicationNumber;

	/** The correspondence id. */
	Long correspondenceId;

	/** The notified date. */
	String notifiedDate;

	/** The npl description. */
	String nplDescription;

	/** The reference base id. */
	Long referenceBaseId;

	/** The reference staging id. */
	Long referenceStagingId;

	/** The locked by. */
	Long lockedBy;

	/** The first name. */
	String firstName;

	/** The last name. */
	String lastName;

	/** The notification process id. */
	Long notificationProcessId;

	/** The document description. */
	String documentDescription;

	/** The family id. */
	String familyId;

	/** The attorney docket. */
	String attorneyDocket;

	/** The parent reference id. */
	Long parentReferenceId;

	/** The filingDate of the application *. */
	private String applicationFilingDate;

	/** The Date of issue of application *. */
	private String applicationIssuedOn;

	/** The type of Publication Number *. */
	private String typeOfNumber;
	
	

	public ReferenceDashboardDto() {
		super();
	}

	/**
	 * Instantiates a new reference dashboard dto.
	 *
	 * @param correspondenceId
	 *            the correspondence id
	 * @param notificationProcessId
	 *            the notification process id
	 */
	@QueryProjection
	public ReferenceDashboardDto(Long correspondenceId, Long notificationProcessId) {
		super();
		this.correspondenceId = correspondenceId;
		this.notificationProcessId = notificationProcessId;
	}

	/**
	 * Instantiates a new reference dashboard dto.
	 *
	 * @param refStagId
	 *            the ref stag id
	 * @param refJurisdictionCode
	 *            the ref jurisdiction code
	 * @param pubNo
	 *            the pub no
	 * @param appNo
	 *            the app no
	 * @param cId
	 *            the c id
	 * @param notifiedDate
	 *            the notified date
	 * @param lockedBy
	 *            the locked by
	 * @param firstName
	 *            the first name
	 * @param lastName
	 *            the last name
	 * @param notificationProcessId
	 *            the notification process id
	 * @param sourceJurisdictionCode
	 *            the source jurisdiction code
	 * @param documentDescription
	 *            the document description
	 * @param applicationFilingDate
	 *            the application filing date
	 * @param applicationIssuedOn
	 *            the application issued on
	 */
	@QueryProjection
	public ReferenceDashboardDto(Long refStagId, String refJurisdictionCode, String pubNo, String appNo, Long cId,
			Calendar notifiedDate, Long lockedBy, String firstName, String lastName, Long notificationProcessId,
			String sourceJurisdictionCode, String documentDescription, Calendar applicationFilingDate,
			Calendar applicationIssuedOn) {
		super();
		this.referenceStagingId = refStagId;
		this.refJurisdictionCode = refJurisdictionCode;
		this.publicationNo = pubNo;
		this.applicationNumber = appNo;
		this.correspondenceId = cId;
		this.notifiedDate = BlackboxDateUtil.calendarToStr(notifiedDate, TimestampFormat.DDMMYYYY);
		this.lockedBy = lockedBy;
		this.firstName = firstName;
		this.lastName = lastName;
		this.notificationProcessId = notificationProcessId;
		this.sourceJurisdictionCode = sourceJurisdictionCode;
		this.documentDescription = documentDescription;
		this.applicationFilingDate = BlackboxDateUtil.calendarToStr(applicationFilingDate, TimestampFormat.YYYYMMDD);
		this.applicationIssuedOn = BlackboxDateUtil.calendarToStr(
				applicationIssuedOn != null ? applicationIssuedOn : Calendar.getInstance(), TimestampFormat.YYYYMMDD);
		this.typeOfNumber = "Publication";
	}

	/**
	 * Instantiates a new reference dashboard dto.
	 *
	 * @param sourceJurisdictionCode
	 *            the source jurisdiction code
	 * @param pubNo
	 *            the pub no
	 * @param appNo
	 *            the app no
	 * @param cId
	 *            the c id
	 * @param nplDesc
	 *            the npl desc
	 * @param notifiedDate
	 *            the notified date
	 * @param refBaseId
	 *            the ref base id
	 * @param lockedBy
	 *            the locked by
	 * @param firstName
	 *            the first name
	 * @param lastName
	 *            the last name
	 * @param notificationProcessId
	 *            the notification process id
	 * @param documentDescription
	 *            the document description
	 */
	@QueryProjection
	public ReferenceDashboardDto(String sourceJurisdictionCode, String pubNo, String appNo, Long cId, String nplDesc,
			Calendar notifiedDate, Long refBaseId, Long lockedBy, String firstName, String lastName,
			Long notificationProcessId, String documentDescription) {
		super();
		this.referenceBaseId = refBaseId;
		this.sourceJurisdictionCode = sourceJurisdictionCode;
		this.publicationNo = pubNo;
		this.applicationNumber = appNo;
		this.correspondenceId = cId;
		this.nplDescription = nplDesc;
		this.notifiedDate = BlackboxDateUtil.calendarToStr(notifiedDate, TimestampFormat.DDMMYYYY);
		this.lockedBy = lockedBy;
		this.firstName = firstName;
		this.lastName = lastName;
		this.notificationProcessId = notificationProcessId;
		this.documentDescription = documentDescription;
	}

	/**
	 * Instantiates a new reference dashboard dto.
	 *
	 * @param nplString
	 *            the npl string
	 * @param familyId
	 *            the family id
	 * @param juris
	 *            the juris
	 * @param applicationNo
	 *            the application no
	 * @param attorneyDocket
	 *            the attorney docket
	 * @param documentDescription
	 *            the document description
	 */
	@QueryProjection
	public ReferenceDashboardDto(String nplString, String familyId, String juris, String applicationNo,
			String attorneyDocket, String documentDescription) {
		this.nplDescription = nplString;
		this.familyId = familyId;
		this.sourceJurisdictionCode = juris;
		this.applicationNumber = applicationNo;
		this.attorneyDocket = attorneyDocket;
		this.documentDescription = documentDescription;
	}

	/**
	 * Instantiates a new reference dashboard dto.
	 *
	 * @param nplString
	 *            the npl string
	 * @param juris
	 *            the juris
	 * @param applicationNo
	 *            the application no
	 * @param documentDescription
	 *            the document description
	 * @param parentReferenceId
	 *            the parent reference id
	 */
	@QueryProjection
	public ReferenceDashboardDto(String nplString, String juris, String applicationNo, String documentDescription,
			Long parentReferenceId,String familyId) {
		this.nplDescription = nplString;
		this.sourceJurisdictionCode = juris;
		this.applicationNumber = applicationNo;
		this.documentDescription = documentDescription;
		this.parentReferenceId = parentReferenceId;
		this.familyId = familyId;
	}

	/**
	 * Instantiates a new reference dashboard dto.
	 *
	 * @param refStagId
	 *            the ref stag id
	 * @param pubNo
	 *            the pub no
	 */
	@QueryProjection
	public ReferenceDashboardDto(Long refStagId, String pubNo) {
		super();
		this.referenceStagingId = refStagId;
		this.publicationNo = pubNo;
	}

	/**
	 * Gets the publication no.
	 *
	 * @return the publication no
	 */
	public String getPublicationNo() {
		return publicationNo;
	}

	/**
	 * Sets the publication no.
	 *
	 * @param publicationNo
	 *            the new publication no
	 */
	public void setPublicationNo(String publicationNo) {
		this.publicationNo = publicationNo;
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
	 * Gets the notified date.
	 *
	 * @return the notified date
	 */
	public String getNotifiedDate() {
		return notifiedDate;
	}

	/**
	 * Sets the notified date.
	 *
	 * @param notifiedDate
	 *            the new notified date
	 */
	public void setNotifiedDate(String notifiedDate) {
		this.notifiedDate = notifiedDate;
	}

	/**
	 * Gets the npl description.
	 *
	 * @return the npl description
	 */
	public String getNplDescription() {
		return nplDescription;
	}

	/**
	 * Sets the npl description.
	 *
	 * @param nplDescription
	 *            the new npl description
	 */
	public void setNplDescription(String nplDescription) {
		this.nplDescription = nplDescription;
	}

	/**
	 * Gets the reference base id.
	 *
	 * @return the reference base id
	 */
	public Long getReferenceBaseId() {
		return referenceBaseId;
	}

	/**
	 * Sets the reference base id.
	 *
	 * @param referenceBaseId
	 *            the new reference base id
	 */
	public void setReferenceBaseId(Long referenceBaseId) {
		this.referenceBaseId = referenceBaseId;
	}

	/**
	 * Gets the reference staging id.
	 *
	 * @return the reference staging id
	 */
	public Long getReferenceStagingId() {
		return referenceStagingId;
	}

	/**
	 * Sets the reference staging id.
	 *
	 * @param referenceStagingId
	 *            the new reference staging id
	 */
	public void setReferenceStagingId(Long referenceStagingId) {
		this.referenceStagingId = referenceStagingId;
	}

	/**
	 * Load by reference base data.
	 *
	 * @param baseData
	 *            the base data
	 */
	public void loadByReferenceBaseData(ReferenceBaseNPLData baseData) {
		this.applicationNumber = baseData.getApplication().getApplicationNumber();
		// TODO Rupesh this.documentPath = baseData.getCorrespondence().
		this.refJurisdictionCode = baseData.getJurisdiction().getCode();
		this.notifiedDate = BlackboxDateUtil.calendarToStr(baseData.getMailingDate(), TimestampFormat.DDMMYYYY);
		this.nplDescription = baseData.getStringData();
		this.publicationNo = "";
		this.referenceBaseId = baseData.getReferenceBaseDataId();
		this.referenceStagingId = -1l;
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
	 * Gets the locked by.
	 *
	 * @return the locked by
	 */
	public Long getLockedBy() {
		return lockedBy;
	}

	/**
	 * Sets the locked by.
	 *
	 * @param lockedBy
	 *            the new locked by
	 */
	public void setLockedBy(Long lockedBy) {
		this.lockedBy = lockedBy;
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
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName
	 *            the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName
	 *            the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the ref jurisdiction code.
	 *
	 * @return the ref jurisdiction code
	 */
	public String getRefJurisdictionCode() {
		return refJurisdictionCode;
	}

	/**
	 * Sets the ref jurisdiction code.
	 *
	 * @param refJurisdictionCode
	 *            the new ref jurisdiction code
	 */
	public void setRefJurisdictionCode(String refJurisdictionCode) {
		this.refJurisdictionCode = refJurisdictionCode;
	}

	/**
	 * Gets the source jurisdiction code.
	 *
	 * @return the source jurisdiction code
	 */
	public String getSourceJurisdictionCode() {
		return sourceJurisdictionCode;
	}

	/**
	 * Sets the source jurisdiction code.
	 *
	 * @param sourceJurisdictionCode
	 *            the new source jurisdiction code
	 */
	public void setSourceJurisdictionCode(String sourceJurisdictionCode) {
		this.sourceJurisdictionCode = sourceJurisdictionCode;
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
	 * Gets the family id.
	 *
	 * @return the family id
	 */
	public String getFamilyId() {
		return familyId;
	}

	/**
	 * Sets the family id.
	 *
	 * @param familyId
	 *            the new family id
	 */
	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	/**
	 * Gets the attorney docket.
	 *
	 * @return the attorney docket
	 */
	public String getAttorneyDocket() {
		return attorneyDocket;
	}

	/**
	 * Sets the attorney docket.
	 *
	 * @param attorneyDocket
	 *            the new attorney docket
	 */
	public void setAttorneyDocket(String attorneyDocket) {
		this.attorneyDocket = attorneyDocket;
	}

	/**
	 * Gets the parent reference id.
	 *
	 * @return the parent reference id
	 */
	public Long getParentReferenceId() {
		return parentReferenceId;
	}

	/**
	 * Sets the parent reference id.
	 *
	 * @param parentReferenceId
	 *            the new parent reference id
	 */
	public void setParentReferenceId(Long parentReferenceId) {
		this.parentReferenceId = parentReferenceId;
	}

	/**
	 * Gets the application filing date.
	 *
	 * @return the application filing date
	 */
	public String getApplicationFilingDate() {
		return applicationFilingDate;
	}

	/**
	 * Sets the application filing date.
	 *
	 * @param applicationFilingDate
	 *            the new application filing date
	 */
	public void setApplicationFilingDate(String applicationFilingDate) {
		this.applicationFilingDate = applicationFilingDate;
	}

	/**
	 * Gets the application issued on.
	 *
	 * @return the application issued on
	 */
	public String getApplicationIssuedOn() {
		return applicationIssuedOn;
	}

	/**
	 * Sets the application issued on.
	 *
	 * @param applicationIssuedOn
	 *            the new application issued on
	 */
	public void setApplicationIssuedOn(String applicationIssuedOn) {
		this.applicationIssuedOn = applicationIssuedOn;
	}

	/**
	 * Gets the type of number.
	 *
	 * @return the type of number
	 */
	public String getTypeOfNumber() {
		return typeOfNumber;
	}

	/**
	 * Sets the type of number.
	 *
	 * @param typeOfNumber
	 *            the new type of number
	 */
	public void setTypeOfNumber(String typeOfNumber) {
		this.typeOfNumber = typeOfNumber;
	}
}

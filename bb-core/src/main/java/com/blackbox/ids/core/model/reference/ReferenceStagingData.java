package com.blackbox.ids.core.model.reference;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Jurisdiction;

/**
 * The Class ReferenceStagingData.
 */
@Entity
@Table(name = "BB_REFERENCE_STAGING_DATA")
public class ReferenceStagingData extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2929903179920651387L;

	/** The reference staging data id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_REFERENCE_STAGING_DATA_ID", unique = true, nullable = false, length = 50)
	private Long referenceStagingDataId;

	/** The source application. */
	@Column(name = "SOURCE_APPLICATION")
	private String sourceApplication;

	/** The correspondence. */
	@ManyToOne
	@JoinColumn(name = "BB_CORRESPONDENCE_ID")
	private CorrespondenceBase correspondence;

	/** The ocr data. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_OCR_DATA_ID", referencedColumnName = "BB_OCR_DATA_ID", nullable = true)
	private OcrData ocrData;

	/** The mailing date. */
	@Column(name = "MAILING_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar mailingDate;

	/** The reference record id. */
	@Column(name = "REFERENCE_RECORD_ID")
	private Long referenceRecordId;

	/** The reference catogory type. */
	@Column(name = "REFERENCE_CATEGORY")
	@Enumerated(EnumType.STRING)
	private ReferenceCategoryType referenceCatogoryType;

	/** The self citation date. */
	@Column(name = "SELF_CITATION_CREATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar selfCitationDate;

	/** The reference source. */
	@Column(name = "REFERENCE_SOURCE")
	private String referenceSource;

	/** The reference sub source. */
	@Column(name = "REFERENCE_SUB_SOURCE")
	private String referenceSubSource;

	/** The application. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_APPLICATION_ID", nullable = false)
	private ApplicationBase application;

	/** The jurisdiction. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_JURISDICTION_ID", referencedColumnName = "BB_JURISDICTION_ID", nullable = false)
	private Jurisdiction jurisdiction;

	/** The reviewed status. */
	@Column(name = "REVIEWED_STATUS")
	private Boolean reviewedStatus;

	/** The coupling id. */
	@Column(name = "COUPLING_ID", nullable = true)
	private Long couplingId;

	/** The primary couple. */
	@Column(name = "PRIMARY_COUPLE", nullable = true)
	private Boolean primaryCouple;

	/** The parent reference id. */
	@Column(name = "PARENT_REFERENCE_ID")
	private Long parentReferenceId;

	/** The reference status. */
	@Column(name = "REFERENCE_STATUS")
	@Enumerated(EnumType.STRING)
	private ReferenceStatus referenceStatus;

	/** The reference type. */
	@Column(name = "REFERENCE_TYPE")
	@Enumerated(EnumType.STRING)
	private ReferenceType referenceType;

	/** The reference comments. */
	@Column(name = "REFERENCE_COMMENTS")
	private String referenceComments;

	/** The publication number. */
	@Column(name = "PUBLICATION_NUMBER")
	private String publicationNumber;

	/** The active. */
	@Column(name = "ACTIVE")
	private Boolean active = true;

	/** The kind code. */
	@Column(name = "KIND_CODE")
	private String kindCode;

	/** The publication date. */
	@Column(name = "PUBLICATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar publicationDate;

	/** The applicant name. */
	@Column(name = "APPLICANT_NAME")
	private String applicantName;

	/** This flag will detarmine is the notification is sent or not *. */
	@Column(name = "NOTIFICATION_SENT")
	private Boolean notificationSent;

	/** The pus data. */
	@Embedded
	private ReferenceStagingPUSData pusData = new ReferenceStagingPUSData();

	/** The fp data. */
	@Embedded
	private ReferenceStagingFPData fpData = new ReferenceStagingFPData();

	/** The npl data. */
	@Embedded
	private ReferenceStagingNPLData nplData = new ReferenceStagingNPLData();

	/**
	 * Gets the reference staging data id.
	 *
	 * @return the reference staging data id
	 */
	public Long getReferenceStagingDataId() {
		return referenceStagingDataId;
	}

	/**
	 * Sets the reference staging data id.
	 *
	 * @param referenceStagingDataId
	 *            the new reference staging data id
	 */
	public void setReferenceStagingDataId(Long referenceStagingDataId) {
		this.referenceStagingDataId = referenceStagingDataId;
	}

	/**
	 * Gets the source application.
	 *
	 * @return the source application
	 */
	public String getSourceApplication() {
		return sourceApplication;
	}

	/**
	 * Sets the source application.
	 *
	 * @param sourceApplication
	 *            the new source application
	 */
	public void setSourceApplication(String sourceApplication) {
		this.sourceApplication = sourceApplication;
	}

	/**
	 * Gets the ocr data.
	 *
	 * @return the ocr data
	 */
	public OcrData getOcrData() {
		return ocrData;
	}

	/**
	 * Sets the ocr data.
	 *
	 * @param ocrData
	 *            the new ocr data
	 */
	public void setOcrData(OcrData ocrData) {
		this.ocrData = ocrData;
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
	 * Gets the reference record id.
	 *
	 * @return the reference record id
	 */
	public Long getReferenceRecordId() {
		return referenceRecordId;
	}

	/**
	 * Sets the reference record id.
	 *
	 * @param referenceRecordId
	 *            the new reference record id
	 */
	public void setReferenceRecordId(Long referenceRecordId) {
		this.referenceRecordId = referenceRecordId;
	}

	/**
	 * Gets the reference catogory type.
	 *
	 * @return the reference catogory type
	 */
	public ReferenceCategoryType getReferenceCatogoryType() {
		return referenceCatogoryType;
	}

	/**
	 * Sets the reference catogory type.
	 *
	 * @param referenceCatogoryType
	 *            the new reference catogory type
	 */
	public void setReferenceCatogoryType(ReferenceCategoryType referenceCatogoryType) {
		this.referenceCatogoryType = referenceCatogoryType;
	}

	/**
	 * Gets the self citation date.
	 *
	 * @return the self citation date
	 */
	public Calendar getSelfCitationDate() {
		return selfCitationDate;
	}

	/**
	 * Sets the self citation date.
	 *
	 * @param selfCitationDate
	 *            the new self citation date
	 */
	public void setSelfCitationDate(Calendar selfCitationDate) {
		this.selfCitationDate = selfCitationDate;
	}

	/**
	 * Gets the reference source.
	 *
	 * @return the reference source
	 */
	public String getReferenceSource() {
		return referenceSource;
	}

	/**
	 * Sets the reference source.
	 *
	 * @param referenceSource
	 *            the new reference source
	 */
	public void setReferenceSource(String referenceSource) {
		this.referenceSource = referenceSource;
	}

	/**
	 * Gets the reference sub source.
	 *
	 * @return the reference sub source
	 */
	public String getReferenceSubSource() {
		return referenceSubSource;
	}

	/**
	 * Sets the reference sub source.
	 *
	 * @param referenceSubSource
	 *            the new reference sub source
	 */
	public void setReferenceSubSource(String referenceSubSource) {
		this.referenceSubSource = referenceSubSource;
	}

	/**
	 * Checks if is reviewed status.
	 *
	 * @return true, if is reviewed status
	 */
	public Boolean getReviewedStatus() {
		return reviewedStatus;
	}

	/**
	 * Sets the reviewed status.
	 *
	 * @param reviewedStatus
	 *            the new reviewed status
	 */
	public void setReviewedStatus(Boolean reviewedStatus) {
		this.reviewedStatus = reviewedStatus;
	}

	/**
	 * Gets the coupling id.
	 *
	 * @return the coupling id
	 */
	public Long getCouplingId() {
		return couplingId;
	}

	/**
	 * Sets the coupling id.
	 *
	 * @param couplingId
	 *            the new coupling id
	 */
	public void setCouplingId(Long couplingId) {
		this.couplingId = couplingId;
	}

	/**
	 * Checks if is primary couple.
	 *
	 * @return true, if is primary couple
	 */
	public Boolean getPrimaryCouple() {
		return primaryCouple;
	}

	/**
	 * Sets the primary couple.
	 *
	 * @param primaryCouple
	 *            the new primary couple
	 */
	public void setPrimaryCouple(Boolean primaryCouple) {
		this.primaryCouple = primaryCouple;
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
	 * Gets the reference status.
	 *
	 * @return the reference status
	 */
	public ReferenceStatus getReferenceStatus() {
		return referenceStatus;
	}

	/**
	 * Sets the reference status.
	 *
	 * @param referenceStatus
	 *            the new reference status
	 */
	public void setReferenceStatus(ReferenceStatus referenceStatus) {
		this.referenceStatus = referenceStatus;
	}

	/**
	 * Gets the reference type.
	 *
	 * @return the reference type
	 */
	public ReferenceType getReferenceType() {
		return referenceType;
	}

	/**
	 * Sets the reference type.
	 *
	 * @param referenceType
	 *            the new reference type
	 */
	public void setReferenceType(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	/**
	 * Gets the reference comments.
	 *
	 * @return the reference comments
	 */
	public String getReferenceComments() {
		return referenceComments;
	}

	/**
	 * Sets the reference comments.
	 *
	 * @param referenceComments
	 *            the new reference comments
	 */
	public void setReferenceComments(String referenceComments) {
		this.referenceComments = referenceComments;
	}

	/**
	 * Gets the application.
	 *
	 * @return the application
	 */
	public ApplicationBase getApplication() {
		return application;
	}

	/**
	 * Sets the application.
	 *
	 * @param application
	 *            the new application
	 */
	public void setApplication(ApplicationBase application) {
		this.application = application;
	}

	/**
	 * Gets the pus data.
	 *
	 * @return the pus data
	 */
	public ReferenceStagingPUSData getPusData() {
		return pusData;
	}

	/**
	 * Sets the pus data.
	 *
	 * @param pusData
	 *            the new pus data
	 */
	public void setPusData(ReferenceStagingPUSData pusData) {
		this.pusData = pusData;
	}

	/**
	 * Gets the fp data.
	 *
	 * @return the fp data
	 */
	public ReferenceStagingFPData getFpData() {
		return fpData;
	}

	/**
	 * Sets the fp data.
	 *
	 * @param fpData
	 *            the new fp data
	 */
	public void setFpData(ReferenceStagingFPData fpData) {
		this.fpData = fpData;
	}

	/**
	 * Gets the npl data.
	 *
	 * @return the npl data
	 */
	public ReferenceStagingNPLData getNplData() {
		return nplData;
	}

	/**
	 * Sets the npl data.
	 *
	 * @param nplData
	 *            the new npl data
	 */
	public void setNplData(ReferenceStagingNPLData nplData) {
		this.nplData = nplData;
	}

	/**
	 * Gets the jurisdiction.
	 *
	 * @return the jurisdiction
	 */
	public Jurisdiction getJurisdiction() {
		return jurisdiction;
	}

	/**
	 * Sets the jurisdiction.
	 *
	 * @param jurisdiction
	 *            the new jurisdiction
	 */
	public void setJurisdiction(Jurisdiction jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	/**
	 * Gets the publication number.
	 *
	 * @return the publication number
	 */
	public String getPublicationNumber() {
		return publicationNumber;
	}

	/**
	 * Sets the publication number.
	 *
	 * @param publicationNumber
	 *            the new publication number
	 */
	public void setPublicationNumber(String publicationNumber) {
		this.publicationNumber = publicationNumber;
	}

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public Boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active
	 *            the new active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Gets the correspondence.
	 *
	 * @return the correspondence
	 */
	public CorrespondenceBase getCorrespondence() {
		return correspondence;
	}

	/**
	 * Sets the correspondence.
	 *
	 * @param correspondence
	 *            the new correspondence
	 */
	public void setCorrespondence(CorrespondenceBase correspondence) {
		this.correspondence = correspondence;
	}

	/**
	 * Gets the kind code.
	 *
	 * @return the kind code
	 */
	public String getKindCode() {
		return kindCode;
	}

	/**
	 * Sets the kind code.
	 *
	 * @param kindCode
	 *            the new kind code
	 */
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	/**
	 * Gets the publication date.
	 *
	 * @return the publication date
	 */
	public Calendar getPublicationDate() {
		return publicationDate;
	}

	/**
	 * Sets the publication date.
	 *
	 * @param publicationDate
	 *            the new publication date
	 */
	public void setPublicationDate(Calendar publicationDate) {
		this.publicationDate = publicationDate;
	}

	/**
	 * Gets the applicant name.
	 *
	 * @return the applicant name
	 */
	public String getApplicantName() {
		return applicantName;
	}

	/**
	 * Sets the applicant name.
	 *
	 * @param applicantName
	 *            the new applicant name
	 */
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	/**
	 * Gets the notification sent.
	 *
	 * @return the notification sent
	 */
	public Boolean getNotificationSent() {
		return notificationSent;
	}

	/**
	 * Sets the notification sent.
	 *
	 * @param notificationSent
	 *            the new notification sent
	 */
	public void setNotificationSent(Boolean notificationSent) {
		this.notificationSent = notificationSent;
	}
}

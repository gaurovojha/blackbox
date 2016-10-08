package com.blackbox.ids.core.model.reference;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Jurisdiction;

// TODO: Auto-generated Javadoc
/**
 * The Class ReferenceBaseData.
 */
@Entity
@Table(name = "BB_REFERENCE_BASE_DATA")
@Inheritance(strategy = InheritanceType.JOINED)
public class ReferenceBaseData extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5414419627479583206L;

	/** The reference base data id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_REFERENCE_BASE_DATA_ID", unique = true, nullable = false, length = 50)
	private Long referenceBaseDataId;

	/** The source application. */
	@Column(name = "SOURCE_APPLICATION")
	private String sourceApplication;

	/** The publication number. */
	@Column(name = "PUBLICATION_NUMBER")
	private String publicationNumber;

	/** The family id. */
	@Column(name = "FAMILY_ID")
	private String familyId;

	/** The application. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_APPLICATION_ID", referencedColumnName = "BB_APPLICATION_ID", nullable = false)
	private ApplicationBase application;

	/** The correspondence. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_CORRESPONDENCE_ID", referencedColumnName = "BB_CORRESPONDENCE_ID", nullable = true)
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

	/** The reference catogory. */
	@Column(name = "REFERENCE_CATEGORY", nullable = false)
	@Enumerated(EnumType.STRING)
	private ReferenceCategoryType referenceCatogory;

	/** The self citation date. */
	@Column(name = "SELF_CITATION_CREATION_DATE", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar selfCitationDate;

	/** The reference source. */
	@Column(name = "REFERENCE_SOURCE")
	@Enumerated(EnumType.STRING)
	private ReferenceSourceType referenceSource;

	/** The reference sub source. */
	@Column(name = "REFERENCE_SUB_SOURCE")
	@Enumerated(EnumType.STRING)
	private ReferenceSubSourceType referenceSubSource;

	/** The jurisdiction. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_JURISDICTION_ID", referencedColumnName = "BB_JURISDICTION_ID", nullable = false)
	private Jurisdiction jurisdiction;

	/** The reviewed status. */
	@Column(name = "REVIEWED_STATUS", nullable = false)
	private Boolean reviewedStatus = false;

	/** The reference flow flag. */
	@Column(name = "REFERENCE_FLOW_FLAG", nullable = false)
	private Boolean referenceFlowFlag = false;

	/** The coupling id. */
	@Column(name = "COUPLING_ID", nullable = true)
	private Long couplingId;

	/** The primary couple. */
	@Column(name = "PRIMARY_COUPLE", nullable = true)
	private Boolean primaryCouple;

	/** The parent reference id. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_REFERENCE_ID", referencedColumnName = "BB_REFERENCE_BASE_DATA_ID", nullable = true)
	private ReferenceBaseData parentReferenceId;

	/** The reference status. */
	@Column(name = "REFERENCE_STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private ReferenceStatus referenceStatus;

	/** The reference type. */
	@Column(name = "REFERENCE_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private ReferenceType referenceType;

	/** The reference comments. */
	@Column(name = "REFERENCE_COMMENTS")
	private String referenceComments;

	/** The active. */
	@Column(name = "ACTIVE", nullable = false)
	private Boolean active = true;

	/** The source reference. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_SOURCE_REFERENCE_ID", referencedColumnName = "BB_SOURCE_REFERENCE_ID", nullable = true)
	private SourceReference sourceReference;

	/** The notification sent. */
	@Column(name = "NOTIFICATION_SENT")
	private Boolean notificationSent = false;

	/** The notification sent. */
	@Column(name = "ENGLISH_TRANSLATION_FLAG")
	private Boolean englishTranslationFlag;

	/** The Attachment. */
	@Column(name = "ATTACHMENT")
	private Boolean attachment;

	/**
	 * Instantiates a new reference base data.
	 */
	public ReferenceBaseData() {
		super();
	}

	/**
	 * Instantiates a new reference base data.
	 *
	 * @param referenceBaseDataId
	 *            the reference base data id
	 */
	public ReferenceBaseData(Long referenceBaseDataId) {
		super();
		this.referenceBaseDataId = referenceBaseDataId;
	}

	/**
	 * Gets the reference base data id.
	 *
	 * @return the reference base data id
	 */
	public Long getReferenceBaseDataId() {
		return referenceBaseDataId;
	}

	/**
	 * Sets the reference base data id.
	 *
	 * @param referenceBaseDataId
	 *            the new reference base data id
	 */
	public void setReferenceBaseDataId(Long referenceBaseDataId) {
		this.referenceBaseDataId = referenceBaseDataId;
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
	 * Gets the reference catogory.
	 *
	 * @return the reference catogory
	 */
	public ReferenceCategoryType getReferenceCatogory() {
		return referenceCatogory;
	}

	/**
	 * Sets the reference catogory.
	 *
	 * @param referenceCatogory
	 *            the new reference catogory
	 */
	public void setReferenceCatogory(ReferenceCategoryType referenceCatogory) {
		this.referenceCatogory = referenceCatogory;
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
	public ReferenceSourceType getReferenceSource() {
		return referenceSource;
	}

	/**
	 * Sets the reference source.
	 *
	 * @param referenceSource
	 *            the new reference source
	 */
	public void setReferenceSource(ReferenceSourceType referenceSource) {
		this.referenceSource = referenceSource;
	}

	/**
	 * Gets the reference sub source.
	 *
	 * @return the reference sub source
	 */
	public ReferenceSubSourceType getReferenceSubSource() {
		return referenceSubSource;
	}

	/**
	 * Sets the reference sub source.
	 *
	 * @param referenceSubSource
	 *            the new reference sub source
	 */
	public void setReferenceSubSource(ReferenceSubSourceType referenceSubSource) {
		this.referenceSubSource = referenceSubSource;
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
	 * Checks if is reviewed status.
	 *
	 * @return true, if is reviewed status
	 */
	public boolean isReviewedStatus() {
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
	 * Checks if is reference flow flag.
	 *
	 * @return true, if is reference flow flag
	 */
	public Boolean isReferenceFlowFlag() {
		return referenceFlowFlag;
	}

	/**
	 * Sets the reference flow flag.
	 *
	 * @param referenceFlowFlag
	 *            the new reference flow flag
	 */
	public void setReferenceFlowFlag(Boolean referenceFlowFlag) {
		this.referenceFlowFlag = referenceFlowFlag;
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
	 * Sets the correspondence.
	 *
	 * @param correspondence
	 *            the new correspondence
	 */
	public void setCorrespondence(CorrespondenceBase correspondence) {
		this.correspondence = correspondence;
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
	public ReferenceBaseData getParentReferenceId() {
		return parentReferenceId;
	}

	/**
	 * Sets the parent reference id.
	 *
	 * @param parentReferenceId
	 *            the new parent reference id
	 */
	public void setParentReferenceId(ReferenceBaseData parentReferenceId) {
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
	 * Gets the source reference.
	 *
	 * @return the source reference
	 */
	public SourceReference getSourceReference() {
		return sourceReference;
	}

	/**
	 * Sets the source reference.
	 *
	 * @param sourceReference
	 *            the new source reference
	 */
	public void setSourceReference(SourceReference sourceReference) {
		this.sourceReference = sourceReference;
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

	/**
	 * Gets the primary couple.
	 *
	 * @return the primary couple
	 */
	public Boolean getPrimaryCouple() {
		return primaryCouple;
	}

	/**
	 * @return the englishTranslationFlag
	 */
	public Boolean getEnglishTranslationFlag() {
		return englishTranslationFlag;
	}

	/**
	 * @param englishTranslationFlag
	 *            the englishTranslationFlag to set
	 */
	public void setEnglishTranslationFlag(Boolean englishTranslationFlag) {
		this.englishTranslationFlag = englishTranslationFlag;
	}

	/**
	 * @return the attachment
	 */
	public Boolean isAttachment() {
		return attachment;
	}

	/**
	 * @param attachment
	 *            the attachment to set
	 */
	public void setAttachment(Boolean attachment) {
		this.attachment = attachment;
	}
}

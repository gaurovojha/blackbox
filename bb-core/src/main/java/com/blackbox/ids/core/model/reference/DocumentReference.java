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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.mstr.Jurisdiction;

/**
 * The Class DocumentReference.
 */
@Entity
@Table(name = "BB_DOCUMENT_REFERENCE")
public class DocumentReference extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1793586111392490002L;

	/** The document reference id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_DOCUMENT_REFERENCE_ID", unique = true, nullable = false, length = 50)
	private Long documentReferenceId;

	/** The source application. */
	@Column(name = "SOURCE_APPLICATION")
	private String sourceApplication;

	/** The correspondence id. */
	@Column(name = "BB_CORRESPONDENCE_ID")
	private Long correspondenceId;

	/** The mailing date. */
	@Column(name = "MAILING_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar mailingDate;

	/** The reference record id. */
	@Column(name = "REFERENCE_RECORD_ID")
	private Long referenceRecordId;

	/** The reference source. */
	@Column(name = "REFERENCE_SOURCE")
	private String referenceSource;

	/** The reference sub source. */
	@Column(name = "REFERENCE_SUB_SOURCE")
	private String referenceSubSource;

	/** The application number. */
	@Column(name = "APPLICATION_NUMBER")
	private Long applicationNumber;

	/** The jurisdiction. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_JURISDICTION_ID", referencedColumnName = "BB_JURISDICTION_ID", nullable = false)
	private Jurisdiction jurisdiction;

	/** The reviewed status. */
	@Column(name = "REVIEWED_STATUS")
	private Boolean reviewedStatus = false;

	/** The reference flow flag. */
	@Column(name = "REFERENCE_FLOW_FLAG")
	private Boolean referenceFlowFlag = false;

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

	/** The active. */
	@Column(name = "ACTIVE")
	private Boolean active = true;

	/** The document type. */
	@Column(name = "DOCUMENT_TYPE")
	private DocumentType documentType;

	/** The author. */
	@Column(name = "AUTHOR")
	private String author;

	/** The title. */
	@Column(name = "TITLE", nullable = false)
	private String title;

	/** The publication detail. */
	@Column(name = "PUBLICATION_DETAIL")
	private String publicationDetail;

	/** The publication month. */
	@Column(name = "PUBLICATION_MONTH")
	private Integer publicationMonth;

	/** The publication day. */
	@Column(name = "PUBLICATION_DAY")
	private Integer publicationDay;

	/** The publication year. */
	@Column(name = "PUBLICATION_YEAR")
	private Integer publicationYear;

	/** The relevant pages. */
	@Column(name = "RELEVANT_PAGES")
	private String relevantPages;

	/** The volume number. */
	@Column(name = "VOLUME_NUMBER")
	private String volumeNumber;

	/** The url. */
	@Column(name = "URL")
	private String URL;

	/** The publication city. */
	@Column(name = "PUBLICATION_CITY")
	private String publicationCity;

	/** The string data. */
	@Column(name = "STRING_DATA", nullable = false)
	private String stringData;

	/** The attachement. */
	@Column(name = "ATTACHMENT")
	private String attachement;

	/** The translated attachement. */
	@Column(name = "TRANSLATED_ATTACHMENT")
	private String translatedAttachement;

	/** The un published. */
	@Column(name = "US_UNPUBLISHED", nullable = false)
	private Boolean unPublished = false;

	/** The application serial number. */
	@Column(name = "APPLICATION_SERIAL_NUMBER")
	private String applicationSerialNumber;

	/**
	 * Gets the document reference id.
	 *
	 * @return the document reference id
	 */
	public Long getDocumentReferenceId() {
		return documentReferenceId;
	}

	/**
	 * Sets the document reference id.
	 *
	 * @param documentReferenceId
	 *            the new document reference id
	 */
	public void setDocumentReferenceId(Long documentReferenceId) {
		this.documentReferenceId = documentReferenceId;
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
	 * Gets the application number.
	 *
	 * @return the application number
	 */
	public Long getApplicationNumber() {
		return applicationNumber;
	}

	/**
	 * Sets the application number.
	 *
	 * @param applicationNumber
	 *            the new application number
	 */
	public void setApplicationNumber(Long applicationNumber) {
		this.applicationNumber = applicationNumber;
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
	 * Gets the reviewed status.
	 *
	 * @return the reviewed status
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
	 * Gets the reference flow flag.
	 *
	 * @return the reference flow flag
	 */
	public Boolean getReferenceFlowFlag() {
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
	 * Gets the active.
	 *
	 * @return the active
	 */
	public Boolean getActive() {
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
	 * Gets the document type.
	 *
	 * @return the document type
	 */
	public DocumentType getDocumentType() {
		return documentType;
	}

	/**
	 * Sets the document type.
	 *
	 * @param documentType
	 *            the new document type
	 */
	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	/**
	 * Gets the author.
	 *
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the author.
	 *
	 * @param author
	 *            the new author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the publication detail.
	 *
	 * @return the publication detail
	 */
	public String getPublicationDetail() {
		return publicationDetail;
	}

	/**
	 * Sets the publication detail.
	 *
	 * @param publicationDetail
	 *            the new publication detail
	 */
	public void setPublicationDetail(String publicationDetail) {
		this.publicationDetail = publicationDetail;
	}

	/**
	 * Gets the publication month.
	 *
	 * @return the publication month
	 */
	public Integer getPublicationMonth() {
		return publicationMonth;
	}

	/**
	 * Sets the publication month.
	 *
	 * @param publicationMonth
	 *            the new publication month
	 */
	public void setPublicationMonth(Integer publicationMonth) {
		this.publicationMonth = publicationMonth;
	}

	/**
	 * Gets the publication day.
	 *
	 * @return the publication day
	 */
	public Integer getPublicationDay() {
		return publicationDay;
	}

	/**
	 * Sets the publication day.
	 *
	 * @param publicationDay
	 *            the new publication day
	 */
	public void setPublicationDay(Integer publicationDay) {
		this.publicationDay = publicationDay;
	}

	/**
	 * Gets the publication year.
	 *
	 * @return the publication year
	 */
	public Integer getPublicationYear() {
		return publicationYear;
	}

	/**
	 * Sets the publication year.
	 *
	 * @param publicationYear
	 *            the new publication year
	 */
	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	/**
	 * Gets the relevant pages.
	 *
	 * @return the relevant pages
	 */
	public String getRelevantPages() {
		return relevantPages;
	}

	/**
	 * Sets the relevant pages.
	 *
	 * @param relevantPages
	 *            the new relevant pages
	 */
	public void setRelevantPages(String relevantPages) {
		this.relevantPages = relevantPages;
	}

	/**
	 * Gets the volume number.
	 *
	 * @return the volume number
	 */
	public String getVolumeNumber() {
		return volumeNumber;
	}

	/**
	 * Sets the volume number.
	 *
	 * @param volumeNumber
	 *            the new volume number
	 */
	public void setVolumeNumber(String volumeNumber) {
		this.volumeNumber = volumeNumber;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * Sets the url.
	 *
	 * @param uRL
	 *            the new url
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * Gets the publication city.
	 *
	 * @return the publication city
	 */
	public String getPublicationCity() {
		return publicationCity;
	}

	/**
	 * Sets the publication city.
	 *
	 * @param publicationCity
	 *            the new publication city
	 */
	public void setPublicationCity(String publicationCity) {
		this.publicationCity = publicationCity;
	}

	/**
	 * Gets the string data.
	 *
	 * @return the string data
	 */
	public String getStringData() {
		return stringData;
	}

	/**
	 * Sets the string data.
	 *
	 * @param stringData
	 *            the new string data
	 */
	public void setStringData(String stringData) {
		this.stringData = stringData;
	}

	/**
	 * Gets the attachement.
	 *
	 * @return the attachement
	 */
	public String getAttachement() {
		return attachement;
	}

	/**
	 * Sets the attachement.
	 *
	 * @param attachement
	 *            the new attachement
	 */
	public void setAttachement(String attachement) {
		this.attachement = attachement;
	}

	/**
	 * Gets the translated attachement.
	 *
	 * @return the translated attachement
	 */
	public String getTranslatedAttachement() {
		return translatedAttachement;
	}

	/**
	 * Sets the translated attachement.
	 *
	 * @param translatedAttachement
	 *            the new translated attachement
	 */
	public void setTranslatedAttachement(String translatedAttachement) {
		this.translatedAttachement = translatedAttachement;
	}

	/**
	 * Gets the un published.
	 *
	 * @return the un published
	 */
	public Boolean getUnPublished() {
		return unPublished;
	}

	/**
	 * Sets the un published.
	 *
	 * @param unPublished
	 *            the new un published
	 */
	public void setUnPublished(Boolean unPublished) {
		this.unPublished = unPublished;
	}

	/**
	 * Gets the application serial number.
	 *
	 * @return the application serial number
	 */
	public String getApplicationSerialNumber() {
		return applicationSerialNumber;
	}

	/**
	 * Sets the application serial number.
	 *
	 * @param applicationSerialNumber
	 *            the new application serial number
	 */
	public void setApplicationSerialNumber(String applicationSerialNumber) {
		this.applicationSerialNumber = applicationSerialNumber;
	}
}

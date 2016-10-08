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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.mstr.Jurisdiction;

/**
 * The Class SourceReference.
 */
@Entity
@Table(name = "BB_SOURCE_REFERENCE")
public class SourceReference extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 160074356651158673L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_SOURCE_REFERENCE_ID", unique = true, nullable = false, length = 50)
	private Long id;

	/** The reference category. */
	@Column(name = "REFERENCE_CATEGORY")
	@Enumerated(EnumType.STRING)
	private ReferenceCategoryType referenceCategory;

	/** The self citation creation date. */
	@Column(name = "SELF_CITATION_CREATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar selfCitationCreationDate;

	/** The source of reference. */
	@Column(name = "SOURCE_OF_REFERENCE")
	@Enumerated(EnumType.STRING)
	private ReferenceSourceType sourceOfReference;

	/** The sub source of reference. */
	@Column(name = "SUB_SOURCE_OF_REFERENCE")
	@Enumerated(EnumType.STRING)
	private ReferenceSubSourceType subSourceOfReference;

	/** The source application number. */
	@Column(name = "SOURCE_APPLICATION_NUMBER")
	private String sourceApplicationNumber;

	/** The jurisdiction. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_JURISDICTION_ID", referencedColumnName = "BB_JURISDICTION_ID")
	private Jurisdiction jurisdiction;

	/** The correspondence. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_CORRESPONDENCE_ID", referencedColumnName = "BB_CORRESPONDENCE_ID")
	private CorrespondenceBase correspondence;

	/** The mailing date. */
	@Column(name = "MAILING_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar mailingDate;

	/** The reference review status. */
	@Column(name = "REFERENCE_REVIEW_STATUS")
	private Boolean referenceReviewStatus = false;

	/** The reference flow flag. */
	@Column(name = "REFERENCE_FLOW_FLAG")
	private Boolean referenceFlowFlag = false;

	/** The reference coupling id. */
	@Column(name = "REFERENCE_COUPLING_ID")
	private Long referenceCouplingId;

	/** The original reference base data. */
	@Column(name = "ORIGINAL_REFERENCE_BASE_DATA")
	private Long originalReferenceBaseData;

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

	/** The npl title. */
	@Column(name = "NPL_TITLE")
	private String nplTitle;

	/** The npl string. */
	@Column(name = "NPL_STRING")
	private String nplString;

	/** The npl us unpublished. */
	@Column(name = "NPL_US_UNPUBLISHED")
	private Boolean nplUsUnpublished = false;

	/** The npl application serial number. */
	@Column(name = "NPL_APPLICATION_SERIAL_NUMBER")
	private String nplApplicationSerialNumber;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the reference category.
	 *
	 * @return the reference category
	 */
	public ReferenceCategoryType getReferenceCategory() {
		return referenceCategory;
	}

	/**
	 * Sets the reference category.
	 *
	 * @param referenceCategory
	 *            the new reference category
	 */
	public void setReferenceCategory(ReferenceCategoryType referenceCategory) {
		this.referenceCategory = referenceCategory;
	}

	/**
	 * Gets the self citation creation date.
	 *
	 * @return the self citation creation date
	 */
	public Calendar getSelfCitationCreationDate() {
		return selfCitationCreationDate;
	}

	/**
	 * Sets the self citation creation date.
	 *
	 * @param selfCitationCreationDate
	 *            the new self citation creation date
	 */
	public void setSelfCitationCreationDate(Calendar selfCitationCreationDate) {
		this.selfCitationCreationDate = selfCitationCreationDate;
	}

	/**
	 * Gets the source of reference.
	 *
	 * @return the source of reference
	 */
	public ReferenceSourceType getSourceOfReference() {
		return sourceOfReference;
	}

	/**
	 * Sets the source of reference.
	 *
	 * @param sourceOfReference
	 *            the new source of reference
	 */
	public void setSourceOfReference(ReferenceSourceType sourceOfReference) {
		this.sourceOfReference = sourceOfReference;
	}

	/**
	 * Gets the sub source of reference.
	 *
	 * @return the sub source of reference
	 */
	public ReferenceSubSourceType getSubSourceOfReference() {
		return subSourceOfReference;
	}

	/**
	 * Sets the sub source of reference.
	 *
	 * @param subSourceOfReference
	 *            the new sub source of reference
	 */
	public void setSubSourceOfReference(ReferenceSubSourceType subSourceOfReference) {
		this.subSourceOfReference = subSourceOfReference;
	}

	/**
	 * Gets the source application number.
	 *
	 * @return the source application number
	 */
	public String getSourceApplicationNumber() {
		return sourceApplicationNumber;
	}

	/**
	 * Sets the source application number.
	 *
	 * @param sourceApplicationNumber
	 *            the new source application number
	 */
	public void setSourceApplicationNumber(String sourceApplicationNumber) {
		this.sourceApplicationNumber = sourceApplicationNumber;
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
	 * Checks if is reference review status.
	 *
	 * @return true, if is reference review status
	 */
	public Boolean isReferenceReviewStatus() {
		return referenceReviewStatus;
	}

	/**
	 * Sets the reference review status.
	 *
	 * @param referenceReviewStatus
	 *            the new reference review status
	 */
	public void setReferenceReviewStatus(Boolean referenceReviewStatus) {
		this.referenceReviewStatus = referenceReviewStatus;
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
	 * Gets the reference coupling id.
	 *
	 * @return the reference coupling id
	 */
	public Long getReferenceCouplingId() {
		return referenceCouplingId;
	}

	/**
	 * Sets the reference coupling id.
	 *
	 * @param referenceCouplingId
	 *            the new reference coupling id
	 */
	public void setReferenceCouplingId(Long referenceCouplingId) {
		this.referenceCouplingId = referenceCouplingId;
	}

	/**
	 * Gets the original reference base data.
	 *
	 * @return the original reference base data
	 */
	public Long getOriginalReferenceBaseData() {
		return originalReferenceBaseData;
	}

	/**
	 * Sets the original reference base data.
	 *
	 * @param originalReferenceBaseData
	 *            the new original reference base data
	 */
	public void setOriginalReferenceBaseData(Long originalReferenceBaseData) {
		this.originalReferenceBaseData = originalReferenceBaseData;
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
	 * Gets the npl title.
	 *
	 * @return the npl title
	 */
	public String getNplTitle() {
		return nplTitle;
	}

	/**
	 * Sets the npl title.
	 *
	 * @param nplTitle
	 *            the new npl title
	 */
	public void setNplTitle(String nplTitle) {
		this.nplTitle = nplTitle;
	}

	/**
	 * Gets the npl string.
	 *
	 * @return the npl string
	 */
	public String getNplString() {
		return nplString;
	}

	/**
	 * Sets the npl string.
	 *
	 * @param nplString
	 *            the new npl string
	 */
	public void setNplString(String nplString) {
		this.nplString = nplString;
	}

	/**
	 * Checks if is npl us unpublished.
	 *
	 * @return true, if is npl us unpublished
	 */
	public Boolean isNplUsUnpublished() {
		return nplUsUnpublished;
	}

	/**
	 * Sets the npl us unpublished.
	 *
	 * @param nplUsUnpublished
	 *            the new npl us unpublished
	 */
	public void setNplUsUnpublished(Boolean nplUsUnpublished) {
		this.nplUsUnpublished = nplUsUnpublished;
	}

	/**
	 * Gets the npl application serial number.
	 *
	 * @return the npl application serial number
	 */
	public String getNplApplicationSerialNumber() {
		return nplApplicationSerialNumber;
	}

	/**
	 * Sets the npl application serial number.
	 *
	 * @param nplApplicationSerialNumber
	 *            the new npl application serial number
	 */
	public void setNplApplicationSerialNumber(String nplApplicationSerialNumber) {
		this.nplApplicationSerialNumber = nplApplicationSerialNumber;
	}
}

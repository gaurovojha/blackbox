package com.blackbox.ids.core.model.reference;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class ReferenceBasePUSData.
 */
@Entity
@Table(name = "BB_REFERENCE_BASE_PUS_DATA")
@PrimaryKeyJoinColumn(name = "BB_REFERENCE_BASE_DATA_ID")
public class ReferenceBasePUSData extends ReferenceBaseData {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7152685473171346356L;

	/** The converted publication number. */
	@Column(name = "PUBLICATION_NUMBER_CONVERTED")
	private String convertedPublicationNumber;

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

	/**
	 * Instantiates a new reference base pus data.
	 */
	public ReferenceBasePUSData() {
		super();
	}

	/*- ------------------------------------- JPA Callbacks -- */
	@Override
	public void prePersist() {
		setReferenceType();
		super.prePersist();
	}

	@Override
	public void preUpdate() {
		setReferenceType();
		super.preUpdate();
	}

	private void setReferenceType() {
		int lenPublicationNo = convertedPublicationNumber == null ? 0 : convertedPublicationNumber.length();
		setReferenceType(
				(lenPublicationNo == 7 || lenPublicationNo == 8) ? ReferenceType.PUS : ReferenceType.US_PUBLICATION);
	}

	/*- ------------------------------------- getter-setters -- */
	/**
	 * Gets the converted publication number.
	 *
	 * @return the converted publication number
	 */
	public String getConvertedPublicationNumber() {
		return convertedPublicationNumber;
	}

	/**
	 * Sets the converted publication number.
	 *
	 * @param convertedPublicationNumber
	 *            the new converted publication number
	 */
	public void setConvertedPublicationNumber(String convertedPublicationNumber) {
		this.convertedPublicationNumber = convertedPublicationNumber;
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

}

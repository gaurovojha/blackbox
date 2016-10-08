package com.blackbox.ids.core.model.reference;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class ReferenceBaseFPData.
 */
@Entity
@Table(name = "BB_REFERENCE_BASE_FP_DATA")
@PrimaryKeyJoinColumn(name = "BB_REFERENCE_BASE_DATA_ID")
public class ReferenceBaseFPData extends ReferenceBaseData {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3138819503058565278L;

	/** The foreign document number. */
	@Column(name = "FOREIGN_DOCUMENT_NUMBER")
	private String foreignDocumentNumber;

	/** The converted foreign document number. */
	@Column(name = "FOREIGN_DOCUMENT_NUMBER_CONVERTED")
	private String convertedForeignDocumentNumber;

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
	 * Gets the foreign document number.
	 *
	 * @return the foreign document number
	 */
	public String getForeignDocumentNumber() {
		return foreignDocumentNumber;
	}

	/**
	 * Sets the foreign document number.
	 *
	 * @param foreignDocumentNumber
	 *            the new foreign document number
	 */
	public void setForeignDocumentNumber(String foreignDocumentNumber) {
		this.foreignDocumentNumber = foreignDocumentNumber;
	}

	/**
	 * Gets the converted foreign document number.
	 *
	 * @return the converted foreign document number
	 */
	public String getConvertedForeignDocumentNumber() {
		return convertedForeignDocumentNumber;
	}

	/**
	 * Sets the converted foreign document number.
	 *
	 * @param convertedForeignDocumentNumber
	 *            the new converted foreign document number
	 */
	public void setConvertedForeignDocumentNumber(String convertedForeignDocumentNumber) {
		this.convertedForeignDocumentNumber = convertedForeignDocumentNumber;
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
	 * Instantiates a new reference base fp data.
	 */
	public ReferenceBaseFPData() {
		super();
	}
}

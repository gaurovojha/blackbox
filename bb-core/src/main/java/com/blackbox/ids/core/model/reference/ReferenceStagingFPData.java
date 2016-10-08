package com.blackbox.ids.core.model.reference;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The Class ReferenceStagingFPData.
 */
@Embeddable
public class ReferenceStagingFPData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8583149945790443525L;

	/** The foreign document number. */
	@Column(name = "FOREIGN_DOCUMENT_NUMBER")
	private String foreignDocumentNumber;

	/** The converted foreign document number. */
	@Column(name = "FOREIGN_DOCUMENT_NUMBER_CONVERTED")
	private String convertedForeignDocumentNumber;

	/** The jurisdiction code. */
	@Column(name = "JURISDICTION")
	private String jurisdictionCode;

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
}
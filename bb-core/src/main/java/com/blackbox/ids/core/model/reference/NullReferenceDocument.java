package com.blackbox.ids.core.model.reference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.blackbox.ids.core.model.base.BaseEntity;

/**
 * The Class NullReferenceDocument.
 */
@Entity
@Table(name = "BB_NULL_REFERENCE_DOCUMENT")
public class NullReferenceDocument extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6372609613337551153L;

	/** The null ref doc id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_NULL_REFERENCE_DOCUMENT_ID", unique = true, nullable = false, length = 50)
	private Long nullRefDocId;

	/** The ocr data id. */
	@Column(name = "BB_OCR_DATA_ID")
	private Long ocrDataId;

	/** The correspondence id. */
	@Column(name = "BB_CORRESPONDENCE_ID")
	private Long correspondenceID;

	/** The null reference. */
	@Column(name = "NULL_REFERENCE", nullable = false)
	private Boolean nullReference = false;

	/** The npl string. */
	@Column(name = "NPL_STRING")
	private String nplString;

	/**
	 * Gets the null ref doc id.
	 *
	 * @return the null ref doc id
	 */
	public Long getNullRefDocId() {
		return nullRefDocId;
	}

	/**
	 * Sets the null ref doc id.
	 *
	 * @param nullRefDocId
	 *            the new null ref doc id
	 */
	public void setNullRefDocId(Long nullRefDocId) {
		this.nullRefDocId = nullRefDocId;
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
	 * Checks if is null reference.
	 *
	 * @return true, if is null reference
	 */
	public boolean isNullReference() {
		return nullReference;
	}

	/**
	 * Sets the null reference.
	 *
	 * @param nullReference
	 *            the new null reference
	 */
	public void setNullReference(boolean nullReference) {
		this.nullReference = nullReference;
	}

	/**
	 * Gets the correspondence id.
	 *
	 * @return the correspondence id
	 */
	public Long getCorrespondenceID() {
		return correspondenceID;
	}

	/**
	 * Sets the correspondence id.
	 *
	 * @param correspondenceID
	 *            the new correspondence id
	 */
	public void setCorrespondenceID(Long correspondenceID) {
		this.correspondenceID = correspondenceID;
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
}

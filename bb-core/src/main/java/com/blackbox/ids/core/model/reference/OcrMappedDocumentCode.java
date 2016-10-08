package com.blackbox.ids.core.model.reference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.blackbox.ids.core.model.base.BaseEntity;

/**
 * The Class OcrMappedDocumentCode.
 */
@Entity
@Table(name = "BB_OCR_MAPPED_DOCUMENT_CODE")
public class OcrMappedDocumentCode extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8658191642823439683L;

	/** The ocr mapped document code id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_OCR_MAPPED_DOCUMENT_CODE_ID", unique = true, nullable = false, length = 50)
	private Long ocrMappedDocumentCodeId;

	/** The jurisdiction code. */
	@Column(name = "JURISDICTION_CODE")
	private String jurisdictionCode;

	/** The document code. */
	@Column(name = "DOCUMENT_CODE")
	private String documentCode;

	/**
	 * Gets the ocr mapped document code id.
	 *
	 * @return the ocr mapped document code id
	 */
	public Long getOcrMappedDocumentCodeId() {
		return ocrMappedDocumentCodeId;
	}

	/**
	 * Sets the ocr mapped document code id.
	 *
	 * @param ocrMappedDocumentCodeId
	 *            the new ocr mapped document code id
	 */
	public void setOcrMappedDocumentCodeId(Long ocrMappedDocumentCodeId) {
		this.ocrMappedDocumentCodeId = ocrMappedDocumentCodeId;
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
	 * Gets the document code.
	 *
	 * @return the document code
	 */
	public String getDocumentCode() {
		return documentCode;
	}

	/**
	 * Sets the document code.
	 *
	 * @param documentCode
	 *            the new document code
	 */
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	/**
	 * Instantiates a new ocr mapped document code.
	 */
	public OcrMappedDocumentCode() {
		super();
	}

}

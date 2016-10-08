package com.blackbox.ids.core.model.reference;

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

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;

/*
 * Entity which contains data extracted by OCR.
 */

/**
 * The Class OcrData.
 */
@Entity
@Table(name = "BB_OCR_DATA")
public class OcrData extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6904700063529092162L;

	/** The ocr data id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_OCR_DATA_ID", unique = true, nullable = false, length = 50)
	private Long ocrDataId;

	/** The jurisdiction. */
	@Column(name = "JURISDICTION_CODE")
	private String jurisdictionCode;

	/** The correspondence id. */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_CORRESPONDENCE_ID", referencedColumnName = "BB_CORRESPONDENCE_ID")
	private CorrespondenceBase correspondenceId;

	/** The ocr status. */
	@Column(name = "OCR_STATUS")
	@Enumerated(EnumType.STRING)
	private OcrStatus ocrStatus;

	/** The processed. */
	@Column(name = "PROCESSED")
	private Boolean processed = false;

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
	 * Gets the correspondence id.
	 *
	 * @return the correspondence id
	 */
	public CorrespondenceBase getCorrespondenceId() {
		return correspondenceId;
	}

	/**
	 * Sets the correspondence id.
	 *
	 * @param correspondenceId
	 *            the new correspondence id
	 */
	public void setCorrespondenceId(CorrespondenceBase correspondenceId) {
		this.correspondenceId = correspondenceId;
	}

	/**
	 * Gets the ocr status.
	 *
	 * @return the ocr status
	 */
	public OcrStatus getOcrStatus() {
		return ocrStatus;
	}

	/**
	 * Sets the ocr status.
	 *
	 * @param ocrStatus
	 *            the new ocr status
	 */
	public void setOcrStatus(OcrStatus ocrStatus) {
		this.ocrStatus = ocrStatus;
	}

	/**
	 * Checks if is processed.
	 *
	 * @return true, if is processed
	 */
	public Boolean isProcessed() {
		return processed;
	}

	/**
	 * Sets the processed.
	 *
	 * @param processed
	 *            the new processed
	 */
	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}
}

package com.blackbox.ids.core.dto.reference;

/**
 * The Class NullReferenceDocumentDTO.
 */
public class NullReferenceDocumentDTO {

	/** The id. */
	private Long id;

	/** The ocr data id. */
	private Long ocrDataId;

	/** The null reference. */
	private boolean nullReference;

	/** The correspondence id. */
	private Long correspondenceId;

	/** The application number. */
	private Long applicationNumber;

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
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the ocr data id.
	 *
	 * @return the ocrDataId
	 */
	public Long getOcrDataId() {
		return ocrDataId;
	}

	/**
	 * Sets the ocr data id.
	 *
	 * @param ocrDataId
	 *            the ocrDataId to set
	 */
	public void setOcrDataId(Long ocrDataId) {
		this.ocrDataId = ocrDataId;
	}

	/**
	 * Checks if is null reference.
	 *
	 * @return the nullReference
	 */
	public boolean isNullReference() {
		return nullReference;
	}

	/**
	 * Sets the null reference.
	 *
	 * @param nullReference
	 *            the nullReference to set
	 */
	public void setNullReference(boolean nullReference) {
		this.nullReference = nullReference;
	}

	/**
	 * Gets the correspondence id.
	 *
	 * @return the correspondenceId
	 */
	public Long getCorrespondenceId() {
		return correspondenceId;
	}

	/**
	 * Sets the correspondence id.
	 *
	 * @param correspondenceId
	 *            the correspondenceId to set
	 */
	public void setCorrespondenceId(Long correspondenceId) {
		this.correspondenceId = correspondenceId;
	}

	/**
	 * Gets the application number.
	 *
	 * @return the applicationNumber
	 */
	public Long getApplicationNumber() {
		return applicationNumber;
	}

	/**
	 * Sets the application number.
	 *
	 * @param applicationNumber
	 *            the applicationNumber to set
	 */
	public void setApplicationNumber(Long applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

}

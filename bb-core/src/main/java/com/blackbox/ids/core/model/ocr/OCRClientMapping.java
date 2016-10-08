package com.blackbox.ids.core.model.ocr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.blackbox.ids.core.model.base.BaseEntity;

@Entity
@Table(name = "BB_OCR_CLIENT_MAPPING")
public class OCRClientMapping extends BaseEntity {

	private static final long serialVersionUID = -3629864722225636924L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_OCR_CLIENT_MAPPING_ID", unique = true, nullable = false, length = 50)
	private Long id;

	/** The code. */
	@Column(name = "DOCUMENT_CODE", nullable = false)
	private String docCode;

	@Column(name = "IS_OCR", nullable = false)
	private boolean isOCR;

	public Long getId() {
		return id;
	}

	public String getDocCode() {
		return docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public boolean isOCR() {
		return isOCR;
	}

	public void setOCR(boolean isOCR) {
		this.isOCR = isOCR;
	}
}

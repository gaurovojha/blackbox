package com.blackbox.ids.core.model.ocr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BB_DOCUMENT_OCR_DETAIL")
public class DocumentOCRDetail implements Serializable {

	private static final long serialVersionUID = 1845168536013012395L;

	public enum OCRProduct {
		FLEXI_CAPTURE, FINE_READER;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_DOCUMENT_OCR_DETAIL_ID", unique = true, nullable = false, length = 50)
	private Long id;

	/** The code. */
	@Column(name = "DOCUMENT_CODE", nullable = false)
	private String docCode;

	@Column(name = "JURISDICTION_CODE", nullable = false)
	private String jurisdictionCode;
	
	@Column(name = "IS_OCR", nullable = false)
	private boolean isOCR;

	@Enumerated(EnumType.STRING)
	@Column(name = "OCR_PRODUCT", nullable = false)
	private OCRProduct ocrProduct;

	@Column(name = "TEMPLATE_ID", nullable = false)
	private String templateId;

	@Column(name = "IS_AUTOMATED_DATA_EXTRACTION", nullable = false)
	private boolean isAutomatedExtraction;

	@Column(name = "DATA_EXTRACTION_TYPE", nullable = false)
	private String dataExtractionType;
	
	public Long getId() {
		return id;
	}

	public String getDocCode() {
		return docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public String getJurisdictionCode() {
		return jurisdictionCode;
	}

	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}

	public OCRProduct getOcrProduct() {
		return ocrProduct;
	}

	public void setOcrProduct(OCRProduct ocrProduct) {
		this.ocrProduct = ocrProduct;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public boolean isAutomatedExtraction() {
		return isAutomatedExtraction;
	}

	public void setAutomatedExtraction(boolean isAutomatedExtraction) {
		this.isAutomatedExtraction = isAutomatedExtraction;
	}

	public String isDataExtractionType() {
		return dataExtractionType;
	}

	public void setDataExtractionType(String dataExtractionType) {
		this.dataExtractionType = dataExtractionType;
	}

	public boolean isOCR() {
		return isOCR;
	}

	public void setOCR(boolean isOCR) {
		this.isOCR = isOCR;
	}
}

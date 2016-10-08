package com.blackbox.ids.core.model.ocr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BB_OCR_DOC")
public class OCRDocument implements Serializable{

	private static final long serialVersionUID = -5613430350440219411L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_OCR_DOC_ID", unique = true, nullable = false, length = 50)
	private Long id;

	@Column(name = "BB_CORRESPONDENCE_ID", nullable = false)
	private Long correspondenceId;

	/** The code. */
	@Column(name = "DOCUMENT_CODE", nullable = false)
	private String docCode;

	@Column(name = "DOCUMENT_PAGE_COUNT", nullable = false)
	private Integer documentPageCount;

	public Long getId() {
		return id;
	}

	public Long getCorrespondenceId() {
		return correspondenceId;
	}

	public void setCorrespondenceId(Long correspondenceId) {
		this.correspondenceId = correspondenceId;
	}

	public String getDocCode() {
		return docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public Integer getDocumentPageCount() {
		return documentPageCount;
	}

	public void setDocumentPageCount(Integer documentPageCount) {
		this.documentPageCount = documentPageCount;
	}

}

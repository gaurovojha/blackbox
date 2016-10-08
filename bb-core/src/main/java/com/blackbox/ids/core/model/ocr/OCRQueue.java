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

import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;

@Entity
@Table(name = "BB_OCR_QUEUE")
public class OCRQueue implements Serializable{

	private static final long serialVersionUID = -5964910804063127172L;

	public enum Status {
		INITIATED, IN_PROGRESS, SUCCESS, FAILED, ERROR;
	}

	public enum OCRStatus {
		SUCCESS, FAILED, ERROR;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_OCR_QUEUE_ID", unique = true, nullable = false, length = 50)
	private Long id;

	@Column(name = "BB_CORRESPONDENCE_ID", nullable = false)
	private CorrespondenceBase correspondenceBase;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private Status status;

	@Enumerated(EnumType.STRING)
	@Column(name = "OCR_STATUS", nullable = true)
	private OCRStatus ocrStatus;

	public Long getId() {
		return id;
	}

	public CorrespondenceBase getCorrespondenceBase() {
		return correspondenceBase;
	}

	public void setCorrespondenceBase(CorrespondenceBase correspondenceBase) {
		this.correspondenceBase = correspondenceBase;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public OCRStatus getOcrStatus() {
		return ocrStatus;
	}

	public void setOcrStatus(OCRStatus ocrStatus) {
		this.ocrStatus = ocrStatus;
	}
}

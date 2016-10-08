package com.blackbox.ids.core.model.correspondence;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;

/**
 * The Class CorrespondenceBase.
 */
@Entity
@Table(name = "BB_CORRESPONDENCE")
public class CorrespondenceBase extends Correspondence {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5669984510936712922L;


	public enum OCRStatus {
		FAILED;
	}
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_CORRESPONDENCE_ID", unique = true, nullable = false)
	private Long id;

	/** The application. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BB_APPLICATION_ID", referencedColumnName = "BB_APPLICATION_ID", nullable = false)
	private ApplicationBase application;

	/** The document code. */
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BB_DOCUMENT_CODE_ID", referencedColumnName = "BB_DOCUMENT_CODE_ID", nullable = false)
	private DocumentCode documentCode;

	/** The step. */
	@Enumerated(EnumType.STRING)
	@Column(name = "STEP", nullable = false)
	private StepType step;

	/** The step status. */
	@Enumerated(EnumType.STRING)
	@Column(name = "STEP_STATUS", nullable = false)
	private StepStatus stepStatus;

	/** The private pair upload date. */
	@Column(name = "PRIVATE_PAIR_UPLOAD_DATE")
	private Calendar privatePairUploadDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "OCR_STATUS")
	private OCRStatus ocrStatus;

	/** The jurisdiction code. */
	@Transient
	private String jurisdictionCode;

	@OneToMany(mappedBy = "correspondence")
	private Set<ReferenceBaseData> refrenceBaseData;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * Gets the document code.
	 *
	 * @return the document code
	 */
	public DocumentCode getDocumentCode() {
		return documentCode;
	}

	/**
	 * Sets the document code.
	 *
	 * @param documentCode the new document code
	 */
	public void setDocumentCode(DocumentCode documentCode) {
		this.documentCode = documentCode;
	}

	/**
	 * Gets the step.
	 *
	 * @return the step
	 */
	public StepType getStep() {
		return step;
	}

	/**
	 * Sets the step.
	 *
	 * @param step the new step
	 */
	public void setStep(StepType step) {
		this.step = step;
	}

	/**
	 * Gets the step status.
	 *
	 * @return the step status
	 */
	public StepStatus getStepStatus() {
		return stepStatus;
	}

	/**
	 * Sets the step status.
	 *
	 * @param stepStatus the new step status
	 */
	public void setStepStatus(StepStatus stepStatus) {
		this.stepStatus = stepStatus;
	}

	/**
	 * Gets the private pair upload date.
	 *
	 * @return the private pair upload date
	 */
	public Calendar getPrivatePairUploadDate() {
		return privatePairUploadDate;
	}

	/**
	 * Sets the private pair upload date.
	 *
	 * @param privatePairUploadDate the new private pair upload date
	 */
	public void setPrivatePairUploadDate(Calendar privatePairUploadDate) {
		this.privatePairUploadDate = privatePairUploadDate;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}


	public ApplicationBase getApplication() {
		return application;
	}


	public void setApplication(ApplicationBase application) {
		this.application = application;
	}


	public OCRStatus getOcrStatus() {
		return ocrStatus;
	}


	public void setOcrStatus(OCRStatus ocrStatus) {
		this.ocrStatus = ocrStatus;
	}


	public String getJurisdictionCode() {
		return jurisdictionCode;
	}


	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}


	public Set<ReferenceBaseData> getRefrenceBaseData() {
		return refrenceBaseData;
	}


	public void setRefrenceBaseData(Set<ReferenceBaseData> refrenceBaseData) {
		this.refrenceBaseData = refrenceBaseData;
	}

}

/**
 *
 */
package com.blackbox.ids.core.model.IDS;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

/**
 * The entity class <code>CertificationStatement</code> maintains the certification details for an IDS record.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_IDS_CERTIFICATION_STATEMENT")
public class CertificationStatement implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -517400216161940110L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_CERTIFICATION_STATEMENT_ID", updatable = false)
	private Long id;

	/**
	 * Indicates that each item of information contained in the information disclosure statement was first cited in any
	 * communication from a foreign patent office in a counterpart foreign application not more than three months prior
	 * to the filing of the information disclosure statement. See 37 CFR 1.97(e)(1).
	 */
	@Column(name = "IS_PRIOR_CITED")
	private boolean priorCited;

	/**
	 * Indicates that no item of information contained in the information disclosure statement was cited in a
	 * communication from a foreign patent office in a counterpart foreign application, and ...
	 */
	@Column(name = "IS_PRIOR_UNCITED")
	private boolean priorUncited;

	/** Indicates whether a certification statement is attached or not. */
	@Column(name = "IS_CERTIFICATION_ATTACHED")
	private boolean certificationAttached;

	/** Name the file given as certification statement. */
	@Column(name = "CERTIFICATION_STATEMENT")
	private String certificationStatement;

	/** Indicates that the fee set forth in 37 CFR 1.17 (p) has been submitted herewith. */
	@Column(name = "IS_FEE_SUBMITTED")
	private boolean feeSubmitted;

	/** Indicates that a certification statement is not submitted herewith. */
	@Column(name = "IS_CERTIFICATION_STATEMENT_SUBMITTED")
	private boolean certificationStatementSubmitted;

	/** e-signature of the paralegal user, who has requested the approval for IDS. */
	/*-
	@Lob
	@Column(name = "ESIGNATURE", length = Integer.MAX_VALUE)
	private byte[] signature;
	 */

	/** Comments added by paralegal user on certification statement for attorney user. */
	@Column(name = "COMMENT")
	private String comment;

	/** Attachment document uploaded by the user. */
	@Transient
	private MultipartFile attachment;

	/*- ------------------------------------ getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isPriorCited() {
		return priorCited;
	}

	public void setPriorCited(boolean priorCited) {
		this.priorCited = priorCited;
	}

	public boolean isPriorUncited() {
		return priorUncited;
	}

	public void setPriorUncited(boolean priorUncited) {
		this.priorUncited = priorUncited;
	}

	public boolean isCertificationAttached() {
		return certificationAttached;
	}

	public void setCertificationAttached(boolean certificationAttached) {
		this.certificationAttached = certificationAttached;
	}

	public String getCertificationStatement() {
		return certificationStatement;
	}

	public void setCertificationStatement(String certificationStatement) {
		this.certificationStatement = certificationStatement;
	}

	public boolean isFeeSubmitted() {
		return feeSubmitted;
	}

	public void setFeeSubmitted(boolean feeSubmitted) {
		this.feeSubmitted = feeSubmitted;
	}

	public boolean isCertificationStatementSubmitted() {
		return certificationStatementSubmitted;
	}

	public void setCertificationStatementSubmitted(boolean certificationStatementSubmitted) {
		this.certificationStatementSubmitted = certificationStatementSubmitted;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public MultipartFile getAttachment() {
		return attachment;
	}

	public void setAttachment(MultipartFile attachment) {
		this.attachment = attachment;
	}

}

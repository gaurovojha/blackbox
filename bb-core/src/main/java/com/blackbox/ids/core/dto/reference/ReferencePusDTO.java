/**
 * 
 */
package com.blackbox.ids.core.dto.reference;

import java.io.Serializable;
import java.util.Map;

/**
 * The Class ReferencePusDTO.
 *
 * @author Nagarro
 */
public class ReferencePusDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -276684168079797556L;

	/** The id. */
	private Long id;

	/** The publication number. */
	private String publicationNumber;

	/** The converted publication number. */
	private String convertedPublicationNumber;

	/** The kind code. */
	private String kindCode;

	/** The publication date. */
	private String publicationDate;

	/** The applicant name. */
	private String applicantName;

	/** The comment. */
	private String comment;

	/** The issue date. */
	private String issueDate;

	/** The filing date. */
	private String filingDate;

	/** The flow status. */
	private String flowStatus;

	/** The reference reviewed details. */
	private Map<String, String> referenceReviewedDetails;

	/** The IDS id. */
	private Long idsId;

	/** The do not file flag. */
	private boolean doNotFile;

	/** IDS status */
	private String idsStatus;

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
	 * Gets the publication number.
	 *
	 * @return the publicationNumber
	 */
	public String getPublicationNumber() {
		return publicationNumber;
	}

	/**
	 * Sets the publication number.
	 *
	 * @param publicationNumber
	 *            the publicationNumber to set
	 */
	public void setPublicationNumber(String publicationNumber) {
		this.publicationNumber = publicationNumber;
	}

	/**
	 * Gets the converted publication number.
	 *
	 * @return the convertedPublicationNumber
	 */
	public String getConvertedPublicationNumber() {
		return convertedPublicationNumber;
	}

	/**
	 * Sets the converted publication number.
	 *
	 * @param convertedPublicationNumber
	 *            the convertedPublicationNumber to set
	 */
	public void setConvertedPublicationNumber(String convertedPublicationNumber) {
		this.convertedPublicationNumber = convertedPublicationNumber;
	}

	/**
	 * Gets the kind code.
	 *
	 * @return the kindCode
	 */
	public String getKindCode() {
		return kindCode;
	}

	/**
	 * Sets the kind code.
	 *
	 * @param kindCode
	 *            the kindCode to set
	 */
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	/**
	 * Gets the publication date.
	 *
	 * @return the publicationDate
	 */
	public String getPublicationDate() {
		return publicationDate;
	}

	/**
	 * Sets the publication date.
	 *
	 * @param publicationDate
	 *            the publicationDate to set
	 */
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	/**
	 * Gets the applicant name.
	 *
	 * @return the applicantName
	 */
	public String getApplicantName() {
		return applicantName;
	}

	/**
	 * Sets the applicant name.
	 *
	 * @param applicantName
	 *            the applicantName to set
	 */
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	/**
	 * Gets the issue date.
	 *
	 * @return the issueDate
	 */
	public String getIssueDate() {
		return issueDate;
	}

	/**
	 * Sets the issue date.
	 *
	 * @param issueDate
	 *            the issueDate to set
	 */
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment.
	 *
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the filingDate
	 */
	public String getFilingDate() {
		return filingDate;
	}

	/**
	 * @param filingDate
	 *            the filingDate to set
	 */
	public void setFilingDate(String filingDate) {
		this.filingDate = filingDate;
	}

	/**
	 * @return the flowStatus
	 */
	public String getFlowStatus() {
		return flowStatus;
	}

	/**
	 * @param flowStatus
	 *            the flowStatus to set
	 */
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	/**
	 * @return the referenceReviewedDetails
	 */
	public Map<String, String> getReferenceReviewedDetails() {
		return referenceReviewedDetails;
	}

	/**
	 * @param referenceReviewedDetails
	 *            the referenceReviewedDetails to set
	 */
	public void setReferenceReviewedDetails(Map<String, String> referenceReviewedDetails) {
		this.referenceReviewedDetails = referenceReviewedDetails;
	}

	/**
	 * @return the idsId
	 */
	public Long getIdsId() {
		return idsId;
	}

	/**
	 * @param idsId
	 *            the idsId to set
	 */
	public void setIdsId(Long idsId) {
		this.idsId = idsId;
	}

	/**
	 * @return the doNotFile
	 */
	public boolean isDoNotFile() {
		return doNotFile;
	}

	/**
	 * @param doNotFile
	 *            the doNotFile to set
	 */
	public void setDoNotFile(boolean doNotFile) {
		this.doNotFile = doNotFile;
	}

	/**
	 * @return the idsStatus
	 */
	public String getIdsStatus() {
		return idsStatus;
	}

	/**
	 * @param idsStatus
	 *            the idsStatus to set
	 */
	public void setIdsStatus(String idsStatus) {
		this.idsStatus = idsStatus;
	}

}
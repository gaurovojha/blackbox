/**
 * 
 */
package com.blackbox.ids.core.dto.reference;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * The Class ReferenceFpDTO.
 *
 * @author Nagarro
 */
public class ReferenceFpDTO {

	/** The id. */
	private Long id;

	/** The foreign document number. */
	private String foreignDocumentNumber;

	/** The converted foreign document number. */
	private String convertedForeignDocumentNumber;

	/** The kind code. */
	private String kindCode;

	/** The publication date. */
	private String publicationDate;

	/** The applicant name. */
	private String applicantName;

	/** The jurisdiction. */
	private String jurisdiction;

	/** The publication number. */
	private String publicationNumber;

	/** The comment. */
	private String comment;

	/** The attached file **/
	private MultipartFile file;

	/** English translation **/
	private boolean englishTranslation;

	/** The filing date. */
	private String filingDate;

	/** The flow status. */
	private String flowStatus;

	/** The reference reviewed details. */
	private Map<String, String> referenceReviewedDetails;

	/** Attachment **/
	private boolean attachment;

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
	 * Gets the foreign document number.
	 *
	 * @return the foreignDocumentNumber
	 */
	public String getForeignDocumentNumber() {
		return foreignDocumentNumber;
	}

	/**
	 * Sets the foreign document number.
	 *
	 * @param foreignDocumentNumber
	 *            the foreignDocumentNumber to set
	 */
	public void setForeignDocumentNumber(String foreignDocumentNumber) {
		this.foreignDocumentNumber = foreignDocumentNumber;
	}

	/**
	 * Gets the converted foreign document number.
	 *
	 * @return the convertedForeignDocumentNumber
	 */
	public String getConvertedForeignDocumentNumber() {
		return convertedForeignDocumentNumber;
	}

	/**
	 * Sets the converted foreign document number.
	 *
	 * @param convertedForeignDocumentNumber
	 *            the convertedForeignDocumentNumber to set
	 */
	public void setConvertedForeignDocumentNumber(String convertedForeignDocumentNumber) {
		this.convertedForeignDocumentNumber = convertedForeignDocumentNumber;
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
	 * Gets the jurisdiction.
	 *
	 * @return the jurisdiction
	 */
	public String getJurisdiction() {
		return jurisdiction;
	}

	/**
	 * Sets the jurisdiction.
	 *
	 * @param jurisdiction
	 *            the jurisdiction to set
	 */
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
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

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public boolean isEnglishTranslation() {
		return englishTranslation;
	}

	public void setEnglishTranslation(boolean englishTranslation) {
		this.englishTranslation = englishTranslation;
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
	 * @return the attachment
	 */
	public boolean isAttachment() {
		return attachment;
	}

	/**
	 * @param attachment
	 *            the attachment to set
	 */
	public void setAttachment(boolean attachment) {
		this.attachment = attachment;
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

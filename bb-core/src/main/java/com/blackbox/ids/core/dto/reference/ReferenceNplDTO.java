/**
 * 
 */
package com.blackbox.ids.core.dto.reference;

import java.util.Map;

/**
 * The Class ReferenceNplDTO.
 *
 * @author Nagarro
 */
public class ReferenceNplDTO {

	/** The id. */
	private Long id;

	/** The author. */
	private String author;

	/** The title. */
	private String title;

	/** The publication date. */
	private String publicationDate;

	/** The publication detail. */
	private String publicationDetail;

	/** The publication month. */
	private Integer publicationMonth;

	/** The publication day. */
	private Integer publicationDay;

	/** The publication year. */
	private Integer publicationYear;

	/** The relevant pages. */
	private String relevantPages;

	/** The url. */
	private String URL;

	/** The publication city. */
	private String publicationCity;

	/** The string data. */
	private String stringData;

	/** The un published. */
	private boolean unPublished;

	/** The application serial number. */
	private String applicationSerialNumber;

	/** The issue number. */
	private String issueNumber;

	/** The retrival date. */
	private String retrivalDate;

	/** The filing date. */
	private String filingDate;

	/** The flow status. */
	private String flowStatus;

	/** The reference reviewed details. */
	private Map<String, String> referenceReviewedDetails;

	/** Attachment **/
	private boolean attachment;

	/** English translation **/
	private boolean englishTranslation;

	/** The IDS id. */
	private Long idsId;

	/** The do not file flag. */
	private boolean doNotFile;

	/** IDS status */
	private String idsStatus;

	// private Calendar retrivalDate;

	/**
	 * Gets the issue number.
	 *
	 * @return the issue number
	 */
	public String getIssueNumber() {
		return issueNumber;
	}

	/**
	 * Sets the issue number.
	 *
	 * @param issueNumber
	 *            the new issue number
	 */
	public void setIssueNumber(String issueNumber) {
		this.issueNumber = issueNumber;
	}

	/*
	 * public Calendar getRetrivalDate() { return retrivalDate; }
	 * 
	 * public void setRetrivalDate(Calendar retrivalDate) { this.retrivalDate = retrivalDate; }
	 */

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
	 * Gets the author.
	 *
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the author.
	 *
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * Gets the publication detail.
	 *
	 * @return the publicationDetail
	 */
	public String getPublicationDetail() {
		return publicationDetail;
	}

	/**
	 * Sets the publication detail.
	 *
	 * @param publicationDetail
	 *            the publicationDetail to set
	 */
	public void setPublicationDetail(String publicationDetail) {
		this.publicationDetail = publicationDetail;
	}

	/**
	 * Gets the publication month.
	 *
	 * @return the publicationMonth
	 */
	public Integer getPublicationMonth() {
		return publicationMonth;
	}

	/**
	 * Sets the publication month.
	 *
	 * @param publicationMonth
	 *            the publicationMonth to set
	 */
	public void setPublicationMonth(Integer publicationMonth) {
		this.publicationMonth = publicationMonth;
	}

	/**
	 * Gets the publication day.
	 *
	 * @return the publicationDay
	 */
	public Integer getPublicationDay() {
		return publicationDay;
	}

	/**
	 * Sets the publication day.
	 *
	 * @param publicationDay
	 *            the publicationDay to set
	 */
	public void setPublicationDay(Integer publicationDay) {
		this.publicationDay = publicationDay;
	}

	/**
	 * Gets the publication year.
	 *
	 * @return the publicationYear
	 */
	public Integer getPublicationYear() {
		return publicationYear;
	}

	/**
	 * Sets the publication year.
	 *
	 * @param publicationYear
	 *            the publicationYear to set
	 */
	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	/**
	 * Gets the relevant pages.
	 *
	 * @return the relevantPages
	 */
	public String getRelevantPages() {
		return relevantPages;
	}

	/**
	 * Sets the relevant pages.
	 *
	 * @param relevantPages
	 *            the relevantPages to set
	 */
	public void setRelevantPages(String relevantPages) {
		this.relevantPages = relevantPages;
	}

	/**
	 * Gets the url.
	 *
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * Sets the url.
	 *
	 * @param uRL
	 *            the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * Gets the publication city.
	 *
	 * @return the publicationCity
	 */
	public String getPublicationCity() {
		return publicationCity;
	}

	/**
	 * Sets the publication city.
	 *
	 * @param publicationCity
	 *            the publicationCity to set
	 */
	public void setPublicationCity(String publicationCity) {
		this.publicationCity = publicationCity;
	}

	/**
	 * Gets the string data.
	 *
	 * @return the stringData
	 */
	public String getStringData() {
		return stringData;
	}

	/**
	 * Sets the string data.
	 *
	 * @param stringData
	 *            the stringData to set
	 */
	public void setStringData(String stringData) {
		this.stringData = stringData;
	}

	/**
	 * Checks if is un published.
	 *
	 * @return the unPublished
	 */
	public boolean isUnPublished() {
		return unPublished;
	}

	/**
	 * Sets the un published.
	 *
	 * @param unPublished
	 *            the unPublished to set
	 */
	public void setUnPublished(boolean unPublished) {
		this.unPublished = unPublished;
	}

	/**
	 * Gets the application serial number.
	 *
	 * @return the applicationSerialNumber
	 */
	public String getApplicationSerialNumber() {
		return applicationSerialNumber;
	}

	/**
	 * Sets the application serial number.
	 *
	 * @param applicationSerialNumber
	 *            the applicationSerialNumber to set
	 */
	public void setApplicationSerialNumber(String applicationSerialNumber) {
		this.applicationSerialNumber = applicationSerialNumber;
	}

	/**
	 * Gets the retrival date.
	 *
	 * @return the retrivalDate
	 */
	public String getRetrivalDate() {
		return retrivalDate;
	}

	/**
	 * Sets the retrival date.
	 *
	 * @param retrivalDate
	 *            the retrivalDate to set
	 */
	public void setRetrivalDate(String retrivalDate) {
		this.retrivalDate = retrivalDate;
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
	 * @return the englishTranslation
	 */
	public boolean isEnglishTranslation() {
		return englishTranslation;
	}

	/**
	 * @param englishTranslation
	 *            the englishTranslation to set
	 */
	public void setEnglishTranslation(boolean englishTranslation) {
		this.englishTranslation = englishTranslation;
	}

	/**
	 * @return the idsId
	 */
	public Long getIdsId() {
		return idsId;
	}

	/**
	 * @param idsId the idsId to set
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
	 * @param doNotFile the doNotFile to set
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
	 * @param idsStatus the idsStatus to set
	 */
	public void setIdsStatus(String idsStatus) {
		this.idsStatus = idsStatus;
	}

}
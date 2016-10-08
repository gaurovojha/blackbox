package com.blackbox.ids.core.model.correspondence;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Correspondence Staging table entity.
 *
 * @author nagarro
 */
@Entity
@Table(name = "BB_CORRESPONDENCE_STAGING")
public class CorrespondenceStaging extends Correspondence {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5929754601332916177L;

	/** The Id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_CORRESPONDENCE_STAGING_ID", nullable = false)
	private long Id;

	/** The application number. */
	@Column(name = "APPLICATION_NUMBER")
	private String applicationNumber;
	
	@Column(name = "APPLICATION_NUMBER_UNFORMATTED")
	private String applicationNumberUnFormatted;

	/** The jurisdiction code. */
	@Column(name = "JURISDICTION_CODE")
	private String jurisdictionCode;

	/** The customer number. */
	@Column(name = "CUSTOMER_NUMBER")
	private String customerNumber;

	/** The document code. */
	@Column(name = "DOCUMENT_CODE")
	private String documentCode;

	/** The document description. */
	@Column(name = "DOCUMENT_DESCRIPTION")
	private String documentDescription;

	/** The private pair upload data. */
	@Column(name = "PRIVATE_PAIR_UPLOAD_DATE")
	private Calendar privatePairUploadDate;

	/** The attachment name. */
	@Column(name = "ATTACHMENT_NAME")
	private String attachmentName;

	/** Assignee */
	@Column(name = "ASSIGNEE")
	private String assignee;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return Id;
	}

	/**
	 * Gets the application number.
	 *
	 * @return the application number
	 */
	public String getApplicationNumber() {
		return applicationNumber;
	}

	/**
	 * Sets the application number.
	 *
	 * @param applicationNumber
	 *            the new application number
	 */
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	/**
	 * Gets the jurisdiction code.
	 *
	 * @return the jurisdiction code
	 */
	public String getJurisdictionCode() {
		return jurisdictionCode;
	}

	/**
	 * Sets the jurisdiction code.
	 *
	 * @param jurisdictionCode
	 *            the new jurisdiction code
	 */
	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}

	/**
	 * Gets the customer number.
	 *
	 * @return the customer number
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}

	/**
	 * Sets the customer number.
	 *
	 * @param customerNumber
	 *            the new customer number
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	/**
	 * Gets the document code.
	 *
	 * @return the document code
	 */
	public String getDocumentCode() {
		return documentCode;
	}

	/**
	 * Sets the document code.
	 *
	 * @param documentCode
	 *            the new document code
	 */
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	/**
	 * Gets the document description.
	 *
	 * @return the document description
	 */
	public String getDocumentDescription() {
		return documentDescription;
	}

	/**
	 * Sets the document description.
	 *
	 * @param documentDescription
	 *            the new document description
	 */
	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		Id = id;
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
	 * @param privatePairUploadDate
	 *            the new private pair upload date
	 */
	public void setPrivatePairUploadDate(Calendar privatePairUploadDate) {
		this.privatePairUploadDate = privatePairUploadDate;
	}

	/**
	 * Gets the attachment name.
	 *
	 * @return the attachment name
	 */
	public String getAttachmentName() {
		return attachmentName;
	}

	/**
	 * Sets the attachment name.
	 *
	 * @param attachmentName
	 *            the new attachment name
	 */
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	/**
	 * @return the assignee
	 */
	public String getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee
	 *            the assignee to set
	 */
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getApplicationNumberUnFormatted() {
		return applicationNumberUnFormatted;
	}

	public void setApplicationNumberUnFormatted(String applicationNumberUnFormatted) {
		this.applicationNumberUnFormatted = applicationNumberUnFormatted;
	}
	
}
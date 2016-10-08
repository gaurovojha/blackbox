package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The Class ApplicationCorrespondenceData.
 */
@XStreamAlias("ApplicationCorrespondenceData")
public class ApplicationCorrespondenceData {

	/** The last modified timestamp. */
	@XStreamAlias("LastModifiedTimestamp")
	private String lastModifiedTimestamp;

	/** The application number. */
	@XStreamAlias("ApplicationNumber")
	private String applicationNumber;

	/** The filing date. */
	@XStreamAlias("FilingDate")
	private String filingDate;

	/** The attorney docket number. */
	@XStreamAlias("AttorneyDocketNumber")
	private String attorneyDocketNumber;

	/** The customer number. */
	@XStreamAlias("CustomerNumber")
	private String customerNumber;
	
	/** The document list. */
	@XStreamAlias("DocumentList")
	private DocumentList documentList;
	
	/** The earliest publication number. */
	@XStreamAlias("EarliestPublicationNumber")
	private EarliestPublicationNumber earliestPublicationNumber;
	
	/** The patent number. */
	@XStreamAlias("PatentNumber")
	private String patentNumber;

	/**
	 * Gets the last modified timestamp.
	 *
	 * @return the lastModifiedTimestamp
	 */
	public String getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}

	/**
	 * Sets the last modified timestamp.
	 *
	 * @param lastModifiedTimestamp            the lastModifiedTimestamp to set
	 */
	public void setLastModifiedTimestamp(final String lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}

	/**
	 * Gets the application number.
	 *
	 * @return the applicationNumber
	 */
	public String getApplicationNumber() {
		return applicationNumber;
	}

	/**
	 * Sets the application number.
	 *
	 * @param applicationNumber            the applicationNumber to set
	 */
	public void setApplicationNumber(final String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	/**
	 * Gets the filing date.
	 *
	 * @return the filingDate
	 */
	public String getFilingDate() {
		return filingDate;
	}

	/**
	 * Sets the filing date.
	 *
	 * @param filingDate            the filingDate to set
	 */
	public void setFilingDate(final String filingDate) {
		this.filingDate = filingDate;
	}

	/**
	 * Gets the attorney docket number.
	 *
	 * @return the attorneyDocketNumber
	 */
	public String getAttorneyDocketNumber() {
		return attorneyDocketNumber;
	}

	/**
	 * Sets the attorney docket number.
	 *
	 * @param attorneyDocketNumber            the attorneyDocketNumber to set
	 */
	public void setAttorneyDocketNumber(final String attorneyDocketNumber) {
		this.attorneyDocketNumber = attorneyDocketNumber;
	}

	/**
	 * Gets the customer number.
	 *
	 * @return the customerNumber
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}

	/**
	 * Sets the customer number.
	 *
	 * @param customerNumber            the customerNumber to set
	 */
	public void setCustomerNumber(final String customerNumber) {
		this.customerNumber = customerNumber;
	}


	/**
	 * Gets the doc list.
	 *
	 * @return the doc list
	 */
	public DocumentList getDocumentList() {
		return documentList;
	}

	/**
	 * Sets the doc list.
	 *
	 * @param docList the new doc list
	 */
	public void setDocumentList(final DocumentList docList) {
		this.documentList = docList;
	}

	/**
	 * Gets the earliest publication number.
	 *
	 * @return the earliest publication number
	 */
	public EarliestPublicationNumber getEarliestPublicationNumber() {
		return earliestPublicationNumber;
	}

	/**
	 * Sets the earliest publication number.
	 *
	 * @param earliestPublicationNumber the new earliest publication number
	 */
	public void setEarliestPublicationNumber(final EarliestPublicationNumber earliestPublicationNumber) {
		this.earliestPublicationNumber = earliestPublicationNumber;
	}

	/**
	 * Gets the patent number.
	 *
	 * @return the patent number
	 */
	public String getPatentNumber() {
		return patentNumber;
	}

	/**
	 * Sets the patent Number.
	 *
	 * @param patentNumber the new patent Number
	 */
	public void setPatentNumber(String patentNumber) {
		this.patentNumber = patentNumber;
	}

}

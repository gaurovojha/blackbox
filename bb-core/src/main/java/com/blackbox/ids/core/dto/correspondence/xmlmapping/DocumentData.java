package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DocumentData")
public class DocumentData {

	@XStreamAlias("DocumentCategory")
	private String documentCategory;

	@XStreamAlias("DocumentDescription")
	private String documentDescription;

	@XStreamAlias("FileWrapperDocumentCode")
	private String fileWrapperDocumentCode;

	@XStreamAlias("MailRoomDate")
	private String mailRoomDate;

	@XStreamAlias("UploadDate")
	private String UploadDate;

	@XStreamAlias("BeginPageNumber")
	private String beginPageNumber;

	@XStreamAlias("PageQuantity")
	private String pageQuantity;

	@XStreamAlias("PageOffsetQuantity")
	private String pageOffsetQuantity;

	@XStreamAlias("EarliestViewBy")
	private String earliestViewBy;
	
	@XStreamAlias("EarliestViewDate")
	private String earliestViewDate;

	/**
	 * @return the documentCategory
	 */
	public String getDocumentCategory() {
		return documentCategory;
	}

	/**
	 * @param documentCategory
	 *            the documentCategory to set
	 */
	public void setDocumentCategory(final String documentCategory) {
		this.documentCategory = documentCategory;
	}

	/**
	 * @return the documentDescription
	 */
	public String getDocumentDescription() {
		return documentDescription;
	}

	/**
	 * @param documentDescription
	 *            the documentDescription to set
	 */
	public void setDocumentDescription(final String documentDescription) {
		this.documentDescription = documentDescription;
	}

	/**
	 * @return the fileWrapperDocumentCode
	 */
	public String getFileWrapperDocumentCode() {
		return fileWrapperDocumentCode;
	}

	/**
	 * @param fileWrapperDocumentCode
	 *            the fileWrapperDocumentCode to set
	 */
	public void setFileWrapperDocumentCode(final String fileWrapperDocumentCode) {
		this.fileWrapperDocumentCode = fileWrapperDocumentCode;
	}

	/**
	 * @return the mailRoomDate
	 */
	public String getMailRoomDate() {
		return mailRoomDate;
	}

	/**
	 * @param mailRoomDate
	 *            the mailRoomDate to set
	 */
	public void setMailRoomDate(final String mailRoomDate) {
		this.mailRoomDate = mailRoomDate;
	}

	/**
	 * @return the uploadDate
	 */
	public String getUploadDate() {
		return UploadDate;
	}

	/**
	 * @param uploadDate
	 *            the uploadDate to set
	 */
	public void setUploadDate(final String uploadDate) {
		UploadDate = uploadDate;
	}

	/**
	 * @return the beginPageNumber
	 */
	public String getBeginPageNumber() {
		return beginPageNumber;
	}

	/**
	 * @param beginPageNumber
	 *            the beginPageNumber to set
	 */
	public void setBeginPageNumber(final String beginPageNumber) {
		this.beginPageNumber = beginPageNumber;
	}

	/**
	 * @return the pageQuantity
	 */
	public String getPageQuantity() {
		return pageQuantity;
	}

	/**
	 * @param pageQuantity
	 *            the pageQuantity to set
	 */
	public void setPageQuantity(final String pageQuantity) {
		this.pageQuantity = pageQuantity;
	}

	/**
	 * @return the pageOffsetQuantity
	 */
	public String getPageOffsetQuantity() {
		return pageOffsetQuantity;
	}

	/**
	 * @param pageOffsetQuantity
	 *            the pageOffsetQuantity to set
	 */
	public void setPageOffsetQuantity(final String pageOffsetQuantity) {
		this.pageOffsetQuantity = pageOffsetQuantity;
	}

	/**
	 * @return the earliestViewBy
	 */
	public String getEarliestViewBy() {
		return earliestViewBy;
	}

	/**
	 * @param earliestViewBy
	 *            the earliestViewBy to set
	 */
	public void setEarliestViewBy(final String earliestViewBy) {
		this.earliestViewBy = earliestViewBy;
	}

	public String getEarliestViewDate() {
		return earliestViewDate;
	}

	public void setEarliestViewDate(final String earliestViewDate) {
		this.earliestViewDate = earliestViewDate;
	}

}

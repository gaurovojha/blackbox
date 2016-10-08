package com.blackbox.ids.dto.correspondence;

import java.util.Date;

import com.blackbox.ids.core.model.correspondence.Correspondence.Source;
import com.blackbox.ids.core.model.correspondence.Correspondence.SubSourceTypes;

public class CorrespondenceFormDto {
	public interface CorrespondenceFormFields {
		String APPLICATION_NUMBER = "applicationNumber";
		String JURISDICTION = "jurisdiction";
		String FILE = "file";
		String MAILING_DATE = "mailingDate";
		String ASSIGNEE="assignee";
	}

	public interface CorrespondenceFormFieldErrors {
		String CORRESPONDENCE_ALREADYEXISTS = "correspondence.alreadyexists";
		String CORRESPONDENCE_CREATEFORM_APPLICATION_INVALID = "correspondence.createform.application.invalid";
		String CORRESPONDENCE_CREATEFORM_NOTFOUND_APPLICATION = "correspondence.createform.notfound.application";
		String CORRESPONDENCE_CREATEFORM_JURISDICTION_INVALID = "correspondence.createform.jurisdiction.invalid";
		String CORRESPONDENCE_CREATEFORM_ASSIGNEE_INVALID="correspondence.createform.assignee.invalid";
		String CORRESPONDENCE_INVALIDBOOKMARK_APPLICATIONNUMBER_MISMATCH = "correspondence.invalidbookmark.applicationnumber.mismatch";
		String CORRESPONDENCE_INVALIDBOOKMARK_CORRESPONDENCEFORM = "correspondence.invalidbookmark.correspondenceform";
		String CORRESPONDENCE_MULTIPLEBOOKMARKS_CORRESPONDENCEFORM = "correspondence.multiplebookmarks.correspondenceform";
		String CORRESPONDENCE_NOBOOKMARK_CORRESPONDENCEFORM = "correspondence.nobookmark.correspondenceform";
		String FAILED_TO_UPLOAD_FILE = "correspondence.upload.failed";
		String CORRESPONDENCE_INVALIDBOOKMARK_MAILING_DATE_MISMATCH = "correspondence.invalidbookmark.mailingdate.mismatch";
		String CORRESPONDENCE_INVALIDBOOKMARK_CUSTOMER_NUMBER_MISMATCH = "correspondence.invalidbookmark.customernumber.mismatch";
		String CORRESPONDENCE_INVALIDBOOKMARK_DOCUMENT_DESCRIPTION_MISMATCH = "correspondence.invalidbookmark.documentdescription.mismatch";
		String CORRESPONDENCE_CREATEFORM_NOTACCESS_APPLICATION = "correspondence.createform.notaccess.application";
	}

	private String file;
	private String applicationNumber;
	private String jurisdiction;
	private Long documentCode;
	private Date mailingDate;
	private boolean isUrgent;
	private Source source;
	private SubSourceTypes subSourceTypes;
	private Long applicationId;
	private String assignee;

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return the applicationNumber
	 */
	public String getApplicationNumber() {
		return applicationNumber;
	}

	/**
	 * @param applicationNumber
	 *            the applicationNumber to set
	 */
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	/**
	 * @return the jurisdiction
	 */
	public String getJurisdiction() {
		return jurisdiction;
	}

	/**
	 * @param jurisdiction
	 *            the jurisdiction to set
	 */
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	/**
	 * @return the documentCode
	 */
	public Long getDocumentCode() {
		return documentCode;
	}

	/**
	 * @param documentCode
	 *            the documentCode to set
	 */
	public void setDocumentCode(Long documentCode) {
		this.documentCode = documentCode;
	}

	/**
	 * @return the mailingDate
	 */
	public Date getMailingDate() {
		return mailingDate;
	}

	/**
	 * @param mailingDate
	 *            the mailingDate to set
	 */
	public void setMailingDate(Date mailingDate) {
		this.mailingDate = mailingDate;
	}

	/**
	 * @return the isUrgent
	 */
	public boolean isUrgent() {
		return isUrgent;
	}

	/**
	 * @param isUrgent
	 *            the isUrgent to set
	 */
	public void setUrgent(boolean isUrgent) {
		this.isUrgent = isUrgent;
	}

	/**
	 * @return the source
	 */
	public Source getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(Source source) {
		this.source = source;
	}

	/**
	 * @return the subSourceTypes
	 */
	public SubSourceTypes getSubSourceTypes() {
		return subSourceTypes;
	}

	/**
	 * @param subSourceTypes
	 *            the subSourceTypes to set
	 */
	public void setSubSourceTypes(SubSourceTypes subSourceTypes) {
		this.subSourceTypes = subSourceTypes;
	}

	/**
	 * @return the applicationId
	 */
	public Long getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId
	 *            the applicationId to set
	 */
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
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

}

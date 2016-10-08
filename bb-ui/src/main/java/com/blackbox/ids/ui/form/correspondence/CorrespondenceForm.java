package com.blackbox.ids.ui.form.correspondence;

import org.springframework.web.multipart.MultipartFile;

import com.blackbox.ids.core.util.date.TimestampFormat;

public class CorrespondenceForm {
	public static final TimestampFormat DATE_FORMAT = TimestampFormat.MMMDDYYYY;
	private String applicationNumber;
	private String jurisdiction;
	private String mailingDate;
	private Long documentDescription;
	private MultipartFile file;
	private boolean urgent;
	private String assignee;
	
	
	/**
	 * @return the applicationNumber
	 */
	public String getApplicationNumber() {
		return applicationNumber;
	}
	/**
	 * @param applicationNumber the applicationNumber to set
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
	 * @param jurisdiction the jurisdiction to set
	 */
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}
	/**
	 * @return the file
	 */
	public MultipartFile getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	/**
	 * @return the mailingDate
	 */
	public String getMailingDate() {
		return mailingDate;
	}
	/**
	 * @param mailingDate the mailingDate to set
	 */
	public void setMailingDate(String mailingDate) {
		this.mailingDate = mailingDate;
	}
	/**
	 * @return the documentDescription
	 */
	public Long getDocumentDescription() {
		return documentDescription;
	}
	/**
	 * @param documentDescription the documentDescription to set
	 */
	public void setDocumentDescription(Long documentDescription) {
		this.documentDescription = documentDescription;
	}
	
	/**
	 * @return the urgent
	 */
	public boolean isUrgent() {
		return urgent;
	}
	/**
	 * @param urgent the urgent to set
	 */
	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}
	/**
	 * @return the assignee
	 */
	public String getAssignee() {
		return assignee;
	}
	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	
	
}

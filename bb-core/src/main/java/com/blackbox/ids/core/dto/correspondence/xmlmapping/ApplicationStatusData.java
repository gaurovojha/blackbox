package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 
 * @author ruchiwadhwa
 *
 */
@XStreamAlias("ApplicationStatusData")
public class ApplicationStatusData {
	
	@XStreamAlias("ApplicationNumber")
	private String applicationNumber;
	
	@XStreamAlias("ApplicationStatusCode")
	private String applicationStatusCode;

	@XStreamAlias("ApplicationStatusText")
	private String applicationStatusText;

	@XStreamAlias("ApplicationStatusDate")
	private String applicationStatusDate;
	
	@XStreamAlias("AttorneyDocketNumber")
	private String attorneyDocketNumber;
	
	@XStreamAlias("FilingDate")
	private String filingDate;
	
	@XStreamAlias("LastModifiedTimestamp")
	private String lastModifiedTimestamp;
	
	@XStreamAlias("CustomerNumber")
	private String customerNumber;

	@XStreamAlias("LastFileHistoryTransaction")
	private LastFileHistoryTransaction fileHistory;

	@XStreamAlias("ImageAvailabilityIndicator")
	private String imageAvailabilityIndicator;
	
	@XStreamAlias("GroupArtUnit")
	private String groupArtUnit;
	
	@XStreamAlias("PatentClass")
	private String patentClass;
	
	@XStreamAlias("PatentSubclass")
	private String patentSubclass;

	
	//Getters and setters
	
	public String getGroupArtUnit() {
		return groupArtUnit;
	}

	public void setGroupArtUnit(String groupArtUnit) {
		this.groupArtUnit = groupArtUnit;
	}

	public String getPatentClass() {
		return patentClass;
	}

	public void setPatentClass(String patentClass) {
		this.patentClass = patentClass;
	}

	public String getPatentSubclass() {
		return patentSubclass;
	}

	public void setPatentSubclass(String patentSubclass) {
		this.patentSubclass = patentSubclass;
	}
	
	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getApplicationStatusCode() {
		return applicationStatusCode;
	}

	public void setApplicationStatusCode(String applicationStatusCode) {
		this.applicationStatusCode = applicationStatusCode;
	}

	public String getApplicationStatusText() {
		return applicationStatusText;
	}

	public void setApplicationStatusText(String applicationStatusText) {
		this.applicationStatusText = applicationStatusText;
	}

	public String getApplicationStatusDate() {
		return applicationStatusDate;
	}

	public void setApplicationStatusDate(String applicationStatusDate) {
		this.applicationStatusDate = applicationStatusDate;
	}

	public String getAttorneyDocketNumber() {
		return attorneyDocketNumber;
	}

	public void setAttorneyDocketNumber(String attorneyDocketNumber) {
		this.attorneyDocketNumber = attorneyDocketNumber;
	}

	public String getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(String filingDate) {
		this.filingDate = filingDate;
	}

	public String getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}

	public void setLastModifiedTimestamp(String lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	
	public String getImageAvailabilityIndicator() {
		return imageAvailabilityIndicator;
	}

	public void setImageAvailabilityIndicator(String imageAvailabilityIndicator) {
		this.imageAvailabilityIndicator = imageAvailabilityIndicator;
	}

	public LastFileHistoryTransaction getFileHistory() {
		return fileHistory;
	}

	public void setFileHistory(LastFileHistoryTransaction fileHistory) {
		this.fileHistory = fileHistory;
	}
	
}

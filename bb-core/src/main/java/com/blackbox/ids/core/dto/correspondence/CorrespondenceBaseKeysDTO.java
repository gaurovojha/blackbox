package com.blackbox.ids.core.dto.correspondence;

import java.util.Calendar;

public class CorrespondenceBaseKeysDTO {

	private String applicationNumber ;
	
	private String jurisdictionCode ;
	
	private String docCode ;
	
	private Calendar mailingDate ;
	
	private int pageCount ;
	
	private Long attachmentSize ;

	public CorrespondenceBaseKeysDTO(String applicationNumber, String jurisdictionCode, String docCode,
			Calendar mailingDate, int pageCount, Long attachmentSize) {
		super();
		this.applicationNumber = applicationNumber;
		this.jurisdictionCode = jurisdictionCode;
		this.docCode = docCode;
		this.mailingDate = mailingDate;
		this.pageCount = pageCount;
		this.attachmentSize = attachmentSize;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getJurisdictionCode() {
		return jurisdictionCode;
	}

	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}

	public String getDocCode() {
		return docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public Calendar getMailingDate() {
		return mailingDate;
	}

	public void setMailingDate(Calendar mailingDate) {
		this.mailingDate = mailingDate;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public Long getAttachmentSize() {
		return attachmentSize;
	}

	public void setAttachmentSize(Long attachmentSize) {
		this.attachmentSize = attachmentSize;
	}
	
	
}

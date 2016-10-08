package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.IDS.IDS.FilingChannel;
import com.blackbox.ids.core.model.IDS.IDS.Status;
import com.blackbox.ids.core.model.IDS.IDS.SubStatus;
import com.blackbox.ids.core.model.mstr.IDSRelevantStatus;
import com.blackbox.ids.core.model.mstr.Jurisdiction;

public class FilingInProgressDTO extends IDSDashBoardBaseDTO{
	
	private String filingInstructedBy;
	private Date filingInstructedOn;
	private String filingFee;
	private String filingChannel;
	private String status;
	private String subStatus;
	private boolean enableIdsDownload;
	private boolean enableIdsPackageDownload;
	
	
	/****************constructor for filing in progress*******************/
	
	public FilingInProgressDTO(Long dbId, String familyId, Jurisdiction jurisdiction, String applicationNo , IDSRelevantStatus idsStatus, String filingInstructedBy, Calendar filingInstructedOn, Double filingFee,
			FilingChannel filingChannel, Status status , SubStatus subStatus) {
		super(dbId, familyId, jurisdiction.getCode(), applicationNo, idsStatus);
		this.filingInstructedBy = filingInstructedBy;
		this.filingInstructedOn = filingInstructedOn ==null ? null :  filingInstructedOn.getTime() ;
		this.filingFee = String.valueOf(filingFee);
		this.filingChannel = filingChannel ==null ? null : filingChannel.name();
		this.status = status==null ? null : status.name();
		this.subStatus = subStatus==null ? null : subStatus.name();
	}
	
	
	/*********************************Getters and Setters***********************/
	
	public String getFilingInstructedBy() {
		return filingInstructedBy;
	}
	public void setFilingInstructedBy(String filingInstructedBy) {
		this.filingInstructedBy = filingInstructedBy;
	}
	public Date getFilingInstructedOn() {
		return filingInstructedOn;
	}
	public void setFilingInstructedOn(Date filingInstructedOn) {
		this.filingInstructedOn = filingInstructedOn;
	}
	public String getFilingFee() {
		return filingFee;
	}
	public void setFilingFee(String filingFee) {
		this.filingFee = filingFee;
	}
	public String getFilingChannel() {
		return filingChannel;
	}
	public void setFilingChannel(String filingChannel) {
		this.filingChannel = filingChannel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}
	public boolean isEnableIdsDownload() {
		return enableIdsDownload;
	}
	public void setEnableIdsDownload(boolean enableIdsDownload) {
		this.enableIdsDownload = enableIdsDownload;
	}
	public boolean isEnableIdsPackageDownload() {
		return enableIdsPackageDownload;
	}
	public void setEnableIdsPackageDownload(boolean enableIdsPackageDownload) {
		this.enableIdsPackageDownload = enableIdsPackageDownload;
	}

}

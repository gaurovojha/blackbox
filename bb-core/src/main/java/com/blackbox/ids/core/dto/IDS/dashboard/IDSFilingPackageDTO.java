package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.Calendar;
import java.util.Date;

import com.blackbox.ids.core.model.IDS.IDS.FilingChannel;
import com.blackbox.ids.core.model.IDS.IDS.Status;
import com.mysema.query.annotations.QueryProjection;

public class IDSFilingPackageDTO extends IDSDashBoardBaseDTO{
	
	private String idsId;
	private String filingInstructedBy;
	private Date filingInstructedOn;
	private Double filingFee;
	private String filingChannel;
	private String status;
	
	@QueryProjection
	public IDSFilingPackageDTO(Long dbId, String familyId, String jurisdiction, String applicationNo, String idsId, String filingInstructedBy, Calendar filingInstructedOn, Double filingFee,
			FilingChannel filingChannel, Status status) {
		super(dbId, familyId, jurisdiction, applicationNo);
		
		this.idsId = idsId;
		this.filingInstructedBy = filingInstructedBy;
		if(filingInstructedOn != null)
		{
			this.filingInstructedOn = filingInstructedOn.getTime();
		}
		else
		{
			this.filingInstructedOn = null;
		}
		this.filingFee = filingFee == null?0:filingFee;
		this.filingChannel = filingChannel ==null ? null : filingChannel.name();
		this.status = status.name();
	}

	public String getIdsId() {
		return idsId;
	}

	public void setIdsId(String idsId) {
		this.idsId = idsId;
	}

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
	public Double getFilingFee() {
		return filingFee;
	}
	public void setFilingFee(Double filingFee) {
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

}

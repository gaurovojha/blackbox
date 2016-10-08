package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.blackbox.ids.core.model.mstr.ProsecutionStatus;
import com.mysema.query.annotations.QueryProjection;

public class InitiateIDSRecordDTO extends IDSDashBoardBaseDTO {

	private Date filingDate;
	
	private Long uncitedReferences;
	
	private Long uncitedReferencesAge;
	
	private String prosecutionStatus;
	
	private Long lastOAResponse;
	
	public InitiateIDSRecordDTO()
	{
		super();
	}
	
	@QueryProjection
	public InitiateIDSRecordDTO(final Long dbId, final String familyId, final String jurisdiction, final String applicationNo,final Calendar filingDate, final Long uncitedReferences,final Long uncitedReferenceAge,final String prosecutionStatus,final Long lastOAResponse ) {
		super(dbId,familyId, jurisdiction, applicationNo);
		this.filingDate = filingDate.getTime();
		this.uncitedReferences = uncitedReferences;
		this.uncitedReferencesAge =  uncitedReferenceAge;
		this.prosecutionStatus = prosecutionStatus;
		this.lastOAResponse = lastOAResponse;
	}
	
	@QueryProjection
	public InitiateIDSRecordDTO(final Long dbId, final String familyId, final String jurisdiction, final String applicationNo,final Calendar filingDate, final Long uncitedReferences,final Calendar uncitedReferenceAge,final ProsecutionStatus prosecutionStatus,final Calendar lastOAResponse ) {
		super(dbId,familyId, jurisdiction, applicationNo);
		if(filingDate != null)
		{
			this.filingDate = filingDate.getTime();
		}
		else
		{
			this.filingDate = null;
		}
		this.uncitedReferences = uncitedReferences;
		this.uncitedReferencesAge = TimeUnit.DAYS.convert(new Date().getTime() - uncitedReferenceAge.getTime().getTime(), TimeUnit.MILLISECONDS);
		this.prosecutionStatus = prosecutionStatus.name();
		this.lastOAResponse = lastOAResponse == null ? null : TimeUnit.DAYS.convert(new Date().getTime() - lastOAResponse.getTime().getTime(), TimeUnit.MILLISECONDS);
	}

	public Date getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(Date filingDate) {
		this.filingDate = filingDate;
	}

	public Long getUncitedReferences() {
		return uncitedReferences;
	}

	public void setUncitedReferences(Long uncitedReferences) {
		this.uncitedReferences = uncitedReferences;
	}

	public Long getUncitedReferencesAge() {
		return uncitedReferencesAge;
	}

	public void setUncitedReferencesAge(Long uncitedReferencesAge) {
		this.uncitedReferencesAge = uncitedReferencesAge;
	}

	public String getProsecutionStatus() {
		return prosecutionStatus;
	}

	public void setProsecutionStatus(String prosecutionStatus) {
		this.prosecutionStatus = prosecutionStatus;
	}

	public Long getLastOAResponse() {
		return lastOAResponse;
	}

	public void setLastOAResponse(Long lastOAResponse) {
		this.lastOAResponse = lastOAResponse;
	}
	
	

}

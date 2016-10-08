package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.Calendar;
import java.util.Date;

public class CitedReferenceDTO {
	
	private Long refFlowId;
	private Calendar filingDate;
	
	public Long getRefFlowId() {
		return refFlowId;
	}
	public void setRefFlowId(Long refFlowId) {
		this.refFlowId = refFlowId;
	}
	public Calendar getFilingDate() {
		return filingDate;
	}
	public void setFilingDate(Calendar filingDate) {
		this.filingDate = filingDate;
	}

}

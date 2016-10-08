package com.blackbox.ids.ui.form.ids;

import java.util.Date;

public class ReferenceForm {
	
	private boolean notCited;
	private boolean mappedInIDS;
	private boolean cited;
	private String filingDate;
	private Long refFlowId;
	
	
	public boolean isNotCited() {
		return notCited;
	}
	public void setNotCited(boolean notCited) {
		this.notCited = notCited;
	}
	public boolean isMappedInIDS() {
		return mappedInIDS;
	}
	public void setMappedInIDS(boolean mappedInIDS) {
		this.mappedInIDS = mappedInIDS;
	}
	public boolean isCited() {
		return cited;
	}
	public void setCited(boolean cited) {
		this.cited = cited;
	}
	public String getFilingDate() {
		return filingDate;
	}
	public void setFilingDate(String filingDate) {
		this.filingDate = filingDate;
	}
	public Long getRefFlowId() {
		return refFlowId;
	}
	public void setRefFlowId(Long refFlowId) {
		this.refFlowId = refFlowId;
	}
}

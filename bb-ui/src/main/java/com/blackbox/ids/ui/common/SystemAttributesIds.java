package com.blackbox.ids.ui.common;

import java.io.Serializable;
import java.util.List;


public class SystemAttributesIds implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Long> juricIds;
	private List<Long> assigneeIds;
	private List<Long> techGrpIds;
	private List<Long> custmNoIds;
	private List<Long> organIds;
	
	
	public SystemAttributesIds() {
		super();
	}
	
	public List<Long> getJuricIds() {
		return juricIds;
	}
	public void setJuricIds(List<Long> juricIds) {
		this.juricIds = juricIds;
	}
	public List<Long> getAssigneeIds() {
		return assigneeIds;
	}
	public void setAssigneeIds(List<Long> assigneeIds) {
		this.assigneeIds = assigneeIds;
	}
	public List<Long> getTechGrpIds() {
		return techGrpIds;
	}
	public void setTechGrpIds(List<Long> techGrpIds) {
		this.techGrpIds = techGrpIds;
	}
	public List<Long> getCustmNoIds() {
		return custmNoIds;
	}
	public void setCustmNoIds(List<Long> custmNoIds) {
		this.custmNoIds = custmNoIds;
	}
	public List<Long> getOrganIds() {
		return organIds;
	}
	public void setOrganIds(List<Long> organIds) {
		this.organIds = organIds;
	}
	
	
}

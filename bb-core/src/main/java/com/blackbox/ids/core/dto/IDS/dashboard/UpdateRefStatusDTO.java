package com.blackbox.ids.core.dto.IDS.dashboard;

import java.util.List;

public class UpdateRefStatusDTO {
	
	private List<Long> notCitedRef;
	private	List<Long> mappedToFiledIdsRef;
	private List<CitedReferenceDTO> citedRef;
	private Long notificationProcessId;
	private Long idsId;
	
	public List<Long> getNotCitedRef() {
		return notCitedRef;
	}
	public void setNotCitedRef(List<Long> notCitedRef) {
		this.notCitedRef = notCitedRef;
	}
	public List<Long> getMappedToFiledIdsRef() {
		return mappedToFiledIdsRef;
	}
	public void setMappedToFiledIdsRef(List<Long> mappedToFiledIdsRef) {
		this.mappedToFiledIdsRef = mappedToFiledIdsRef;
	}
	public List<CitedReferenceDTO> getCitedRef() {
		return citedRef;
	}
	public void setCitedRef(List<CitedReferenceDTO> citedRef) {
		this.citedRef = citedRef;
	}
	public Long getNotificationProcessId() {
		return notificationProcessId;
	}
	public void setNotificationProcessId(Long notificationProcessId) {
		this.notificationProcessId = notificationProcessId;
	}
	public Long getIdsFilingInfoId() {
		return idsId;
	}
	public void setIdsFilingInfoId(Long idsFilingInfoId) {
		this.idsId = idsFilingInfoId;
	}
}

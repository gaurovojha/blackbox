package com.blackbox.ids.core.dto.IDS;

public class IDSReferenceRecordStatusCountDTO {
	
	private Long citeCount;

	private Long uncitedCount;
	
	private Long examinerCitedCount;
	
	private Long citedInParentCount;
	
	private Long doNotFileCount;
	
	private Long deletedCount;
	
	private Long citedpatentCount;
	private Long citedNplCount;
	private Long uncitedpatentCount;
	private Long uncitedNplCount;
	private Long examinerpatentCount;
	private Long examinerNplCount;
	private Long citeInParentpatentCount;
	private Long citeInParentNplCount;
	private Long doNotFilepatentCount;
	private Long doNotFileNplCount;
	private Long deletedpatentCount;
	private Long deletedNplCount;
	
	public Long getCitedpatentCount() {
		return citedpatentCount;
	}

	public void setCitedpatentCount(Long citedpatentCount) {
		this.citedpatentCount = citedpatentCount;
	}

	public Long getCitedNplCount() {
		return citedNplCount;
	}

	public void setCitedNplCount(Long citedNplCount) {
		this.citedNplCount = citedNplCount;
	}

	public Long getUncitedpatentCount() {
		return uncitedpatentCount;
	}

	public void setUncitedpatentCount(Long uncitedpatentCount) {
		this.uncitedpatentCount = uncitedpatentCount;
	}

	public Long getUncitedNplCount() {
		return uncitedNplCount;
	}

	public void setUncitedNplCount(Long uncitedNplCount) {
		this.uncitedNplCount = uncitedNplCount;
	}

	public Long getExaminerpatentCount() {
		return examinerpatentCount;
	}

	public void setExaminerpatentCount(Long examinerpatentCount) {
		this.examinerpatentCount = examinerpatentCount;
	}

	public Long getExaminerNplCount() {
		return examinerNplCount;
	}

	public void setExaminerNplCount(Long examinerNplCount) {
		this.examinerNplCount = examinerNplCount;
	}

	public Long getCiteInParentpatentCount() {
		return citeInParentpatentCount;
	}

	public void setCiteInParentpatentCount(Long citeInParentpatentCount) {
		this.citeInParentpatentCount = citeInParentpatentCount;
	}

	public Long getCiteInParentNplCount() {
		return citeInParentNplCount;
	}

	public void setCiteInParentNplCount(Long citeInParentNplCount) {
		this.citeInParentNplCount = citeInParentNplCount;
	}

	public Long getDoNotFilepatentCount() {
		return doNotFilepatentCount;
	}

	public void setDoNotFilepatentCount(Long doNotFilepatentCount) {
		this.doNotFilepatentCount = doNotFilepatentCount;
	}

	public Long getDoNotFileNplCount() {
		return doNotFileNplCount;
	}

	public void setDoNotFileNplCount(Long doNotFileNplCount) {
		this.doNotFileNplCount = doNotFileNplCount;
	}

	public Long getDeletedpatentCount() {
		return deletedpatentCount;
	}

	public void setDeletedpatentCount(Long deletedpatentCount) {
		this.deletedpatentCount = deletedpatentCount;
	}

	public Long getDeletedNplCount() {
		return deletedNplCount;
	}

	public void setDeletedNplCount(Long deletedNplCount) {
		this.deletedNplCount = deletedNplCount;
	}

	public IDSReferenceRecordStatusCountDTO() {
		super();
	}

	public IDSReferenceRecordStatusCountDTO(Long citedCount, Long unCitedCount, Long examinerCount,
			Long citeInParentCount, Long doNotFileCount, Long deletedCount, Long citedpatentCount, 
			Long citedNplCount, Long uncitedpatentCount, Long uncitedNplCount, Long examinerpatentCount, 
			Long examinerNplCount, Long citeInParentpatentCount, Long citeInParentNplCount, Long doNotFilepatentCount, 
			Long doNotFileNplCount, Long deletedpatentCount, Long deletedNplCount) {
		// TODO Auto-generated constructor stub
		this.citeCount = citedCount;
		this.uncitedCount = unCitedCount;
		this.examinerCitedCount = examinerCount;
		this.citedInParentCount = citeInParentCount;
		this.doNotFileCount = doNotFileCount;
		this.deletedCount = deletedCount;
		this.citedpatentCount = citedpatentCount;
		this.citedNplCount = citedNplCount;
		this.uncitedpatentCount = uncitedpatentCount;
		this.uncitedNplCount = uncitedNplCount;
		this.examinerpatentCount = examinerpatentCount;
		this.examinerNplCount = examinerNplCount;
		this.citeInParentpatentCount = citeInParentpatentCount;
		this.citeInParentNplCount = citeInParentNplCount;
		this.doNotFilepatentCount = doNotFilepatentCount;
		this.doNotFileNplCount = doNotFileNplCount;
		this.deletedpatentCount = deletedpatentCount;
		this.deletedNplCount = deletedNplCount;
	}

	public Long getCiteCount() {
		return citeCount;
	}

	public void setCiteCount(Long citeCount) {
		this.citeCount = citeCount;
	}

	public Long getUncitedCount() {
		return uncitedCount;
	}

	public void setUncitedCount(Long uncitedCount) {
		this.uncitedCount = uncitedCount;
	}

	public Long getExaminerCitedCount() {
		return examinerCitedCount;
	}

	public void setExaminerCitedCount(Long examinerCitedCount) {
		this.examinerCitedCount = examinerCitedCount;
	}

	public Long getCitedInParentCount() {
		return citedInParentCount;
	}

	public void setCitedInParentCount(Long citedInParentCount) {
		this.citedInParentCount = citedInParentCount;
	}

	public Long getDoNotFileCount() {
		return doNotFileCount;
	}

	public void setDoNotFileCount(Long doNotFileCount) {
		this.doNotFileCount = doNotFileCount;
	}

	public Long getDeletedCount() {
		return deletedCount;
	}

	public void setDeletedCount(Long deletedCount) {
		this.deletedCount = deletedCount;
	}
	
	

}

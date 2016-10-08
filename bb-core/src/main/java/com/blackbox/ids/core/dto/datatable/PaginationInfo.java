/**
 *
 */
package com.blackbox.ids.core.dto.datatable;

import com.blackbox.ids.core.dto.mdm.dashboard.SortAttribute;

/**
 * Contains the pagination detail attributes for any data table.
 *
 * @author ajay2258
 */
public class PaginationInfo {

	public static final Long DEFAULT_LIMIT = 10L;
	public static final Long DEFAULT_OFFSET = 0L;

	private long limit;

	private long offset;

	private String sortBy;

	private SortAttribute sortAttribute;

	private boolean asc;

	/*- ---------------------------- constructors -- */
	public PaginationInfo() {
		super();
	}

	public PaginationInfo(Long limit, Long offset, String sortBy, Boolean isAsc) {
		super();
		this.limit = limit == null ? DEFAULT_LIMIT : limit;
		this.offset = offset == null ? DEFAULT_OFFSET : offset;
		this.sortBy = sortBy;
		this.sortAttribute = SortAttribute.fromString(sortBy);
		this.asc = isAsc == null ? true : isAsc;
	}
	
	public PaginationInfo(Long limit, Long offset) {
		super();
		this.limit = limit;
		this.offset = offset;
	}

	/*- ---------------------------- getter-setters -- */
	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public SortAttribute getSortAttribute() {
		return sortAttribute;
	}

	public void setSortAttribute(SortAttribute sortAttribute) {
		this.sortAttribute = sortAttribute;
	}

}

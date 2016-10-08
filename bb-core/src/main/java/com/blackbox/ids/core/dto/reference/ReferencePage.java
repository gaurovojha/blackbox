package com.blackbox.ids.core.dto.reference;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * The Class ReferencePage.
 *
 * @param <T>
 *            the generic type
 */
public class ReferencePage<T> extends PageImpl<T> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 338690056266455102L;

	/** The total elements witout filter. */
	private Long totalElementsWitoutFilter;

	/**
	 * Instantiates a new reference page.
	 *
	 * @param content
	 *            the content
	 * @param pageable
	 *            the pageable
	 * @param total
	 *            the total
	 * @param totalElementsWitoutFilter
	 *            the total elements witout filter
	 */
	public ReferencePage(List<T> content, Pageable pageable, long total, long totalElementsWitoutFilter) {
		super(content, pageable, total);
		this.totalElementsWitoutFilter = totalElementsWitoutFilter;
	}

	/**
	 * Gets the total elements witout filter.
	 *
	 * @return the total elements witout filter
	 */
	public Long getTotalElementsWitoutFilter() {
		return totalElementsWitoutFilter;
	}

	/**
	 * Sets the total elements witout filter.
	 *
	 * @param totalElementsWitoutFilter
	 *            the new total elements witout filter
	 */
	public void setTotalElementsWitoutFilter(Long totalElementsWitoutFilter) {
		this.totalElementsWitoutFilter = totalElementsWitoutFilter;
	}
}

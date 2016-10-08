package com.blackbox.ids.core.dto.reference;

import java.util.Calendar;

/**
 * The Class ReferenceRecordFilter.
 */
public class ReferenceRecordFilter {

	/**
	 * The Enum DateFilter.
	 */
	public enum DateFilter {

		/** The created date. */
		CREATED_DATE,
		/** The mailing date. */
		MAILING_DATE
	}

	/** The jurisdiction. */
	private String jurisdiction;

	/** The uploaded by. */
	private Long uploadedBy;

	/** The date. */
	private Calendar date;

	/** The filter. */
	private DateFilter filter;

	/**
	 * Instantiates a new reference record filter.
	 *
	 * @param jurisdiction
	 *            the jurisdiction
	 */
	public ReferenceRecordFilter(String jurisdiction) {
		super();
		this.jurisdiction = jurisdiction;
	}

	/**
	 * Instantiates a new reference record filter.
	 *
	 * @param uploadedBy
	 *            the uploaded by
	 */
	public ReferenceRecordFilter(Long uploadedBy) {
		super();
		this.uploadedBy = uploadedBy;
	}

	/**
	 * Gets the jurisdiction.
	 *
	 * @return the jurisdiction
	 */
	public String getJurisdiction() {
		return jurisdiction;
	}

	/**
	 * Instantiates a new reference record filter.
	 *
	 * @param date
	 *            the date
	 * @param filter
	 *            the filter
	 */
	public ReferenceRecordFilter(Calendar date, DateFilter filter) {
		super();
		this.date = date;
		this.filter = filter;
	}

	/**
	 * Instantiates a new reference record filter.
	 *
	 * @param jurisdiction
	 *            the jurisdiction
	 * @param uploadedBy
	 *            the uploaded by
	 */
	public ReferenceRecordFilter(String jurisdiction, Long uploadedBy) {
		super();
		this.jurisdiction = jurisdiction;
		this.uploadedBy = uploadedBy;
	}

	/**
	 * Instantiates a new reference record filter.
	 *
	 * @param uploadedBy
	 *            the uploaded by
	 * @param date
	 *            the date
	 * @param filter
	 *            the filter
	 */
	public ReferenceRecordFilter(Long uploadedBy, Calendar date, DateFilter filter) {
		super();
		this.uploadedBy = uploadedBy;
		this.date = date;
		this.filter = filter;
	}

	/**
	 * Instantiates a new reference record filter.
	 *
	 * @param jurisdiction
	 *            the jurisdiction
	 * @param uploadedBy
	 *            the uploaded by
	 * @param date
	 *            the date
	 * @param filter
	 *            the filter
	 */
	public ReferenceRecordFilter(String jurisdiction, Long uploadedBy, Calendar date, DateFilter filter) {
		super();
		this.jurisdiction = jurisdiction;
		this.uploadedBy = uploadedBy;
		this.date = date;
		this.filter = filter;
	}

	/**
	 * Instantiates a new reference record filter.
	 *
	 * @param jurisdiction
	 *            the jurisdiction
	 * @param date
	 *            the date
	 * @param filter
	 *            the filter
	 */
	public ReferenceRecordFilter(String jurisdiction, Calendar date, DateFilter filter) {
		super();
		this.jurisdiction = jurisdiction;
		this.date = date;
		this.filter = filter;
	}

	/**
	 * Sets the jurisdiction.
	 *
	 * @param jurisdiction
	 *            the jurisdiction to set
	 */
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	/**
	 * Gets the uploaded by.
	 *
	 * @return the uploadedBy
	 */
	public Long getUploadedBy() {
		return uploadedBy;
	}

	/**
	 * Sets the uploaded by.
	 *
	 * @param uploadedBy
	 *            the uploadedBy to set
	 */
	public void setUploadedBy(Long uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date
	 *            the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * Gets the filter.
	 *
	 * @return the filter
	 */
	public DateFilter getFilter() {
		return filter;
	}

	/**
	 * Sets the filter.
	 *
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(DateFilter filter) {
		this.filter = filter;
	}

}
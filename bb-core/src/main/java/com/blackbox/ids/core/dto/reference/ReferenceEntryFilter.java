package com.blackbox.ids.core.dto.reference;

import java.util.Set;

/**
 * The Class ReferenceEntryFilter.
 */
public class ReferenceEntryFilter {

	/** The jurisdiction. */
	private String jurisdiction;

	/** The roles. */
	private Set<Long> roles;

	/** The my records. */
	String myRecords;

	/** The date range. */
	String dateRange;

	/**
	 * Gets the jurisdiction.
	 *
	 * @return the jurisdiction
	 */
	public String getJurisdiction() {
		return jurisdiction;
	}

	/**
	 * Sets the jurisdiction.
	 *
	 * @param jurisdiction
	 *            the new jurisdiction
	 */
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	/**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
	public Set<Long> getRoles() {
		return roles;
	}

	/**
	 * Sets the roles.
	 *
	 * @param roles
	 *            the new roles
	 */
	public void setRoles(Set<Long> roles) {
		this.roles = roles;
	}

	/**
	 * Gets the my records.
	 *
	 * @return the my records
	 */
	public String getMyRecords() {
		return myRecords;
	}

	/**
	 * Sets the my records.
	 *
	 * @param myRecords
	 *            the new my records
	 */
	public void setMyRecords(String myRecords) {
		this.myRecords = myRecords;
	}

	/**
	 * Gets the date range.
	 *
	 * @return the date range
	 */
	public String getDateRange() {
		return dateRange;
	}

	/**
	 * Sets the date range.
	 *
	 * @param dateRange
	 *            the new date range
	 */
	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

}

package com.blackbox.ids.core.auth;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Contains the current user's data access authorization details.
 * The authorized data is retrieved from the roles assigned to the user.
 */
public class UserAuthDetail implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 8640228786815673754L;

	private Set<Long> assigneeIds = new LinkedHashSet<>();
	private Set<Long> customerIds = new LinkedHashSet<>();
	private Set<Long> technologyGroupIds = new LinkedHashSet<>();
	private Set<Long> organizationsIds = new LinkedHashSet<>();
	private Set<Long> jurisdictionIds = new LinkedHashSet<>();

	/*- ---------------------------- getter-setters -- */
	public Set<Long> getAssigneeIds() {
		return assigneeIds;
	}

	public void setAssigneeIds(Set<Long> assigneeIds) {
		this.assigneeIds = assigneeIds;
	}

	public Set<Long> getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(Set<Long> customerIds) {
		this.customerIds = customerIds;
	}

	public Set<Long> getTechnologyGroupIds() {
		return technologyGroupIds;
	}

	public void setTechnologyGroupIds(Set<Long> technologyGroupIds) {
		this.technologyGroupIds = technologyGroupIds;
	}

	public Set<Long> getOrganizationsIds() {
		return organizationsIds;
	}

	public void setOrganizationsIds(Set<Long> organizationsIds) {
		this.organizationsIds = organizationsIds;
	}

	public Set<Long> getJurisdictionIds() {
		return jurisdictionIds;
	}

	public void setJurisdictionIds(Set<Long> jurisdictionIds) {
		this.jurisdictionIds = jurisdictionIds;
	}

}

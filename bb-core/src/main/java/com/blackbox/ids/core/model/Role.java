package com.blackbox.ids.core.model;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.blackbox.ids.core.model.base.BaseEntity;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.mstr.Organization;
import com.blackbox.ids.core.model.mstr.TechnologyGroup;
import com.blackbox.ids.core.model.usermanagement.AccessProfile;

/**
 * Role entity
 * 
 * @author Nagarro
 *
 */
@NamedQueries({
		@NamedQuery(name = "findAllRoleNames", query = "SELECT distinct r.name FROM Role r where r.active = true") })
@Entity
@Table(name = "BB_ROLE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role extends BaseEntity {

	private static final long serialVersionUID = 120112958028689294L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_ROLE_ID", nullable = false)
	private Long id;

	/**
	 * 
	 */
	public Role() {
		super();
	}

	/**
	 * @param id
	 */
	public Role(Long id) {
		super();
		this.id = id;
	}

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

	@Column(name = "END_DATE")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Calendar endDate;

	@Column(name = "OTP_ENABLED", nullable = false)
	private boolean otpEnabled;

	@Column(name = "SEEDED")
	private boolean seeded;

	@Column(name = "ACTIVE")
	private boolean active;

	@Column(name = "SYSTEM_ROLE")
	private boolean systemRole = false;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BB_ACCESS_PROFILE_ID", nullable = true)
	private AccessProfile accessProfile;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "BB_ROLE_JURISDICTION", joinColumns = {
			@JoinColumn(name = "BB_ROLE_ID", unique = false), }, inverseJoinColumns = {
					@JoinColumn(name = "BB_JURISDICTION_ID", unique = false) })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Jurisdiction> jurisdictions;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "BB_ROLE_ASSIGNEE", joinColumns = {
			@JoinColumn(name = "BB_ROLE_ID", unique = false), }, inverseJoinColumns = {
					@JoinColumn(name = "BB_ASSIGNEE_ID", unique = false) })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Assignee> assignees;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "BB_ROLE_CUSTOMER", joinColumns = {
			@JoinColumn(name = "BB_ROLE_ID", unique = false), }, inverseJoinColumns = {
					@JoinColumn(name = "BB_CUSTOMER_ID", unique = false) })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Customer> customers;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "BB_ROLE_TECHNOLOGY_GROUP", joinColumns = {
			@JoinColumn(name = "BB_ROLE_ID", unique = false), }, inverseJoinColumns = {
					@JoinColumn(name = "BB_TECHNOLOGY_GROUP_ID", unique = false) })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<TechnologyGroup> technologyGroups;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "BB_ROLE_ORGANIZATION", joinColumns = {
			@JoinColumn(name = "BB_ROLE_ID", unique = false), }, inverseJoinColumns = {
					@JoinColumn(name = "BB_ORGANIZATION_ID", unique = false) })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Organization> organizations;

	/**
	 * @return the role
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the otpEnabled
	 */
	public boolean isOtpEnabed() {
		return otpEnabled;
	}

	/**
	 * @param otpEnabled
	 *            the otpEnabled to set
	 */
	public void setOtpEnabed(boolean otpEnabled) {
		this.otpEnabled = otpEnabled;
	}

	/**
	 * @return the seeded
	 */
	public boolean isSeeded() {
		return seeded;
	}

	/**
	 * @param seeded
	 *            the seeded to set
	 */
	public void setSeeded(boolean seeded) {
		this.seeded = seeded;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the systemRole
	 */
	public boolean isSystemRole() {
		return systemRole;
	}

	/**
	 * @param systemRole
	 *            the systemRole to set
	 */
	public void setSystemRole(boolean systemRole) {
		this.systemRole = systemRole;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the otpEnabled
	 */
	public boolean isOtpEnabled() {
		return otpEnabled;
	}

	/**
	 * @param otpEnabled
	 *            the otpEnabled to set
	 */
	public void setOtpEnabled(boolean otpEnabled) {
		this.otpEnabled = otpEnabled;
	}

	/**
	 * @return the jurisdictions
	 */
	public Set<Jurisdiction> getJurisdictions() {

		if (null == jurisdictions) {
			jurisdictions = new LinkedHashSet<Jurisdiction>();
		}
		return jurisdictions;
	}

	/**
	 * @param jurisdictions
	 *            the jurisdictions to set
	 */
	public void setJurisdictions(Set<Jurisdiction> jurisdictions) {
		this.jurisdictions = jurisdictions;
	}

	/**
	 * @return the assignees
	 */
	public Set<Assignee> getAssignees() {

		if (null == assignees) {
			assignees = new LinkedHashSet<Assignee>();
		}
		return assignees;
	}

	/**
	 * @param assignees
	 *            the assignees to set
	 */
	public void setAssignees(Set<Assignee> assignees) {
		this.assignees = assignees;
	}

	/**
	 * @return the customer
	 */
	public Set<Customer> getCustomers() {

		if (null == customers) {
			customers = new LinkedHashSet<Customer>();
		}
		return customers;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Set<Customer> customers) {
		this.customers = customers;
	}

	/**
	 * @return the technologyGroups
	 */
	public Set<TechnologyGroup> getTechnologyGroups() {

		if (null == technologyGroups) {
			technologyGroups = new LinkedHashSet<TechnologyGroup>();
		}
		return technologyGroups;
	}

	/**
	 * @param technologyGroups
	 *            the technologyGroups to set
	 */
	public void setTechnologyGroups(Set<TechnologyGroup> technologyGroups) {
		this.technologyGroups = technologyGroups;
	}

	/**
	 * @return the organizations
	 */
	public Set<Organization> getOrganizations() {

		if (null == organizations) {
			organizations = new LinkedHashSet<Organization>();
		}
		return organizations;
	}

	/**
	 * @param organizations
	 *            the organizations to set
	 */
	public void setOrganizations(Set<Organization> organizations) {
		this.organizations = organizations;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	public AccessProfile getAccessProfile() {
		return accessProfile;
	}

	public void setAccessProfile(AccessProfile accessProfile) {
		this.accessProfile = accessProfile;
	}

}

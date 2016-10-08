package com.blackbox.ids.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.blackbox.ids.dto.usermanagement.AccessProfileDTO;

/**
 * Transports role related information
 *
 * @author Nagarro
 *
 */
public class RoleDTO {

	private Long id;

	@NotNull(message = "Name is required")
	private String name;

	@NotNull(message = "Description is required")
	private String description;

	private long userCount;

	private String createdBy;

	private String createdDate;

	private String modifiedBy;

	private String modifiedDate;

	private String endDate;

	private boolean otpActivate;

	private String status;

	private boolean seeded;

	private Long accessProfileId;

	private long[] jurisdictions;

	private long[] assignees;

	private long[] customers;

	private long[] technologyGroups;

	private long[] organizations;

	private List<JurisdictionDTO> jurisdictionList;

	private List<AssigneeDTO> assigneeList;

	private List<CustomerDTO> customerList;

	private List<TechnologyGroupDTO> technologyGroupList;

	private List<OrganizationDTO> organizationList;

	private AccessProfileDTO accessProfile;

	private String dataAccessErrors;

	private int version;

	/*- --------------------------- Constructor -- */
	public RoleDTO() {
		super();
	}

	public RoleDTO(final Long id, final String name) {
		super();
		this.id = id;
		this.name = name;
	}

	/*- --------------------------- getter-setters -- */
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
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
	 * @return the userCount
	 */
	public long getUserCount() {
		return userCount;
	}

	/**
	 * @param userCount
	 *            the userCount to set
	 */
	public void setUserCount(long userCount) {
		this.userCount = userCount;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy
	 *            the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modifiedDate
	 */
	public String getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate
	 *            the modifiedDate to set
	 */
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the otpActivate
	 */
	public boolean isOtpActivate() {
		return otpActivate;
	}

	/**
	 * @param otpActivate
	 *            the otpActivate to set
	 */
	public void setOtpActivate(boolean otpActivate) {
		this.otpActivate = otpActivate;
	}

	/**
	 * @return the accessProfileId
	 */
	public Long getAccessProfileId() {
		return accessProfileId;
	}

	/**
	 * @param accessProfileProfileId
	 *            the accessProfileProfileId to set
	 */
	public void setAccessProfileId(Long accessProfileId) {
		this.accessProfileId = accessProfileId;
	}

	/**
	 * @return the jurisdictions
	 */
	public long[] getJurisdictions() {
		return jurisdictions;
	}

	/**
	 * @param jurisdictions
	 *            the jurisdictions to set
	 */
	public void setJurisdictions(long[] jurisdictions) {
		this.jurisdictions = jurisdictions;
	}

	/**
	 * @return the assignees
	 */
	public long[] getAssignees() {
		return assignees;
	}

	/**
	 * @param assignees
	 *            the assignees to set
	 */
	public void setAssignees(long[] assignees) {
		this.assignees = assignees;
	}

	/**
	 * @return the customers
	 */
	public long[] getCustomers() {
		return customers;
	}

	/**
	 * @param customers
	 *            the customers to set
	 */
	public void setCustomers(long[] customers) {
		this.customers = customers;
	}

	/**
	 * @return the technologyGroups
	 */
	public long[] getTechnologyGroups() {
		return technologyGroups;
	}

	/**
	 * @param technologyGroups
	 *            the technologyGroups to set
	 */
	public void setTechnologyGroups(long[] technologyGroups) {
		this.technologyGroups = technologyGroups;
	}

	/**
	 * @return the organizations
	 */
	public long[] getOrganizations() {
		return organizations;
	}

	/**
	 * @param organizations
	 *            the organizations to set
	 */
	public void setOrganizations(long[] organizations) {
		this.organizations = organizations;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the accessProfile
	 */
	public AccessProfileDTO getAccessProfile() {
		return accessProfile;
	}

	/**
	 * @param accessProfile
	 *            the accessProfile to set
	 */
	public void setAccessProfile(AccessProfileDTO accessProfile) {
		this.accessProfile = accessProfile;
	}

	/**
	 * @return the jurisdictionList
	 */
	public List<JurisdictionDTO> getJurisdictionList() {
		return jurisdictionList;
	}

	/**
	 * @param jurisdictionList
	 *            the jurisdictionList to set
	 */
	public void setJurisdictionList(List<JurisdictionDTO> jurisdictionList) {
		this.jurisdictionList = jurisdictionList;
	}

	/**
	 * @return the assigneeList
	 */
	public List<AssigneeDTO> getAssigneeList() {
		return assigneeList;
	}

	/**
	 * @param assigneeList
	 *            the assigneeList to set
	 */
	public void setAssigneeList(List<AssigneeDTO> assigneeList) {
		this.assigneeList = assigneeList;
	}

	/**
	 * @return the customerList
	 */
	public List<CustomerDTO> getCustomerList() {
		return customerList;
	}

	/**
	 * @param customerList
	 *            the customerList to set
	 */
	public void setCustomerList(List<CustomerDTO> customerList) {
		this.customerList = customerList;
	}

	/**
	 * @return the technologyGroupList
	 */
	public List<TechnologyGroupDTO> getTechnologyGroupList() {
		return technologyGroupList;
	}

	/**
	 * @param technologyGroupList
	 *            the technologyGroupList to set
	 */
	public void setTechnologyGroupList(List<TechnologyGroupDTO> technologyGroupList) {
		this.technologyGroupList = technologyGroupList;
	}

	/**
	 * @return the organizationList
	 */
	public List<OrganizationDTO> getOrganizationList() {
		return organizationList;
	}

	/**
	 * @param organizationList
	 *            the organizationList to set
	 */
	public void setOrganizationList(List<OrganizationDTO> organizationList) {
		this.organizationList = organizationList;
	}

	/**
	 * @return the dataAccessErrors
	 */
	public String getDataAccessErrors() {
		return dataAccessErrors;
	}

	/**
	 * @param dataAccessErrors the dataAccessErrors to set
	 */
	public void setDataAccessErrors(String dataAccessErrors) {
		this.dataAccessErrors = dataAccessErrors;
	}

}
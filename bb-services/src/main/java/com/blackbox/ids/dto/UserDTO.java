package com.blackbox.ids.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDTO 
{
	private Long id;
	
	private String firstName;
	
	private String middleName;	
	
	private String lastName;	
	
	private String emailId;	
	
	private NationalityDTO nationality;	
	
	private List<RoleDTO> roles;
	
	private List<Long> roleIds;
	
	private List<Long> userRoles = new ArrayList<Long>();
	
	private String employeeId;
	
	private String designation;	
	
	private UserTypeDTO userType;
	
	private String agentName;
	
	private String clientName;
	
	private String endingOn;
	
	private String userId; 
	
	private String userName;

	private boolean status;

	private boolean locked;
	
	private String password;
	
	private String newPassword;
	
	private String confirmPassword;
	
	private String otp;
	
	private String otpTimer;

	private boolean otpEnabled;
	
	private boolean enabled;
	
	private String createdOn;
	
	private String modifiedOn;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the nationality
	 */
	public NationalityDTO getNationality() {
		return nationality;
	}

	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(NationalityDTO nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return the roles
	 */
	public List<RoleDTO> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}
	

	/**
	 * @return the roleIds
	 */
	public List<Long> getRoleIds() {
		return roleIds;
	}

	/**
	 * @param roleIds the roleIds to set
	 */
	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the userType
	 */
	public UserTypeDTO getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(UserTypeDTO userType) {
		this.userType = userType;
	}

	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}


	/**
	 * @return the endingOn
	 */
	public String getEndingOn() {
		return endingOn;
	}

	/**
	 * @param endingOn the endingOn to set
	 */
	public void setEndingOn(String endingOn) {
		this.endingOn = endingOn;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	

	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getOtpTimer() {
		return otpTimer;
	}

	public void setOtpTimer(String otpTimer) {
		this.otpTimer = otpTimer;
	}

	/**
	 * @return the otpEnabled
	 */
	public boolean isOtpEnabled() {
		return otpEnabled;
	}

	/**
	 * @param otpEnabled the otpEnabled to set
	 */
	public void setOtpEnabled(boolean otpEnabled) {
		this.otpEnabled = otpEnabled;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Long> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<Long> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * @return the createdOn
	 */
	public String getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the modifiedOn
	 */
	public String getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	
	
}

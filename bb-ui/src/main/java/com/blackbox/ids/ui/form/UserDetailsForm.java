package com.blackbox.ids.ui.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.blackbox.ids.core.model.Nationality;
import com.blackbox.ids.core.model.Person;
import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.mstr.PersonClassification;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.dto.NationalityDTO;
import com.blackbox.ids.dto.RoleDTO;
import com.blackbox.ids.dto.UserTypeDTO;
import com.blackbox.ids.ui.form.base.EntityForm;

public class UserDetailsForm implements EntityForm<User> {

	public static final String MODEL_NAME = "userForm";
	public static final TimestampFormat DATE_FORMAT = TimestampFormat.MMDDYYYY;

	public enum Action {
		CREATE_USER,
		UPDATE_USER;
	}

	private Action action;

	private Long id;

	private String firstName;

	private String middleName;

	private String lastName;

	private String emailId;

	private NationalityDTO nationality;

	private List<RoleDTO> roles = new ArrayList<RoleDTO>();

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

	/*- ------------------------------- Conversion methods with entity -- */
	@Override
	public User toEntity() {
		final User user = new User();
		// Set personal details
		user.setId(this.id);
		user.setLoginId(emailId);
		user.setPassword(password);
		user.setIsLocked(locked);
		user.setLoginAttempts(0L);
		if (StringUtils.isNoneBlank(endingOn)) {
			user.setEndDate(BlackboxDateUtil.strToDate(endingOn, DATE_FORMAT));
		}
		user.setClassification(new PersonClassification(userType.getId()));

		// Set 'Person' Details
		final Person person = new Person();
		person.setTitle(designation);
		person.setFirstName(firstName);
		if(!StringUtils.isBlank(middleName))
		{
			person.setMiddleName(middleName);
		}
		person.setLastName(lastName);
		//person.setGender(null); // TODO: Confirm
		person.setEmail(emailId);
		person.setEmployeeId(employeeId);
		person.setNationality(new Nationality(nationality.getId()));
		user.setPerson(person);

		// Set 'UserRoles'
		Role role = null;
		for (final Long rId : roleIds) {
			role = new Role(rId);
			user.getUserRoles().add(role);
		}

		return user;
	}

	@Override
	public void load(final User user) {
		this.id = user.getId();
		this.emailId = user.getLoginId();
		if (user.getClassification()!=null) {
			userType = new UserTypeDTO(user.getClassification().getId(), user.getClassification().getValue());
		}

		// load 'Person' attributes
		if (user.getPerson() != null) {
			loadPerson(user.getPerson());
		}

		// load user 'roles'
		if (CollectionUtils.isNotEmpty(user.getUserRoles())) {
			for (final Role role : user.getUserRoles()) {
				roles.add(new RoleDTO(role.getId(), role.getName()));
				userRoles.add(role.getId());
			}
		}
		endingOn = BlackboxDateUtil.dateToStr(user.getEndDate(), DATE_FORMAT);
		status = user.isActive();
	}

	private void loadPerson(final Person person) {
		firstName = person.getFirstName();
		middleName = person.getMiddleName();
		lastName = person.getLastName();
		employeeId = person.getEmployeeId();
		emailId = person.getEmail();
		designation = person.getTitle();

		if (person.getNationality() != null) {
			nationality = new NationalityDTO(person.getNationality().getId(), person.getNationality().getValue());
		}
	}

	/*- -------------------------------- getter - setters -- */
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

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

}

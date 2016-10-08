package com.blackbox.ids.services.impl.usermanagement;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.common.constant.Constant;
import com.blackbox.ids.common.notifier.Notifier;
import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.RandomTokenGenerator;
import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.dao.UserDAO;
import com.blackbox.ids.core.dto.user.UserAccessDTO;
import com.blackbox.ids.core.model.Nationality;
import com.blackbox.ids.core.model.Person;
import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.UserOTP;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.mstr.PersonClassification;
import com.blackbox.ids.core.repository.NationalityRepository;
import com.blackbox.ids.core.repository.PersonClassificationRepository;
import com.blackbox.ids.core.repository.RoleRepository;
import com.blackbox.ids.core.repository.UserOTPRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.usermanagement.AccessProfileRepository;
import com.blackbox.ids.dto.NationalityDTO;
import com.blackbox.ids.dto.OTPStatusDTO;
import com.blackbox.ids.dto.RoleDTO;
import com.blackbox.ids.dto.UserDTO;
import com.blackbox.ids.dto.UserTypeDTO;
import com.blackbox.ids.services.common.EmailService;
import com.blackbox.ids.services.usermanagement.UserService;
import com.blackbox.ids.user.connectivity.factory.UserConnectivityFactory;

@Service("userService")
public class UserServiceImpl implements UserService {

	private static Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private NationalityRepository nationalityRepository;

	@Autowired
	private PersonClassificationRepository personClassificationRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserOTPRepository userOTPRepository;

	@Autowired
	private AccessProfileRepository accessProfileRepository;

	@Autowired
	private Notifier emailNotifier;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserConnectivityFactory userConnectivityFactory;

	@Override
	public User getUserById(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	@Transactional
	public User getUserByUsername(String username) {
		return userRepository.findByLoginId(username);
	}

	@Override
	@Transactional
	public void createUser(User user) {
		userRepository.save(user);
	}

	@Override
	@Transactional
	public List<UserDTO> getUsers() {
		// TODO Auto-generated method stub
		List<User> users = userRepository.findAll();
		List<UserDTO> userDTOList = pouplateUserList(users);
		return userDTOList;
	}

	@Override
	@Transactional
	public void createNewUser(final User user) throws ApplicationException {
		assertUserProps(user);
		updateBusinessFieldsForNewUser(user);
		updateDataReferences(user);
		try {
			userRepository.save(user);
		} catch (final DataAccessException e) {
			logger.error(MessageFormat.format("[Create New User] Saving user {0} failed due to database exception.",
					user.getLoginId()), e);
			throw new ApplicationException(e);
		}
	}

	/**
	 * Assert for mandatory user properties.
	 * @throws IllegalArgumentException
	 *             If any of mandatory attributes is found missing.
	 */
	private void assertUserProps(User user) {
		if (user == null || StringUtils.isBlank(user.getLoginId()) || user.getPerson() == null
				|| CollectionUtils.isEmpty(user.getUserRoles())) {
			throw new IllegalArgumentException("[Create New User]: Missing mandatory attributes.");
		}
	}

	private void updateBusinessFieldsForNewUser(User user) {
		String password = generatePassword();
		String emailId = user.getLoginId();
		String subject = "Password Request";
		String content = "Your System generated Password is :" + password;

		/* send email to new user with auto generated password */
		sendEmail(emailId, subject, content);

		user.setPassword(password);
		user.setActive(true);
		user.setIsLocked(false);
		user.setEnabled(true);
		user.setPasswordCreatedON(new Date());
		user.setCreatedDate(Calendar.getInstance());
		user.setCreatedByUser(userRepository.getId(getLoginId()));
		user.setFirstLogin(true);

	}

	private void updateDataReferences(User user) {
		user.setAssignee(null);
	}

	@Override
	@Transactional
	public void submitNewUser(UserDTO newUser) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public List<UserDTO> getUserDetails(Collection<Long> roleIds) {

		List<UserDTO> userDTOList = null;
		List<Long> idList = new ArrayList<Long>();
		try {
			// add all user id in a list
			for (Long id : roleIds) {
				idList.addAll(roleRepository.findUserIdByRoleId(id));
			}

			// fetch user details for each user id
			List<User> users = userRepository.findAll(idList);
			userDTOList = new ArrayList<UserDTO>();
			for (User user : users) {
				UserDTO dto = populateBasicUserDetails(user);
				userDTOList.add(dto);
			}

		} catch (DataAccessException e) {
			logger.debug("Exception occurred while getting user details for role id : " + roleIds, e);
			throw new ApplicationException(10004,
					"Exception occurred while getting user details for role id " + roleIds, e);
		}

		return userDTOList;
	}

	@Override
	@Transactional
	public List<UserDTO> getUserDetailsByProfileId(Long profileId) {

		List<UserDTO> userDTOList = null;
		try {
			Set<Long> idList = accessProfileRepository.findUserIdByAccessProfileId(profileId);

			// fetch user details for each user id
			List<User> users = userRepository.findAll(idList);

			userDTOList = new ArrayList<UserDTO>();

			for (User user : users) {
				UserDTO dto = populateBasicUserDetails(user);
				userDTOList.add(dto);
			}

		} catch (DataAccessException e) {
			logger.debug("Exception occurred while getting user details for profile id : " + profileId, e);
			throw new ApplicationException(10004,
					"Exception occurred while getting user details for profile id " + profileId, e);
		}

		return userDTOList;
	}

	@Override
	@Transactional
	public List<NationalityDTO> getNationalities() {
		// TODO Auto-generated method stub
		List<Nationality> nationalities = nationalityRepository.findAll();
		List<NationalityDTO> nationalitiesDTO = new ArrayList<>();
		for (Nationality nationality : nationalities) {
			NationalityDTO nationalityDTO = new NationalityDTO();
			nationalityDTO.setId(nationality.getId());
			nationalityDTO.setName(nationality.getValue());
			nationalitiesDTO.add(nationalityDTO);
		}
		return nationalitiesDTO;
	}

	@Override
	@Transactional
	public List<UserTypeDTO> getUserTypes() {
		// TODO Auto-generated method stub
		List<PersonClassification> personClassifications = personClassificationRepository.findAll();
		List<UserTypeDTO> userTypeDTOs = new ArrayList<UserTypeDTO>();
		for (PersonClassification personClassification : personClassifications) {
			UserTypeDTO userTypeDTO = new UserTypeDTO();
			userTypeDTO.setId(personClassification.getId());
			userTypeDTO.setName(personClassification.getValue());
			userTypeDTOs.add(userTypeDTO);
		}
		return userTypeDTOs;
	}

	@Override
	@Transactional
	public List<RoleDTO> getRoles() {
		// TODO Auto-generated method stub
		List<Role> roles = roleRepository.findAll();
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		for (Role role : roles) {
			RoleDTO roleDTO = new RoleDTO();
			roleDTO.setId(role.getId());
			roleDTO.setName(role.getName());
			roleDTOs.add(roleDTO);
		}
		return roleDTOs;
	}

	@Override
	@Transactional
	public void updateUser(final User srcUser) throws ApplicationException {
		assertUserProps(srcUser);
		User dbUser = userRepository.findOne(srcUser.getId());
		mergeUserUpdates(dbUser, srcUser);
		try {
			userRepository.save(dbUser);
		} catch (final DataAccessException e) {
			logger.error(MessageFormat.format("Update user failed to user {0} due to database exception.",
					dbUser.getLoginId()), e);
			throw new ApplicationException(e);
		}
	}

	private void mergeUserUpdates(User dbUser, User srcUser) {
		mergePersonUpdates(dbUser.getPerson(), srcUser.getPerson());
		dbUser.setClassification(new PersonClassification(srcUser.getClassification().getId()));
		dbUser.setLoginId(srcUser.getLoginId());
		dbUser.getUserRoles().clear();
		dbUser.setUserRoles(srcUser.getUserRoles());
		dbUser.setEndDate(srcUser.getEndDate());

	}

	private void mergePersonUpdates(Person dbPerson, Person srcPerson) {
		dbPerson.setFirstName(srcPerson.getFirstName());
		dbPerson.setMiddleName(srcPerson.getMiddleName());
		dbPerson.setLastName(srcPerson.getLastName());
		dbPerson.setEmail(srcPerson.getEmail());
		dbPerson.setEmployeeId(srcPerson.getEmployeeId());
		dbPerson.setTitle(srcPerson.getTitle());

		if (srcPerson.getNationality() != null) {
			dbPerson.setNationality(new Nationality(srcPerson.getNationality().getId()));
		}
	}

	@Override
	@Transactional
	public OTPStatusDTO generateOtp(Long id) throws ApplicationException {
		final OTPStatusDTO otpStatus = new OTPStatusDTO();
		try {
			String userId = userRepository.getEmailId(id);
			if (!StringUtils.isEmpty(userId)) {
				sendOTP(userId);
				UserOTP userOTP = getUserOTPById(userId);
				otpStatus.setCode(userOTP.getToken());
				otpStatus.setMessage("Success");
			} else {
				otpStatus.setCode(null);
				otpStatus.setMessage("Fail");
			}
			return otpStatus;
		} catch (Exception e) {
			logger.debug(e);
			otpStatus.setMessage("Fail");
			otpStatus.setCode("");
			return otpStatus;
		}
	}

	@Override
	@Transactional
	public List<Long> disableAccess(List<Long> ids) {
		List<Long> failedIds = new ArrayList<Long>();
		for (Long id : ids) {
			if (!userRepository.disableAccess(id)) {
				failedIds.add(id);
			}
		}
		return failedIds;
	}

	@Override
	@Transactional
	public List<Long> deleteUser(List<Long> ids) {
		List<Long> failedIds = new ArrayList<Long>();
		for (Long id : ids) {
			if (!userRepository.deleteUser(id)) {
				failedIds.add(id);
			}
		}
		return failedIds;
	}

	private UserDTO populateBasicUserDetails(User user) {

		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setEmailId(user.getLoginId());
		if (user.getPerson() != null) {

			String middleName = "";
			if (user.getPerson().getMiddleName() != null) {
				middleName = user.getPerson().getMiddleName();
			}
			userDTO.setUserName(
					user.getPerson().getFirstName() + " " + middleName + " " + user.getPerson().getLastName());
			userDTO.setDesignation(user.getPerson().getTitle());
		}
		userDTO.setEmailId(user.getLoginId());
		return userDTO;
	}

	private List<UserDTO> pouplateUserList(List<User> users) {
		List<UserDTO> userDTOList = new ArrayList<UserDTO>();
		for (User user : users) {
			UserDTO userDTO = new UserDTO();
			userDTO.setId(user.getId());
			userDTO.setEmailId(user.getLoginId());
			UserTypeDTO userTypeDTO = new UserTypeDTO();
			userTypeDTO.setId(user.getClassification().getId());
			userTypeDTO.setName(user.getClassification().getValue());
			userDTO.setUserType(userTypeDTO);
			String firstName, lastName, middleName = "";
			if (user.getPerson() != null) {
				firstName = user.getPerson().getFirstName();
				if (user.getPerson().getMiddleName() != null) {
					middleName = user.getPerson().getMiddleName();
				}
				lastName = user.getPerson().getLastName();
				userDTO.setUserName(firstName + " " + middleName + " " + lastName);
				userDTO.setDesignation(user.getPerson().getTitle());
			}

			userDTO.setEmailId(user.getLoginId());
			Set<Role> roles = user.getUserRoles();
			List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
			for (Role role : roles) {
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setId(role.getId());
				roleDTO.setName(role.getName());
				if (role.isOtpEnabed()) {
					userDTO.setOtpEnabled(role.isOtpEnabed());
				}
				roleDTOs.add(roleDTO);
			}
			userDTO.setRoles(roleDTOs);
			userDTO.setEndingOn(convertDate(user.getEndDate()));
			userDTO.setStatus(user.isActive());
			userDTO.setLocked(user.getIsLocked());
			userDTO.setEnabled(user.isEnabled());
			userDTO.setCreatedOn(convertCalendar(user.getCreatedDate()));
			userDTO.setModifiedOn(convertCalendar(user.getUpdatedDate()));
			userDTOList.add(userDTO);
		}
		return userDTOList;

	}

	private Person populatePersonForSave(UserDTO newUser) {
		Person person = new Person();
		person.setFirstName(newUser.getFirstName());
		person.setLastName(newUser.getLastName());
		person.setMiddleName(newUser.getMiddleName());
		person.setEmployeeId(newUser.getEmployeeId());
		person.setTitle(newUser.getDesignation());
		person.setEmail(newUser.getEmailId());

		if (newUser.getNationality() != null) {
			Nationality nationality = nationalityRepository.findOne(newUser.getNationality().getId());
			person.setNationality(nationality);
		}
		/*
		 * AssociatedAgent associatedAgent = associatedAgentRepository.findOne(1L);
		 * person.setAssociatedAgent(associatedAgent); if("ASSOCIATED AGENT" .equals(newUserDTO.getUserType())) {
		 * associatedAgent.setDescription(newUserDTO.getAgentName()); } else if( "EXTERNAL CLIENT"
		 * .equals(newUserDTO.getUserType())) { associatedAgent.setDescription(newUserDTO.getClientName()); }
		 */
		return person;
	}

	private Person populatePersonForUpdate(Person person, UserDTO newUser) {
		person.setFirstName(newUser.getFirstName());
		person.setLastName(newUser.getLastName());
		person.setMiddleName(newUser.getMiddleName());
		person.setEmployeeId(newUser.getEmployeeId());
		person.setTitle(newUser.getDesignation());
		person.setEmail(newUser.getEmailId());

		if (newUser.getNationality() != null) {
			Nationality nationality = nationalityRepository.findOne(newUser.getNationality().getId());
			person.setNationality(nationality);
		}

		/*
		 * AssociatedAgent associatedAgent = associatedAgentRepository.findOne(1L);
		 * person.setAssociatedAgent(associatedAgent); if("ASSOCIATED AGENT" .equals(newUserDTO.getUserType())) {
		 * associatedAgent.setDescription(newUserDTO.getAgentName()); } else if( "EXTERNAL CLIENT"
		 * .equals(newUserDTO.getUserType())) { associatedAgent.setDescription(newUserDTO.getClientName()); }
		 */
		return person;
	}

	@Override
	@Transactional
	public boolean sendOTP(String userId) {
		boolean isSent = true;
		final String feature = "[Send OTP Email]";
		if (StringUtils.isBlank(userId)) {
			throw new IllegalArgumentException(MessageFormat.format("{0}: UserId must not be blank.", feature));
		}
		Long id = userRepository.getUserIdByLoginId(userId);
		if (id != null) {
			UserOTP userOTP = createAndSaveUserOTP(userId);
			List<String> recipient = new ArrayList<String>();
			recipient.add(userId);
			List<MessageData> messageDatas = createMessageDataForOTP(userOTP);
			createAndSaveMessage(TemplateType.OTP, recipient, messageDatas);
		} else {
			logger.error("Username : " + userId + " is invalid.");
			isSent = false;
		}
		return isSent;
	}

	private UserOTP createAndSaveUserOTP(String userId) {
		UserOTP userToken = userOTPRepository.findByUserId(userId);
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.MINUTE, 15);
		dt = c.getTime();
		if (userToken != null) {
			userToken.setExpiryDate(dt);
		} else {
			userToken = new UserOTP();
			userToken.setUserId(userId);
			userToken.setExpiryDate(dt);
		}
		userToken.setCreatedOn(new Date());
		String otp = RandomTokenGenerator.generateRandomOTP();
		userToken.setToken(otp);
		return userOTPRepository.save(userToken);
	}

	private List<MessageData> createMessageDataForOTP(UserOTP userOTP) {
		List<MessageData> messageDatas = null;
		if (userOTP != null) {
			messageDatas = new ArrayList<>();
			MessageData otpData = new MessageData(com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_OTP,
					userOTP.getToken());
			messageDatas.add(otpData);
			String firstName = userRepository.getFirstNameByLoginId(userOTP.getUserId());
			MessageData receiverData = new MessageData(
					com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_RECEIVER_NAME, firstName);
			messageDatas.add(receiverData);
		}
		return messageDatas;
	}

	private void createAndSaveMessage(final TemplateType templateType, List<String> recipients,
			Collection<MessageData> messageDatas) {
		Message message = null;
		if (templateType != null && !CollectionUtils.isEmpty(recipients)) {
			message = new Message();
			message.setTemplateType(templateType);
			StringBuilder strBuilder = new StringBuilder();
			for (String recipient : recipients) {
				strBuilder.append(recipient).append(Constant.SEMI_COLON);
			}
			message.setReceiverList(strBuilder.toString());
			message.setStatus(Constant.MESSAGE_STATUS_NOT_SENT);
			emailService.send(message, messageDatas);
		}
	}

	@Override
	@Transactional
	public boolean generateAndSendPassword(final String userId) {
		final String feature = "[Send Password Email]";
		if (StringUtils.isBlank(userId)) {
			throw new IllegalArgumentException(MessageFormat.format("{0}: UserId must not be blank.", feature));
		}

		boolean isSent = false;
		logger.info(MessageFormat.format("{0}: Generating password for user {1}.", feature, userId));
		User user = userRepository.findByLoginId(userId);
		if (user != null) {
			String password = RandomTokenGenerator.generateRandomPassword();
			user.setFirstLogin(true);
			user.setPassword(password);
			userRepository.save(user);
			logger.info(MessageFormat.format("{0}: Emailing password for user {1}.", feature, userId));
			List<MessageData> messageDatas = createMessageDataForForgotPassword(user);
			createAndSaveMessage(TemplateType.FORGOT_PASSWORD, Arrays.asList(userId), messageDatas);
			isSent = true;
		} else {
			logger.error("Username : " + userId + " is invalid.");
		}
		return isSent;
	}

	private List<MessageData> createMessageDataForForgotPassword(User user) {
		List<MessageData> messageDatas = null;
		if (user != null) {
			messageDatas = new ArrayList<>();
			MessageData otpData = new MessageData(com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_PASSWORD,
					user.getPassword());
			messageDatas.add(otpData);
			String firstName = userRepository.getFirstNameByLoginId(user.getLoginId());
			MessageData receiverData = new MessageData(
					com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_RECEIVER_NAME, firstName);
			messageDatas.add(receiverData);
		}
		return messageDatas;
	}

	@Override
	@Transactional
	public boolean validatePassword(String password) {
		boolean isValidated = false;
		if (!StringUtils.isEmpty(password)) {
			String loginId = getLoginId();
			User user = userRepository.findByLoginId(loginId);
			if (user != null && password.equals(user.getPassword())) {
				isValidated = true;
			}
		}
		return isValidated;
	}

	@Override
	@Transactional
	public void savePassword(String username, String password) {
		final String feature = "[Save Password]";
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException(MessageFormat.format("{0}: UserId must not be blank.", feature));
		}
		if (StringUtils.isBlank(password)) {
			throw new IllegalArgumentException(MessageFormat.format("{0}: Password must not be blank.", feature));
		}
		User user = userRepository.findByLoginId(username);
		user.setPassword(password);
		user.setFirstLogin(false);
		userRepository.save(user);
	}

	private String getLoginId() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@Override
	@Transactional
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public void lockUserAccount(String loginId) {
		final String feature = "[Lock User Account]";
		if (StringUtils.isBlank(loginId)) {
			throw new IllegalArgumentException(MessageFormat.format("{0}: UserId must not be blank.", feature));
		}
		User user = userRepository.findByLoginId(loginId);
		if (user != null && !user.getIsLocked()) {
			user.setIsLocked(true);
			userRepository.save(user);
			List<MessageData> messageDatas = createMessageDataForReceiverName(user.getLoginId());
			createAndSaveMessage(TemplateType.ACCOUNT_LOCKED, Arrays.asList(loginId), messageDatas);
		}
	}

	private List<MessageData> createMessageDataForReceiverName(String loginId) {
		List<MessageData> messageDatas = null;
		if (!StringUtils.isEmpty(loginId)) {
			messageDatas = new ArrayList<>();
			String firstName = userRepository.getFirstNameByLoginId(loginId);
			MessageData receiverData = new MessageData(
					com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_RECEIVER_NAME, firstName);
			messageDatas.add(receiverData);
		}
		return messageDatas;
	}

	@Override
	@Transactional
	public UserOTP getUserOTPById(String userId) {
		return userOTPRepository.findByUserId(userId);
	}

	@Override
	@Transactional
	public UserOTP saveUserOTP(UserOTP userToken) {
		return userOTPRepository.save(userToken);
	}

	@Override
	@Transactional
	public UserOTP getUserOTPByUsername(String userId) {
		return userOTPRepository.findByUserId(userId);
	}

	@Override
	@Transactional
	public boolean checkOTPValidity(String username, String otp) {
		boolean isValid = false;
		UserOTP userOTP = userOTPRepository.findByUserId(username);
		if (userOTP != null
				&& ((userOTP.getExpiryDate().compareTo(new Date()) > 0) && otp.equals(userOTP.getToken()))) {
			isValid = true;
		}
		return isValid;
	}

	@Override
	@Transactional
	public Long getUserByEmailId(String emailId) {
		return userRepository.getId(emailId);
	}

	private String convertDate(Date date) {
		String stringDate = null;
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			stringDate = formatter.format(date);
		}
		return stringDate;
	}

	private String convertCalendar(Calendar date) {
		String stringDate = null;
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
			stringDate = formatter.format(date.getTime());
		}
		return stringDate;
	}

	@Override
	@Transactional
	public UserTypeDTO getUserType(Long id) {
		PersonClassification personClassification = personClassificationRepository.findOne(id);
		UserTypeDTO userTypeDTO = new UserTypeDTO();
		if (personClassification != null) {
			userTypeDTO.setId(personClassification.getId());
			userTypeDTO.setName(personClassification.getValue());
		}
		return userTypeDTO;
	}

	@Override
	@Transactional
	public UserDTO unlockUser(Long id) {

		logger.info("Unlocking User Id : " + id);
		String password = generatePassword();
		userDAO.unlockAccess(id, password);
		unlockUserBusinessRules(id, password);
		UserAccessDTO accessDTO = userDAO.fetchUserAccessDTO(id);
		return populateAccess(accessDTO);
	}

	@Override
	@Transactional
	public UserDTO enableUser(Long id) {
		userDAO.enableAccess(id);
		UserAccessDTO accessDTO = userDAO.fetchUserAccessDTO(id);
		return populateAccess(accessDTO);
	}

	private UserDTO populateAccess(UserAccessDTO user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.id);
		userDTO.setEnabled(user.enabled);
		userDTO.setLocked(user.locked);
		userDTO.setStatus(user.active);
		return userDTO;
	}

	@Override
	@Transactional
	public String getUserFullName(String userId) {
		return userRepository.getUserFullName(userId);
	}

	@Override
	@Transactional
	public User findByLoginId(String username) {
		User user = userRepository.findByLoginId(username);
		for (Role role : user.getUserRoles()) {
			role.getId();
		}
		return user;
	}

	@Override
	// @Transactional
	public Long getUserIdByEmailId(String emailId) {
		if (StringUtils.isBlank(emailId)) {
			throw new IllegalArgumentException("Email Id must not be blank.");
		}
		return userRepository.getActiveUserIdByLoginId(emailId);
	}

	public boolean sendEmail(String emailId, String subject, String content) throws ApplicationException {
		boolean isSent = true;
		if (!StringUtils.isEmpty(emailId)) {
			List<String> recipient = new ArrayList<String>();
			recipient.add(emailId);
			emailNotifier.notificationWithAttachment(recipient, subject, content + ".", null, null, null);
		} else {
			logger.error("Username : " + emailId + " is invalid.");
			isSent = false;
		}
		return isSent;
	}

	private String generatePassword() {
		String password = RandomTokenGenerator.generateRandomPassword();
		return password;
	}

	private void unlockUserBusinessRules(Long id, String password) {
		String emailId = userRepository.getEmailId(id);
		String subject = "Password Request";
		String content = "Your System generated Password is :" + password;

		/* send password email */
		sendEmail(emailId, subject, content);
	}

	@Override
	public boolean isLdapEnabled() {
		return (userConnectivityFactory.getImplementation() != null);
	}

}

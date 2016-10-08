package com.blackbox.ids.services.usermanagement;

import java.util.Collection;
import java.util.List;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.UserOTP;
import com.blackbox.ids.dto.NationalityDTO;
import com.blackbox.ids.dto.OTPStatusDTO;
import com.blackbox.ids.dto.RoleDTO;
import com.blackbox.ids.dto.UserDTO;
import com.blackbox.ids.dto.UserTypeDTO;

/**
 * @author Nagarro
 */
public interface UserService {

	public abstract User getUserByUsername(String username);

	public abstract void createUser(User user);

	public abstract User saveUser(User user);

	public abstract void lockUserAccount(String username);

	public abstract UserOTP getUserOTPById(String userId);

	public abstract UserOTP saveUserOTP(UserOTP userOTP);

	public abstract boolean sendOTP(String userId);

	public abstract boolean generateAndSendPassword(String userId);

	public User getUserById(Long id);

	public abstract UserOTP getUserOTPByUsername(String userId);

	public abstract boolean validatePassword(String password);

	public abstract void savePassword(String username, String password);

	public User findByLoginId(String username);
	
	public boolean sendEmail(String emailId, String subject, String content); 

	/**
	 * Returns all the users available in the application
	 * @return
	 */
	List<UserDTO> getUsers();

	/**
	 * Save new user in the system
	 * @param userDTO
	 */
	public void createNewUser(final User user) throws ApplicationException;

	/**
	 * Save new user in the system
	 * @param user
	 */
	void submitNewUser(UserDTO user);

	/**
	 * Return all the nationalities available in the application
	 * @return
	 */
	List<NationalityDTO> getNationalities();

	/**
	 * Return all the user types available in the application
	 * @return
	 */
	List<UserTypeDTO> getUserTypes();

	/**
	 * Return all the roles available in the application
	 * @return
	 */
	List<RoleDTO> getRoles();

	/**
	 * Updates user entity
	 * @param userDTO
	 * @return
	 * @throws ApplicationException
	 */
	public void updateUser(final User srcUser) throws ApplicationException;

	/**
	 * Generate OTP for the given user id
	 * @param id
	 * @return
	 * @throws ApplicationException
	 */
	OTPStatusDTO generateOtp(Long id) throws ApplicationException;

	/**
	 * Disable user access for the given list of id
	 * @param id
	 * @return
	 */
	List<Long> disableAccess(List<Long> id);

	/**
	 * Deletes all the user for the given list of id
	 * @param id
	 * @return
	 */
	List<Long> deleteUser(List<Long> id);

	/**
	 * Checks if the otp is expired for given user
	 * @param username
	 * @param otp
	 * @return
	 */
	boolean checkOTPValidity(String username, String otp);

	/**
	 * Returns user id by email id
	 * @param emailId
	 * @return
	 */
	Long getUserByEmailId(String emailId);

	/**
	 * Returns user type
	 * @param id
	 * @return
	 */

	UserTypeDTO getUserType(Long id);

	/**
	 * Get all user details attached to given role
	 * @param list
	 *            of roleId
	 * @return
	 */
	public List<UserDTO> getUserDetails(Collection<Long> roleId);

	/**
	 * Get all user details attached to given profile
	 * @param profileId
	 * @return
	 */
	public List<UserDTO> getUserDetailsByProfileId(Long profileId);

	public String getUserFullName(String userId);

	UserDTO unlockUser(Long id);

	UserDTO enableUser(Long id);

	Long getUserIdByEmailId(String emailId);

	/**
	 * Checks whether Ldap is enabled.
	 * @param username
	 * @param otp
	 * @return true if Ldap enabled else false
	 */
	public boolean isLdapEnabled();

}

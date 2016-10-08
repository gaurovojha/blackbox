package com.blackbox.ids.core.repository;

/**
 * Custom Dao to manage custom methods which can not be implemented by Spring Jpa. Add all such custom methods in here.
 * It must be implemented by CustomImpl. Note: Never inject this custom repository anywhere rather inject the main
 * repository.
 * @author Nagarro
 */
public interface UserCustomRepository<User, ID> {

	String getEmailId(Long id);

	boolean disableAccess(Long id);

	boolean deleteUser(Long id);

	Long getId(String email);

	void savePassword(String username, String password);

	public String getUserFullName(String userId);

	public String getFirstNameByLoginId(String userId);

	public String getUserFirstAndLastName(Long userId);

}
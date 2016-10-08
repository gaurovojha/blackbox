package com.blackbox.ids.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackbox.ids.core.model.UserOTP;

/**
 * A DAO for the entity UserToken is simply created by extending the
 * JpaRepository interface provided by spring. The following methods are some of
 * the ones available from such interface: save, delete, deleteAll, findOne and
 * findAll. The magic is that such methods must not be implemented, and moreover
 * it is possible create new query methods working only by defining their
 * signature!
 * 
 * @author Nagarro
 */
public interface UserOTPRepository extends JpaRepository<UserOTP, Long> {

	/**
	 * Return the user having the passed id or null if no user is found.
	 * 
	 * @param id
	 *            the Id.
	 */
	public UserOTP findById(Long id);

	public UserOTP findByUserId(String userId);

}

package com.blackbox.ids.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.dto.user.UserDTO;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.repository.base.BaseRepository;

/**
 * A DAO for the entity User is simply created by extending the JpaRepository interface provided by spring and custom
 * repository. The following methods are some of the ones available from former interface: save, delete, deleteAll,
 * findOne and findAll. The magic is that such methods must not be implemented, and moreover it is possible create new
 * query methods working only by defining their signature! Custom interface can be used to define custom query methods
 * that can not be implemented by Spring.
 * @author Nagarro
 */
public interface UserRepository
		extends JpaRepository<User, Long>, UserCustomRepository<User, Long>, BaseRepository<User, Long> {

	/**
	 * Return the user having the passed username or null if no user is found.
	 * @param username
	 *            the user username.
	 */
	public User findByLoginId(String username);

	@Query("SELECT user.id from User user where lower(user.loginId) = lower(:loginId)")
	Long getUserIdByLoginId(@Param("loginId") final String loginId);

	@Query("Select new com.blackbox.ids.core.dto.user.UserDTO(user.id, user.person.firstName, user.person.lastName) from User user where user.id = :id")
	UserDTO getUserDetailsById(@Param("id") final Long id);

	@Query(value = "SELECT user.id from User user where lower(user.loginId) = lower(:loginId) and user.isActive=true")
	Long getActiveUserIdByLoginId(@Param("loginId") final String loginId);

}

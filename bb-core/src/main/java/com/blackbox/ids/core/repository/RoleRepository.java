package com.blackbox.ids.core.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.usermanagement.AccessProfile;

/**
 * A DAO for the entity Role is simply created by extending the JpaRepository interface provided by spring and custom
 * repository. The following methods are some of the ones available from former interface: save, delete, deleteAll,
 * findOne and findAll. The magic is that such methods must not be implemented, and moreover it is possible create new
 * query methods working only by defining their signature! Custom interface can also be used to define custom query
 * methods that can not be implemented by Spring automatically.
 * @author Nagarro
 */
public interface RoleRepository extends JpaRepository<Role, Long>, RoleCustomRepository<Role, Long> {

	/**
	 * Return role by given role name or null if no role name is found.
	 * @param roleName
	 * @return
	 */
	public Role findByName(String roleName);

	/**
	 * @param systemRole
	 * @return
	 */
	public List<Role> findBySystemRole(boolean systemRole);

	/**
	 * @param seeded
	 * @return
	 */
	public List<Role> findBySeeded(boolean seeded);

	/**
	 * @param active
	 * @return
	 */
	public List<Role> findByActive(boolean active);

	/**
	 * @param active
	 * @param systemRole
	 * @return
	 */
	public List<Role> findByActiveAndSystemRole(boolean active, boolean systemRole);

	/**
	 * @param active
	 * @param seeded
	 * @return
	 */
	public List<Role> findByActiveAndSeeded(boolean active, boolean seeded);

	/**
	 * @param accessProfile
	 * @return
	 */
	public List<Role> findByAccessProfile(AccessProfile accessProfile);
	
	/**
	 * Find by id in.
	 *
	 * @param roleIds
	 *            the role ids
	 * @return the list
	 */
	public List<Role> findByIdIn(Set<Long> roleIds);
	
	/**
	 * Gets the notification message.
	 *
	 * @param notificationType
	 *            the notification type
	 * @return the notification message
	 */
	@Query("select r.active from Role r where r.id = ?1")
	Boolean isActiveRoleId(Long roleId);
}

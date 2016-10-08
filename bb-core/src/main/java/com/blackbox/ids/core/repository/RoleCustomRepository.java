package com.blackbox.ids.core.repository;

import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Interface RoleCustomRepository.
 *
 * @param <Role>
 *            the generic type
 * @param <ID>
 *            the generic type
 */
public interface RoleCustomRepository<Role, ID> {

	/**
	 * Returns all the user id's attached to a particular role.
	 *
	 * @param id
	 *            the id
	 * @return list of user id's
	 */
	public Set<Long> findUserIdByRoleId(ID id);

	/**
	 * Finds all the role names available.
	 *
	 * @return list of role name
	 */
	public List<String> findAllRoleNames();

	/**
	 * Deactivates role by given id.
	 *
	 * @param id
	 *            the id
	 * @return boolean
	 */
	public boolean deactivateRole(Long id);

	/**
	 * Count users by role ids.
	 *
	 * @param roleIds
	 *            the role ids
	 * @return the int
	 */
	public int countUsersByRoleIds(Set<Long> roleIds);

	/**
	 * Gets the users by role ids.
	 *
	 * @param roleIds
	 *            the role ids
	 * @return the users by role ids
	 */
	public List<Long> getUsersByRoleIds(Set<Long> roleIds);
}
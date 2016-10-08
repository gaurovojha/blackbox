package com.blackbox.ids.core.repository.usermanagement;

import java.util.List;
import java.util.Set;

/**
 * Custom Dao to manage custom methods which can not be implemented by Spring Jpa. Add all such custom methods in here.
 * It must be implemented by CustomImpl. Note: Never inject this custom repository anywhere rather inject the main
 * repository.
 * @author Nagarro
 */
public interface AccessProfileCustomRepository<AccessProfile, ID> {

	/**
	 * Returns all the user id's attached to a particular role
	 * @param id
	 * @return list of id
	 */
	public Set<Long> findUserIdByAccessProfileId(ID id);

	/**
	 * Returns list of access profile names
	 * @return List of access profile names
	 */
	public List<String> findAllAccessProfileNames();

	/**
	 * Deactivates access profile provided no active role attached to it
	 * @param id
	 * @return boolean
	 */
	public boolean deactivateAccessProfile(Long id);
}
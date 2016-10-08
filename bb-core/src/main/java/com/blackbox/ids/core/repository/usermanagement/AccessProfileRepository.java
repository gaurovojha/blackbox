package com.blackbox.ids.core.repository.usermanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackbox.ids.core.model.usermanagement.AccessProfile;

/**
 * A DAO for the entity AccessProfile is simply created by extending the JpaRepository
 * interface provided by spring and custom repository. The following methods are some of the ones
 * available from former interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature! 
 * Custom interface can also be used to define custom query methods that can not be implemented by Spring automatically. 
 * 
 * @author Nagarro
 */
public interface AccessProfileRepository extends JpaRepository<AccessProfile, Long>, AccessProfileCustomRepository<AccessProfile, Long> {
	
	/**
	 * Return access profile by given name
	 * 
	 * @param name
	 * @return access profile
	 */
	public AccessProfile findAccessProfileByName(String name);
	
	/**
	 * Return all the active profiles
	 * 
	 * @param active
	 * @return list of active access profile
	 */
	public List<AccessProfile> findAccessProfileByActive(boolean active);
}

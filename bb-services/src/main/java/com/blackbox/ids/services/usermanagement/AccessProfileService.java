package com.blackbox.ids.services.usermanagement;

import java.util.List;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.dto.usermanagement.AccessProfileDTO;

/**
 * Handles Access Profile related service requests
 * 
 * Nagarro
 */
public interface AccessProfileService {

	/**
	 * Retrieve access profile by provided name 
	 * 
	 * @param name
	 * @return access profile dto
	 * @throws ApplicationException
	 */
    public AccessProfileDTO getAccessProfileByName(final String name) throws ApplicationException;
    
    /**
     * Fetch access profile by provided id
     * 
     * @param id
     * @return access profile dto
     * @throws ApplicationException
     */
    public AccessProfileDTO getAccessProfileById(Long id) throws ApplicationException;
    
    /**
     * 
     * 
     * @return list of access profile names
     * @throws ApplicationException
     */
    public List<String> getAllAccessProfileNames() throws ApplicationException;

    /**
     * Creates new access profile
     * 
     * @param accessProfile
     * @throws ApplicationException
     */
    public void createAccessProfile(final AccessProfileDTO accessProfile) throws ApplicationException;
    
    /**
     * Updates existing access profile
     * 
     * @param accessProfile
     * @throws ApplicationException
     */
    public void updateAccessProfile(final AccessProfileDTO accessProfile) throws ApplicationException;
    
    /**
     * Return all the access profiles
     * 
     * @return list of access profile dto
     * @throws ApplicationException
     */
    public List<AccessProfileDTO> getAllAccessProfiles() throws ApplicationException;
    
    /**
     * Removes access profile provided id
     * 
     * @param id
     * @throws ApplicationException
     */
    public void removeAccessProfile(final Long id) throws ApplicationException;
    
    /**
     * Return user count attached to given access profile
     * 
     * @param id
     * @return user count
     * @throws ApplicationException
     */
    public long getUserCount(final Long id) throws ApplicationException;

    /**
     * Deactivates access profile provided id
     * 
     * @param id
     * @return boolean
     * @throws ApplicationException
     */
	public boolean deactivate(final Long id) throws ApplicationException;

	/**
	 * 
	 * Returns all the active profiles
	 * 
	 * @return list of access profile dto
	 * @throws ApplicationException
	 */
	public List<AccessProfileDTO> getActiveAccessProfiles() throws ApplicationException;
}
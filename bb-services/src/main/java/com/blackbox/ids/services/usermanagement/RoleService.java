package com.blackbox.ids.services.usermanagement;

import java.util.List;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.dto.AssigneeDTO;
import com.blackbox.ids.dto.CustomerDTO;
import com.blackbox.ids.dto.JurisdictionDTO;
import com.blackbox.ids.dto.OrganizationDTO;
import com.blackbox.ids.dto.RoleDTO;
import com.blackbox.ids.dto.TechnologyGroupDTO;

/**
 * Handles role related service requests
 * 
 * Nagarro
 */
public interface RoleService {

	/**
	 * Fetch role by provided name 
	 * 
	 * @param username
	 * @return role dto
	 * @throws ApplicationException
	 */
    public RoleDTO getRoleByName(final String username) throws ApplicationException;
    
    /**
     * Fetch role by provided id
     * 
     * @param id
     * @return role dto
     * @throws ApplicationException
     */
    public RoleDTO getRoleById(final Long id) throws ApplicationException;

    /**
     * Creates new role in the system
     * 
     * @param dto
     * @throws ApplicationException
     */
    public void createRole(final RoleDTO dto) throws ApplicationException;
    
    /**
     * Returns all the roles
     * 
     * @return list of role dto
     * @throws ApplicationException
     */
    public List<RoleDTO> getAllRoles() throws ApplicationException;
    
    /**
     * Removes role from the system provided id
     * 
     * @param id
     * @throws ApplicationException
     */
    public void removeRole(final Long id) throws ApplicationException;
    
    /**
     * Updates existing role
     * 
     * @param dto
     * @throws ApplicationException
     */
    public void updateRole(final RoleDTO dto) throws ApplicationException;
    
    /**
     * Returns user count attached to given role
     * 
     * @param roleId
     * @return user count
     * @throws ApplicationException
     */
    public long getUserCount(final Long roleId) throws ApplicationException;
    
    /**
     * Returns all the role names
     * 
     * @return list of role names
     * @throws ApplicationException
     */
    public List<String> getAllRoleNames() throws ApplicationException;
    
    /**
     * Returns all the jurisdictions
     * 
     * @return list of jurisdictions
     * @throws ApplicationException
     */
    public List<JurisdictionDTO> getAllJurisdiction() throws ApplicationException;
    
    /**
     * Returns all the assignees
     * 
     * @return list of assignees
     * @throws ApplicationException
     */
    public List<AssigneeDTO> getAllAssignee() throws ApplicationException;
    
    /**
     * Returns all the customers
     * 
     * @return list of customers
     * @throws ApplicationException
     */
    public List<CustomerDTO> getAllCustomer() throws ApplicationException;
    
    /**
     * Returns all the technology groups
     * 
     * @return list of technology group
     * @throws ApplicationException
     */
    public List<TechnologyGroupDTO> getAllTechnologyGroup() throws ApplicationException;
    
    /**
     * Returns all the organizations
     * 
     * @return list of organizations
     * @throws ApplicationException
     */
    public List<OrganizationDTO> getAllOrganization() throws ApplicationException;

    /**
     * Deacivates a given role also called soft delete
     * 
     * @param id
     * @return boolean
     * @throws ApplicationException
     */
    public boolean deactivate(final Long id) throws ApplicationException;
    
    /**
     * Search for roles given by id's of jurisdictions, assignees, customer, techGroup and organization 
     * 
     * @param juricIds
     * @param assigneeIds
     * @param techGrpIds
     * @param custmNoIds
     * @param organIds
     * @return list of role dto
     * @throws ApplicationException
     */
    public List<RoleDTO> findRolesForSystem(final List<Long> juricIds, final List<Long> assigneeIds, final List<Long> techGrpIds, final List<Long> custmNoIds, final List<Long> organIds) throws ApplicationException;
    
    /**
     * Return all the active roles
     * 
     * @return list of roles
     * @throws ApplicationException
     */
    public List<RoleDTO> getAllActiveRoles() throws ApplicationException;
}

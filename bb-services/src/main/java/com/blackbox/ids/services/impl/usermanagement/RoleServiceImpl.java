package com.blackbox.ids.services.impl.usermanagement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.mstr.Organization;
import com.blackbox.ids.core.model.mstr.TechnologyGroup;
import com.blackbox.ids.core.model.usermanagement.AccessProfile;
import com.blackbox.ids.core.model.usermanagement.Permission;
import com.blackbox.ids.core.repository.AssigneeRepository;
import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.core.repository.OrganizationRepository;
import com.blackbox.ids.core.repository.RoleRepository;
import com.blackbox.ids.core.repository.TechnologyGroupRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.usermanagement.AccessProfileRepository;
import com.blackbox.ids.dto.AssigneeDTO;
import com.blackbox.ids.dto.CustomerDTO;
import com.blackbox.ids.dto.JurisdictionDTO;
import com.blackbox.ids.dto.OrganizationDTO;
import com.blackbox.ids.dto.RoleDTO;
import com.blackbox.ids.dto.TechnologyGroupDTO;
import com.blackbox.ids.dto.usermanagement.AccessProfileDTO;
import com.blackbox.ids.services.usermanagement.RoleService;

/**
 * Handles all the role related request and perform business logic
 * 
 * @author Nagarro
 *
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

	private final static Logger log = Logger.getLogger(RoleServiceImpl.class);

	// date format
	private final static DateFormat format = new SimpleDateFormat("MMM dd, yyyy");
	
	private final static DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccessProfileRepository accessProfileRepository;

	@Autowired
	private JurisdictionRepository jurisdictionRepository;

	@Autowired
	private AssigneeRepository assigneeRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TechnologyGroupRepository technologyGroupRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Override
	@Transactional
	public List<String> getAllRoleNames() throws ApplicationException {
		List<String> roleNames = null;
		;
		try {
			roleNames = roleRepository.findAllRoleNames();
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting role names", e);
			throw new ApplicationException(10001, "Exception occurred while getting role Names", e);
		}
		return roleNames;
	}

	@Override
	@Transactional
	public RoleDTO getRoleByName(final String name) throws ApplicationException {
		try {
			Role role = roleRepository.findByName(name);
			RoleDTO dto = populateDTOFromRole(role);
			return dto;
		} catch (Exception e) {
			log.debug("Exception occurred while getting role : " + name, e);
			throw new ApplicationException(10001, "Exception occurred while getting role : " + name, e);
		}
	}

	@Override
	@Transactional
	public RoleDTO getRoleById(final Long id) throws ApplicationException {
		try {
			Role role = roleRepository.findOne(id);
			RoleDTO dto = populateDTOFromRole(role);
			return dto;
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting role : " + id, e);
			throw new ApplicationException(10001, "Exception occurred while getting role : " + id, e);
		}
	}

	@Override
	@Transactional
	public List<RoleDTO> getAllRoles() throws ApplicationException {
		List<RoleDTO> roleList = null;
		try {
			// exclude system generated roles
			List<Role> roles = roleRepository.findBySystemRole(false);
			roleList = new ArrayList<>();

			for (Role role : roles) {
				RoleDTO dto = populateDTOFromRole(role);
				roleList.add(dto);
			}
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting roles ", e);
			throw new ApplicationException(10001, "Exception occurred while getting roles ", e);
		}
		return roleList;
	}

	@Override
	@Transactional
	public void createRole(final RoleDTO dto) throws ApplicationException {

		Role role = new Role();
		try {
			populateRoleFromDTO(role, dto);
			role.setRole(dto.getName());
			role.setSeeded(false);
			role.setActive(true);

			roleRepository.save(role);
		} catch (Exception e) {
			log.debug("Exception occurred while saving role : " + dto.getName(), e);
			throw new ApplicationException(10002, "Exception occurred while saving new role " + dto.getName(), e);
		}
	}

	@Override
	@Transactional
	public void updateRole(final RoleDTO dto) throws ApplicationException {
		Role role = null;
		try {
			role = roleRepository.findOne(dto.getId());
			populateRoleFromDTO(role, dto);
			roleRepository.save(role);
		} catch (DataIntegrityViolationException e) {
			log.debug("Exception occurred while updating role : " + dto.getId(), e);
			throw new ApplicationException(10003, "Exception occurred while updating role : " + dto.getId(), e);
		}
	}

	@Override
	@Transactional
	public void removeRole(final Long id) throws ApplicationException {
		try {
			roleRepository.delete(id);
		} catch (DataIntegrityViolationException e) {
			log.debug("Exception occurred while deleting role : " + id, e);
			throw new ApplicationException(10004, "Exception occurred while deleting role : " + id, e);
		}
	}

	@Override
	@Transactional
	public boolean deactivate(final Long id) {
		try {
			Role role = roleRepository.findOne(id);
			// seeded roles can not be deactivated
			if(!role.isSeeded()){
				role.setAccessProfile(null);
				return roleRepository.deactivateRole(id);
			}
		} catch (Exception e) {
			log.debug("Exception occurred while deactivating role : " + id, e);
			return false;
		}
		return false;
	}

	@Override
	@Transactional
	public long getUserCount(final Long id) throws ApplicationException {
		try {
			Set<Long> idList = roleRepository.findUserIdByRoleId(id);
			return idList.size();
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting user count for role id : " + id, e);
			throw new ApplicationException(10004, "Exception occurred while getting user count for role id " + id, e);
		}
	}

	/**
	 * Populates role entity from dto
	 * 
	 * @param dto
	 * @return
	 */
	private Role populateRoleFromDTO(Role role, RoleDTO dto) {

		role.setName(dto.getName());
		role.setDescription(dto.getDescription());
		role.setOtpEnabed(dto.isOtpActivate());

		role.setVersion(dto.getVersion());
		// role.setEndDate(formatDate(dto.getEndDate()));

		List<Jurisdiction> jurisdictionList = new ArrayList<Jurisdiction>();
		for (long id : dto.getJurisdictions()) {
			Jurisdiction jurisdiction = jurisdictionRepository.findOne(id);
			jurisdictionList.add(jurisdiction);
		}
		role.getJurisdictions().clear();
		role.getJurisdictions().addAll(jurisdictionList);

		List<Assignee> assigneeList = new ArrayList<Assignee>();
		for (long id : dto.getAssignees()) {
			Assignee assignee = assigneeRepository.findOne(id);
			assigneeList.add(assignee);
		}
		role.getAssignees().clear();
		role.getAssignees().addAll(assigneeList);

		List<Customer> customerList = new ArrayList<Customer>();
		for (long id : dto.getCustomers()) {
			Customer customer = customerRepository.findOne(id);
			customerList.add(customer);
		}
		role.getCustomers().clear();
		role.getCustomers().addAll(customerList);

		List<TechnologyGroup> technologyGroupList = new ArrayList<TechnologyGroup>();
		for (long id : dto.getTechnologyGroups()) {
			TechnologyGroup technologyGroup = technologyGroupRepository.findOne(id);
			technologyGroupList.add(technologyGroup);
		}
		role.getTechnologyGroups().clear();
		role.getTechnologyGroups().addAll(technologyGroupList);

		List<Organization> organizationList = new ArrayList<Organization>();
		for (long id : dto.getOrganizations()) {
			Organization organizationGroup = organizationRepository.findOne(id);
			organizationList.add(organizationGroup);
		}
		role.getOrganizations().clear();
		role.getOrganizations().addAll(organizationList);

		AccessProfile accessProfile = accessProfileRepository.findOne(dto.getAccessProfileId());
		role.setAccessProfile(accessProfile);

		return role;
	}

	/**
	 * Populated dto from role entity
	 * 
	 * @param role
	 * @return
	 */
	private RoleDTO populateDTOFromRole(Role role) {
		RoleDTO dto = new RoleDTO();
		dto.setId(role.getId());
		dto.setName(role.getName());
		dto.setDescription(role.getDescription());
		dto.setUserCount(getUserCount(role.getId()));
		dto.setOtpActivate(role.isOtpEnabed());
		dto.setStatus(converStatus(role.isActive()));
		dto.setEndDate(formatDate(role.getEndDate()));
		dto.setCreatedBy(userRepository.getEmailId(role.getCreatedByUser()));
		dto.setCreatedDate(convertCalendar(role.getCreatedDate()));
		dto.setSeeded(role.isSeeded());
		dto.setVersion(role.getVersion());

		// if role is associated to a profile
		if (role.getAccessProfile() != null) {
			AccessProfileDTO apdto = new AccessProfileDTO();
			apdto.setId(role.getAccessProfile().getId());
			apdto.setName(role.getAccessProfile().getName());
			dto.setAccessProfileId(role.getAccessProfile().getId());
			dto.setAccessProfile(apdto);
			
			List<Long> accessControlIds = new ArrayList<>();
			
			for (Permission permission : role.getAccessProfile().getPermissions()) {
				accessControlIds.add(permission.getAccessControlId()); 
			}
			
			apdto.setAccessControlIds(accessControlIds);
		}

		populateJurisdictions(dto, role);
		populateAssignees(dto, role);
		populateCustomers(dto, role);
		populateTechnologyGroups(dto, role);
		populateOrganizations(dto, role);

		return dto;
	}

	/**
	 * Populates organizations list
	 * 
	 * @param dto
	 * @param role
	 */
	private void populateOrganizations(RoleDTO dto, Role role) {
		List<OrganizationDTO> odtoList = new ArrayList<OrganizationDTO>();
		Set<Organization> uniqueOrganizations = new HashSet<Organization>(role.getOrganizations());
		long[] odtoIdList = new long[uniqueOrganizations.size()];
		int i = 0;
		for (Organization organization : uniqueOrganizations) {
			OrganizationDTO odto = new OrganizationDTO();
			odto.setId(organization.getId());
			odto.setName(organization.getName());
//			odto.setType(organization.getType());// FIXME
			odtoList.add(odto);
			odtoIdList[i] = organization.getId();
			i++;
		}
		dto.setOrganizations(odtoIdList);
		dto.setOrganizationList(odtoList);
	}

	/**
	 * Populates TechnologyGroupDTO list
	 * 
	 * @param dto
	 * @param role
	 */
	private void populateTechnologyGroups(RoleDTO dto, Role role) {
		List<TechnologyGroupDTO> tdtoList = new ArrayList<TechnologyGroupDTO>();
		Set<TechnologyGroup> uniqueTechnologyGroups = new HashSet<TechnologyGroup>(role.getTechnologyGroups());
		long[] tdtoIdList = new long[uniqueTechnologyGroups.size()];
		int i = 0;
		for (TechnologyGroup technologyGroup : uniqueTechnologyGroups) {
			TechnologyGroupDTO tdto = new TechnologyGroupDTO();
			tdto.setId(technologyGroup.getId());
			tdto.setName(technologyGroup.getName());
			tdtoList.add(tdto);
			tdtoIdList[i] = technologyGroup.getId();
			i++;
		}
		dto.setTechnologyGroups(tdtoIdList);
		dto.setTechnologyGroupList(tdtoList);

	}

	/**
	 * Populates CustomerDTO list
	 * 
	 * @param dto
	 * @param role
	 */
	private void populateCustomers(RoleDTO dto, Role role) {
		List<CustomerDTO> cdtoList = new ArrayList<CustomerDTO>();
		Set<Customer> uniqueCustomers = new HashSet<Customer>(role.getCustomers());
		long[] cdtoIdList = new long[uniqueCustomers.size()];
		int i = 0;
		for (Customer customer : uniqueCustomers) {
			CustomerDTO cdto = new CustomerDTO();
			cdto.setId(customer.getId());
			cdto.setNumber(customer.getCustomerNumber());
			cdtoList.add(cdto);
			cdtoIdList[i] = customer.getId();
			i++;
		}
		dto.setCustomers(cdtoIdList);
		dto.setCustomerList(cdtoList);

	}

	/**
	 * Populates AssigneeDTO list
	 * 
	 * @param dto
	 * @param role
	 */
	private void populateAssignees(RoleDTO dto, Role role) {
		List<AssigneeDTO> adtoList = new ArrayList<AssigneeDTO>();
		Set<Assignee> uniqueAssignees = new HashSet<Assignee>(role.getAssignees());
		long[] adtoIdList = new long[uniqueAssignees.size()];
		int i = 0;
		for (Assignee assignee : uniqueAssignees) {
			AssigneeDTO adto = new AssigneeDTO();
			adto.setId(assignee.getId());
			adto.setName(assignee.getName());
			adtoList.add(adto);
			adtoIdList[i] = assignee.getId();
			i++;
		}
		dto.setAssignees(adtoIdList);
		dto.setAssigneeList(adtoList);

	}

	/**
	 * Populates JurisdictionDTO list
	 * 
	 * @param dto
	 * @param role
	 */
	private void populateJurisdictions(RoleDTO dto, Role role) {
		List<JurisdictionDTO> jdtoList = new ArrayList<JurisdictionDTO>();
		Set<Jurisdiction> uniqueJurisdictions = new HashSet<Jurisdiction>(role.getJurisdictions());
		long[] jdtoIdList = new long[uniqueJurisdictions.size()];
		int i = 0;
		for (Jurisdiction jurisdiction : uniqueJurisdictions) {
			JurisdictionDTO adto = new JurisdictionDTO();
			adto.setId(jurisdiction.getId());
			adto.setName(jurisdiction.getCode());
			jdtoList.add(adto);
			jdtoIdList[i] = jurisdiction.getId();
			i++;
		}
		dto.setJurisdictions(jdtoIdList);
		dto.setJurisdictionList(jdtoList);

	}

	/**
	 * Converts boolean to string
	 * 
	 * @param flag
	 * @return
	 */
	private String converStatus(boolean flag) {
		return flag ? "Active" : "Inactive";
	}

	/**
	 * Formats date to string
	 * 
	 * @param date
	 * @return
	 */
	private String formatDate(Calendar calendar) {
		if (calendar != null) {
			return format.format(calendar.getTime());
		} else {
			return "";
		}
	}

	@Override
	@Transactional
	public List<JurisdictionDTO> getAllJurisdiction() throws ApplicationException {
		List<JurisdictionDTO> jurisdictionDtos = new ArrayList<>();

		try {
			List<Jurisdiction> jurisdictions = jurisdictionRepository.findAll();

			if (CollectionUtils.isNotEmpty(jurisdictions)) {
				for (Jurisdiction jurisdiction : jurisdictions) {
					JurisdictionDTO jurisdictionDTO = new JurisdictionDTO();
					jurisdictionDTO.setId(jurisdiction.getId());
					jurisdictionDTO.setName(jurisdiction.getCode());
					jurisdictionDtos.add(jurisdictionDTO);
				}
			}

		} catch (DataAccessException exception) {
			log.error("Error occurred while fetching jurisdiction");
			throw new ApplicationException(exception);
		}

		return jurisdictionDtos;
	}

	@Override
	@Transactional
	public List<AssigneeDTO> getAllAssignee() throws ApplicationException {
		List<AssigneeDTO> assigneeDtos = new ArrayList<>();

		try {
			List<Assignee> assignees = assigneeRepository.findAll();

			if (CollectionUtils.isNotEmpty(assignees)) {
				for (Assignee assignee : assignees) {
					AssigneeDTO assigneeDTO = new AssigneeDTO();
					assigneeDTO.setId(assignee.getId());
					assigneeDTO.setName(assignee.getName());
					assigneeDtos.add(assigneeDTO);
				}
			}
		} catch (DataAccessException exception) {
			log.error("Error occurred while fetching assignee");
			throw new ApplicationException(exception);
		}
		return assigneeDtos;
	}

	@Override
	@Transactional
	public List<CustomerDTO> getAllCustomer() throws ApplicationException {
		List<CustomerDTO> customerDTOs = new ArrayList<>();

		try {
			List<Customer> customers = customerRepository.findAll();

			if (CollectionUtils.isNotEmpty(customers)) {
				for (Customer customer : customers) {
					CustomerDTO customerDTO = new CustomerDTO();
					customerDTO.setId(customer.getId());
					customerDTO.setNumber(customer.getCustomerNumber());
					customerDTOs.add(customerDTO);
				}
			}
		} catch (DataAccessException exception) {
			log.error("Error occurred while fetching customers");
			throw new ApplicationException(exception);
		}

		return customerDTOs;
	}

	@Override
	@Transactional
	public List<TechnologyGroupDTO> getAllTechnologyGroup() throws ApplicationException {
		List<TechnologyGroupDTO> technologyGroupDTOs = new ArrayList<>();

		try {
			List<TechnologyGroup> technologyGroups = technologyGroupRepository.findAll();

			if (CollectionUtils.isNotEmpty(technologyGroups)) {
				for (TechnologyGroup technologyGroup : technologyGroups) {
					TechnologyGroupDTO technologyGroupDTO = new TechnologyGroupDTO();
					technologyGroupDTO.setId(technologyGroup.getId());
					technologyGroupDTO.setName(technologyGroup.getName());
					technologyGroupDTOs.add(technologyGroupDTO);
				}
			}
		} catch (DataAccessException exception) {
			log.error("Error occurred while fetching technology");
			throw new ApplicationException(exception);
		}

		return technologyGroupDTOs;
	}

	@Override
	@Transactional
	public List<OrganizationDTO> getAllOrganization() throws ApplicationException {
		List<OrganizationDTO> organizationDTOs = new ArrayList<>();

		try {
			List<Organization> organizations = organizationRepository.findAll();

			if (CollectionUtils.isNotEmpty(organizations)) {
				for (Organization organization : organizations) {
					OrganizationDTO organizationDTO = new OrganizationDTO();
					organizationDTO.setId(organization.getId());
					organizationDTO.setName(organization.getName());
					organizationDTOs.add(organizationDTO);
				}
			}
		} catch (DataAccessException exception) {
			log.error("Error occurred while fetching organizations");
			throw new ApplicationException(exception);
		}

		return organizationDTOs;
	}

	@Override
	@Transactional
	public List<RoleDTO> findRolesForSystem(final List<Long> juricIds, final List<Long> assigneeIds, final List<Long> techGrpIds,
			final List<Long> custmNoIds, final List<Long> organIds) {
		List<RoleDTO> allRoles = getAllRoles();
		List<RoleDTO> roles = new ArrayList<>();

		for (RoleDTO roleDto : allRoles) {
			List<Long> jurIds = new ArrayList<>();
			List<Long> assigIds = new ArrayList<>();
			List<Long> tecGrIds = new ArrayList<>();
			List<Long> CusNoIds = new ArrayList<>();
			List<Long> OrganIds = new ArrayList<>();

			for (JurisdictionDTO jurDto : roleDto.getJurisdictionList()) {
				jurIds.add(jurDto.getId());
			}
			for (AssigneeDTO assDto : roleDto.getAssigneeList()) {
				assigIds.add(assDto.getId());
			}
			for (TechnologyGroupDTO tgDto : roleDto.getTechnologyGroupList()) {
				tecGrIds.add(tgDto.getId());
			}
			for (CustomerDTO cusDto : roleDto.getCustomerList()) {
				CusNoIds.add(cusDto.getId());
			}
			for (OrganizationDTO orgDto : roleDto.getOrganizationList()) {
				OrganIds.add(orgDto.getId());
			}

			if (jurIds.containsAll(juricIds) && jurIds.containsAll(juricIds) && assigIds.containsAll(assigneeIds)
					&& tecGrIds.containsAll(techGrpIds) && CusNoIds.containsAll(custmNoIds)
					&& OrganIds.containsAll(organIds)) {
				roles.add(roleDto);
			}
		}

		return roles;
	}

	@Override
	@Transactional
	public List<RoleDTO> getAllActiveRoles() throws ApplicationException {
		List<RoleDTO> roleList = null;
		
		try {
			// exclude system generated roles
			List<Role> roles = roleRepository.findByActiveAndSystemRole(true, false);
			roleList = new ArrayList<>();

			for (Role role : roles) {
				RoleDTO dto = populateDTOFromRole(role);
				roleList.add(dto);
			}
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting roles ", e);
			throw new ApplicationException(10001, "Exception occurred while getting roles ", e);
		}
		
		return roleList;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	private String convertCalendar(Calendar date) {
		String stringDate = null;
		if (date != null) {
			stringDate = formatter.format(date.getTime());
		}
		return stringDate;
	}
}
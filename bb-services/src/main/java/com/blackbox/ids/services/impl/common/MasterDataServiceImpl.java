package com.blackbox.ids.services.impl.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.mstr.AssigneeDAO;
import com.blackbox.ids.core.dao.mstr.CustomerDAO;
import com.blackbox.ids.core.dao.mstr.JurisdictionDAO;
import com.blackbox.ids.core.dao.mstr.OrganizationDAO;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Assignee.Entity;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.model.mstr.AssigneeAttorneyDocketNo;
import com.blackbox.ids.core.model.mstr.AttorneyDocketFormat;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.mstr.Organization;
import com.blackbox.ids.core.model.mstr.TechnologyGroup;
import com.blackbox.ids.core.model.usermanagement.AccessControl;
import com.blackbox.ids.core.model.usermanagement.Module;
import com.blackbox.ids.core.repository.AssigneeRepository;
import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.core.repository.OrganizationRepository;
import com.blackbox.ids.core.repository.TechnologyGroupRepository;
import com.blackbox.ids.core.repository.usermanagement.ModuleRepository;
import com.blackbox.ids.dto.AssigneeDTO;
import com.blackbox.ids.dto.ClientDTO;
import com.blackbox.ids.dto.CustomerDTO;
import com.blackbox.ids.dto.JurisdictionDTO;
import com.blackbox.ids.dto.OrganizationDTO;
import com.blackbox.ids.dto.TechnologyGroupDTO;
import com.blackbox.ids.dto.usermanagement.AccessControlDTO;
import com.blackbox.ids.dto.usermanagement.ModuleDTO;
import com.blackbox.ids.services.common.MasterDataService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;

/**
 * Handles and populates reference master data related to application
 *
 * @author Nagarro
 */
@Service("masterDataService")
@Cacheable
public class MasterDataServiceImpl implements MasterDataService {

	private Logger log = Logger.getLogger(MasterDataServiceImpl.class);

	@Autowired
	JurisdictionRepository jurisdictionRepository;

	@Autowired
	private AssigneeRepository assigneeRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	TechnologyGroupRepository technologyGroupRepository;

	@Autowired
	OrganizationRepository organizationRepository;

	@Autowired
	ModuleRepository moduleRepository;

	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;

	@Autowired
	private AssigneeDAO assigneeDAO;

	@Autowired
	private JurisdictionDAO jurisdictionDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private OrganizationDAO organizationDAO;

	@Autowired
	private NotificationProcessService notificationProcessService;

	@Override
	@Transactional
	public List<JurisdictionDTO> getAllJurisdictions() throws ApplicationException {
		List<JurisdictionDTO> jurisDtoList;
		try {
			List<Jurisdiction> juris = jurisdictionRepository.findAll();
			jurisDtoList = populateJurisdictions(juris);
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting jurisdictions ", e);
			throw new ApplicationException(10001, "Exception occurred while getting jurisdictions ", e);
		}
		return jurisDtoList;
	}

	private List<JurisdictionDTO> populateJurisdictions(List<Jurisdiction> juris) {
		List<JurisdictionDTO> jdtoList = new ArrayList<JurisdictionDTO>();
		for(Jurisdiction jurisdiction : juris) {
			JurisdictionDTO dto = new JurisdictionDTO();
			dto.setId(jurisdiction.getId());
			dto.setName(jurisdiction.getCode());
			jdtoList.add(dto);
		}
		return jdtoList;
	}

	@Override
	@Transactional
	public List<AssigneeDTO> getAllAssignees() throws ApplicationException {
		try {
			List<Assignee> assignees = assigneeRepository.findAll();
			List<AssigneeDTO> assigneeDtoList= populateAssignees(assignees);
			return assigneeDtoList;
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting assignees ", e);
			throw new ApplicationException(10001, "Exception occurred while getting assignees ", e);
		}
	}

	private List<AssigneeDTO> populateAssignees(List<Assignee> assignees) {
		List<AssigneeDTO> adtoList = new ArrayList<AssigneeDTO>();
		for(Assignee assignee : assignees) {
			AssigneeDTO dto = new AssigneeDTO();
			dto.setId(assignee.getId());
			dto.setName(assignee.getName());
			dto.setDescription(assignee.getDescription());
			ClientDTO cdto = new ClientDTO();
			// FIXME: MDM
			//    		cdto.setId(assignee.getClient().getId());
			//    		cdto.setDescription(assignee.getClient().getClientType().name());
			dto.setClient(cdto);
			adtoList.add(dto);
		}
		return adtoList;
	}

	@Override
	@Transactional
	public List<CustomerDTO> getAllCustomers() throws ApplicationException {
		try {
			List<Customer> customers = customerRepository.findAll();
			List<CustomerDTO> customerDtoList = populateCustomers(customers);
			return customerDtoList;
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting customers ", e);
			throw new ApplicationException(10001, "Exception occurred while getting customers ", e);
		}
	}

	private List<CustomerDTO> populateCustomers(List<Customer> customers) {
		List<CustomerDTO> cdtoList = new ArrayList<CustomerDTO>();
		for(Customer customer : customers) {
			CustomerDTO dto = new CustomerDTO();
			dto.setId(customer.getId());
			dto.setNumber(customer.getCustomerNumber());
			cdtoList.add(dto);
		}
		return cdtoList;
	}

	@Override
	@Transactional
	public List<TechnologyGroupDTO> getAllTechnologyGroups() throws ApplicationException {
		try {
			List<TechnologyGroup> technologyGroups = technologyGroupRepository.findAll();
			List<TechnologyGroupDTO> technologyGroupDtoList= populaTetechnologyGroups(technologyGroups);
			return technologyGroupDtoList;
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting technology groups ", e);
			throw new ApplicationException(10001, "Exception occurred while getting technology groups ", e);
		}
	}

	private List<TechnologyGroupDTO> populaTetechnologyGroups(List<TechnologyGroup> technologyGroups) {
		List<TechnologyGroupDTO> tdtoList = new ArrayList<TechnologyGroupDTO>();
		for(TechnologyGroup techGroup : technologyGroups) {
			TechnologyGroupDTO dto = new TechnologyGroupDTO();
			dto.setId(techGroup.getId());
			dto.setName(techGroup.getName());
			dto.setDescription(techGroup.getDescription());
			ClientDTO cdto = new ClientDTO();
			// FIXME:
			//cdto.setId(techGroup.getClient().getId());
			//cdto.setDescription(techGroup.getClient().getClientType().name());
			dto.setClient(cdto);
			tdtoList.add(dto);
		}
		return tdtoList;
	}

	@Override
	@Transactional
	public List<OrganizationDTO> getAllOrganizations() throws ApplicationException {
		try {
			List<Organization> organizations = organizationRepository.findAll();
			List<OrganizationDTO> organizationDtoList= populateOrganizations(organizations);
			return organizationDtoList;
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting organizations ", e);
			throw new ApplicationException(10001, "Exception occurred while getting organizations ", e);
		}
	}

	private List<OrganizationDTO> populateOrganizations(List<Organization> organizations) {
		List<OrganizationDTO> odtoList = new ArrayList<OrganizationDTO>();
		for(Organization organization : organizations) {
			OrganizationDTO dto = new OrganizationDTO();
			dto.setId(organization.getId());
			dto.setName(organization.getName());
			//    		dto.setType(organization.getType());// FIXME
			odtoList.add(dto);
		}
		return odtoList;
	}

	@Override
	@Transactional
	public List<ModuleDTO> getAllModules() throws ApplicationException {
		try {
			List<Module> modules = moduleRepository.findAll();
			List<ModuleDTO> mdtoList= populateModules(modules);
			return mdtoList;
		} catch (DataAccessException e) {
			log.debug("Exception occurred while getting Modules ", e);
			throw new ApplicationException(10001, "Exception occurred while getting Modules ", e);
		}
	}

	private List<ModuleDTO> populateModules(List<Module> modules) {
		List<ModuleDTO> mdtoList = new ArrayList<ModuleDTO>();

		for(Module module : modules) {
			ModuleDTO mdto = new ModuleDTO();
			mdto.setId(module.getId());
			mdto.setCode(module.getModuleCode());
			mdto.setName(module.getModuleName());
			mdto.setDescription(module.getModuleDescription());

			for (AccessControl accessControl : module.getAccessControls()) {
				AccessControlDTO acDto = new AccessControlDTO();
				acDto.setAccessControlId(accessControl.getAccessControlId());
				acDto.setActionType(accessControl.isActionType());
				acDto.setName(accessControl.getName());
				mdto.getAccessControlDtos().add(acDto);
			}

			mdtoList.add(mdto);
		}
		return mdtoList;
	}

	/*- APIs to fetch data as per the logged in users data access rights. */
	@Override
	public List<Jurisdiction> getUserJurisdictions() {
		final Set<Long> userJurisdictions = BlackboxSecurityContextHolder.getUserAuthData().getJurisdictionIds();
		return CollectionUtils.isEmpty(userJurisdictions)
				? Collections.emptyList()
						: jurisdictionRepository.fetchJurisdictionsWithIdsIn(userJurisdictions);
	}

	@Override
	public List<String> getUserAssignees() {
		final Set<Long> userAssignees = BlackboxSecurityContextHolder.getUserAuthData().getAssigneeIds();
		return CollectionUtils.isEmpty(userAssignees)
				? Collections.emptyList()
						: assigneeRepository.findNamesByIdsIn(userAssignees);
	}

	@Override
	public List<String> getUserCustomerNumbers() {
		final Set<Long> customerIds = BlackboxSecurityContextHolder.getUserAuthData().getCustomerIds();
		return CollectionUtils.isEmpty(customerIds) ? Collections.emptyList()
				: customerDAO.findCustomerNumbersByIdsIn(customerIds);
	}

	@Override
	@Transactional
	public long updateAssignee(String familyId, String strAssignee,long notificationId) {
		if (StringUtils.isEmpty(familyId) || StringUtils.isEmpty(strAssignee)) {
			throw new IllegalArgumentException("[Application by Number]: Missing required parameters.");
		}
		notificationProcessService.completeTask(notificationId, NotificationStatus.COMPLETED);
		log.info(String.format("[Notification]: Successfully updated notification for {0}.",notificationId));
		return applicationBaseDAO.updateAssignee(familyId, strAssignee);
	}

	/*- APIs to map entity identifier codes with their DB Ids */
	@Override
	public Map<String, Long> mapAssigneeNameIds(Collection<String> assigneeNames) {
		if (CollectionUtils.isEmpty(assigneeNames)) {
			throw new IllegalArgumentException("Missing required fields.");
		}
		return assigneeDAO.mapAssigneeNameIds(assigneeNames);
	}

	@Override
	public Map<String, Long> mapCustomerNameIds(Collection<String> customerNumbers) {
		if (CollectionUtils.isEmpty(customerNumbers)) {
			throw new IllegalArgumentException("Missing required fields.");
		}
		return customerDAO.mapCustomerNameIds(customerNumbers);
	}

	@Override
	public Map<String, Long> mapJurisdictionNameIds(Collection<String> jurisdictionCodes) {
		if (CollectionUtils.isEmpty(jurisdictionCodes)) {
			throw new IllegalArgumentException("Missing required fields.");
		}
		return jurisdictionDAO.mapJurisdictionNameIds(jurisdictionCodes);
	}

	@Override
	public Map<String, Long> mapOrganizationNameIds(Collection<String> organizationNames) {
		if (CollectionUtils.isEmpty(organizationNames)) {
			throw new IllegalArgumentException("Missing required fields.");
		}
		return organizationDAO.mapOrganizationNameIds(organizationNames);
	}

	@Override
	public List<String> getAllEntity() throws ApplicationException {
		// TODO Auto-generated method stub
		List<String> entities = Arrays.stream(Entity.values()).map(Enum :: name).collect(Collectors.toList());
		return entities;
	}

	@Override
	public boolean isValidJurisdiction(final String jurisdictionCode) {
		if (StringUtils.isBlank(jurisdictionCode)) {
			throw new IllegalArgumentException("Jurisdiction code mustn't be blank.");
		}
		return jurisdictionDAO.getIdByCode(jurisdictionCode) != null;
	}

	@Override
	public AttorneyDocketFormat getAttorneyDocketFormat() {
		return applicationBaseDAO.getAttorneyDocketFormat();
	}

	@Override
	public String getAttorneyDocketSeperator() {
		return applicationBaseDAO.getAttorneyDocketSeperator();
	}

	@Override
	public List<AssigneeAttorneyDocketNo> getAssigneeAttorneyDocketNo(String segmentValue) {
		return applicationBaseDAO.getAssigneeAttorneyDocketNo(segmentValue);
	}

}
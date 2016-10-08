package com.blackbox.ids.core.cache;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.repository.AssigneeRepository;
import com.blackbox.ids.core.repository.CustomerRepository;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.core.repository.OrganizationRepository;
import com.blackbox.ids.core.repository.PersonClassificationRepository;
import com.blackbox.ids.core.repository.RoleRepository;
import com.blackbox.ids.core.repository.TechnologyGroupRepository;
import com.blackbox.ids.core.repository.usermanagement.AccessProfileRepository;
import com.blackbox.ids.core.repository.usermanagement.ModuleRepository;
import com.blackbox.ids.core.repository.usermanagement.PermissionRepository;


public class DefaultEnityCacheLoader implements EntityCacheLoader {

	@Autowired RoleRepository roleRepository;
	@Autowired AccessProfileRepository accessProfileRepository;
	@Autowired JurisdictionRepository jurisdictionRepository;
	@Autowired AssigneeRepository assigneeRepository;
	@Autowired CustomerRepository customerRepository;
	@Autowired TechnologyGroupRepository technologyGroupRepository;
	@Autowired OrganizationRepository organizationRepository;
	@Autowired PermissionRepository permissionRepository;
	@Autowired ModuleRepository moduleRepository;
	@Autowired PersonClassificationRepository personClassificationRepository;

	boolean isRunning = false;

	@Override
	public void load() {
		/*-isRunning = true;
		List<Role> roles = roleRepository.findAll();
		for (Role role : roles) {
			roleRepository.findOne(role.getId());
		}

		List<AccessProfile> accessProfiles = accessProfileRepository.findAll();
		for (AccessProfile accessProfile : accessProfiles) {
			accessProfileRepository.findOne(accessProfile.getId());
		}

		List<Jurisdiction> jurisdictions = jurisdictionRepository.findAll();
		for (Jurisdiction jurisdiction : jurisdictions) {
			jurisdictionRepository.findOne(jurisdiction.getId());
		}

		List<Assignee> assignees = assigneeRepository.findAll();
		for (Assignee assignee : assignees) {
			assigneeRepository.findOne(assignee.getId());
		}

		List<Customer> customers = customerRepository.findAll();
		for (Customer customer : customers) {
			customerRepository.findOne(customer.getId());
		}


		List<TechnologyGroup> technologyGroups = technologyGroupRepository.findAll();
		for (TechnologyGroup technologyGroup : technologyGroups) {
			technologyGroupRepository.findOne(technologyGroup.getId());
		}


		List<Office> organizations = organizationRepository.findAll();
		for (Office organization : organizations) {
			organizationRepository.findOne(organization.getId());
		}


		List<Permission> permissions = permissionRepository.findAll();
		for (Permission permission : permissions) {
			permissionRepository.findOne(permission.getId());
		}


		List<Module> modules = moduleRepository.findAll();
		for (Module module : modules) {
			moduleRepository.findOne(module.getId());
		}

		List<PersonClassification> personClassifications = personClassificationRepository.findAll();
		for (PersonClassification personClassification : personClassifications) {
			personClassificationRepository.findOne(personClassification.getId());
		}*/
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {

	}

	@Override
	public void start() {
		load();
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public int getPhase() {
		return 0;
	}
}

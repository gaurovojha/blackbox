package com.blackbox.ids.ui.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.blackbox.ids.dto.RoleDTO;
import com.blackbox.ids.services.usermanagement.RoleService;

/**
 * @author Nagarro
 *
 */
@Component("roleFormValidator")
public class RoleFormValidator implements Validator {

	@Autowired
	private RoleService roleService;

	private final static String SELECT_MESSAGE = "[Please select at least one ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return RoleDTO.class.equals(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object obj, Errors errors) {

		RoleDTO dto = (RoleDTO) obj;

		List<String> roleNames = roleService.getAllRoleNames();

		StringBuilder dataAcccessErrors = new StringBuilder();
		;

		if (roleNames.contains(dto.getName()) && (dto.getId() == null || dto.getId() == 0)) {
			errors.rejectValue("name", "roleform.rolename.exist");
		} else {
			if (dto.getId() != null) {
				RoleDTO roleDto = roleService.getRoleById(dto.getId());
				if (roleDto != null && !roleDto.getName().equalsIgnoreCase(dto.getName())
						&& containsIgnoreCase(dto.getName(), roleNames)) {
					errors.rejectValue("name", "roleform.rolename.exist");
				}
			}
		}

		if (dto.getAccessProfileId() == 0) {
			errors.rejectValue("accessProfileId", "roleform.accessprofile.required");
		}

		if (dto.getJurisdictions().length == 0) {
			dataAcccessErrors.append(SELECT_MESSAGE);
			dataAcccessErrors.append("Jurisdiction");
			dataAcccessErrors.append("] ");
			errors.rejectValue("jurisdictions", "roleform.jurisdiction.required");
		}

		if (dto.getAssignees().length == 0) {
			dataAcccessErrors.append(SELECT_MESSAGE);
			dataAcccessErrors.append("Assignee");
			dataAcccessErrors.append("] ");
			errors.rejectValue("assignees", "roleform.assignee.required");
		}

		if (dto.getCustomers().length == 0) {
			dataAcccessErrors.append(SELECT_MESSAGE);
			dataAcccessErrors.append("Customer");
			dataAcccessErrors.append("] ");
			errors.rejectValue("customers", "roleform.customer.required");
		}

		if (dto.getTechnologyGroups().length == 0) {
			dataAcccessErrors.append(SELECT_MESSAGE);
			dataAcccessErrors.append("Technology Group");
			dataAcccessErrors.append("] ");
			errors.rejectValue("technologyGroups", "roleform.technologygroup.required");
		}

		if (dto.getOrganizations().length == 0) {
			dataAcccessErrors.append(SELECT_MESSAGE);
			dataAcccessErrors.append("Organization");
			dataAcccessErrors.append("] ");
			errors.rejectValue("organizations", "roleform.organization.required");
		}

		if (dto.getDescription().length() > 20) {
			errors.rejectValue("description", "roleform.description.size");
		}

		if (!dataAcccessErrors.toString().isEmpty()) {
			errors.rejectValue("dataAccessErrors", "", dataAcccessErrors.toString());
		}

		dto.setDataAccessErrors(dataAcccessErrors.toString());
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "roleform.name.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "roleform.description.required");
	}

	/**
	 * Compares string in list with ignore case
	 * 
	 * @param roleName
	 * @param listRoleNames
	 * @return
	 */
	protected boolean containsIgnoreCase(String roleName, List<String> list) {
		for (String name : list) {
			if (name.equalsIgnoreCase(roleName))
				return true;
		}
		return false;
	}

}
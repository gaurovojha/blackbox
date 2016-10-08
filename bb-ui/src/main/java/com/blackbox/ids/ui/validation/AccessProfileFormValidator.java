package com.blackbox.ids.ui.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.blackbox.ids.dto.usermanagement.AccessProfileDTO;
import com.blackbox.ids.services.usermanagement.AccessProfileService;

/**
 * @author Nagarro
 *
 */
@Component("accessProfileFormValidator")
public class AccessProfileFormValidator implements Validator {

	@Autowired
	AccessProfileService accessProfileService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return AccessProfileDTO.class.equals(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object obj, Errors errors) {

		AccessProfileDTO dto = (AccessProfileDTO) obj;

		List<String> accessProfileNames = accessProfileService.getAllAccessProfileNames();

		// check for duplicate profile name
		if (accessProfileNames.contains(dto.getName()) && (dto.getId() == null || dto.getId() == 0)) {
			errors.rejectValue("name", "accessprofileform.name.exist");
		} else {
			if (dto.getId() != null) {
				AccessProfileDTO apDto = accessProfileService.getAccessProfileById(dto.getId());
				if (apDto != null && !apDto.getName().equalsIgnoreCase(dto.getName())
						&& containsIgnoreCase(dto.getName(), accessProfileNames)) {
					errors.rejectValue("name", "accessprofileform.name.exist");
				}
			}
		}

		if (dto.getDescription().length() > 20) {
			errors.rejectValue("description", "accessprofileform.description.size");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "accessprofileform.name.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "accessprofileform.description.required");
	}

	/**
	 * Compares string in list with ignore case
	 * 
	 * @param roleName
	 * @param listRoleNames
	 * @return
	 */
	protected boolean containsIgnoreCase(String apName, List<String> list) {
		for (String name : list) {
			if (name.equalsIgnoreCase(apName))
				return true;
		}
		return false;
	}

}

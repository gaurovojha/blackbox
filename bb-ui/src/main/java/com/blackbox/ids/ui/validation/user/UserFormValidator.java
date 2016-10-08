package com.blackbox.ids.ui.validation.user;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.blackbox.ids.core.model.mstr.PersonClassification;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.ui.form.UserDetailsForm;
import com.blackbox.ids.ui.form.UserDetailsForm.Action;
import com.blackbox.ids.ui.validation.EmailValidator;
import com.blackbox.ids.ui.validation.common.ValidationConstant;
import com.blackbox.ids.ui.validation.common.ValidationProp;
import com.blackbox.ids.ui.validation.common.ValidationUtil;

@Component
public class UserFormValidator implements Validator {

	@Autowired
	EmailValidator emailValidator;

	@Autowired
	com.blackbox.ids.services.usermanagement.UserService userService;

	/**
	 * Checks whether {@link UserFormValidator this} validator can validate the instance of specified class.
	 *
	 * @param clazz
	 *            the Class that this Validator is being asked if it can validate.
	 * @return <code>true</code>, if this Validator can indeed validate instances of the supplied clazz;<br/>
	 *         <code>false</code>, otherwise.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return UserDetailsForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		final UserDetailsForm user = (UserDetailsForm) target;
		user.setAction((user.getId() == null) ? Action.CREATE_USER : Action.UPDATE_USER);
		ValidationProp prop = new ValidationProp() {};

		// firstName: Mandatory, Alphabets with dot and hyphen.
		ValidationUtil.validateString(prop, errors, user.getFirstName(), "firstName", "userForm.firstName",
				true, ValidationConstant.REGEX_NAME, null);

		// middleName: Non-mandatory.
		ValidationUtil.validateString(prop, errors, user.getMiddleName(), "middleName", "userForm.middleName",
				false, ValidationConstant.REGEX_NAME, null);

		// lastName: Mandatory, Alphabets with dot and hyphen
		ValidationUtil.validateString(prop, errors, user.getLastName(), "lastName", "userForm.lastName",
				true, ValidationConstant.REGEX_NAME, null);

		// userType: Validate end date for temporary users.
		final Long userType = (user.getUserType() == null) ? null : user.getUserType().getId();
		if (userType == null) {
			errors.rejectValue("userType", "userForm.notempty.userType");

		} else if (PersonClassification.Code.TEMP.name().equals(userService.getUserType(userType).getName())
				&& StringUtils.isBlank(user.getEndingOn())) {
			errors.rejectValue("endingOn", "userForm.notempty.endingOn");
		}

		// userRoles: must be assigned atleast 1 role.
		if (CollectionUtils.isEmpty(user.getRoleIds())) {
			errors.rejectValue("roleIds", "error.empty.userForm.accessRole");
		}

		// emailId: mandatory, with proper format.
		final String pathEmail = "emailId";
		ValidationUtil.validateString(prop, errors, user.getEmailId(), "emailId", "userForm.emailId", true,
				ValidationConstant.REGEX_EMAIL, null);

		// emailId: validate uniqueness only if given email is properly formatted.
		if (!errors.hasFieldErrors(pathEmail)) {
			final Long emailUser = userService.getUserIdByEmailId(user.getEmailId());
			if ((UserDetailsForm.Action.CREATE_USER.equals(user.getAction()) && emailUser != null) ||
					(emailUser != null && emailUser != user.getId())) {
				errors.rejectValue(pathEmail, "userForm.notempty.conditional.emailId");
			}
		}

		// endingOn: must be a future date.
		if (UserDetailsForm.Action.CREATE_USER.equals(user.getAction()) || UserDetailsForm.Action.UPDATE_USER.equals(user.getAction())){
			try{
				if (PersonClassification.Code.TEMP.name().equals(userService.getUserType(userType).getName())){
					if (StringUtils.isBlank(user.getEndingOn()) || BlackboxDateUtil.isPastDate(BlackboxDateUtil.strToDate(user.getEndingOn(), UserDetailsForm.DATE_FORMAT))) {
						errors.rejectValue("endingOn", "userForm.conditional.endingOn.futureDate");
					}
				}else if (!StringUtils.isBlank(user.getEndingOn()) && BlackboxDateUtil.isPastDate(BlackboxDateUtil.strToDate(user.getEndingOn(), UserDetailsForm.DATE_FORMAT))) {
					errors.rejectValue("endingOn", "userForm.conditional.endingOn.futureDate");
				}
			} catch(Exception e) {
					errors.rejectValue("endingOn", "userForm.conditional.endingOn.invalidDate");
		}

		}
	}
}
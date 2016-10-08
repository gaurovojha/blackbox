package com.blackbox.ids.ui.validation;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.dto.UserDTO;
import com.blackbox.ids.services.usermanagement.UserService;
import com.blackbox.ids.ui.common.Constants;

@Component
public class ChangePasswordFormValidator implements Validator {

	private static final Logger LOGGER = Logger.getLogger(ChangePasswordFormValidator.class);

	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return UserDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LOGGER.info("Validating the User DTO for Change Password Feature.");
		if (target instanceof UserDTO) {
			UserDTO userDTO = (UserDTO) target;

			String password = userDTO.getPassword();
			String newPassword = userDTO.getNewPassword();
			String confirmPassword = userDTO.getConfirmPassword();
			boolean isValid = true;
			if (StringUtils.isEmpty(password)) {
				errors.rejectValue(Constants.PASSWORD, "NotEmpty.changePasswordForm.password");
				isValid = false;
			}
			if (StringUtils.isEmpty(newPassword)) {
				errors.rejectValue(Constants.NEW_PASSWORD, "NotEmpty.changePasswordForm.newPassword");
				isValid = false;
			}
			if (StringUtils.isEmpty(confirmPassword)) {
				errors.rejectValue(Constants.CONFIRM_PASSWORD, "NotEmpty.changePasswordForm.confirmPassword");
				isValid = false;
			}

			if (isValid) {
				try {
					isValid = userService.validatePassword(password);
					if (!isValid) {
						errors.rejectValue(Constants.PASSWORD, "PasswordMatch.changePasswordForm.password");
					}
				} catch (ApplicationException exception) {
					LOGGER.error("Error occured on validate Password", exception);
					errors.rejectValue(Constants.PASSWORD, "PasswordMatch.changePasswordForm.password");
					isValid = false;
				}
			}

			if (isValid && newPassword != null && !validatePasswordFormat(newPassword)) {
				errors.rejectValue(Constants.NEW_PASSWORD, "PasswordFormat.changePasswordForm.newPassword");
				isValid = false;
			}
			if (isValid && !StringUtils.isEmpty(newPassword) && !StringUtils.isEmpty(confirmPassword)
					&& !newPassword.equals(confirmPassword)) {
				errors.rejectValue(Constants.CONFIRM_PASSWORD, "ConfirmPasswordMatch.changePasswordForm.newPassword");
				isValid = false;
			}
		}
	}

	private static boolean validatePasswordFormat(String password) {
		Pattern letter = Pattern.compile("[A-Z]");
		Pattern digit = Pattern.compile("[0-9]");
		Pattern specialCharacter = Pattern.compile("[^A-Za-z0-9]");

		boolean isValidated = true;
		if (StringUtils.isEmpty(password)) {
			isValidated = false;
		} else {
			if (password.length() < 8 || !specialCharacter.matcher(password).find() || !digit.matcher(password).find()
					|| !letter.matcher(password).find()) {
				isValidated = false;
			}
		}
		return isValidated;
	}
}
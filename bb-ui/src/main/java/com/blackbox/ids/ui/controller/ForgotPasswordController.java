package com.blackbox.ids.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blackbox.ids.common.notifier.EmailNotifier;
import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.dto.UserDTO;
import com.blackbox.ids.services.usermanagement.UserService;
import com.blackbox.ids.ui.common.Constants;
import com.blackbox.ids.ui.common.Messages;

/**
 * This controller handles and retrieves the forgot password JSP page.
 * @author Nagarro
 */
@Controller
public class ForgotPasswordController {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ForgotPasswordController.class);

	/** The email notifier. */
	@Autowired
	private EmailNotifier emailNotifier;

	/** The user service. */
	@Autowired
	private UserService userService;

	/**
	 * Gets the forgot password page.
	 * @param userDTO
	 *            the user dto
	 * @return the forgot password page
	 */
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public String getForgotPasswordPage(@ModelAttribute("forgotPasswordForm") final UserDTO userDTO) {
		logger.debug("Received request to show forgot password page.");
		return "forgot";
	}

	/**
	 * Validate and set the password for forgot password functionality.
	 * @param userDTO
	 *            the user dto
	 * @param request
	 *            the request
	 * @param modelMap
	 *            the model map
	 * @param errors
	 *            the errors
	 * @return the forgot password page
	 */
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public String setForgotPassword(@ModelAttribute("forgotPasswordForm") final UserDTO userDTO,
			final HttpServletRequest request, final ModelMap modelMap, final Errors errors) {
		logger.debug("Received request to process forgot password request.");
		final String userId = request.getParameter(Constants.USER_ID);
		boolean isValid = true;
		if (StringUtils.isEmpty(userId)) {
			logger.error("Username can't be empty.");
			errors.rejectValue(Constants.USER_ID, "NotEmpty.username");
			isValid = false;
		}
		if (isValid) {
			try {
				if (userService.generateAndSendPassword(userId)) {
					modelMap.addAttribute(Constants.MESSAGE, Messages.SUCCESS_MSG_PASSWORD_SENT);
				} else {
					errors.rejectValue(Constants.USER_ID, "Invalid.username");
				}
			} catch (final ApplicationException exception) {
				logger.info(exception.getCause());
			}
		}
		return "forgot";
	}

	/**
	 * Gets the email notifier.
	 * @return the email notifier
	 */
	public EmailNotifier getEmailNotifier() {
		return emailNotifier;
	}

	/**
	 * Sets the email notifier.
	 * @param emailNotifier
	 *            the new email notifier
	 */
	public void setEmailNotifier(final EmailNotifier emailNotifier) {
		this.emailNotifier = emailNotifier;
	}
}

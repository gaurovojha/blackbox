package com.blackbox.ids.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blackbox.ids.common.constant.UIConstants;
import com.blackbox.ids.common.notifier.EmailNotifier;
import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.dto.UserDTO;
import com.blackbox.ids.services.usermanagement.UserService;
import com.blackbox.ids.ui.common.Constants;
import com.blackbox.ids.ui.common.Messages;

/**
 * This controller handles and retrieves the OTP page depending on the URI template.
 * @author Nagarro
 */
@Controller
public class OTPController {

	/** The logger. */
	protected static Logger logger = Logger.getLogger(OTPController.class);

	/** The email notifier. */
	@Autowired
	private EmailNotifier emailNotifier;

	/** The user service. */
	@Autowired
	private UserService userService;

	/**
	 * Gets the OTP page.
	 * @param userDTO
	 *            the user dto
	 * @param model
	 *            the model
	 * @return the OTP page
	 */
	@RequestMapping(value = "/otp", method = RequestMethod.GET)
	public String getOTPPage(@ModelAttribute("otpForm") final UserDTO userDTO, final Model model) {
		logger.debug("Received request to send OTP.");
		final String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		if (userId != null) {
			userDTO.setUserId(userId);
		}
		return "otp";
	}

	/**
	 * Handles the Request OTP functionality.
	 * @param userDTO
	 *            the user DTO
	 * @param redirectAttributes
	 *            the redirect attributes
	 * @param request
	 *            the request
	 * @param modelMap
	 *            the model map
	 * @return the OTP page
	 */
	@RequestMapping(value = "/otp/requestOTP", method = RequestMethod.GET)
	public String requestOTP(@ModelAttribute("otpForm") final UserDTO userDTO, final RedirectAttributes redirectAttributes,
			final HttpServletRequest request, final ModelMap modelMap) {
		logger.debug("Received request to send OTP.");
		String view = "otp";
		final String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			if (userService.sendOTP(userId)) {
				view = "otp";
				modelMap.addAttribute(Constants.MESSAGE, Messages.SUCCESS_MSG_OTP_SENT);
			}
		} catch (final ApplicationException exception) {
			logger.info(exception.getCause());
			view = "otp";
		}
		return "redirect:/" + view;
	}

	/**
	 * Validate OTP against the user OTP saved in DB.
	 * @param userDTO
	 *            the user DTO
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param errors
	 *            the errors
	 * @return the login page if otp validation successful else OTP page
	 */
	@RequestMapping(value = "/otp", method = RequestMethod.POST)
	public String validateOTP(@ModelAttribute("otpForm") final UserDTO userDTO, final HttpServletRequest request, final Model model,
			final Errors errors) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final String otp = userDTO.getOtp();
		boolean isValid = false;
		if (StringUtils.isEmpty(otp)) {
			logger.error("OTP can't be empty.");
			errors.rejectValue(Constants.OTP, "NotEmpty.otp");
		} else {
			if (userService.checkOTPValidity(authentication.getName(), otp)) {
				isValid = true;
			} else {
				logger.error("OTP don't match.");
				errors.rejectValue(Constants.OTP, "NotMatch.otp");
			}
		}
		if (isValid) {
			setFullyAuthenticatedAttributes(authentication);
			request.getSession().setAttribute(Constants.USERFULLNAME,
					userService.getUserFullName(authentication.getName()));
		} else {
			userDTO.setUserId(authentication.getName());
			return "otp";
		}
		return "redirect:" + UIConstants.LOGIN_HOME_PAGE;
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

	/**
	 * Sets the fully authenticated attributes in blackbox user.
	 * @param authentication
	 *            the new fully authenticated attributes
	 */
	private void setFullyAuthenticatedAttributes(final Authentication authentication) {
		if (authentication != null) {
			final BlackboxUser blackboxUser = (BlackboxUser) authentication.getPrincipal();
			if (blackboxUser != null) {
				blackboxUser.setAuthenticationType(Constants.FULLY_AUTHENTICATED);
				blackboxUser.setOtpSuccess(true);
			}
		}
	}

}

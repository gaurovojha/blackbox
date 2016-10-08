package com.blackbox.ids.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blackbox.ids.common.constant.UIConstants;
import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.dto.UserDTO;
import com.blackbox.ids.services.usermanagement.UserService;
import com.blackbox.ids.ui.common.Constants;

/**
 * This controller handles the change password functionality.
 * @author Nagarro
 */
@Controller
public class ChangePasswordController {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ChangePasswordController.class);

	/** The change password form validator. */
	@Autowired
	Validator changePasswordFormValidator;

	/** The user service. */
	@Autowired
	UserService userService;

	/**
	 * Binds the form validator.
	 * @param binder
	 *            the binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {

		binder.setValidator(changePasswordFormValidator);
	}

	/**
	 * Handles and retrieves the change password JSP page.
	 * @param request
	 *            the request
	 * @param model
	 *            the model
	 * @param userDTO
	 *            the user DTO
	 * @return the change password JSP page
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String getChangePasswordPage(final HttpServletRequest request, final Model model,
			@ModelAttribute("changePasswordForm") final UserDTO userDTO) {
		logger.debug("Received request to show change password page.");
		if (userDTO != null) {
			userDTO.setPassword(null);
			userDTO.setNewPassword(null);
			userDTO.setConfirmPassword(null);
		}
		return "change-password";
	}

	/**
	 * Validated and sets the changed password in change password functionality.
	 * @param userDTO
	 *            the user dto
	 * @param result
	 *            the result
	 * @param attributes
	 *            the attributes
	 * @param request
	 *            the request
	 * @param modelMap
	 *            the model map
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String setChangedPassword(@ModelAttribute("changePasswordForm") @Validated final UserDTO userDTO,
			final BindingResult result, final RedirectAttributes attributes, final HttpServletRequest request, final ModelMap modelMap,
			final Model model) {
		logger.debug("Received request to process change password request.");

		if (result.hasErrors()) {
			userDTO.setPassword(null);
			userDTO.setNewPassword(null);
			userDTO.setConfirmPassword(null);

			return "change-password";
		}
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		userService.savePassword(authentication.getName(), userDTO.getNewPassword());
		final String view = getTargetUrl(authentication);
		if (view.equals(UIConstants.LOGIN_HOME_PAGE)) {
			request.getSession().setAttribute(Constants.USERFULLNAME,
					userService.getUserFullName(authentication.getName()));
		}
		return "redirect:" + view;
	}

	/**
	 * Gets the target URL.
	 * @param authentication
	 *            the authentication
	 * @return the target URL
	 */
	private String getTargetUrl(final Authentication authentication) {
		String view = UIConstants.LOGIN_HOME_PAGE;
		if (authentication != null) {
			final BlackboxUser blackboxUser = (BlackboxUser) authentication.getPrincipal();
			if (blackboxUser != null) {
				if (!blackboxUser.isOTPEnabled()) {
					view = UIConstants.LOGIN_HOME_PAGE;
					blackboxUser.setAuthenticationType(Constants.FULLY_AUTHENTICATED);
					blackboxUser.setOtpSuccess(true);
					blackboxUser.setFirstLogin(false);
				} else {
					if (blackboxUser.isOtpSuccess()) {
						view = UIConstants.LOGIN_HOME_PAGE;
						blackboxUser.setAuthenticationType(Constants.FULLY_AUTHENTICATED);
						blackboxUser.setOtpSuccess(true);
						blackboxUser.setFirstLogin(false);
					} else {
						view = "/otp";
						blackboxUser.setAuthenticationType(Constants.PARTIALLY_AUTHENTICATED);
						blackboxUser.setOtpSuccess(false);
						blackboxUser.setFirstLogin(false);
						userService.sendOTP(authentication.getName());
					}
				}
			}
		}
		return view;
	}

}

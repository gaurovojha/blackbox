package com.blackbox.ids.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blackbox.ids.common.constant.Constant;
import com.blackbox.ids.services.usermanagement.UserService;
import com.blackbox.ids.ui.common.Constants;
import com.blackbox.ids.ui.common.Messages;

/**
 * This controller handles and retrieves the login or denied page depending on the URI template.
 * @author Nagarro
 */
@Controller
public class LoginController {

	/** The logger. */
	protected static Logger logger = Logger.getLogger(LoginController.class);
	
	/** The user service. */
	@Autowired
	private UserService userService;

	/**
	 * Gets the login page.
	 * @param request
	 *            the request
	 * @param error
	 *            the error
	 * @param model
	 *            the model
	 * @return the login page
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginPage(final HttpServletRequest request,
			@RequestParam(value = "error", required = false) final String error, final ModelMap model) {
		logger.debug("Received request to show login page");
		model.put(Constants.LDAP_ENABLED_FLAG, userService.isLdapEnabled());
		if (error != null) {
			model.put(Constants.ERROR, getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
			model.put(Constants.USERNAME, obtainUsername(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}
		return "login";
	}

	/**
	 * Gets the error message.
	 * @param request
	 *            the request
	 * @param key
	 *            the key
	 * @return the error message
	 */
	private String getErrorMessage(final HttpServletRequest request, final String key) {

		final Exception exception = (Exception) request.getSession().getAttribute(key);
		String error = Constant.EMPTY_STRING;
		if (exception instanceof BadCredentialsException) {
			error = Messages.ERR_MSG_INVALID_USER_AND_PASSWORD;
		} else if (exception instanceof LockedException) {
			error = exception.getMessage();
		} else if (exception instanceof DisabledException) {
			error = exception.getMessage();
		} else {
			error = Messages.ERR_MSG_INVALID_USER_AND_PASSWORD;
		}
		return error;
	}

	/**
	 * Obtain user name.
	 * @param request
	 *            the request
	 * @param key
	 *            the key
	 * @return the string
	 */
	private String obtainUsername(final HttpServletRequest request, final String key) {
		final Exception exception = (Exception) request.getSession().getAttribute(key);
		String username = null;
		if (exception instanceof AuthenticationException) {
			final Authentication authentication = ((AuthenticationException) exception).getAuthentication();
			if (authentication != null) {
				username = authentication.getName();
			}
		}
		return username;
	}

	/**
	 * Gets the denied page.
	 * @return the denied page
	 */
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
	public String getDeniedPage() {
		logger.debug("Received request to show denied page");
		return "deniedpage";
	}
}
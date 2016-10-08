package com.blackbox.ids.services.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.common.constant.UIConstants;
import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.services.usermanagement.UserService;

/**
 * The Class BlackboxAuthenticationSuccessHandler handles the action taken on authentication success.
 *
 * @author Nagarro
 */
@Component("authenticationSuccessHandler")
public class BlackboxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	/** The log. */
	private static final Logger log = Logger.getLogger(BlackboxAuthenticationSuccessHandler.class);

	/** The redirect strategy. */
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	/** The user service. */
	@Autowired
	private UserService userService;

	/**
	 * This API is used to take action on authentication success.
	 * 
	 * @param request the request
	 * @param response the response
	 * @param authentication the authentication
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException Signals that an Servlet exception has occurred.
	 * 
	 */
	@Override
	@Transactional
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException {
		handle(request, response, authentication);
		clearAuthenticationAttributes(request);

	}

	/**
	 * Handles the authentication success.
	 *
	 * @param request the request
	 * @param response the response
	 * @param authentication the authentication
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void handle(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication)
			throws IOException {
		final String targetUrl = determineTargetUrl(authentication, request);

		if (response.isCommitted()) {
			log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	/**
	 * Builds the target URL on authentication success.
	 *
	 * @param authentication the authentication
	 * @param request the request
	 * @return the string
	 */
	protected String determineTargetUrl(final Authentication authentication, final HttpServletRequest request) {
		if(authentication!=null) {
		boolean isFirstLogin = false;
		final BlackboxUser user = (BlackboxUser) authentication.getPrincipal();

		if (user != null) {
			isFirstLogin = user.isFirstLogin();
		}
		String targerUrl = UIConstants.LOGIN_HOME_PAGE;
		setAuthenticationType(authentication, UIConstants.FULLY_AUTHENTICATED);
		setOTPSuccess(authentication, true);
		if (authentication != null && isFirstLogin) {
			setAuthenticationType(authentication, UIConstants.PARTIALLY_AUTHENTICATED);
			setOTPSuccess(authentication, false);
			targerUrl = "/changePassword";
		}
		else if(user.isOTPEnabled()) {
			setAuthenticationType(authentication, UIConstants.PARTIALLY_AUTHENTICATED);
			setOTPSuccess(authentication, false);
			userService.sendOTP(authentication.getName());
			targerUrl = "/otp";
		}
		else {
			request.getSession().setAttribute(UIConstants.USERFULLNAME, userService.getUserFullName(user.getUsername()));
		}
		return targerUrl;
		}
		else{
			throw new ApplicationException(
					"Exception thorwn after authentication success when authentication null");
		}
	}

	/**
	 * Clear authentication attributes.
	 *
	 * @param request the request
	 */
	protected void clearAuthenticationAttributes(final HttpServletRequest request) {
		final HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	/**
	 * Sets the redirect strategy.
	 *
	 * @param redirectStrategy the new redirect strategy
	 */
	public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	/**
	 * Gets the redirect strategy.
	 *
	 * @return the redirect strategy
	 */
	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	/**
	 * Gets the user service.
	 *
	 * @return the user service
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user service.
	 *
	 * @param userService the new user service
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	/**
	 * Sets the authentication type.
	 *
	 * @param authentication the authentication
	 * @param authenticationType the authentication type
	 */
	private void setAuthenticationType(final Authentication authentication, final String authenticationType) {

		if(authentication != null) {
			final BlackboxUser blackboxUser = (BlackboxUser)authentication.getPrincipal();
			if(blackboxUser != null) {
				blackboxUser.setAuthenticationType(authenticationType);
			}
		}
	}

	/**
	 * Sets the otp success.
	 *
	 * @param authentication the authentication
	 * @param otpSuccess the otp success
	 */
	private void setOTPSuccess(final Authentication authentication, final boolean otpSuccess) {

		if(authentication != null) {
			final BlackboxUser blackboxUser = (BlackboxUser)authentication.getPrincipal();
			if(blackboxUser != null) {
				blackboxUser.setOtpSuccess(otpSuccess);
			}
		}
	}

}

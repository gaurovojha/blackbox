
package com.blackbox.ids.services.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.Assert;

import com.blackbox.ids.common.constant.Constant;
import com.blackbox.ids.services.usermanagement.UserService;

/**
 * This filter is a authentication processing filter deals with authentication for user.
 * @author Nagarro
 */
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private static final Logger LOGGER = Logger.getLogger(UsernamePasswordAuthenticationFilter.class);

	/** The Constant SPRING_SECURITY_FORM_USERNAME_KEY. */
	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";

	/** The Constant SPRING_SECURITY_FORM_PASSWORD_KEY. */
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

	/** The Constant SPRING_SECURITY_LAST_USERNAME_KEY. */
	@Deprecated
	public static final String SPRING_SECURITY_LAST_USERNAME_KEY = "SPRING_SECURITY_LAST_USERNAME";

	/** The username parameter. */
	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

	/** The password parameter. */
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

	/** The reattampt count. */
	private final String REATTAMPT_COUNT = "REATTAMPT_COUNT";

	/** The user service. */
	private final UserService userService;

	/** The post only. */
	private boolean postOnly = true;

	/**
	 * Instantiates a new username password authentication filter.
	 * @param userService
	 *            the user service
	 */
	public UsernamePasswordAuthenticationFilter(final UserService userService)
	{
		super("/j_spring_security_check");
		this.userService = userService;
	}

	/**
	 * This API is used to authenticate the user.
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws AuthenticationException
	 * @return the authentication
	 */
	@Override
	@SuppressWarnings("deprecation")
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
			throws AuthenticationException {
		if ((this.postOnly) && (!("POST".equals(request.getMethod())))) {
			LOGGER.error("Authentication method not supported: " + request.getMethod());
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		final HttpSession session = request.getSession();
		Integer reattampCount = (Integer) session.getAttribute(REATTAMPT_COUNT);
		if (reattampCount == null) {
			reattampCount = 0;
		}

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = Constant.EMPTY_STRING;
		}

		if (password == null) {
			password = Constant.EMPTY_STRING;
		}

		final UserAuthenticationToken authRequest = new UserAuthenticationToken(username, password);

		setDetails(request, authRequest);

		try {
			final Authentication authentication = getAuthenticationManager().authenticate(authRequest);
			session.setAttribute(REATTAMPT_COUNT, null);
			return authentication;
		} catch (AuthenticationException exception) {
			LOGGER.error("Exception occured during Authentication", exception);
			if (!(exception instanceof LockedException)) {
				if (!userService.isLdapEnabled()) {
					reattampCount++;
					if (reattampCount < 3) {
						session.setAttribute(REATTAMPT_COUNT, reattampCount);
					} else {
						logger.info("Handling of User Account Locking");
						userService.lockUserAccount(username);
						exception = new LockedException("User Account is locked");
						exception.setAuthentication(authRequest);
					}
				}
			}
			throw exception;
		}
	}

	/**
	 * Obtain password from request parameter.
	 * @param request
	 *            the request
	 * @return the string
	 */
	protected String obtainPassword(final HttpServletRequest request) {
		return request.getParameter(this.passwordParameter);
	}

	/**
	 * Obtain user name from request parameter.
	 * @param request
	 *            the request
	 * @return the string
	 */
	protected String obtainUsername(final HttpServletRequest request) {
		return request.getParameter(this.usernameParameter);
	}

	/**
	 * Sets the details in authentication.
	 * @param request
	 *            the request
	 * @param authRequest
	 *            the auth request
	 */
	protected void setDetails(final HttpServletRequest request, final UserAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}

	/**
	 * Sets the user name.
	 * @param usernameParameter
	 *            the new user name parameter
	 */
	public void setUsernameParameter(final String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.usernameParameter = usernameParameter;
	}

	/**
	 * Sets the password.
	 * @param passwordParameter
	 *            the new password parameter
	 */
	public void setPasswordParameter(final String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		this.passwordParameter = passwordParameter;
	}

	/**
	 * Sets the post only.
	 * @param postOnly
	 *            the new post only
	 */
	public void setPostOnly(final boolean postOnly) {
		this.postOnly = postOnly;
	}

	/**
	 * Gets the user name parameter.
	 * @return the user name parameter
	 */
	public final String getUsernameParameter() {
		return this.usernameParameter;
	}

	/**
	 * Gets the password.
	 * @return the password
	 */
	public final String getPasswordParameter() {
		return this.passwordParameter;
	}
}
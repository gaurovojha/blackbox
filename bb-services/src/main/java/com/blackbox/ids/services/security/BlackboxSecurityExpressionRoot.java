package com.blackbox.ids.services.security;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;

import com.blackbox.ids.common.constant.UIConstants;
import com.blackbox.ids.core.auth.BlackboxUser;

/**
 * This Class is extending the class {@link WebSecurityExpressionRoot}. This class provides the expression based
 * security for authorizing a user whether a user has authority to access a URL.
 * @author Nagarro
 */
public class BlackboxSecurityExpressionRoot extends WebSecurityExpressionRoot {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(BlackboxSecurityExpressionRoot.class);

	/** The authentication. */
	private final Authentication authentication;

	/** The invocation. */
	private final FilterInvocation invocation;

	/** The path matcher. */
	private final PathMatcher pathMatcher;

	/**
	 * Instantiates a new blackbox security expression root.
	 * @param authentication
	 *            the authentication
	 * @param filterInvocation
	 *            the filter invocation
	 */
	public BlackboxSecurityExpressionRoot(final Authentication authentication, final FilterInvocation filterInvocation)
	{
		super(authentication, filterInvocation);
		this.authentication = authentication;
		this.invocation = filterInvocation;
		pathMatcher = new AntPathMatcher();
	}

	/**
	 * This API is used to check whether a user is allowed to access the requested URL.
	 * @return true, if successful else false
	 */
	public boolean permitUrl() {
		final String url = invocation.getRequestUrl();
		logger.debug("Url : " + url);
		if (authentication instanceof AnonymousAuthenticationToken) {
			return false;
		}
		return (authentication != null) ? processUrlRequest(url) : false;
	}

	/**
	 * The method holds the responsibility to validate whether the currently logged in user has rights to access the
	 * given URL.
	 * <p/>
	 * This method can be used to conditionally control the visibility of a particular DOM element, such as showing
	 * action items or navigation links to only authenticated users.
	 * @param url
	 *            URL to be inspected for current user access.
	 * @return {@code true} if user can process the given URL, {@code false} otherwise.
	 */
	public boolean canAccessUrl(final String url) {
		return processUrlRequest(url);
	}

	/**
	 * Process url request.
	 * @param url
	 *            the url
	 * @return true, if successful
	 */
	private boolean processUrlRequest(final String url) {
		List<? extends GrantedAuthority> authorities;
		final BlackboxUser blackboxUser = getLoggedInUser();
		if (blackboxUser.getAuthenticationType().equals(UIConstants.FULLY_AUTHENTICATED)) {
			authorities = (List<? extends GrantedAuthority>) authentication.getAuthorities();
			if (CollectionUtils.isEmpty(authorities)) {
				return false;
			} else {
				for (final GrantedAuthority authority : authorities) {
					if (pathMatcher.match(authority.getAuthority(), url)) {
						return true;
					}
				}
			}
		}
		logger.info("Access Denied on URL " + url);
		return false;
	}

	/**
	 * Checks whether a user is authenticated or not.
	 * @return true, if successful else false
	 */
	public boolean authenticated() {
		if (authentication instanceof AnonymousAuthenticationToken) {
			return false;
		}
		if (authentication != null) {
			return authentication.isAuthenticated();
		}
		return false;
	}

	/**
	 * Checks whether a user is allowed to access the password change URL.
	 * @return true, if successful else false
	 */
	public boolean permitPasswordChangeUrl() {
		if (authentication instanceof AnonymousAuthenticationToken) {
			return false;
		}
		if (authentication != null) {
			final BlackboxUser blackboxUser = (BlackboxUser) authentication.getPrincipal();
			if (blackboxUser != null) {
				if ((blackboxUser.getAuthenticationType().equalsIgnoreCase(UIConstants.PARTIALLY_AUTHENTICATED)
						&& blackboxUser.isFirstLogin())
						|| blackboxUser.getAuthenticationType().equalsIgnoreCase(UIConstants.FULLY_AUTHENTICATED)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks whether a user is allowed to access the OTP URL.
	 * @return true, if successful
	 */
	public boolean permitOTPUrl() {
		if (authentication instanceof AnonymousAuthenticationToken) {
			return false;
		}
		if (authentication != null) {
			final BlackboxUser blackboxUser = getLoggedInUser();
			if (blackboxUser != null) {
				if (blackboxUser.getAuthenticationType().equalsIgnoreCase(UIConstants.PARTIALLY_AUTHENTICATED)
						&& !blackboxUser.isFirstLogin() && blackboxUser.isOTPEnabled() && !blackboxUser.isOtpSuccess()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the logged in user.
	 * @return the logged in user
	 */
	private BlackboxUser getLoggedInUser() {
		return (BlackboxUser) authentication.getPrincipal();
	}

}

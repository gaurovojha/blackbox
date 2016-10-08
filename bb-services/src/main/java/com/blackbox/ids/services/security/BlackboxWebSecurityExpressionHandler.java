package com.blackbox.ids.services.security;

import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

/**
 * The Class is the handler for handling the expression based security for authorizing a user.
 * @author Nagarro
 */
public class BlackboxWebSecurityExpressionHandler extends DefaultWebSecurityExpressionHandler {

	/**
	 * Creates Security Expression Root for expression based security.
	 * @param authentication
	 *            the authentication
	 * @param filterInvocation
	 *            the filterInvocation
	 */
	@Override
	protected SecurityExpressionOperations createSecurityExpressionRoot(final Authentication authentication,
			final FilterInvocation filterInvocation) {
		return new BlackboxSecurityExpressionRoot(authentication, filterInvocation);
	}

}

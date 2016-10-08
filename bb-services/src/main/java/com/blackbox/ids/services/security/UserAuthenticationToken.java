package com.blackbox.ids.services.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * This Class is extending the class {@link UsernamePasswordAuthenticationToken}. It represents the Authentication
 * Token.
 * @author Nagarro
 */
public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new user authentication token.
	 * @param principal
	 *            the principal
	 * @param credentials
	 *            the credentials
	 */
	public UserAuthenticationToken(final Object principal, final Object credentials)
	{
		super(principal, credentials);
	}

	/**
	 * Instantiates a new user authentication token.
	 * @param principal
	 *            the principal
	 * @param credentials
	 *            the credentials
	 * @param authorities
	 *            the authorities
	 */
	public UserAuthenticationToken(final Object principal, final Object credentials,
			final Collection<? extends GrantedAuthority> authorities)
	{
		super(principal, credentials, authorities);
	}
}

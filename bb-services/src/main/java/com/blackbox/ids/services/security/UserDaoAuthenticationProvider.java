package com.blackbox.ids.services.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.user.connectivity.UserConnectivity;
import com.blackbox.ids.user.connectivity.factory.UserConnectivityFactory;

/**
 * The Class UserDaoAuthenticationProvider is extending the class {@link AbstractUserDetailsAuthenticationProvider} and
 * deals with Authentication of a user.
 * @author Nagarro
 */
public class UserDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	/** The LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(UserDaoAuthenticationProvider.class);

	/** The user details service. */
	private UserDetailsService userDetailsService;

	@Autowired
	private UserConnectivityFactory userConnectivityFactory;

	/** The password encoder. */
	@SuppressWarnings("deprecation")
	private org.springframework.security.authentication.encoding.PasswordEncoder passwordEncoder;

	/** The user not found encoded password. */
	private String userNotFoundEncodedPassword;

	/** The salt source. */
	private SaltSource saltSource;

	/**
	 * Instantiates a new user dao authentication provider.
	 */
	public UserDaoAuthenticationProvider()
	{
		setPasswordEncoder(new PlaintextPasswordEncoder());
	}

	/**
	 * This API is used for additional checks during authenticating a user.
	 * @param userDetails
	 *            the user details
	 * @param authentication
	 *            the authentication
	 */
	@SuppressWarnings("deprecation")
	protected void additionalAuthenticationChecks(final UserDetails userDetails,
			final UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		LOGGER.debug("Additional Authentication Checks In Spring Security.");

		Object salt = null;

		if (this.saltSource != null) {
			salt = this.saltSource.getSalt(userDetails);
		}

		if (authentication.getCredentials() == null) {
			this.LOGGER.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(this.messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"), userDetails);
		}

		final String presentedPassword = authentication.getCredentials().toString();
		// Authenticating users against Ldap
		UserConnectivity ldapConnectivity = userConnectivityFactory.getImplementation();
		if (ldapConnectivity != null) {
			BlackboxUser blackboxUser = (BlackboxUser) userDetails;
			boolean isAuthenticated = ldapConnectivity.authenticate(blackboxUser.getUsername(), presentedPassword);
			if (!isAuthenticated) {
				throw new BadCredentialsException(this.messages.getMessage(
						"AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"), userDetails);
			}
		} else if (!(this.passwordEncoder.isPasswordValid(userDetails.getPassword(), presentedPassword, salt))) {
			this.LOGGER.debug("Authentication failed: password does not match stored value");
			throw new BadCredentialsException(this.messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"), userDetails);
		}
	}

	/**
	 * Checks for properties set.
	 */
	protected void doAfterPropertiesSet() throws Exception {
		Assert.notNull(this.userDetailsService, "An UserDetailsService must be set");
	}

	/**
	 * This API is used by the framework to retrieve the user details.
	 * @param username
	 *            the user name
	 * @param authentication
	 *            the authentication
	 */
	@SuppressWarnings("deprecation")
	protected final UserDetails retrieveUser(final String username,
			final UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		UserDetails loadedUser;
		try {
			loadedUser = userDetailsService.loadUserByUsername(username);
		} catch (final UsernameNotFoundException notFound) {
			if (authentication.getCredentials() != null) {
				final String presentedPassword = authentication.getCredentials().toString();
				this.passwordEncoder.isPasswordValid(this.userNotFoundEncodedPassword, presentedPassword, null);
			}
			throw notFound;
		} catch (final Exception repositoryProblem) {
			throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}

		if (loadedUser == null) {
			throw new InternalAuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		}

		return loadedUser;
	}

	/**
	 * Sets the password encoder.
	 * @param passwordEncoder
	 *            the new password encoder
	 */
	@SuppressWarnings("deprecation")
	private void setPasswordEncoder(
			final org.springframework.security.authentication.encoding.PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

		this.userNotFoundEncodedPassword = passwordEncoder.encodePassword("userNotFoundPassword", null);
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Gets the password encoder.
	 * @return the password encoder
	 */
	@SuppressWarnings("deprecation")
	protected org.springframework.security.authentication.encoding.PasswordEncoder getPasswordEncoder() {
		return this.passwordEncoder;
	}

	/**
	 * Sets the salt source.
	 * @param saltSource
	 *            the new salt source
	 */
	public void setSaltSource(final SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	/**
	 * Gets the salt source.
	 * @return the salt source
	 */
	protected SaltSource getSaltSource() {
		return this.saltSource;
	}

	/**
	 * Gets the user details service.
	 * @return the user details service
	 */
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	/**
	 * Sets the user details service.
	 * @param userDetailsService
	 *            the new user details service
	 */
	public void setUserDetailsService(final UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
}

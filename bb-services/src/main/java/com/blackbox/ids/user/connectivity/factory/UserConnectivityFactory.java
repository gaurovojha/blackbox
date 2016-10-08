package com.blackbox.ids.user.connectivity.factory;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.user.connectivity.UserConnectivity;

/**
 * This class reads the value provided in user.connection in the user-connectivity.properties file it will return the
 * implementation of the user connectivity.
 * @author BlackBox
 * @see com.bb.authenticate.user.connectivity.impl.LdapConnectivity
 */
public class UserConnectivityFactory {

	/**
	 * Used for handling logs.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UserConnectivityFactory.class);

	/**
	 * Instance of LdapConnectivity.
	 */
	@Autowired
	@Qualifier("ldapConnectivity")
	private UserConnectivity ldapConnectivity;

	/** The connectivity represents. */
	private UserConnectivity connectivity;

	@PostConstruct
	private void init() {
		getImplementation();
	}

	/**
	 * This value get from the user.connection in user-connectivity.properties file.
	 */
	private int choice;

	/**
	 * @param choice
	 *            the choice to set
	 */
	public void setChoice(final int choice) {
		this.choice = choice;
	}

	/**
	 * This method is used to return the implementation of user connectivity in accordance of the choice provided in the
	 * user.connection in environment.properties file. It return ldapConnectivity for choice 1.
	 * @return ldapConnectivity if choice is 1 else return null
	 */

	public UserConnectivity getImplementation() {
		if (ldapConnectivity == null) {
			LOG.error("Ldap Connectivity is null");
		}

		if (connectivity == null && choice == 1) {
			final boolean isSuccessful = ldapConnectivity.checkConnection();
			if (!isSuccessful) {
				throw new ApplicationException("Exception oocured during Ldap connection.");
			}
			connectivity = ldapConnectivity;
		}
		return connectivity;
	}
}

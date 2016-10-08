package com.blackbox.ids.user.connectivity.impl;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.CollectingAuthenticationErrorCallback;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.user.connectivity.UserConnectivity;
import com.blackbox.ids.user.connectivity.constant.UserConnectivityConstant;

/**
 * This class connect to the LDAP server and fetching the result from the LDAP directory server.
 * @author BlackBox
 * @version 1.0
 * @see com.blackbox.ids.user.connectivity.UserConnectivity
 */
public class LdapConnectivity implements UserConnectivity {

	/** The Constant ERROR_IN_CLOSING_LDAP_CONNECTION. */
	private static final String ERROR_IN_CLOSING_LDAP_CONNECTION = "Error in closing LDAP connection";

	/**
	 * Used for handling logs.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(LdapConnectivity.class);

	private LdapTemplate	ldapTemplate;
	/**
	 * This value get from the user.ldap_url in user-connectivity.properties file.
	 */
	private String			ldapURL;

	/**
	 * This value get from the user.ldap_config in user-connectivity.properties file.
	 */
	private String ldapConfig;

	/**
	 * This value get from the user.ldap_domain_component_name in user-connectivity.properties file.
	 */
	private String ldapDomainName;

	/**
	 * This value get from the user.ldap_domain_component_organization in user-connectivity.properties file.
	 */
	private String ldapDomainOrganization;

	/** The base distinguish name for LDAP connectivity. */
	private String baseDN;

	/**
	 * This value get from the user.ldap_user_name in user-connectivity.properties file.
	 */
	private String ldapUserName;

	/**
	 * This value get from the user.ldap_password in user-connectivity.properties file.
	 */
	private String ldapPassword;

	/**
	 * This value get from the user.ldap_user_base in user-connectivity.properties file.
	 */
	private String userBasePath;

	/**
	 * This value get from the user.ldap_group_base in user-connectivity.properties file.
	 */
	private String	groupBasePath;
	/**
	 * This Value is fetched from attribute user.ldap_groupSearchAttributeFilter in user-connectivity.properties file.
	 */
	private String	groupSearchAttribute;
	/**
	 * This Value is fetched from attribute user.ldap_userSearchAttributeFilter in user-connectivity.properties file.
	 */
	private String	userSearchAttribute;

	/**
	 * Gets the user search attribute.
	 * @return Attribute Value on which Users are searched
	 */
	public String getUserSearchAttribute() {
		return userSearchAttribute;
	}

	/**
	 * Sets the user search attribute.
	 * @param userSearchAttribute
	 *            is Value to be set for getting Users,on the basis of attribute specified.
	 */
	public void setUserSearchAttribute(final String userSearchAttribute) {
		this.userSearchAttribute = userSearchAttribute;
	}

	/**
	 * Gets the group search attribute.
	 * @return Attribute Value on which groups are searched
	 */
	public String getGroupSearchAttribute() {
		return groupSearchAttribute;
	}

	/**
	 * Sets the group search attribute.
	 * @param groupSearchAttribute
	 *            is Value to be set for getting groups,on the basis of attribute specified.
	 */
	public void setGroupSearchAttribute(final String groupSearchAttribute) {
		this.groupSearchAttribute = groupSearchAttribute;
	}

	/**
	 * Gets the ldap url.
	 * @return the ldapURL
	 */
	public String getLdapURL() {
		return ldapURL;
	}

	/**
	 * Sets the ldap url.
	 * @param ldapURL
	 *            the ldapURL to set
	 */
	public void setLdapURL(final String ldapURL) {
		this.ldapURL = ldapURL;
	}

	/**
	 * Gets the ldap config.
	 * @return the ldapConfig
	 */
	public String getLdapConfig() {
		return ldapConfig;
	}

	/**
	 * Sets the ldap config.
	 * @param ldapConfig
	 *            the ldapConfig to set
	 */
	public void setLdapConfig(final String ldapConfig) {
		this.ldapConfig = ldapConfig;
	}

	/**
	 * Gets the ldap domain name.
	 * @return the ldapDomainName
	 */

	public String getLdapDomainName() {
		return ldapDomainName;
	}

	/**
	 * Sets the ldap domain name.
	 * @param ldapDomainName
	 *            the ldapDomainName to set
	 */
	public void setLdapDomainName(final String ldapDomainName) {
		this.ldapDomainName = ldapDomainName;
	}

	/**
	 * Gets the ldap domain organization.
	 * @return the ldapDomainOrganization
	 */
	public String getLdapDomainOrganization() {
		return ldapDomainOrganization;
	}

	/**
	 * Sets the ldap domain organization.
	 * @param ldapDomainOrganization
	 *            the ldapDomainOrganization to set
	 */
	public void setLdapDomainOrganization(final String ldapDomainOrganization) {
		this.ldapDomainOrganization = ldapDomainOrganization;
	}

	/**
	 * Gets the ldap password.
	 * @return the ldapPassword
	 */
	public String getLdapPassword() {
		return ldapPassword;
	}

	/**
	 * Gets the ldap user name.
	 * @return the ldapUserName
	 */
	public String getLdapUserName() {
		return ldapUserName;
	}

	/**
	 * Sets the ldap password.
	 * @param ldapPassword
	 *            the ldapPassword to set
	 */
	public void setLdapPassword(final String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}

	/**
	 * Sets the ldap user name.
	 * @param ldapUserName
	 *            the ldapUserName to set
	 */
	public void setLdapUserName(final String ldapUserName) {
		this.ldapUserName = ldapUserName;
	}

	public boolean authenticate(final String username, final String password) {

		if (ldapTemplate == null) {
			try {
				ldapTemplate = getLdapTemplate();
			} catch (Exception ex) {
				LOG.error("Exception occured during user Authentication through Ldap: Ldap is not configured properly.",
						ex);
				return false;
			}
		}

		String[] userSearch = userBasePath.split("=");

		if (userSearch.length >= 2) {
			AndFilter filter = new AndFilter();
			filter.and(new EqualsFilter(userSearchAttribute, username));
			CollectingAuthenticationErrorCallback errorCallback = new CollectingAuthenticationErrorCallback();
			boolean isAuthenticated = ldapTemplate.authenticate(userBasePath, filter.toString(), password,
					errorCallback);
			if (!isAuthenticated) {
				LOG.error("Exception occured during User Authentication against Ldap: ", errorCallback.getError());
			}
			return isAuthenticated;
		} else {
			LOG.error("No user search base provided in configuration file.");
			throw new ApplicationException("No user search base provided in configuration file.");
		}
	}

	@Override
	public boolean checkUserExistence(final String userName) {
		boolean isUserExist = false;
		DirContext dctx = null;
		try {
			dctx = getDirectoryContext();
			NamingEnumeration<SearchResult> results = null;
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			results = dctx.search(userBasePath, userSearchAttribute + UserConnectivityConstant.EQUAL_SYMBOL + userName,
					controls);
			if (results != null) {
				isUserExist = true;
			}
		} catch (Exception ex) {
			LOG.info("User is not found in ldap for :" + userName);
			return false;
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
		return isUserExist;
	}

	/**
	 * Gets the user base path.
	 * @return the userBasePath
	 */
	public String getUserBasePath() {
		return userBasePath;
	}

	/**
	 * Sets the user base path.
	 * @param userBasePath
	 *            the userBasePath to set
	 */
	public void setUserBasePath(final String userBasePath) {
		this.userBasePath = userBasePath;
	}

	/**
	 * Gets the group base path.
	 * @return the groupBasePath
	 */
	public String getGroupBasePath() {
		return groupBasePath;
	}

	/**
	 * Sets the group base path.
	 * @param groupBasePath
	 *            the groupBasePath to set
	 */
	public void setGroupBasePath(final String groupBasePath) {
		this.groupBasePath = groupBasePath;
	}

	public String getBaseDN() {
		return baseDN;
	}

	public void setBaseDN(String baseDN) {
		this.baseDN = baseDN;
	}

	/**
	 * Gets the spring security context.
	 * @return the spring security context
	 */
	private LdapContextSource getSpringSecurityContext() {
		DirContext ctx = null;
		LdapContextSource ldapContext = new DefaultSpringSecurityContextSource(ldapURL);
		ldapContext.setBase(baseDN);
		ldapContext.setUserDn(ldapUserName);
		ldapContext.setPassword(ldapPassword);
		try {
			ldapContext.afterPropertiesSet();
			ctx = ldapContext.getContext(ldapUserName, ldapPassword);
		} catch (Exception e) {
			LOG.error("Exception occured during creation of LdapContext:", e);
			throw new ApplicationException(e);
		} finally {
			LdapUtils.closeContext(ctx);
		}
		return ldapContext;
	}

	/**
	 * Gets the directory context.
	 * @return the directory context
	 */
	private DirContext getDirectoryContext() {
		DirContext dirCtx = getSpringSecurityContext().getReadWriteContext();
		return dirCtx;
	}

	/**
	 * Gets the base ldap path.
	 * @return the base ldap path
	 */
	private String getBaseLdapPath() {
		return getSpringSecurityContext().getBaseLdapPathAsString();
	}

	/**
	 * Gets the ldap template.
	 * @return the ldap template
	 */
	private LdapTemplate getLdapTemplate() {
		if (ldapTemplate == null) {
			ldapTemplate = new LdapTemplate(getSpringSecurityContext());
			try {
				ldapTemplate.afterPropertiesSet();
			} catch (Exception ex) {
				LOG.error("Exception occured during Ldap Template creation.");
			}
			ldapTemplate.setIgnorePartialResultException(true);
		}
		return ldapTemplate;
	}

	public boolean checkConnection() {
		try {
			if (getLdapTemplate() != null) {
				return true;
			}
		} catch (ApplicationException exception) {
			LOG.error("Exception occured during connecting to Ldap: ", exception);
		}
		return false;
	}

}

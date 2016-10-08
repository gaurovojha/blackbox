package com.blackbox.ids.services.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.usermanagement.AccessProfile;
import com.blackbox.ids.core.model.usermanagement.Permission;

/**
 * The abstract super class to load the user information for user name.
 * @author Nagarro
 */
public abstract class AbstractUserDetailsService implements UserDetailsService {

	/**
	 * Loads the User Details for user name.
	 * @param username
	 *            the user name
	 * @throws UsernameNotFoundException
	 *             Signals that user not found.
	 * @return the user details
	 */
	@Override
	public abstract UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

	/**
	 * Builds the granted authorities for a user.
	 * @param user
	 *            the user
	 * @return the list of granted authorities
	 */
	protected List<GrantedAuthority> buildUserAuthority(final com.blackbox.ids.core.model.User user) {

		final Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
		AccessProfile accessProfile = null;
		if (user != null) {
			final Set<Role> userRoles = user.getUserRoles();
			if (!CollectionUtils.isEmpty(userRoles)) {
				for (final Role userRole : userRoles) {
					accessProfile = userRole.getAccessProfile();
					setAuths.addAll(buildProfileAuthority(accessProfile));
				}
			}
		}
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(setAuths);
		return authorities;
	}

	/**
	 * Builds the granted authorities for access profile.
	 * @param accessProfile
	 *            the access profile
	 * @return the set of granted authorities
	 */
	protected Set<GrantedAuthority> buildProfileAuthority(final AccessProfile accessProfile) {

		List<Permission> permissions = null;
		final Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
		if (accessProfile != null) {
			permissions = accessProfile.getPermissions();
			if (!CollectionUtils.isEmpty(permissions)) {
				for (final Permission permission : permissions) {
					setAuths.add(new SimpleGrantedAuthority(permission.getUrl()));
				}
			}
		}
		return setAuths;
	}
}

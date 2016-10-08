package com.blackbox.ids.services.security;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.core.auth.UserAuthDetail;
import com.blackbox.ids.core.common.BbxApplicationContextUtil;
import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.mstr.Organization;
import com.blackbox.ids.core.model.mstr.TechnologyGroup;
import com.blackbox.ids.services.usermanagement.UserService;

/**
 * The Class UserDetailsService is used to load the user related details from DB during authentication of a user.
 * @author Nagarro
 */
public class UserDetailsService extends AbstractUserDetailsService {

	/** The log. */
	private static final Logger log = Logger.getLogger(UserDetailsService.class);

	/** The user service. */
	@Autowired
	private UserService userService;

	/**
	 * Loads the User Details for user name.
	 * @param username
	 *            the user name
	 * @throws UsernameNotFoundException
	 *             Signals that user not found.
	 * @return the user details
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		log.debug("Spring Security for Blackbox User");
		if (userService == null) {
			userService = (UserService) BbxApplicationContextUtil.getBean("userService");
		}
		final com.blackbox.ids.core.model.User user = userService.findByLoginId(username);
		if (user == null) {
			throw new UsernameNotFoundException("Username not found.");
		}
		final List<GrantedAuthority> authorities = buildUserAuthority(user);
		return buildUserForAuthentication(user, authorities);
	}

	/**
	 * Builds the security user with authorities.
	 * @param user
	 *            the user
	 * @param authorities
	 *            the authorities
	 * @return the user
	 */
	private User buildUserForAuthentication(final com.blackbox.ids.core.model.User user, final List<GrantedAuthority> authorities) {
		return new BlackboxUser(user.getId(), user.getLoginId(), user.getPassword(),
				isEnabled(user), true, true, !isLocked(user), authorities, isOTPEnabled(user),
				isFirstLogin(user), false, "Partially Authenticated", getUserAuthDetail(user), getRoleIds(user));
	}
	
	private boolean isLocked(final com.blackbox.ids.core.model.User user) {
		return userService.isLdapEnabled() ? false : user.getIsLocked();
	}
	
	private boolean isFirstLogin(final com.blackbox.ids.core.model.User user) {
		return userService.isLdapEnabled() ? false : user.isFirstLogin();
	}
	
	private boolean isEnabled(final com.blackbox.ids.core.model.User user) {
		return userService.isLdapEnabled() ? true : user.isActive() && user.isEnabled();
	}

	/**
	 * Checks if is OTP enabled.
	 * @param user
	 *            the user
	 * @return true, if is OTP enabled
	 */
	private boolean isOTPEnabled(final com.blackbox.ids.core.model.User user) {
		boolean isOTPEnabled = false;
		if (user != null) {
			final Set<Role> userRoles = user.getUserRoles();
			if (!CollectionUtils.isEmpty(userRoles)) {
				for (final Role role : userRoles) {
					if (role.isOtpEnabed()) {
						isOTPEnabled = true;
					}
				}
			}
		}
		return isOTPEnabled;
	}

	/**
	 * Gets the user auth detail for user.
	 * @param user
	 *            the user
	 * @return the user auth detail
	 */
	private Map<Long, UserAuthDetail> getUserAuthDetail(final com.blackbox.ids.core.model.User user) {
		final Map<Long, UserAuthDetail> mapUserRoles = new HashMap<>();

		user.getUserRoles().forEach(role -> {
			final UserAuthDetail authDetail = new UserAuthDetail();

			authDetail.setAssigneeIds(role.getAssignees().stream().map(Assignee::getId).collect(Collectors.toSet()));
			authDetail.setCustomerIds(role.getCustomers().stream().map(Customer::getId).collect(Collectors.toSet()));
			authDetail.setTechnologyGroupIds(
					role.getTechnologyGroups().stream().map(TechnologyGroup::getId).collect(Collectors.toSet()));
			authDetail.setOrganizationsIds(
					role.getOrganizations().stream().map(Organization::getId).collect(Collectors.toSet()));
			authDetail.setJurisdictionIds(
					role.getJurisdictions().stream().map(Jurisdiction::getId).collect(Collectors.toSet()));

			mapUserRoles.put(role.getId(), authDetail);
		});

		return mapUserRoles;
	}

	/**
	 * Gets the role ids for user.
	 * @param user
	 *            the user
	 * @return the role ids
	 */
	private Set<Long> getRoleIds(final com.blackbox.ids.core.model.User user) {
		final Set<Long> roleIds = new LinkedHashSet<>();

		if (user != null) {
			final Set<Role> userRoles = user.getUserRoles();

			for (final Role role : userRoles) {
				roleIds.add(role.getId());
			}
		}

		return roleIds;
	}
}

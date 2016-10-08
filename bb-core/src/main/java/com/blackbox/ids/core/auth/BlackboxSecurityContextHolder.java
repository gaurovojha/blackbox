/**
 *
 */
package com.blackbox.ids.core.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.blackbox.ids.core.model.User;

/**
 * The <tt>BlackboxSecurityContextHolder</tt> class interacts with Springs' {@link SecurityContextHolder} to get
 * authenticated principal details associated with current execution thread.
 *
 * @author ajay2258
 */
public class BlackboxSecurityContextHolder {

	private static PathMatcher pathMatcher = new AntPathMatcher();

	private BlackboxSecurityContextHolder() {
		throw new InstantiationError("Couldn't constuct a utility class.");
	}

	/**
	 * Gets the logged in user's details.
	 *
	 * @return userDetails BlackboxUser instance containing logged in user's details
	 * @throws SecurityException
	 *             if no user is found in the context
	 */
	public static BlackboxUser getUserDetails() throws SecurityException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if ((authentication == null) || !(authentication.getPrincipal() instanceof BlackboxUser)) {
			throw new SecurityException("No logged in user.");
		}
		return (BlackboxUser) authentication.getPrincipal();
	}

	/**
	 * Gets the currently logged in user's database Id.
	 *
	 * @return database Id for logged in user.
	 * @throws SecurityException
	 * 					if no user is found in the context
	 */
	public static Long getUserId() throws SecurityException {
		return isUserAvailable() ? getUserDetails().getId() : User.SYSTEM_ID;
	}

	/**
	 * @return the data access authorization details for currently logged in user.
	 * <tt>null</tt> is returned if user details aren't found in security context.
	 */
	public static UserAuthDetail getUserAuthData() {
		return isUserAvailable() ? getUserDetails().getAuthDetail() : null;
	}

	/**
	 * @return the data access authorization details for currently logged in user, group by user assigned roles.
	 *         {@value Collections#emptyMap()} is returned if user details aren't found in security context.
	 */
	public static Map<Long, UserAuthDetail> getRolesAuthData() {
		return isUserAvailable() ? getUserDetails().getMapUserRoles() : Collections.emptyMap();
	}

	/**
	 * Check whether user information is present in the BlackboxSecurityContextHolder.
	 *
	 * @return true if information is present else return false
	 */
	public static boolean isUserAvailable() {
		boolean userDetailsAvailable = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if ((authentication != null) && (authentication.getPrincipal() instanceof BlackboxUser)) {
			userDetailsAvailable = true;
		}
		return userDetailsAvailable;
	}

	/**
	 * The method holds the responsibility to validate whether the currently logged in user has rights to access the
	 * given URL.<p/>
	 * This method can be used to conditionally control the visibility of a particular DOM element, such as showing
	 * action items or navigation links to only authenticated users.
	 *
	 * @param url URL to be inspected for current user access.
	 * @return {@code true} if user can process the given URL, {@code false} otherwise.
	 */
	public static boolean canAccessUrl(final String url) {
		boolean canAccess = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {
			for (final GrantedAuthority authority : authentication.getAuthorities()) {
				if (pathMatcher.match(authority.getAuthority(), url)) {
					canAccess = true;
					break;
				}
			}
		}

		return canAccess;
	}

	/**
	 * @param urls
	 * @return
	 */
	public static Map<String, Boolean> authorizedUrlMap(List<String> urls) {
		Map<String, Boolean> authorizedUrlMap = new HashMap<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

			for (String url : urls) {
				authorizedUrlMap.put(url, authorities.contains(new SimpleGrantedAuthority(url)));
			}
		}

		return authorizedUrlMap;
	}
}

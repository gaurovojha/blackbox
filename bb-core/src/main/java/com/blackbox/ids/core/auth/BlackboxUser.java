package com.blackbox.ids.core.auth;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class BlackboxUser extends User {

	/** The serial version UID. */
	private static final long serialVersionUID = -444326165041980719L;

	private Long id;
	private boolean isOTPEnabled;
	private boolean isFirstLogin;
	private String authenticationType;
	private boolean otpSuccess;

	/** Contains authentication data from all the assigned roles. */
	private UserAuthDetail authDetail;

	/** Group authentication data for assigned roles. */
	private Map<Long, UserAuthDetail> mapUserRoles;

	private Set<Long> roleIds;

	public BlackboxUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities,
			boolean otp, boolean isFirstLogin, boolean otpSuccess, String authenticationType) {

		super(username, password, true, true, true, true, authorities);
		this.id = id;
		this.isOTPEnabled = otp;
		this.isFirstLogin = isFirstLogin;
		this.otpSuccess = otpSuccess;
		this.authenticationType = authenticationType;
	}

	public BlackboxUser(Long id, String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
			boolean isOTPEnabled, boolean isFirstLogin, boolean otpSuccess, String authenticationType,
			Map<Long, UserAuthDetail> mapUserRoles, Set<Long> roleIds) {

		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.isOTPEnabled = isOTPEnabled;
		this.isFirstLogin = isFirstLogin;
		this.otpSuccess = otpSuccess;
		this.authenticationType = authenticationType;
		this.mapUserRoles = mapUserRoles;
		this.authDetail = mergeAuthDetails(mapUserRoles);
		this.roleIds = roleIds;
	}

	private UserAuthDetail mergeAuthDetails(Map<Long, UserAuthDetail> mapUserRoles) {
		final UserAuthDetail authDetail = new UserAuthDetail();
		mapUserRoles.entrySet().forEach(e -> {
			UserAuthDetail roleData = e.getValue();
			authDetail.getAssigneeIds().addAll(roleData.getAssigneeIds());
			authDetail.getCustomerIds().addAll(roleData.getCustomerIds());
			authDetail.getTechnologyGroupIds().addAll(roleData.getTechnologyGroupIds());
			authDetail.getOrganizationsIds().addAll(roleData.getOrganizationsIds());
			authDetail.getJurisdictionIds().addAll(roleData.getJurisdictionIds());
		});
		return authDetail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	public String getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

	public boolean isOtpSuccess() {
		return otpSuccess;
	}

	public void setOtpSuccess(boolean otpSuccess) {
		this.otpSuccess = otpSuccess;
	}

	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public UserAuthDetail getAuthDetail() {
		return authDetail;
	}

	public void setAuthDetail(UserAuthDetail authDetail) {
		this.authDetail = authDetail;
	}

	public Map<Long, UserAuthDetail> getMapUserRoles() {
		return mapUserRoles;
	}

	public void setMapUserRoles(Map<Long, UserAuthDetail> mapUserRoles) {
		this.mapUserRoles = mapUserRoles;
	}

	public Set<Long> getRoleIds() {
		if (roleIds == null) {
			roleIds = new LinkedHashSet<>();
		}

		return roleIds;
	}

	public void setRoleIds(Set<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public boolean isOTPEnabled() {
		return isOTPEnabled;
	}

	public void setOTPEnabled(boolean isOTPEnabled) {
		this.isOTPEnabled = isOTPEnabled;
	}

	public boolean equals(Object rhs) {
		if (rhs instanceof User) {
			return this.getUsername().equals(((User) rhs).getUsername());
		}
		return false;
	}

	public int hashCode() {
		return this.getUsername().hashCode();
	}
}

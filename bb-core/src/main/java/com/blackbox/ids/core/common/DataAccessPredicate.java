package com.blackbox.ids.core.common;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.core.auth.UserAuthDetail;
import com.blackbox.ids.core.model.QRole;
import com.blackbox.ids.core.model.mdm.QApplicationBase;
import com.blackbox.ids.core.model.notification.process.QNotificationProcess;
import com.mysema.query.BooleanBuilder;

public class DataAccessPredicate {

	/** @deprecated Use {@link #getDataAccessPredicate(QApplicationBase)} instead. */
	@Deprecated
	public static BooleanBuilder getDataAccessPredicate(QApplicationBase application, QRole role) {

		BooleanBuilder predicate = null;
		Set<Long> userRoles = null;
		BlackboxUser blackboxUser = BlackboxSecurityContextHolder.getUserDetails();
		if (blackboxUser != null) {
			userRoles = blackboxUser.getRoleIds();
		}
		if (!CollectionUtils.isEmpty(userRoles)) {
			predicate = new BooleanBuilder();
			predicate.and(role.id.in(userRoles).and(application.assignee.in(role.assignees))
					.and(application.jurisdiction.in(role.jurisdictions)).and(application.customer.in(role.customers))
					.and(application.technologyGroup.in(role.technologyGroups))
					.and(application.organization.in(role.organizations)));
		}
		return predicate;
	}

	/**
	 * The APIs holds the responsibility to prepare predicate to restrict only authorize data fetch. It expects all 5
	 * access attributes in an application to match with union of data access attributes defined in user assigned roles.
	 *
	 * @param application
	 *            QApplicationBase is Querydsl query type for ApplicationBase
	 * @return Cascading builder for Predicate expressions with constraints specific to data authorization added.
	 * @author ajay2258
	 */
	public static BooleanBuilder getDataAccessPredicate(final QApplicationBase application) {
		return dataAccessPredicate(application, BlackboxSecurityContextHolder.getUserAuthData());
	}

	private static BooleanBuilder dataAccessPredicate(final QApplicationBase application, final UserAuthDetail authData) {
		return new BooleanBuilder().and(application.assignee.id.in(authData.getAssigneeIds())
				.and(application.customer.id.in(authData.getCustomerIds()))
				.and(application.technologyGroup.id.in(authData.getTechnologyGroupIds()))
				.and(application.organization.id.in(authData.getOrganizationsIds()))
				.and(application.jurisdiction.id.in(authData.getJurisdictionIds())));
	}
	
	public static BooleanBuilder getNotificationAccessPredicate(final QNotificationProcess notification) {
		return new BooleanBuilder().and(notification.roles.any().id.in(BlackboxSecurityContextHolder.getRolesAuthData().keySet()));
	}
	
}
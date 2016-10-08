package com.blackbox.ids.workflows.notification;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.notification.Notification;
import com.blackbox.ids.core.model.notification.NotificationBusinessRule;
import com.blackbox.ids.core.model.notification.NotificationBusinessRuleLevel;
import com.blackbox.ids.core.model.notification.NotificationLevelRole;
import com.blackbox.ids.services.notification.process.NotificationProcessConstant;

public final class NotificationProcessUtil {

	/**
	 * NotificationProcessUtil class. Not permitted to be instantiated.
	 * 
	 * @throws InstantiationException
	 */
	private NotificationProcessUtil() throws InstantiationException {
		throw new InstantiationException("Can't construct a static class instance.");
	}

	public static Set<Long> getReceiverRoleIds(Set<NotificationBusinessRule> businessRules, int level) {
		Set<Long> roleIds = new LinkedHashSet<>();

		for (NotificationBusinessRule businessRule : businessRules) {
			for (NotificationBusinessRuleLevel ruleLevel : businessRule.getNotificationBusinessRuleLevels()) {
				if (ruleLevel.getLevelNumber() == level) {
					for (NotificationLevelRole levelRole : ruleLevel.getNotificationRoles()) {
						if (levelRole.getType().equalsIgnoreCase(NotificationProcessConstant.RECEIVER)) {
							roleIds.add(levelRole.getUserRole().getId());
						}
					}
				}
			}
		}

		return roleIds;
	}

	public static Set<NotificationBusinessRule> getCandidateBusinessRuleIds(Notification notification,
			Set<NotificationBusinessRule> businessRules, Set<Long> senderRoleIds, int level) {
		if (CollectionUtils.isEmpty(businessRules)) {
			businessRules = notification.getNotificationRules();
		}

		Set<NotificationBusinessRule> candidateBusinessRules = new LinkedHashSet<>();
		boolean isEligible = false;

		for (NotificationBusinessRule businessRule : businessRules) {
			isEligible = false;

			for (NotificationBusinessRuleLevel ruleLevel : businessRule.getNotificationBusinessRuleLevels()) {
				if (ruleLevel.getLevelNumber() == level) {
					Set<NotificationLevelRole> levelRoles = ruleLevel.getNotificationRoles();

					for (NotificationLevelRole levelRole : levelRoles) {
						if (senderRoleIds.contains(levelRole.getUserRole().getId())
								&& levelRole.getType().equalsIgnoreCase(NotificationProcessConstant.SENDER)) {
							isEligible = true;
							break;
						}
					}
				}
			}

			if (isEligible) {
				candidateBusinessRules.add(businessRule);
			}
		}

		return candidateBusinessRules;
	}

	public static Set<Long> getSystemRoleIds(Notification notification, ApplicationBase application) {
		Set<Long> systemRoleIds = new LinkedHashSet<>();

		for (NotificationBusinessRule businessRule : notification.getNotificationRules()) {
			for (NotificationBusinessRuleLevel ruleLevel : businessRule.getNotificationBusinessRuleLevels()) {
				if (ruleLevel.getLevelNumber() == 1) {
					Set<NotificationLevelRole> levelRoles = ruleLevel.getNotificationRoles();

					for (NotificationLevelRole levelRole : levelRoles) {
						Role role = levelRole.getUserRole();
						
						/*List<Long> assigneeIds = role.getAssignees().stream().map(id -> id.getId()).collect(Collectors.toList());
						List<Long> organizationIds = role.getOrganizations().stream().map(id -> id.getId()).collect(Collectors.toList());
						List<Long> customerIds = role.getCustomers().stream().map(id -> id.getId()).collect(Collectors.toList());
						List<Long> techGroupIds = role.getTechnologyGroups().stream().map(id -> id.getId()).collect(Collectors.toList());
						List<Long> jurisdictionIds = role.getJurisdictions().stream().map(id -> id.getId()).collect(Collectors.toList());*/
						
						if (role.getAssignees().contains(application.getAssignee())
								&& role.getOrganizations().contains(application.getOrganization())
								&& role.getCustomers().contains(application.getCustomer())
								&& role.getTechnologyGroups().contains(application.getTechnologyGroup())
								&& role.getJurisdictions().contains(application.getJurisdiction())
								&& levelRole.getType().equalsIgnoreCase(NotificationProcessConstant.SENDER)) {
							systemRoleIds.add(role.getId());
						}
					}
				}
			}
		}

		return systemRoleIds;
	}
}

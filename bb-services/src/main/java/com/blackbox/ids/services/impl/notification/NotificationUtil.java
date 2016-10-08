package com.blackbox.ids.services.impl.notification;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.mstr.Organization;
import com.blackbox.ids.core.model.mstr.TechnologyGroup;
import com.blackbox.ids.core.model.notification.Notification;
import com.blackbox.ids.core.model.notification.NotificationBusinessRule;
import com.blackbox.ids.core.model.notification.NotificationBusinessRuleLevel;
import com.blackbox.ids.core.model.notification.NotificationDefaultRole;
import com.blackbox.ids.core.model.notification.NotificationEscalationRole;
import com.blackbox.ids.core.model.notification.NotificationLevelRole;
import com.blackbox.ids.dto.notification.NotificationBusinessRuleDTO;
import com.blackbox.ids.dto.notification.NotificationBusinessRuleLevelDTO;
import com.blackbox.ids.dto.notification.NotificationDTO;
import com.blackbox.ids.dto.notification.NotificationEscalationRoleDTO;
import com.blackbox.ids.dto.notification.NotificationLevelRoleDTO;
import com.blackbox.ids.dto.notification.RoleTransactionDTO;

/**
 * The Class NotificationUtil.
 */
public final class NotificationUtil {

	/** The yes. */
	private static String YES = "Yes";

	/** The no. */
	private static String NO = "No";

	/**
	 * Instantiates a new notification util.
	 */
	private NotificationUtil() {
	}

	/**
	 * Change boolean to string.
	 *
	 * @param idsReviewDisplay
	 *            the ids review display
	 * @return the string
	 */
	public static String getStringFromBoolean(boolean idsReviewDisplay) {
		String stringValue = StringUtils.EMPTY;

		if (idsReviewDisplay) {
			stringValue = YES;
		} else {
			stringValue = NO;
		}

		return stringValue;
	}

	/**
	 * Gets the notifications dt os.
	 *
	 * @param notifications
	 *            the notifications
	 * @return the notifications dt os
	 */
	public static List<NotificationDTO> getNotificationsDTOs(List<Notification> notifications) {
		List<NotificationDTO> notificationDTOs = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(notifications)) {
			for (Notification notification : notifications) {
				notificationDTOs.add(getNotificationDTO(notification));
			}
		}

		return notificationDTOs;
	}

	/**
	 * Gets the notification dto.
	 *
	 * @param notification
	 *            the notification
	 * @return the notification dto
	 */
	public static NotificationDTO getNotificationDTO(Notification notification) {
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setDisplayOnIDSReview(NotificationUtil.getStringFromBoolean(notification.isIdsReviewDisplay()));
		notificationDTO.setEmailNotification(NotificationUtil.getStringFromBoolean(notification.isEmailNotification()));
		notificationDTO.setEscalation(NotificationUtil.getStringFromBoolean(notification.isEscalation()));
		notificationDTO.setNoOfPastDueDays(notification.getNoOfPastDueDays());
		notificationDTO.setNoOfreminders(notification.getNoOfReminders());
		notificationDTO.setNotificationcode(notification.getCode());
		notificationDTO.setNotificationId(notification.getNotificationId());
		notificationDTO.setNotificationLevelNo(notification.getNotificationLevel());
		notificationDTO.setNotificationMessage(notification.getMessage());
		notificationDTO.setNotificationName(notification.getName());
		notificationDTO.setNotificationSubject(notification.getSubject());
		notificationDTO.setNotificationType(notification.getType());
		notificationDTO.setReminder(NotificationUtil.getStringFromBoolean(notification.isReminder()));
		notificationDTO.setFrequencyOfSendingRemindes(notification.getFrequencyOfSendingReminders());
		notificationDTO.setBusinessRuleDtos(getBusinessRuleDtos(notification.getNotificationRules()));
		notificationDTO.setEscalationRoles(getEscalationRoleDto(notification.getEscalationRoles()));
		notificationDTO.setDefaultRoles(getDefaultRoleDto(notification.getDefaultRoles()));
		notificationDTO.setNoOfBusinessRules(notification.getNotificationRules().size());

		return notificationDTO;
	}

	/**
	 * Gets the escalation role dto.
	 *
	 * @param escalationRoles
	 *            the escalation roles
	 * @return the escalation role dto
	 */
	private static Set<NotificationEscalationRoleDTO> getEscalationRoleDto(
			Set<NotificationEscalationRole> escalationRoles) {
		Set<NotificationEscalationRoleDTO> escalationDtos = new LinkedHashSet<>();

		for (NotificationEscalationRole escalationRole : escalationRoles) {
			NotificationEscalationRoleDTO escalationRoleDto = new NotificationEscalationRoleDTO();
			escalationRoleDto.setName(escalationRole.getRole().getName());
			escalationRoleDto.setRoleId(escalationRole.getRole().getId());
			escalationDtos.add(escalationRoleDto);
		}

		return escalationDtos;
	}

	/**
	 * Gets the escalation role dto.
	 *
	 * @param set
	 *            the escalation roles
	 * @return the escalation role dto
	 */
	private static Set<NotificationEscalationRoleDTO> getDefaultRoleDto(Set<NotificationDefaultRole> defaultRoles) {
		Set<NotificationEscalationRoleDTO> escalationDtos = new LinkedHashSet<>();

		for (NotificationDefaultRole escalationRole : defaultRoles) {
			NotificationEscalationRoleDTO escalationRoleDto = new NotificationEscalationRoleDTO();
			escalationRoleDto.setName(escalationRole.getRole().getName());
			escalationRoleDto.setRoleId(escalationRole.getRole().getId());
			escalationDtos.add(escalationRoleDto);
		}

		return escalationDtos;
	}

	/**
	 * Gets the business rule dtos.
	 *
	 * @param businessRules
	 *            the business rules
	 * @return the business rule dtos
	 */
	private static Set<NotificationBusinessRuleDTO> getBusinessRuleDtos(Set<NotificationBusinessRule> businessRules) {
		List<NotificationBusinessRule> sortedBusinessRules = new ArrayList<>(businessRules);

		Set<NotificationBusinessRuleDTO> businessRuleDtos = new LinkedHashSet<>();

		for (NotificationBusinessRule businessRule : sortedBusinessRules) {
			businessRuleDtos.add(getNotificationBusinessRuleDto(businessRule));
		}

		return businessRuleDtos;
	}

	/**
	 * Gets the notification business rule dto.
	 *
	 * @param businessRule
	 *            the business rule
	 * @return the notification business rule dto
	 */
	public static NotificationBusinessRuleDTO getNotificationBusinessRuleDto(NotificationBusinessRule businessRule) {
		NotificationBusinessRuleDTO notificationBusinessRuleDTO = new NotificationBusinessRuleDTO();
		notificationBusinessRuleDTO.setNotificationBusinessRuleId(businessRule.getNotificationBusinessRuleId());

		List<NotificationBusinessRuleLevelDTO> businessLevelDtos = new ArrayList<>();
		List<NotificationBusinessRuleLevel> sortedBusinessLevels = new ArrayList<>(businessRule.getNotificationBusinessRuleLevels());

		for (NotificationBusinessRuleLevel businessLevel : sortedBusinessLevels) {
			businessLevelDtos.add(getBusinessLevelDto(businessLevel));
		}

		notificationBusinessRuleDTO.setBusinessLevelDto(businessLevelDtos);

		return notificationBusinessRuleDTO;
	}

	/**
	 * Gets the business level dto.
	 *
	 * @param businessLevel
	 *            the business level
	 * @return the business level dto
	 */
	private static NotificationBusinessRuleLevelDTO getBusinessLevelDto(NotificationBusinessRuleLevel businessLevel) {
		NotificationBusinessRuleLevelDTO businessLevelDto = new NotificationBusinessRuleLevelDTO();
		businessLevelDto.setBusinessLevelDtoId(businessLevel.getNotificationBusinessRuleLevelId());
		businessLevelDto.setCurrentLevelNo(businessLevel.getLevelNumber());

		List<NotificationLevelRoleDTO> levelRoleDTOs = new ArrayList<>();

		for (NotificationLevelRole levelRole : businessLevel.getNotificationRoles()) {
			NotificationLevelRoleDTO levelRoleDto = new NotificationLevelRoleDTO();

			levelRoleDto.setLevelRoleId(levelRole.getNotificationLevelRoleId());
			levelRoleDto.setType(levelRole.getType());
			levelRoleDto.setRoleId(levelRole.getUserRole().getId());
			levelRoleDto.setRoleName(levelRole.getUserRole().getName());

			List<RoleTransactionDTO> jurisdictionDtos = new ArrayList<>();
			List<RoleTransactionDTO> assigneeDtos = new ArrayList<>();
			List<RoleTransactionDTO> customerDtos = new ArrayList<>();
			List<RoleTransactionDTO> technologyGroupDtos = new ArrayList<>();
			List<RoleTransactionDTO> organizationsDtos = new ArrayList<>();

			for (Jurisdiction jurisdiction : levelRole.getUserRole().getJurisdictions()) {
				RoleTransactionDTO jurisdictionDto = new RoleTransactionDTO();
				jurisdictionDto.setId(jurisdiction.getId());
				jurisdictionDto.setName(jurisdiction.getCode());
				jurisdictionDtos.add(jurisdictionDto);
			}

			for (Assignee assignee : levelRole.getUserRole().getAssignees()) {
				RoleTransactionDTO assigneeDto = new RoleTransactionDTO();
				assigneeDto.setId(assignee.getId());
				assigneeDto.setName(assignee.getName());
				assigneeDtos.add(assigneeDto);
			}

			for (Customer customer : levelRole.getUserRole().getCustomers()) {
				RoleTransactionDTO customerDto = new RoleTransactionDTO();
				customerDto.setId(customer.getId());
				customerDto.setName(customer.getCustomerNumber());
				customerDtos.add(customerDto);
			}

			for (TechnologyGroup technologyGroup : levelRole.getUserRole().getTechnologyGroups()) {
				RoleTransactionDTO techGroupDto = new RoleTransactionDTO();
				techGroupDto.setId(technologyGroup.getId());
				techGroupDto.setName(technologyGroup.getName());
				technologyGroupDtos.add(techGroupDto);
			}

			for (Organization organization : levelRole.getUserRole().getOrganizations()) {
				RoleTransactionDTO organizationDto = new RoleTransactionDTO();
				organizationDto.setId(organization.getId());
				organizationDto.setName(organization.getName());
				organizationsDtos.add(organizationDto);
			}

			levelRoleDto.setJurisdictions(jurisdictionDtos);
			levelRoleDto.setAssignees(assigneeDtos);
			levelRoleDto.setCustomerNos(customerDtos);
			levelRoleDto.setTechGroups(technologyGroupDtos);
			levelRoleDto.setOrganisations(organizationsDtos);
			levelRoleDTOs.add(levelRoleDto);
		}

		businessLevelDto.setLevelRoleDtos(levelRoleDTOs);

		return businessLevelDto;
	}

	/**
	 * Gets the notification from dto.
	 *
	 * @param notification
	 *            the notification
	 * @param notificationDto
	 *            the notification dto
	 * @return the notification from dto
	 */
	public static void populateNotificationFromDto(Notification notification, NotificationDTO notificationDto) {
		notification.setCode(notificationDto.getNotificationcode());
		notification.setName(notificationDto.getNotificationName());
		notification.setSubject(notificationDto.getNotificationSubject());
		notification.setMessage(notificationDto.getNotificationMessage());
		notification.setType(notificationDto.getNotificationType());
		notification.setNoOfReminders(notificationDto.getNoOfreminders());
		notification.setEmailNotification(getBooleanFromString(notificationDto.getEmailNotification()));
		notification.setIdsReviewDisplay(getBooleanFromString(notificationDto.getDisplayOnIDSReview()));
		notification.setEscalation(getBooleanFromString(notificationDto.getEscalation()));
		notification.setReminder(getBooleanFromString(notificationDto.getReminder()));
		notification.setFrequencyOfSendingReminders(notificationDto.getFrequencyOfSendingRemindes());
		notification.setNoOfPastDueDays(notificationDto.getNoOfPastDueDays());
		notification.setCreatedByUser(notificationDto.getUpdatedBy());
		notification.setCreatedDate(notificationDto.getUpdatedOn());
		notification.setNotificationLevel(notificationDto.getNotificationLevelNo());
		notification.setActive(true);

		populateEscalationRoles(notification, notificationDto.getEscalationRoles());
		populateDefaultRoles(notification, notificationDto.getDefaultRoles());

		if (CollectionUtils.isNotEmpty(notificationDto.getBusinessRuleDtos())) {
			notification.getNotificationRules().clear();
			for (NotificationBusinessRuleDTO notificationBusinessRuleDTO : notificationDto.getBusinessRuleDtos()) {
				NotificationBusinessRule businessRule = new NotificationBusinessRule();
				populateNotificationBusinessRule(businessRule, notificationBusinessRuleDTO);
				notification.getNotificationRules().add(businessRule);
			}
		}
	}

	/**
	 * Gets the escalatioin roles.
	 *
	 * @param notification
	 *            the notification
	 * @param escalationRoleDtos
	 *            the escalation role dtos
	 * @return the escalatioin roles
	 */
	private static void populateEscalationRoles(Notification notification,
			Set<NotificationEscalationRoleDTO> escalationRoleDtos) {
		notification.getEscalationRoles().clear();

		if (CollectionUtils.isNotEmpty(escalationRoleDtos)) {
			for (NotificationEscalationRoleDTO escalationRoleDto : escalationRoleDtos) {
				NotificationEscalationRole escalationRole = new NotificationEscalationRole();
				escalationRole.setRole(new Role(escalationRoleDto.getRoleId()));
				notification.getEscalationRoles().add(escalationRole);
			}
		}
	}

	/**
	 * Gets the escalatioin roles.
	 *
	 * @param notification
	 *            the notification
	 * @param escalationRoleDtos
	 *            the escalation role dtos
	 * @return the escalatioin roles
	 */
	private static void populateDefaultRoles(Notification notification,
			Set<NotificationEscalationRoleDTO> escalationRoleDtos) {
		notification.getDefaultRoles().clear();

		if (CollectionUtils.isNotEmpty(escalationRoleDtos)) {
			for (NotificationEscalationRoleDTO escalationRoleDto : escalationRoleDtos) {
				NotificationDefaultRole defaultRole = new NotificationDefaultRole();
				defaultRole.setRole(new Role(escalationRoleDto.getRoleId()));
				notification.getDefaultRoles().add(defaultRole);
			}
		}
	}

	/**
	 * Gets the boolean from string.
	 *
	 * @param stringValue
	 *            the string value
	 * @return the boolean from string
	 */
	private static boolean getBooleanFromString(String stringValue) {
		boolean booleanValue = false;

		if (stringValue != null && (stringValue.equalsIgnoreCase("on") || stringValue.equalsIgnoreCase("yes"))) {
			booleanValue = true;
		}

		return booleanValue;
	}

	/**
	 * Gets the notification business rule.
	 *
	 * @param businessRule
	 *            the business rule
	 * @param businessRuleDto
	 *            the business rule dto
	 * @return the notification business rule
	 */
	public static NotificationBusinessRule populateNotificationBusinessRule(NotificationBusinessRule businessRule,
			NotificationBusinessRuleDTO businessRuleDto) {
		populateBusinessLevel(businessRuleDto.getBusinessLevelDto(), businessRule);

		return businessRule;
	}

	/**
	 * Gets the business level.
	 *
	 * @param businessLevelDtos
	 *            the business level dtos
	 * @param businessRule
	 *            the business rule
	 * @return the business level
	 */
	private static void populateBusinessLevel(List<NotificationBusinessRuleLevelDTO> businessLevelDtos,
			NotificationBusinessRule businessRule) {
		for (NotificationBusinessRuleLevelDTO businessLevelDto : businessLevelDtos) {
			NotificationBusinessRuleLevel businessLevel = new NotificationBusinessRuleLevel();
			businessLevel.setLevelNumber(businessLevelDto.getCurrentLevelNo());
			populateLevelRoles(businessLevelDto.getLevelRoleDtos(), businessLevel);
			businessRule.getNotificationBusinessRuleLevels().add(businessLevel);
		}
	}

	/**
	 * Gets the level roles.
	 *
	 * @param levelRoleDtos
	 *            the level role dtos
	 * @param businessLevel
	 *            the business level
	 */
	private static void populateLevelRoles(List<NotificationLevelRoleDTO> levelRoleDtos,
			NotificationBusinessRuleLevel businessLevel) {
		for (NotificationLevelRoleDTO levelRoleDto : levelRoleDtos) {
			NotificationLevelRole levelRole = new NotificationLevelRole();
			levelRole.setType(levelRoleDto.getType());
			levelRole.setUserRole(new Role(levelRoleDto.getRoleId()));
			businessLevel.getNotificationRoles().add(levelRole);
		}
	}

	public static Notification getClonedNotification(Notification notification) {
		Notification clonedNotification = SerializationUtils.clone(notification);
		clonedNotification.setNotificationId(null);
		clonedNotification.setActive(true);
		clonedNotification.setUpdatedByUser(null);
		clonedNotification.setUpdatedDate(null);
		clonedNotification.setCreatedByUser(null);
		clonedNotification.setCreatedDate(null);
		clonedNotification.setVersion(clonedNotification.getVersion() + 1);

		for (NotificationEscalationRole escRole : clonedNotification.getEscalationRoles()) {
			escRole.setNotificationEscalationRoleId(null);
		}

		for (NotificationDefaultRole defRole : clonedNotification.getDefaultRoles()) {
			defRole.setNotificationDefaultRoleId(null);
		}

		for (NotificationBusinessRule notificationBusinessRule : clonedNotification.getNotificationRules()) {
			notificationBusinessRule.setNotificationBusinessRuleId(null);

			for (NotificationBusinessRuleLevel businessRuleLevel : notificationBusinessRule
					.getNotificationBusinessRuleLevels()) {
				businessRuleLevel.setNotificationBusinessRuleLevelId(null);

				for (NotificationLevelRole levelRole : businessRuleLevel.getNotificationRoles()) {
					levelRole.setNotificationLevelRoleId(null);
				}
			}
		}

		return clonedNotification;
	}
}

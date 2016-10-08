package com.blackbox.ids.services.impl.notification;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.mstr.Organization;
import com.blackbox.ids.core.model.mstr.TechnologyGroup;
import com.blackbox.ids.core.model.notification.Notification;
import com.blackbox.ids.core.model.notification.NotificationBusinessRule;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.repository.RoleRepository;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.core.repository.notification.NotificationBusinessRuleRepository;
import com.blackbox.ids.core.repository.notification.NotificationRepository;
import com.blackbox.ids.dto.notification.NotificationBusinessRuleDTO;
import com.blackbox.ids.dto.notification.NotificationBusinessRuleLevelDTO;
import com.blackbox.ids.dto.notification.NotificationDTO;
import com.blackbox.ids.dto.notification.NotificationLevelRoleDTO;
import com.blackbox.ids.dto.notification.RoleTransactionDTO;
import com.blackbox.ids.services.notification.NotificationService;

/**
 * The Class NotificationServiceImpl.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

	/** The log. */
	private Logger log = Logger.getLogger(NotificationServiceImpl.class);

	/** The notification repository. */
	@Autowired
	private NotificationRepository notificationRepository;

	/** The role repository. */
	@Autowired
	private RoleRepository roleRepository;

	/** The business rule repository. */
	@Autowired
	private NotificationBusinessRuleRepository businessRuleRepository;
	
	@Autowired
	UserRepository userRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.notification.NotificationService#getAllNotification()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<NotificationDTO> getAllNotification() throws ApplicationException {
		List<Notification> notifications = null;

		try {
			notifications = notificationRepository.findNotificationByActiveOrderByNotificationIdDesc(true);
		} catch (DataAccessException exception) {
			log.error("Error Occured while fetching notificaitons");
			throw new ApplicationException(exception);
		}

		return NotificationUtil.getNotificationsDTOs(notifications);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.notification.NotificationService#getNotificationById(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public NotificationDTO getNotificationById(long notificationId) throws ApplicationException {
		Notification notification = getNotificationFromId(notificationId);
		return NotificationUtil.getNotificationDTO(notification);
	}

	/**
	 * Gets the notification from id.
	 *
	 * @param notificationId
	 *            the notification id
	 * @return the notification from id
	 * @throws ApplicationException
	 *             the application exception
	 */
	private Notification getNotificationFromId(long notificationId) throws ApplicationException {
		Notification notification = null;

		try {
			notification = notificationRepository.findByNotificationIdAndActive(notificationId, true);
		} catch (DataAccessException exception) {
			log.error("Error occured while fetching notification by id");
			throw new ApplicationException(exception);
		}

		if (notification == null) {
			log.error("Not able to find notification for notification Id: " + notificationId);
			throw new ApplicationException("Not able to find notification for notification Id: " + notificationId);
		}

		return notification;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.notification.NotificationService#saveOrUpdateNotification(com.blackbox.ids.dto.
	 * notification.NotificationDTO)
	 */
	@Override
	@Transactional
	public long saveOrUpdateNotification(NotificationDTO notificationDto) throws ApplicationException {
		Notification newNotification = null;
		notificationDto.setUpdatedBy(userRepository.getId(notificationDto.getUpdatedByUsename()));

		if (notificationDto.getNotificationId() != null) {
			newNotification = NotificationUtil.getClonedNotification(getNotificationFromId(notificationDto.getNotificationId()));
			deactivateNotification(notificationDto.getUpdatedBy(), notificationDto.getUpdatedOn(), notificationDto.getNotificationId());
			newNotification.setParentId(notificationDto.getNotificationId());
		} else {
			newNotification = new Notification();
		}

		NotificationUtil.populateNotificationFromDto(newNotification, notificationDto);

		return saveNotification(newNotification).getNotificationId();
	}

	/**
	 * Save notification version.
	 *
	 * @param notificationId
	 *            the notification id
	 * @return the notification
	 */
	private void deactivateNotification(long updatedBy, Calendar updatedDate, long notificationId) {
		notificationRepository.deactivateNotificationById(updatedBy, updatedDate, notificationId);
	}

	/**
	 * Save notification.
	 *
	 * @param notification
	 *            the notification
	 * @return the notification
	 * @throws ApplicationException
	 *             the application exception
	 */
	private Notification saveNotification(Notification notification) throws ApplicationException {
		try {
			return notificationRepository.save(notification);
		} catch (DataAccessException exception) {
			log.error("Error occured while saving notification");
			throw new ApplicationException(exception);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.notification.NotificationService#deleteNotificationBusinessRule(long)
	 */
	@Override
	@Transactional
	public long deleteNotificationBusinessRule(long notificationId, long businessRuleId, Calendar deletedOn, 
			String DeletedBy) throws ApplicationException {
		Long deletedUserId = userRepository.getId(DeletedBy);
		Notification tempNotification = (Notification) SerializationUtils.clone(getNotificationFromId(notificationId));
		deactivateNotification(deletedUserId, deletedOn, notificationId);
		deleteBusinessRule(businessRuleId, tempNotification);
		
		Notification newNotification = NotificationUtil.getClonedNotification(tempNotification);
		newNotification.setParentId(notificationId);
		newNotification.setCreatedByUser(deletedUserId);
		newNotification.setCreatedDate(deletedOn);
		saveNotification(newNotification);
		
		return newNotification.getNotificationId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blackbox.ids.services.notification.NotificationService#getNotificationBusinessRule(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public NotificationBusinessRuleDTO getNotificationBusinessRule(long businessRuleId) throws ApplicationException {
		NotificationBusinessRule businessRule = null;

		try {
			businessRule = businessRuleRepository.findOne(businessRuleId);
		} catch (Exception exception) {
			log.error("Error occred while getting business rule for id: " + businessRuleId);
			throw new ApplicationException(exception);
		}

		return NotificationUtil.getNotificationBusinessRuleDto(businessRule);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.notification.NotificationService#saveOrUpdateNotificationBusinessRule(com.blackbox.ids.
	 * dto.notification.NotificationBusinessRuleDTO)
	 */
	@Override
	@Transactional
	public long saveOrUpdateNotificationBusinessRule(NotificationBusinessRuleDTO businessRuleDto)
			throws ApplicationException {
		businessRuleDto.setCreatedBy(userRepository.getId(businessRuleDto.getUpdatedByUsename()));
		
		Notification tempNotification = (Notification) SerializationUtils.clone(getNotificationFromId(businessRuleDto.getNotificationId()));
		deactivateNotification(businessRuleDto.getCreatedBy(), businessRuleDto.getCreatedOn(), businessRuleDto.getNotificationId());

		if (businessRuleDto.getNotificationBusinessRuleId() != null) {
			deleteBusinessRule(businessRuleDto.getNotificationBusinessRuleId(), tempNotification);
		}

		NotificationBusinessRule businessRule = new NotificationBusinessRule();

		if (businessRuleDto.isSystemInitiated()) {
			setSystemRole(businessRuleDto);
		}

		// Populate does not populate businessRule Model thats why we need to return it.
		businessRule = NotificationUtil.populateNotificationBusinessRule(businessRule, businessRuleDto);
		Notification newNotification = NotificationUtil.getClonedNotification(tempNotification);
		newNotification.getNotificationRules().add(businessRule);
		newNotification.setParentId(businessRuleDto.getNotificationId());
		newNotification.setCreatedByUser(businessRuleDto.getCreatedBy());
		newNotification.setCreatedDate(businessRuleDto.getCreatedOn());

		return saveNotification(newNotification).getNotificationId();
	}

	/**
	 * Delete business rule.
	 *
	 * @param businessRuleId
	 *            the business rule id
	 * @param tempNotification
	 *            the temp notification
	 */
	private void deleteBusinessRule(long businessRuleId, Notification tempNotification) {
		NotificationBusinessRule businessRule = null;

		for (NotificationBusinessRule notificationRule : tempNotification.getNotificationRules()) {
			if (Long.compare(notificationRule.getNotificationBusinessRuleId(), businessRuleId) == 0) {
				businessRule = notificationRule;
				break;
			}
		}

		tempNotification.getNotificationRules().remove(businessRule);
	}

	/**
	 * Sets the system role.
	 *
	 * @param businessRuleDto
	 *            the new system role
	 */
	private void setSystemRole(NotificationBusinessRuleDTO businessRuleDto) {
		for (NotificationBusinessRuleLevelDTO levelDto : businessRuleDto.getBusinessLevelDto()) {
			for (NotificationLevelRoleDTO leveRoleDto : levelDto.getLevelRoleDtos()) {
				if (Long.compare(leveRoleDto.getRoleId(), 0) <= 0) {
					Role role = getRoleFromDto(leveRoleDto, businessRuleDto.getCreatedBy());
					roleRepository.save(role);
					leveRoleDto.setRoleId(role.getId());
				}
			}
		}
	}

	/**
	 * Gets the role from dto.
	 *
	 * @param leveRoleDto
	 *            the leve role dto
	 * @param createdBy
	 *            the created by
	 * @return the role from dto
	 */
	private Role getRoleFromDto(NotificationLevelRoleDTO leveRoleDto, long createdBy) {
		Role role = new Role();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.YEAR, 10);
		role.setName("System Role");
		role.setDescription("Create for System Initiated Notifications");
		role.setEndDate(endDate);
		role.setActive(true);
		role.setCreatedByUser(createdBy);
		role.setCreatedDate(Calendar.getInstance());
		role.setSystemRole(true);

		for (RoleTransactionDTO roleTran : leveRoleDto.getJurisdictions()) {
			Jurisdiction jurisdiction = new Jurisdiction();
			jurisdiction.setId(roleTran.getId());
			role.getJurisdictions().add(jurisdiction);
		}

		for (RoleTransactionDTO roleTran : leveRoleDto.getAssignees()) {
			Assignee assignee = new Assignee();
			assignee.setId(roleTran.getId());
			role.getAssignees().add(assignee);
		}

		for (RoleTransactionDTO roleTran : leveRoleDto.getCustomerNos()) {
			Customer customer = new Customer();
			customer.setId(roleTran.getId());
			role.getCustomers().add(customer);
		}

		for (RoleTransactionDTO roleTran : leveRoleDto.getOrganisations()) {
			Organization organization = new Organization();
			organization.setId(roleTran.getId());
			role.getOrganizations().add(organization);
		}

		for (RoleTransactionDTO roleTran : leveRoleDto.getTechGroups()) {
			TechnologyGroup techGroup = new TechnologyGroup();
			techGroup.setId(roleTran.getId());
			role.getTechnologyGroups().add(techGroup);
		}

		return role;
	}

	@Override
	@Transactional(readOnly = true)
	public String getNotificationMessage(NotificationProcessType notificationProcessType) {
		return notificationRepository.getNotificationMessage(notificationProcessType);
	}
}

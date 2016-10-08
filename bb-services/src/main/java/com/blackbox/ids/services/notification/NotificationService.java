package com.blackbox.ids.services.notification;

import java.util.Calendar;
import java.util.List;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.dto.notification.NotificationBusinessRuleDTO;
import com.blackbox.ids.dto.notification.NotificationDTO;

/**
 * The Interface NotificationService.
 */
public interface NotificationService {

	/**
	 * Gets the all notification.
	 *
	 * @return the all notification
	 * @throws ApplicationException
	 *             the application exception
	 */
	List<NotificationDTO> getAllNotification() throws ApplicationException;

	/**
	 * Gets the notification by id.
	 *
	 * @param notificationId
	 *            the notification id
	 * @return the notification by id
	 * @throws ApplicationException
	 *             the application exception
	 */
	NotificationDTO getNotificationById(long notificationId) throws ApplicationException;

	/**
	 * Save or update notification.
	 *
	 * @param notificationDto
	 *            the notification dto
	 * @return the long
	 * @throws ApplicationException
	 *             the application exception
	 */
	long saveOrUpdateNotification(NotificationDTO notificationDto) throws ApplicationException;

	/**
	 * Delete business rule.
	 *
	 * @param businessRuleId
	 *            the business rule id
	 * @throws ApplicationException
	 *             the application exception
	 */
	long deleteNotificationBusinessRule(long notificationId, long businessRuleId, Calendar deletedOn, String DeletedBy) throws ApplicationException;

	/**
	 * Gets the notification business rule.
	 *
	 * @param businessRuleId
	 *            the business rule id
	 * @return the notification business rule
	 * @throws ApplicationException
	 *             the application exception
	 */
	NotificationBusinessRuleDTO getNotificationBusinessRule(long businessRuleId) throws ApplicationException;

	/**
	 * Save or update notification business rule.
	 *
	 * @param businessRuleDto
	 *            the business rule dto
	 * @return the long
	 * @throws ApplicationException
	 *             the application exception
	 */
	long saveOrUpdateNotificationBusinessRule(NotificationBusinessRuleDTO businessRuleDto) throws ApplicationException;
	
	/**
	 * Gets the notification message.
	 *
	 * @param type the type
	 * @return the notification message
	 */
	String getNotificationMessage(NotificationProcessType type);
}

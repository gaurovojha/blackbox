package com.blackbox.ids.core.repository.notification;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.blackbox.ids.core.model.notification.Notification;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;

/**
 * The Interface NotificationRepository.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	/**
	 * Find notification by active order by notification id desc.
	 *
	 * @param active
	 *            the active
	 * @return the list
	 */
	List<Notification> findNotificationByActiveOrderByNotificationIdDesc(boolean active);

	/**
	 * Find by notification id and active.
	 *
	 * @param notificationId
	 *            the notification id
	 * @param active
	 *            the active
	 * @return the notification
	 */
	Notification findByNotificationIdAndActive(long notificationId, boolean active);

	/**
	 * Find by type and active.
	 *
	 * @param type
	 *            the type
	 * @param active
	 *            the active
	 * @return the notification
	 */
	Notification findByTypeAndActive(String type, boolean active);

	/**
	 * Find by notification process type and active.
	 *
	 * @param type
	 *            the type
	 * @param active
	 *            the active
	 * @return the notification
	 */
	Notification findByNotificationProcessTypeAndActive(NotificationProcessType type, boolean active);

	/**
	 * Deactivate notification by id.
	 *
	 * @param updatedBy
	 *            the updated by
	 * @param updatedDate
	 *            the updated date
	 * @param notificationId
	 *            the notification id
	 */
	@Modifying
	@Query("update Notification notification set notification.updatedByUser = ?1, notification.updatedDate = ?2, notification.active = false where notification.notificationId = ?3")
	void deactivateNotificationById(Long updatedBy, Calendar updatedDate, Long notificationId);

	/**
	 * Gets the notification message.
	 *
	 * @param notificationType
	 *            the notification type
	 * @return the notification message
	 */
	@Query("select n.message from Notification n where n.notificationProcessType = ?1 and n.active = 'true'")
	String getNotificationMessage(NotificationProcessType notificationProcessType);
}

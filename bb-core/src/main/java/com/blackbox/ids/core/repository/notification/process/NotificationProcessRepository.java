package com.blackbox.ids.core.repository.notification.process;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcess;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;

/**
 * The Interface NotificationProcessRepository.
 */
public interface NotificationProcessRepository extends JpaRepository<NotificationProcess, Long>,
		QueryDslPredicateExecutor<NotificationProcess>, NotificationProcessCustomRepository {

	/**
	 * Gets the activiti pro ids by notification pro type.
	 *
	 * @param notificationProType
	 *            the notification pro type
	 * @return the activiti pro ids by notification pro type
	 */
	@Query("select activitiProcessId from NotificationProcess np where np.notificationProcessType = ?1")
	List<Long> getActivitiProIdsByNotificationProType(String notificationProType);

	/**
	 * Gets the active notifications by entity name and role.
	 *
	 * @param entityName
	 *            the entity name
	 * @param roles
	 *            the roles
	 * @return the active notifications by entity name and role
	 */
	@Query("Select notificationProcess.entityId, notificationProcess.lockedByUser, notificationProcess.createdDate from NotificationProcess notificationProcess inner join notificationProcess.roles nrole where notificationProcess.entityName =:entityName and notificationProcess.active = true and nrole.id IN :roles")
	List<NotificationProcess> getActiveNotificationsByEntityNameAndRole(@Param("entityName") EntityName entityName,
			@Param("roles") List<Long> roles);

	/**
	 * Gets the activiti process id by np id.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @return the activiti process id by np id
	 */
	@Query("select activitiProcessId from NotificationProcess np where np.notificationProcessId = ?1")
	String getActivitiProcessIdByNPId(Long notificationProcessId);

	/**
	 * Gets the notification process id by activiti id.
	 *
	 * @param activitiProcessId
	 *            the activiti process id
	 * @return the notification process id by activiti id
	 */
	@Query("select np.notificationProcessId from NotificationProcess np where np.activitiProcessId = ?1")
	Long getNotificationProcessIdByActivitiId(String activitiProcessId);

	/**
	 * Complete notification process.
	 *
	 * @param updatedBy
	 *            the updated by
	 * @param updatedDate
	 *            the updated date
	 * @param activitiProcessId
	 *            the activiti process id
	 * @param status
	 *            the status
	 */
	@Modifying
	@Query("update NotificationProcess np set np.updatedByUser = ?1, np.updatedDate = ?2, np.active = false, np.status = ?4 where np.activitiProcessId = ?3")
	void completeNotificationProcess(Long updatedBy, Calendar updatedDate, String activitiProcessId,
			NotificationStatus status);

	/**
	 * Delete notification process.
	 *
	 * @param updatedBy
	 *            the updated by
	 * @param updatedDate
	 *            the updated date
	 * @param notificationProcessId
	 *            the notification process id
	 */
	@Modifying
	@Query("update NotificationProcess np set np.updatedByUser = ?1, np.updatedDate = ?2, np.active = false, np.status = 'DROPPED' where np.notificationProcessId = ?3")
	void deleteNotificationProcess(Long updatedBy, Calendar updatedDate, Long notificationProcessId);

	/**
	 * Assign notification process.
	 *
	 * @param lockedByUser
	 *            the locked by user
	 * @param updatedDate
	 *            the updated date
	 * @param updatedByUser
	 *            the updated by user
	 * @param notificationProcessId
	 *            the notification process id
	 */
	@Modifying
	@Query("update NotificationProcess np set np.lockedByUser = ?1, np.updatedDate = ?2, np.updatedByUser = ?3 where np.notificationProcessId = ?4")
	void assignNotificationProcess(User lockedByUser, Calendar updatedDate, Long updatedByUser,
			Long notificationProcessId);

	/**
	 * Unassign notification process.
	 *
	 * @param updatedBy
	 *            the updated by
	 * @param updatedDate
	 *            the updated date
	 * @param notificationProcessId
	 *            the notification process id
	 */
	@Modifying
	@Query("update NotificationProcess np set np.updatedByUser = ?1, np.updatedDate = ?2, np.lockedByUser = NULL where np.notificationProcessId = ?3")
	void unassignNotificationProcess(Long updatedBy, Calendar updatedDate, Long notificationProcessId);

	/**
	 * Gets the notifications by id and role.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @param roles
	 *            the roles
	 * @return the notifications by id and role
	 */
	@Query("Select notificationProcess.notificationProcessId from NotificationProcess notificationProcess inner join notificationProcess.roles nrole where notificationProcess.notificationProcessId =:notificationProcessId and nrole.id IN :roles")
	Long getNotificationsByIdAndRole(@Param("notificationProcessId") Long notificationProcessId,
			@Param("roles") Set<Long> roles);

	/**
	 * Delete notification for app id.
	 *
	 * @param updatedBy
	 *            the updated by
	 * @param updatedDate
	 *            the updated date
	 * @param applicationId
	 *            the application id
	 */
	@Modifying
	@Query("update NotificationProcess np set np.updatedByUser = ?1, np.updatedDate = ?2, np.active = false, np.status = 'DROPPED' where np.applicationId = ?3 and np.active = 'true'")
	void deleteNotificationForAppId(Long updatedBy, Calendar updatedDate, Long applicationId);

	/**
	 * Delete notification for corres id.
	 *
	 * @param updatedBy
	 *            the updated by
	 * @param updatedDate
	 *            the updated date
	 * @param correspondenceId
	 *            the correspondence id
	 */
	@Modifying
	@Query("update NotificationProcess np set np.updatedByUser = ?1, np.updatedDate = ?2, np.active = false, np.status = 'DROPPED' where np.correspondenceId = ?3 and np.active = 'true'")
	void deleteNotificationForCorresId(Long updatedBy, Calendar updatedDate, Long correspondenceId);

	/**
	 * Delete notification for ref id.
	 *
	 * @param updatedBy
	 *            the updated by
	 * @param updatedDate
	 *            the updated date
	 * @param referenceId
	 *            the reference id
	 */
	@Modifying
	@Query("update NotificationProcess np set np.updatedByUser = ?1, np.updatedDate = ?2, np.active = false, np.status = 'DROPPED' where np.referenceId = ?3 and np.active = 'true'")
	void deleteNotificationForRefId(Long updatedBy, Calendar updatedDate, Long referenceId);

	/**
	 * Gets the activiti process id by app id.
	 *
	 * @param applicationId
	 *            the application id
	 * @return the activiti process id by app id
	 */
	@Query("select activitiProcessId from NotificationProcess np where np.applicationId = ?1 and np.status = 'PENDING' and np.active = 'true'")
	List<String> getActivitiProcessIdByAppId(Long applicationId);

	/**
	 * Gets the notification process id by app id.
	 *
	 * @param applicationId
	 *            the application id
	 * @return the notification process id by app id
	 */
	@Query("select notificationProcessId from NotificationProcess np where np.applicationId = ?1 and np.status = 'PENDING' and np.active = 'true'")
	List<Long> getNotificationProcessIdByAppId(Long applicationId);

	/**
	 * Gets the activiti process id by corres id.
	 *
	 * @param correspondenceId
	 *            the correspondence id
	 * @return the activiti process id by corres id
	 */
	@Query("select activitiProcessId from NotificationProcess np where np.correspondenceId = ?1 and np.status = 'PENDING' and np.active = 'true'")
	List<String> getActivitiProcessIdByCorresId(Long correspondenceId);

	/**
	 * Gets the notification process id by corres id.
	 *
	 * @param correspondenceId
	 *            the correspondence id
	 * @return the notification process id by corres id
	 */
	@Query("select notificationProcessId from NotificationProcess np where np.correspondenceId = ?1 and np.status = 'PENDING' and np.active = 'true'")
	List<Long> getNotificationProcessIdByCorresId(Long correspondenceId);

	/**
	 * Gets the activiti process id by ref id.
	 *
	 * @param referenceId
	 *            the reference id
	 * @return the activiti process id by ref id
	 */
	@Query("select activitiProcessId from NotificationProcess np where np.referenceId = ?1 and np.status = 'PENDING' and np.active = 'true'")
	List<String> getActivitiProcessIdByRefId(Long referenceId);

	/**
	 * Gets the notification process id by ref id.
	 *
	 * @param referenceId
	 *            the reference id
	 * @return the notification process id by ref id
	 */
	@Query("select notificationProcessId from NotificationProcess np where np.referenceId = ?1 and np.status = 'PENDING' and np.active = 'true'")
	List<Long> getNotificationProcessIdByRefId(Long referenceId);

	/**
	 * Gets the entity id by roles and process id.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @param roles
	 *            the roles
	 * @return the entity id by roles and process id
	 */
	@Query("Select notificationProcess.entityId from NotificationProcess notificationProcess inner join notificationProcess.roles nrole where notificationProcess.notificationProcessId = :notificationProcessId  and nrole.id IN :roles ")
	Long getEntityIdByRolesAndProcessId(@Param("notificationProcessId") Long notificationProcessId,
			@Param("roles") Set<Long> roles);
}

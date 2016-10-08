package com.blackbox.ids.core.repository.notification.process;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.mysema.query.types.Predicate;

/**
 * The Interface NotificationProcessCustomRepository.
 */
public interface NotificationProcessCustomRepository {

	/**
	 * Gets the update reference dtos.
	 *
	 * @param predicate
	 *            the predicate
	 * @param pageable
	 *            the pageable
	 * @return the update reference dtos
	 */
	Page<ReferenceDashboardDto> getUpdateReferenceDtos(Predicate predicate, Pageable pageable);

	/**
	 * Gets the update reference dto.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @return the update reference dto
	 */
	ReferenceDashboardDto getUpdateReferenceDto(Long notificationProcessId);

	/**
	 * Gets the duplicate npl reference dtos.
	 *
	 * @param predicate
	 *            the predicate
	 * @param pageable
	 *            the pageable
	 * @return the duplicate npl reference dtos
	 */
	Page<ReferenceDashboardDto> getDuplicateNplReferenceDtos(Predicate predicate, Pageable pageable);

	/**
	 * Gets the update reference counts.
	 *
	 * @return the update reference counts
	 */
	long getUpdateReferenceCounts();

	/**
	 * Gets the duplicate npl reference counts.
	 *
	 * @return the duplicate npl reference counts
	 */
	long getDuplicateNplReferenceCounts();

	/**
	 * Gets the correspondence from notification.
	 *
	 * @param notificationId
	 *            the notification id
	 * @param ocrDataId
	 *            the ocr data id
	 * @return the correspondence from notification
	 */
	ReferenceDashboardDto getCorrespondenceFromNotification(Long notificationId, Long ocrDataId);
	
	/**
	 * Count pending notifications.
	 *
	 * @param applicationIdList the application id list
	 * @param notificationTypesList the notification types list
	 * @param skipAppNotificationList the skip app notification list
	 * @return the long
	 */
	long countPendingNotifications(final List<Long> applicationIdList,
			final List<NotificationProcessType> notificationTypesList,final List<Long> skipAppNotificationList);
	
	/**
	 * Gets the pending notifications.
	 *
	 * @param applicationIdList the application id list
	 * @param pageInfo the page info
	 * @param notificationTypesList the notification types list
	 * @param skipNotificationList the skip notification list
	 * @return the pending notifications
	 */
	SearchResult<NotificationBaseDTO> getPendingNotifications(final List<Long> applicationIdList,
			final PaginationInfo pageInfo,final List<NotificationProcessType> notificationTypesList,
			final List<Long> skipNotificationList);
	
	/**
	 * Gets the assignee users by notification process id.
	 *
	 * @param notificatinProcessId the notification process id
	 * @return the assignee users by notification process id
	 */
	List<Long> getAssigneeUserIdsByNotificationProcessId(Long notificationProcessId);
	
	/**
	 * Gets the notification process ids only dependent on user id.
	 *
	 * @param userId the user id
	 * @return the notification process ids only dependent on user id
	 */
	List<Long> getNotificationProcessIdsOnlyDependentOnUserId(Long userId);
	
	/**
	 * Gets the notification process ids by user id.
	 *
	 * @param userId the user id
	 * @return the notification process ids by user id
	 */
	List<Long> getNotificationProcessIdsByUserId(Long userId);
	
	/**
	 * Gets the notification process ids only dependent on user id.
	 *
	 * @param userId the user id
	 * @return the notification process ids only dependent on user id
	 */
	List<Long> getNotificationProcessIdsOnlyDependentOnRoleId(Long roleId);
}

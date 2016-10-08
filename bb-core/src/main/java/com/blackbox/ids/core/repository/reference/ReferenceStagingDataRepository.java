package com.blackbox.ids.core.repository.reference;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.model.reference.ReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;

/**
 * The Interface ReferenceStagingDataRepository.
 */
public interface ReferenceStagingDataRepository extends JpaRepository<ReferenceStagingData, Long>,
		QueryDslPredicateExecutor<ReferenceStagingData>, ReferenceStagingDataCustomRepository {

	/**
	 * Find by reference status.
	 *
	 * @param status
	 *            the status
	 * @return the list
	 */
	List<ReferenceStagingData> findByReferenceStatus(ReferenceStatus status);

	/**
	 * Find dtos.
	 *
	 * @param roleIds
	 *            the role ids
	 * @return the list
	 */
	@Query("select new com.blackbox.ids.core.dto.reference.ReferenceDashboardDto(rsd.referenceStagingDataId, rsd.publicationNumber) "
			+ "from ReferenceStagingData rsd, NotificationProcess np inner join rsd.application app inner join rsd.jurisdiction juris "
			+ "inner join np.roles role " + "where np.entityId = rsd.referenceStagingDataId "
			+ "and np.notificationProcessType = 'PENDING_REFERENCE' and np.active = 'true' and role.id in ?1")
	List<ReferenceDashboardDto> findDtos(Set<Long> roleIds);

	/**
	 * Update reference byid.
	 *
	 * @param referenceStatus
	 *            the reference status
	 * @param active
	 *            the active
	 * @param id
	 *            the id
	 */
	@Modifying
	@Query("Update ReferenceStagingData referenceStagingData set referenceStagingData.referenceStatus =:referenceStatus, referenceStagingData.active =:active where referenceStagingData.referenceStagingDataId = :id")
	void updateReferenceByid(@Param("referenceStatus") ReferenceStatus referenceStatus, @Param("active") boolean active,
			@Param("id") Long id);
	
	@Modifying
	@Query("Update ReferenceStagingData referenceStagingData set referenceStagingData.notificationSent =:notificationSent where referenceStagingData.referenceStagingDataId = :id")
	void updateNotificationStatusByid(@Param("notificationSent") Boolean notificationSent,	@Param("id") Long id);
	

	/**
	 * Delete reference stating data.
	 *
	 * @param updatedBy
	 *            the updated by
	 * @param updatedOn
	 *            the updated on
	 * @param referenceStagingDataId
	 *            the reference staging data id
	 */
	@Modifying
	@Query("Update ReferenceStagingData rsd set rsd.updatedByUser = ?1, rsd.updatedDate = ?2 ,rsd.referenceStatus = 'DROPPED', rsd.active = 'false' where rsd.referenceStagingDataId = ?3 and rsd.referenceStatus = 'INPADOC_FAILED'")
	void deleteReferenceStatingData(Long updatedBy, Calendar updatedOn, Long referenceStagingDataId);
}
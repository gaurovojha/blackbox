package com.blackbox.ids.core.repository.reference;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.reference.SourceReference;

/**
 * The Interface ReferenceBaseDataRepository.
 *
 * @param <T>
 *            the generic type
 */
public interface ReferenceBaseDataRepository<T extends ReferenceBaseData>
		extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T>, ReferenceBaseDataCustomRepository<T> {

	/**
	 * Gets the all by family id and reference type and reference status.
	 *
	 * @param familyId
	 *            the family id
	 * @param referenceType
	 *            the reference type
	 * @param referenceStatus
	 *            the reference status
	 * @return the all by family id and reference type and reference status
	 */
	@Query("select referenceBaseData from ReferenceBaseData  referenceBaseData where referenceBaseData.familyId = :familyId and referenceBaseData.referenceType = :referenceType and referenceBaseData.referenceStatus != :referenceStatus ")
	List<T> getAllByFamilyIdAndReferenceTypeAndReferenceStatus(@Param("familyId") String familyId,
			@Param("referenceType") ReferenceType referenceType,
			@Param("referenceStatus") ReferenceStatus referenceStatus);

	/**
	 * Update reference flow flag by id.
	 *
	 * @param flowFlag
	 *            the flow flag
	 * @param id
	 *            the id
	 */
	@Modifying
	@Query("update ReferenceBaseData referenceBaseData set referenceBaseData.referenceFlowFlag =?1 where referenceBaseData.referenceBaseDataId = ?2")
	void updateReferenceFlowFlagById(boolean flowFlag, Long id);

	/**
	 * Update reference status and flow flag.
	 *
	 * @param referenceStatus
	 *            the reference status
	 * @param active
	 *            the active
	 * @param referenceFlowFlag
	 *            the reference flow flag
	 * @param id
	 *            the id
	 */
	@Modifying
	@Query("Update ReferenceBaseData referenceBaseData set referenceBaseData.referenceStatus =:referenceStatus, referenceBaseData.active =:active, referenceBaseData.referenceFlowFlag =:flowFlag  where referenceBaseData.referenceBaseDataId =:Id")
	void updateReferenceStatusAndFlowFlag(@Param("referenceStatus") ReferenceStatus referenceStatus,
			@Param("active") boolean active, @Param("flowFlag") Boolean referenceFlowFlag, @Param("Id") Long id);

	/**
	 * Update reference status and flow flag bycoupling id.
	 *
	 * @param referenceStatus
	 *            the reference status
	 * @param active
	 *            the active
	 * @param referenceFlowFlag
	 *            the reference flow flag
	 * @param couplingId
	 *            the coupling id
	 */
	@Modifying
	@Query("Update ReferenceBaseData referenceBaseData set referenceBaseData.referenceStatus =:referenceStatus, referenceBaseData.active =:active, referenceBaseData.referenceFlowFlag =:flowFlag  where referenceBaseData.couplingId =:couplingId")
	void updateReferenceStatusAndFlowFlagBycouplingId(@Param("referenceStatus") ReferenceStatus referenceStatus,
			@Param("active") boolean active, @Param("flowFlag") Boolean referenceFlowFlag,
			@Param("couplingId") Long couplingId);

	/**
	 * Update reviewed status.
	 *
	 * @param referenceReviewStatus
	 *            the reference review status
	 * @param referenceId
	 *            the reference id
	 */
	@Modifying
	@Query("Update ReferenceBaseData referenceBaseData set referenceBaseData.reviewedStatus =:reviewedStatus where referenceBaseData.referenceBaseDataId = :referenceId")
	void updateReviewedStatus(@Param("reviewedStatus") boolean referenceReviewStatus,
			@Param("referenceId") Long referenceId);

	/**
	 * Gets the all child references.
	 *
	 * @param parentReferenceId
	 *            the parent reference id
	 * @return the all child references
	 */
	@Query("Select referenceBaseData from ReferenceBaseData referenceBaseData where referenceBaseData.parentReferenceId = :parentReferenceId")
	List<T> getAllChildReferences(@Param("parentReferenceId") ReferenceBaseData parentReferenceId);

	/**
	 * Update npl reference.
	 *
	 * @param referenceStatus
	 *            the reference status
	 * @param referenceFlowFlag
	 *            the reference flow flag
	 */
	@Modifying
	@Query("update ReferenceBaseData referenceBaseData set referenceBaseData.referenceStatus = :referenceStatus, referenceBaseData.referenceFlowFlag =:referenceFlowFlag where referenceBaseData.referenceBaseDataId = :referenceBaseDataId")
	void updateNPLReference(@Param("referenceStatus") ReferenceStatus referenceStatus,
			@Param("referenceFlowFlag") boolean referenceFlowFlag,
			@Param("referenceBaseDataId") Long referenceBaseDataId);

	/**
	 * Update reference status and parent by id.
	 *
	 * @param referenceStatus
	 *            the reference status
	 * @param parentId
	 *            the parent id
	 * @param referenceBaseDataId
	 *            the reference base data id
	 */
	@Modifying
	@Query("update ReferenceBaseData referenceBaseData set referenceBaseData.referenceStatus =:referenceStatus, referenceBaseData.parentReferenceId =:parentId  where referenceBaseData.referenceBaseDataId = :referenceBaseDataId")
	void updateReferenceStatusAndParentById(@Param("referenceStatus") ReferenceStatus referenceStatus,
			@Param("parentId") ReferenceBaseData parentId, @Param("referenceBaseDataId") Long referenceBaseDataId);

	/**
	 * Update parent by id.
	 *
	 * @param parentId
	 *            the parent id
	 * @param referenceBaseDataId
	 *            the reference base data id
	 */
	@Modifying
	@Query("update ReferenceBaseData referenceBaseData set referenceBaseData.parentReferenceId =:parentId  where referenceBaseData.referenceBaseDataId IN :referenceBaseDataId")
	void updateParentById(@Param("parentId") ReferenceBaseData parentId,
			@Param("referenceBaseDataId") Set<Long> referenceBaseDataId);

	@Modifying
	@Query("update ReferenceBaseData referenceBaseData set referenceBaseData.notificationSent = :notificationSent where  referenceBaseData.referenceBaseDataId =:referenceBaseDataId")
	void updateNotificationFlagById(@Param("referenceBaseDataId") Long referenceBaseDataId,
			@Param("notificationSent") Boolean notificationSent);

	@Modifying
	@Query("update ReferenceBaseData referenceBaseData set referenceBaseData.sourceReference = :sourceReference where  referenceBaseData.referenceBaseDataId =:referenceBaseDataId")
	void updateSourceReferenceById(@Param("referenceBaseDataId") Long referenceBaseDataId,
			@Param("sourceReference") SourceReference sourceReference);

	@Modifying
	@Query("update ReferenceBaseData referenceBaseData set referenceBaseData.referenceStatus = :referenceStatus, referenceBaseData.active =:active where referenceBaseData.referenceBaseDataId IN :referenceBaseDataIds")
	void updateStatusAndActiveFlagById(@Param("referenceBaseDataIds") List<Long> referenceBaseDataIds,
			@Param("active") boolean active, @Param("referenceStatus") ReferenceStatus referenceStatus);

	@Modifying
	@Query("update ReferenceBaseData referenceBaseData set referenceBaseData.referenceComments = :comment where referenceBaseData.referenceBaseDataId = :referenceBaseDataId")
	void updateComment(@Param("referenceBaseDataId") final Long referenceBaseDataId,
			@Param("comment") final String comment);

	@Modifying
	@Query("update ReferenceBaseData referenceBaseData set referenceBaseData.englishTranslationFlag = :flag where referenceBaseData.referenceBaseDataId = :referenceBaseDataId")
	void updateTranslationFlagk(@Param("referenceBaseDataId") final Long referenceBaseDataId,
			@Param("flag") final Boolean englishTranslationFlag);

	@Modifying
	@Query("update ReferenceBaseData referenceBaseData set referenceBaseData.attachment = :attachmentFlag where referenceBaseData.referenceBaseDataId = :referenceBaseDataId")
	void updateAttachmentFlag(@Param("referenceBaseDataId") final Long referenceBaseDataId,
			@Param("attachmentFlag") final Boolean attachmentFlag);

}

package com.blackbox.ids.services.reference;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.mysema.query.types.Predicate;

/**
 * @author nagarro
 *
 */
public interface ReferenceManagementService {

	/**
	 * Returns list of correspondences grouped by Jurisdiction and Application
	 * 
	 * @param predicate
	 * @param pageable
	 * @return page
	 */
	public Page<CorrespondenceDTO> getAllRecords(final Predicate predicate, final Pageable pageable);

	/**
	 * It is used to get the list of entries from the reference staging data table whose status is Pending. For all the
	 * documents whose OCR is done, but is waiting for the OPS service to complete.
	 * 
	 * @param predicate
	 * @param pageable
	 * @return
	 */
	public Page<CorrespondenceDTO> getPendingReferences(final Predicate predicate, final Pageable pageable);

	/**
	 * Get list of correspondenceDTO for a particular jurisdiction and application
	 *
	 * @param id
	 *            the id
	 * @return the correspondence dto
	 */
	public List<CorrespondenceDTO> getCorrespondence(final String jurisCode, final String appNo);

	/**
	 * Returns list of references
	 * 
	 * @param corrId
	 * @return list of reference dtos
	 */
	public ReferenceBaseDTO getReferencesByCorrespondence(final Long corrId);

	/**
	 * Updates review status
	 * 
	 * @param referenceBaseDataId
	 * @param comments
	 * @return
	 */
	public boolean updateReviewedReference(final Long referenceBaseDataId, final String comments);

	/**
	 * Get list of correspondenceDTO from staging for a particular jurisdiction and application
	 * 
	 * @param referenceIds
	 * @param updatedByUser
	 * @return
	 */
	public List<CorrespondenceDTO> getCorrespondenceFromStaging(String jurisCode, String appNo);;

	/**
	 * Get correspondenceDTO by id
	 * 
	 * @param corrId
	 * @return
	 */
	public CorrespondenceDTO getCorrespondenceById(Long corrId);

	/**
	 * Gets the reference with application details.
	 *
	 * @param id
	 *            the correspondence ID
	 * @return the reference with application details
	 */
	public ReferenceBaseDTO getReferenceWithApplicationDetails(Long correspondenceId);

	/**
	 * Checks if is reference exist.
	 *
	 * @param corrId
	 *            the corr id
	 * @return true, if is reference exist
	 */
	public boolean isReferenceExist(Long corrId);

	/**
	 * Get all the references with their flow status
	 * 
	 * @param correspondenceId
	 * @return
	 */
	public ReferenceBaseDTO getReferenceWithApplicationDetailsAndFlowStatus(final Long correspondenceId);

	/**
	 * updates reference comments
	 * 
	 * @param referenceBaseDataId
	 * @param comments
	 */
	public void updateComments(final Long referenceBaseDataId, final String comments);

	/**
	 * updates translation flag
	 * 
	 * @param refId
	 * @param translationFlag
	 */
	public void updateTranslationFlag(final Long refId, final Boolean translationFlag);

	/**
	 * Delete reference attachment
	 * 
	 * @param referenceBaseDataId
	 * @return
	 */
	public boolean deleteAttachment(final Long referenceId, final ReferenceType referenceType);

	/**
	 * Get reference review details
	 * 
	 * @param refId
	 * @return
	 */
	public Map<String, String> getReviewDetails(final Long refId);

	/**
	 * Update review multiple references
	 * 
	 * @param referenceBaseDataId
	 * @return
	 */
	public boolean updateReviewedReferences(long[] referenceBaseDataId);
}
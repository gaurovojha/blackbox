package com.blackbox.ids.services.reference;

import java.io.IOException;
import java.util.List;

import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.model.reference.ReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceType;

/**
 * @author nagarro
 *
 */
public interface ReferenceService {

	/**
	 * Creates new reference followed by all the checks and validations
	 * 
	 * @param referenceDTO
	 */
	public ReferenceBaseDTO createReference(final ReferenceBaseDTO referenceDTO);

	/**
	 * 
	 * This method is to be used for adding the references either Manually or via Automatic Entry.
	 * 
	 * @param referenceDTO
	 * @return
	 */
	public ReferenceBaseDTO addReference(final ReferenceBaseDTO referenceDTO);

	/**
	 * Imports the reference from Staging to Base table.
	 * 
	 * @param referenceStagingData
	 * @return
	 */
	public void importReference(final ReferenceStagingData referenceStagingData);

	/**
	 * Return reference for a given id
	 * 
	 * @param id
	 * @return dto
	 */
	public ReferenceBaseDTO getReferenceById(final Long id);

	/**
	 * Updates a given reference. It does not actually delete but deactivates it
	 * 
	 * @param id
	 * @return boolean
	 */
	public void removeReference(final Long id);

	/**
	 * Updates a given reference. It does not actually delete but deactivates it
	 * 
	 * @param id
	 *            list
	 * @return boolean
	 */
	public void removeReferences(final List<Long> ids);

	/**
	 * Updates NPL references
	 * 
	 * @param referenceId
	 * @param duplicate
	 * @param notificationProcessId
	 */
	void updateNPLReferenceManually(final Long referenceId, final boolean duplicate, final Long notificationProcessId);

	/**
	 * Deletes document and thus corresponding references
	 * 
	 * @param notificationProcessId
	 */
	void deleteDocument(final Long notificationProcessId);

	/**
	 * Add reference in staging area
	 * 
	 * @param referenceBaseDTO
	 */
	void addReferenceStagingData(final ReferenceBaseDTO referenceBaseDTO);

	/**
	 * 
	 * @param referenceDTO
	 */
	List<ReferenceDashboardDto> getNPLDuplicates(final ReferenceDashboardDto referenceDTO);

	/**
	 * 
	 * @param correspondenceId
	 * @param isNull
	 * @param ocrDataId
	 * @param nplStringData
	 */
	void updateNullReference(final Long correspondenceId, final boolean isNull, final Long ocrDataId, final String nplStringData);

	/**
	 * To get the NPL duplicates
	 * 
	 * @param applicationId
	 * @param oldFamilyId
	 * @param newFamilyId
	 */
	void updateReferenceFamilyLinkage(final Long applicationId, final String oldFamilyId, final String newFamilyId);

	/**
	 * Upload pdf attachment.
	 *
	 * @param referenceBaseDTO
	 *            the reference base dto
	 */
	void uploadPDFAttachment(final ReferenceBaseDTO referenceBaseDTO);

	/**
	 * Get attachment upload path
	 * 
	 * @param referenceId
	 * @param referenceType
	 * @return
	 */
	String getReferenceAttachmentUploadPath(final Long referenceId, final ReferenceType referenceType);
}
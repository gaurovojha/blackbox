package com.blackbox.ids.services.reference;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blackbox.ids.core.dto.reference.ReferenceBaseDTO;
import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.dto.reference.ReferenceEntryDTO;
import com.blackbox.ids.core.dto.reference.ReferenceEntryFilter;
import com.mysema.query.types.Predicate;

/**
 * The Interface ReferenceDashboardService.
 *
 * @author nagarro
 */
public interface ReferenceDashboardService {

    /**
     * Gets the updater ref data.
     *
     * @param predicate
     *            the predicate
     * @param pageable
     *            the pageable
     * @return the updater ref data
     */
    Page<ReferenceDashboardDto> getUpdaterRefData(Predicate predicate, Pageable pageable);

    /**
     * Gets the update ref dto.
     *
     * @param notificationProcessId
     *            the notification process id
     * @return the update ref dto
     */
    ReferenceDashboardDto getUpdateRefDto(Long notificationProcessId);

    /**
     * Gets the duplicate check ref data.
     *
     * @param predicate
     *            the predicate
     * @param pageable
     *            the pageable
     * @return the duplicate check ref data
     */
    Page<ReferenceDashboardDto> getDuplicateCheckRefData(Predicate predicate, Pageable pageable);

    /**
     * Gets the ref entry data.
     *
     * @param filter
     *            the filter
     * @param pageable
     *            the pageable
     * @return the ref entry data
     */
    Page<ReferenceEntryDTO> getRefEntryData(ReferenceEntryFilter filter, Pageable pageable);

    /**
     * Fetch and lock notification.
     *
     * @param notificationProcessId
     *            the notification process id
     * @param ocrDataId
     *            the ocr data id
     * @param lockedByUser
     *            the locked by user
     * @return the reference entry dto
     */
    ReferenceEntryDTO fetchAndLockNotification(Long notificationProcessId, Long ocrDataId, Long lockedByUser);
    
    /**
     * 
     * @param notificationProcessId
     * @param ocrDataId
     * @param lockedByUser
     * @return
     */
    ReferenceEntryDTO fetchNotification(Long notificationProcessId, Long ocrDataId, Long lockedByUser);

    /**
     * Gets the correspondence id.
     *
     * @param notificationProcessId
     *            the notification process id
     * @param ocrDataId
     *            the ocr data id
     * @return the correspondence id
     */
    Long getCorrespondenceId(Long notificationProcessId, Long ocrDataId);

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
     * Gets the reference entry count.
     *
     * @return the reference entry count
     */
    Long getReferenceEntryCount();

    /**
     * Gets the duplicate npl reference.
     *
     * @param referenceBaseDataId
     *            the reference base data id
     * @return the duplicate npl reference
     */
    ReferenceDashboardDto getDuplicateNPLReference(Long referenceBaseDataId);

    
    /**
     * Delete update ref from staging.
     *
     * @param refStagingId
     *            the ref staging id
     * @param notificationProcessId
     *            the notification process id
     * @return true, if successful
     */
    boolean deleteUpdateRefFromStaging(Long refStagingId, Long notificationProcessId);

    /**
     * Adds the reference details.
     *
     * @param referenceDto
     *            the reference dto
     */
    void addReferenceDetails(ReferenceBaseDTO referenceDto);
    
    /**
     * 
     * @param referenceDTO
     * @param notificationProcessId
     * @return
     */
    public ReferenceBaseDTO createReferenceAndCloseNotification(final ReferenceBaseDTO referenceDTO, Long notificationProcessId);
    
    /**
     * 
     * @param correspondenceid
     * @param nplString
     * @param ocrDataId
     * @param notificationProcessId
     */
    void createNullReferenceDocumentAndCloseNotification(Long correspondenceid, String nplString, Long ocrDataId, Long notificationProcessId);

    /**
     * 
     * @param referenceDTO
     * @param notificationProcessId
     * @return
     */
    void createStagingReferenceAndCloseNotification(ReferenceBaseDTO referenceDTO,
            Long notificationProcessId);
    
    /**
     * 
     * @param correspondenceid
     * @return
     */
    String createNPLStringForNullReferenceDoc(Long correspondenceid);
}

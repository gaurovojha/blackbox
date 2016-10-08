package com.blackbox.ids.core.repository.reference;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blackbox.ids.core.dto.IDS.IDSReferenceRecordDTO;
import com.blackbox.ids.core.dto.IDS.IDSReferenceRecordStatusCountDTO;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.dto.reference.PendingNPLReferenceDTO;
import com.blackbox.ids.core.dto.reference.ReferenceDashboardDto;
import com.blackbox.ids.core.model.reference.ReferenceBaseData;
import com.blackbox.ids.core.model.reference.ReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.mysema.query.types.Predicate;

/**
 * Custom repository to manage custom methods which can not be implemented by Spring Jpa. Add all such custom methods in
 * here. It must be implemented by CustomImpl.
 * 
 * Note: Never inject this custom repository anywhere rather inject the main repository.
 *
 * @author Nagarro
 * @param <T>
 *            the generic type
 */
public interface ReferenceBaseDataCustomRepository<T extends ReferenceBaseData> {

	/**
	 * Filter records.
	 *
	 * @param page
	 *            the page
	 * @param pageInfo
	 *            the page info
	 * @return the page
	 */
	Page<ReferenceBaseData> filterRecords(final Predicate page, final Pageable pageInfo);

	/**
	 * Gets the npl records.
	 *
	 * @param page
	 *            the page
	 * @param pageInfo
	 *            the page info
	 * @return the npl records
	 */
	Page<ReferenceBaseNPLData> getNplRecords(final Predicate page, Pageable pageInfo);

	/**
	 * Drop reference.
	 *
	 * @param referenceId
	 *            the reference id
	 * @return true, if successful
	 */
	boolean dropReference(final Long referenceId);

	/**
	 * Fetch reference count.
	 *
	 * @param correspondenceId
	 *            the correspondence id
	 * @return the integer
	 */
	Integer fetchReferenceCount(final Long correspondenceId);

	/**
	 * Gets the list of distinct correspondence id.
	 *
	 * @param applicationId
	 *            the application id
	 * @return the list of distinct correspondence id
	 */
	List<Long> getListOfDistinctCorrespondenceId(final Long applicationId);

	/**
	 * Filter correspondence data.
	 *
	 * @param page
	 *            the page
	 * @param pageInfo
	 *            the page info
	 * @return the page
	 */
	Page<CorrespondenceDTO> filterCorrespondenceData(final Predicate page, final Pageable pageInfo);

	/**
	 * Fetch correspondence data.
	 *
	 * @param jurisId
	 *            the juris id
	 * @param appId
	 *            the app id
	 * @return the list
	 */
	List<CorrespondenceDTO> fetchCorrespondenceData(final String jurisId, final String appId);

	/**
	 * Fetch references by correspondence id.
	 *
	 * @param corrId
	 *            the corr id
	 * @return the list
	 */
	List<ReferenceBaseData> fetchReferencesByCorrespondenceId(final Long corrId);

	/**
	 * Gets the all pending npl references.
	 *
	 * @param referenceStatus
	 *            the reference status
	 * @param referenceType
	 *            the reference type
	 * @param active
	 *            the active
	 * @return the all pending npl references
	 */
	List<PendingNPLReferenceDTO> getAllPendingNPLReferences(ReferenceStatus referenceStatus,
			ReferenceType referenceType, boolean active);

	/**
	 * Gets the duplicate npl reference by id.
	 *
	 * @param referenceBaseDataId
	 *            the reference base data id
	 * @return the duplicate npl reference by id
	 */
	ReferenceDashboardDto getDuplicateNPLReferenceById(Long referenceBaseDataId);

	
	/**
	 * Fetch references id's by correspondence id.
	 *
	 * @param corrId
	 *            the corr id
	 * @return the list
	 */
	List<Long> fetchReferenceIdsByCorrespondenceId(final Long corrId);
  
	/**
	 * Gets the references count by correpondence id.
	 *
	 * @param corrId the corr id
	 * @return the reference countby correpondence id
	 */
	Long getReferenceCountbyCorrepondenceId(final Long corrId);
	
	/**
	 * 
	 * @param familyId
	 * @param referenceBaseDataId
	 * @return
	 */
	List<ReferenceDashboardDto> getDuplicateNPLByFamilyId(String familyId, Long referenceBaseDataId);

	/**
	 * 
	 * @param applicationId
	 * @param familyId
	 * @return
	 */
	List<ReferenceBaseData> getReferenceByApplicationAndFamilyId(Long applicationId, String familyId);
	
	//List<IDSReferenceRecordDTO> fetchAllIDSReferenceRecords(Predicate predicate, Pageable pageable);
	
	IDSReferenceRecordStatusCountDTO fetchAllTabStatusCount(final Predicate predicate);
	
	Page<IDSReferenceRecordDTO> fetchPatentReferenceRecords(Predicate predicate, Pageable pageable);
	
	Page<IDSReferenceRecordDTO> fetchNPLReferenceRecords(Predicate predicate, Pageable pageable);
	
	Map<String, Long> fetchDistinctFilingDateAndCount();
	
	IDSReferenceRecordDTO fetchNplAndPatentCount();
	
	List<ReferenceFlowSubStatus> fetchChangeSubStatus();
	
	IDSReferenceRecordDTO fetchSourceDocumentData(final Long refFlowId);

}

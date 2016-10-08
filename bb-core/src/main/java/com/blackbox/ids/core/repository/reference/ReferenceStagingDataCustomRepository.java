package com.blackbox.ids.core.repository.reference;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceDTO;
import com.blackbox.ids.core.model.reference.ReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.mysema.query.types.Predicate;

/**
 * The Interface ReferenceStagingDataCustomRepository.
 */
public interface ReferenceStagingDataCustomRepository {

	/**
	 * Filter pending records.
	 *
	 * @param predicate
	 *            the predicate
	 * @param pageable
	 *            the pageable
	 * @return the page
	 * @throws ApplicationException
	 *             the application exception
	 */
	Page<CorrespondenceDTO> filterPendingRecords(Predicate predicate, Pageable pageable) throws ApplicationException;

	/**
	 * Find by reference status in.
	 *
	 * @param status
	 *            the status
	 * @return the list
	 */
	List<ReferenceStagingData> findByReferenceStatusIn(Set<ReferenceStatus> status);

	/**
	 * Fetch correspondence data.
	 *
	 * @param jurisCode
	 *            the juris code
	 * @param appNo
	 *            the app no
	 * @return the list
	 * @throws ApplicationException
	 *             the application exception
	 */
	List<CorrespondenceDTO> fetchCorrespondenceData(String jurisCode, String appNo) throws ApplicationException;

	/**
	 * Find correspondence data by id.
	 *
	 * @param corrId
	 *            the corr id
	 * @return the correspondence dto
	 * @throws ApplicationException
	 *             the application exception
	 */
	CorrespondenceDTO findCorrespondenceDataById(Long corrId) throws ApplicationException;
}
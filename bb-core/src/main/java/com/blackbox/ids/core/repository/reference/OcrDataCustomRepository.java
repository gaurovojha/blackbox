package com.blackbox.ids.core.repository.reference;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blackbox.ids.core.dto.reference.ReferenceEntryDTO;
import com.blackbox.ids.core.dto.reference.ReferenceEntryFilter;

/**
 * The Interface OcrDataCustomRepository with pagination and filter.
 */
public interface OcrDataCustomRepository {

	/**
	 * Gets the reference entry data.
	 *
	 * @param filter
	 *            the filter
	 * @param pageable
	 *            the pageable
	 * @return the reference entry data
	 */
	Page<ReferenceEntryDTO> getReferenceEntryData(ReferenceEntryFilter filter, Pageable pageable);

	/**
	 * Gets the reference data by notification process id and ocr data id.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @param ocrDataId
	 *            the ocr data id
	 * @return the reference data by notification process id and ocr data id
	 */
	ReferenceEntryDTO getReferenceDataByNotificationProcessIdAndOcrDataId(Long notificationProcessId, Long ocrDataId);

	/**
	 * Gets the reference data list by notification process.
	 *
	 * @param notificationProcessId
	 *            the notification process id
	 * @return the reference data list by notification process
	 */
//	Long getReferenceDataListByNotificationProcess(Long notificationProcessId);

	/**
	 * Gets the reference entry count.
	 *
	 * @return the reference entry count
	 */
	Long getReferenceEntryCount();
}

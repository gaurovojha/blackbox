package com.blackbox.ids.core.repository.reference;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.model.reference.OcrData;

/**
 * The Interface OcrDataRepository.
 */
public interface OcrDataRepository
		extends JpaRepository<OcrData, Long>, QueryDslPredicateExecutor<OcrData>, OcrDataCustomRepository {

	/**
	 * Find by processed.
	 *
	 * @param processed
	 *            the processed
	 * @return the list
	 */
	@Query("Select ocrData from OcrData ocrData where ocrData.processed =:processed")
	List<OcrData> findByProcessed(@Param("processed") boolean processed);

	/**
	 * Update ocr data as processed.
	 *
	 * @param ocrDataId
	 *            the ocr data id
	 */
	@Modifying
	@Query("update OcrData ocrData set ocrData.processed = true where ocrData.ocrDataId =:ocrDataId")
	void updateOcrDataAsProcessed(@Param("ocrDataId") Long ocrDataId);
}

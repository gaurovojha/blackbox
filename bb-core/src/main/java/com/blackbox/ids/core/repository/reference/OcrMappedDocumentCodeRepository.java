package com.blackbox.ids.core.repository.reference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.model.reference.OcrMappedDocumentCode;

/**
 * The Interface OcrMappedDocumentCodeRepository.
 */
public interface OcrMappedDocumentCodeRepository extends JpaRepository<OcrMappedDocumentCode, Long> {

	/**
	 * Find by document code.
	 *
	 * @param documentCode
	 *            the document code
	 * @return the ocr mapped document code
	 */
	@Query("SELECT ocrMappedDocumentCode from OcrMappedDocumentCode ocrMappedDocumentCode where lower(ocrMappedDocumentCode.documentCode) = lower(:documentCode)")
	public abstract OcrMappedDocumentCode findByDocumentCode(@Param("documentCode") String documentCode);

}

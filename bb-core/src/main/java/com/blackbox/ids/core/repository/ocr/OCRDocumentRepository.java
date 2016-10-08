package com.blackbox.ids.core.repository.ocr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.blackbox.ids.core.model.ocr.OCRDocument;

public interface OCRDocumentRepository
		extends JpaRepository<OCRDocument, Long>, QueryDslPredicateExecutor<OCRDocument> {

	public OCRDocument findById(Long id);
}

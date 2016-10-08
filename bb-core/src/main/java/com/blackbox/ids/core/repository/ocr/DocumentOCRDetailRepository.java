package com.blackbox.ids.core.repository.ocr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.blackbox.ids.core.model.ocr.DocumentOCRDetail;

public interface DocumentOCRDetailRepository
		extends JpaRepository<DocumentOCRDetail, Long>, QueryDslPredicateExecutor<DocumentOCRDetail> {

	public DocumentOCRDetail findById(Long id);
}

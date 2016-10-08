package com.blackbox.ids.core.repository.ocr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.blackbox.ids.core.model.ocr.OCRClientMapping;

public interface OCRClientMappingRepository
		extends JpaRepository<OCRClientMapping, Long>, QueryDslPredicateExecutor<OCRClientMapping> {

	public OCRClientMapping findById(Long id);
}

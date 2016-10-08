package com.blackbox.ids.core.repository.ocr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.blackbox.ids.core.model.ocr.OCRQueue;

public interface OCRQueueRepository extends JpaRepository<OCRQueue, Long>, QueryDslPredicateExecutor<OCRQueue> {

	public OCRQueue findById(Long id);
}

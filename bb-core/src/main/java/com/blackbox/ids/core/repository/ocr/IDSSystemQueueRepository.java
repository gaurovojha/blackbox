package com.blackbox.ids.core.repository.ocr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.blackbox.ids.core.model.ocr.IDSSystemQueue;

public interface IDSSystemQueueRepository
		extends JpaRepository<IDSSystemQueue, Long>, QueryDslPredicateExecutor<IDSSystemQueue> {

	public IDSSystemQueue findById(Long id);
}

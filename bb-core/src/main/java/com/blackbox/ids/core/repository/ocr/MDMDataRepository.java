package com.blackbox.ids.core.repository.ocr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.blackbox.ids.core.model.ocr.MDMData;

public interface MDMDataRepository extends JpaRepository<MDMData, Long>, QueryDslPredicateExecutor<MDMData> {

	public MDMData findById(Long id);
}

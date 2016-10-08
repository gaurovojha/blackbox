package com.blackbox.ids.core.repository.reference;

import java.util.List;

import javax.persistence.OrderBy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackbox.ids.core.model.reference.ReferenceReviewLog;

/**
 * The Interface ReferenceReviewLogRepository.
 */
public interface ReferenceReviewLogRepository extends JpaRepository<ReferenceReviewLog, Long> {

	/**
	 * Find by correspondence base data.
	 *
	 * @param corr
	 *            the corr
	 * @return the list
	 */
	@OrderBy(value = "CREATED_ON DESC")
	public List<ReferenceReviewLog> findByReferenceBaseDataId(Long refId);
}
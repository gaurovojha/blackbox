package com.blackbox.ids.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.model.OAResponseCode;

/**
 * JPA repository for OA Response codes.
 */
public interface OAResponseCodeRepository extends JpaRepository<OAResponseCode, Long> {
	
	/**
	 * Finds the document description by document code.
	 * @param code
	 *            the code
	 * @return the document description
	 */
	@Query("SELECT responseCode.description from OAResponseCode responseCode where lower(responseCode.code) = lower(:code)")
	public String findDescriptionByDocumentCode(@Param("code") final String code);

}

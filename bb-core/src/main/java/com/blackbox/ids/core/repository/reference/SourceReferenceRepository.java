package com.blackbox.ids.core.repository.reference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.model.reference.SourceReference;

/**
 * The Interface SourceReferenceRepository.
 */
public interface SourceReferenceRepository extends JpaRepository<SourceReference, Long> {

	/**
	 * Gets the source reference by correspondence.
	 *
	 * @param correspondence
	 *            the correspondence
	 * @return the source reference by correspondence
	 */
	@Query("Select sourceReference.id from SourceReference sourceReference where sourceReference.correspondence.id = :correspondence")
	Long getSourceReferenceByCorrespondence(@Param("correspondence") Long correspondence);
}

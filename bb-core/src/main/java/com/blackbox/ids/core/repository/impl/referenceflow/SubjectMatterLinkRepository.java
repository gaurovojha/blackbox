/**
 * 
 */
package com.blackbox.ids.core.repository.impl.referenceflow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.model.mstr.Status;
import com.blackbox.ids.core.model.referenceflow.SubjectMatterLink;

/**
 * @author nagarro
 *
 */
public interface SubjectMatterLinkRepository<T extends SubjectMatterLink>
		extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T> {

	public List<SubjectMatterLink> findBySourceFamilyIdAndStatusNot(String sourceFamilyId, Status status);

	public SubjectMatterLink findBySourceFamilyIdAndTargetFamilyIdAndStatus(String sourceFamilyId, String targetFamilyId, Status status);

	@Modifying
	@Query("update SubjectMatterLink link set link.status = :status where link.sourceFamilyId = :sourceFamilyId and link.targetFamilyId = :targetFamilyId")
	public void removeSML(@Param("sourceFamilyId") final String sourceFamilyId,
			@Param("targetFamilyId") final String targetFamilyId, @Param("status") final Status status);
}

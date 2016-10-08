package com.blackbox.ids.core.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.mstr.Jurisdiction;

/**
 * A DAO for the entity Jurisdiction is simply created by extending the JpaRepository interface provided by spring and
 * custom repository. The following methods are some of the ones available from former interface: save, delete,
 * deleteAll, findOne and findAll. The magic is that such methods must not be implemented, and moreover it is possible
 * create new query methods working only by defining their signature! Custom interface can be used to define custom
 * query methods that can not be implemented by Spring.
 * @author Nagarro
 */
@Repository("jurisdictionRepository")
public interface JurisdictionRepository extends JpaRepository<Jurisdiction, Long> {

	/**
	 * Find by jurisdiction value.
	 * @param jursidictionValue
	 *            the jursidiction value
	 * @return the jurisdiction
	 * @throws ApplicationException
	 *             the application exception
	 */
	@Query("SELECT jursidiction from Jurisdiction jursidiction where lower(jursidiction.code) = lower(:code)")
	public Jurisdiction findByJurisdictionValue(@Param("code") String code);

	@Query("SELECT juri FROM Jurisdiction juri WHERE juri.id IN (:jurisdictions)")
	public List<Jurisdiction> fetchJurisdictionsWithIdsIn(@Param("jurisdictions") Set<Long> userJurisdictions);

}

package com.blackbox.ids.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.model.mdm.InclusionList;

/**
 * A DAO for the entity InclusionList is simply created by extending the JpaRepository interface provided by spring and
 * custom repository. The following methods are some of the ones available from former interface: save, delete,
 * deleteAll, findOne and findAll. The magic is that such methods must not be implemented, and moreover it is possible
 * create new query methods working only by defining their signature! Custom interface can be used to define custom
 * query methods that can not be implemented by Spring.
 *
 * @author abhay2566
 */
public interface InclusionListRepository extends JpaRepository<InclusionList, Long> {

	/**
	 * Find all application numbers.
	 *
	 * @return the list
	 */
	@Query("Select inc.applications.appNumber From InclusionList inc where applications.status = com.blackbox.ids.core.model.enums.ApplicationNumberStatus.ACTIVE")
	public List<String> findAllActiveApplicatinNumbers();

	@Query("Select inc.applications.appNumber From InclusionList inc where inc.applications.jurisdiction = :jurisdictionCode")
	public List<String> findApplicationNumbersByCode(@Param("jurisdictionCode") String jurisdictionCode);

	@Query("Select inc.applications.appNumber From InclusionList inc where applications.status = com.blackbox.ids.core.model.enums.ApplicationNumberStatus.ACTIVE"
			+ " and inc.applications.jurisdiction = :jurisdictionCode")
	public List<String> findActiveApplicationNumbersByCode(@Param("jurisdictionCode") String jurisdictionCode);

	@Query("Select inc From InclusionList inc Where UPPER(inc.applications.jurisdiction) = UPPER(:jurisdiction) And UPPER(inc.applications.appNumber) = UPPER(:applicationNo)")
	public InclusionList findByJuridictionAndApplication(@Param("jurisdiction") String jurisdiction, @Param("applicationNo") String applicationNo);

}

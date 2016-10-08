package com.blackbox.ids.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.model.mdm.ExclusionList;

/**
 * A DAO for the entity ExclusionList is simply created by extending the JpaRepository interface provided by spring and
 * custom repository. The following methods are some of the ones available from former interface: save, delete,
 * deleteAll, findOne and findAll. The magic is that such methods must not be implemented, and moreover it is possible
 * create new query methods working only by defining their signature! Custom interface can be used to define custom
 * query methods that can not be implemented by Spring.
 * @author abhay2566
 */
public interface ExclusionListRepository extends JpaRepository<ExclusionList, Long> {
	/**
	 * Find all application numbers.
	 * @return the list
	 */
	@Query("Select exc.applications.appNumber From ExclusionList exc")
	public List<String> findAllApplicationNumbers();

	/**
	 * Find application numbers by code.
	 * @param jurisdictionCode
	 *            the jurisdiction code
	 * @return the list
	 */
	@Query("Select exc.applications.appNumber From ExclusionList exc where exc.applications.jurisdiction = :jurisdictionCode")
	public List<String> findApplicationNumbersByCode(@Param("jurisdictionCode") String jurisdictionCode);

	/**
	 * Find active application numbers by code.
	 * @param jurisdictionCode
	 *            the jurisdiction code
	 * @return the list
	 */
	@Query("Select exc.applications.appNumber From ExclusionList exc where applications.status = com.blackbox.ids.core.model.enums.ApplicationNumberStatus.ACTIVE"
			+ " and exc.applications.jurisdiction = :jurisdictionCode")
	public List<String> findActiveApplicationNumbersByCode(@Param("jurisdictionCode") String jurisdictionCode);

	/**
	 * Find active application numbers.
	 * @return the list
	 */
	@Query("Select exc.applications.appNumber From ExclusionList exc where applications.status = com.blackbox.ids.core.model.enums.ApplicationNumberStatus.ACTIVE")
	public List<String> findActiveApplicationNumbers();

	/**
	 * The method scans the ExclusionList for matching application no. and jurisdiction in active status.
	 * <p/>
	 * The resultant count can therefore be used to see whether a particular application has been excluded or not.
	 * @param jurisdiction
	 *            2 character jurisdiction identifier code
	 * @param applicationNo
	 *            Application number converted
	 * @return 0 if given application is not excluded; >0 otherwise.
	 */
	@Query("Select Count(exc) From ExclusionList exc Where exc.applications.status = com.blackbox.ids.core.model.enums.ApplicationNumberStatus.ACTIVE"
			+ " And UPPER(exc.applications.appNumber) = UPPER(:applicationNo) And UPPER(exc.applications.jurisdiction) = UPPER(:jurisdiction)")
	public Long isExcluded(@Param("jurisdiction") String jurisdiction, @Param("applicationNo") String applicationNo);

	/**
	 * Find by juridiction and application.
	 * @param jurisdiction
	 *            the jurisdiction
	 * @param applicationNo
	 *            the application no
	 * @return the exclusion list
	 */
	@Query("Select exc From ExclusionList exc Where exc.applications.status = com.blackbox.ids.core.model.enums.ApplicationNumberStatus.ACTIVE"
			+ " AND UPPER(exc.applications.jurisdiction) = UPPER(:jurisdiction) And UPPER(exc.applications.appNumber) = UPPER(:applicationNo)")
	public ExclusionList findByJuridictionAndApplication(@Param("jurisdiction") String jurisdiction,
			@Param("applicationNo") String applicationNo);

}

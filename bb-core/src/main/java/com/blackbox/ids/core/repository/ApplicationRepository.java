package com.blackbox.ids.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.model.mdm.Application;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Jurisdiction;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationBase, Long> {

	@Query("Select app From ApplicationBase app Where app.applicationNumber = :apn")
	Application findApplicationByApn(@Param("apn") final String apn);

	/**
	 * Find all application numbers.
	 * @return the list
	 */
	@Query("Select app From ApplicationBase app")
	public List<Application> findAllApplicationNumbers();

	/**
	 * @param apn
	 * @param jursdctn
	 * @return ApplicationBase
	 */
	@Query("Select app From ApplicationBase app Where app.applicationNumber = :apn and app.jurisdiction = :jursdctn")
	ApplicationBase findApplicationBaseByApn(@Param("apn") final String apn,
			@Param("jursdctn") final Jurisdiction jursdctn);

	@Query("Select appBase from ApplicationBase appBase where	appBase.jurisdiction.code =:jursdctnCode and appBase.recordStatus  in ('ACTIVE','TRANSFERRED','ALLOWED_TO_ABANDON')"
			+ " and appBase.familyId in (Select familyId from ApplicationBase app  where"
			+ " app.jurisdiction.code =:jursdctnCodeInner and patentDetails.patentNumberRaw is null and"
			+ " app.recordStatus  in ('ACTIVE','TRANSFERRED','ALLOWED_TO_ABANDON'))")
	public List<ApplicationBase> findEPOfficeRecords(@Param("jursdctnCode") final String jursdctnCode,
			@Param("jursdctnCodeInner") final String jursdctnCodeInner);

	/**
	 * Find all application numbers for given customers.
	 * @return the list
	 */
	@Query("Select app From ApplicationBase app where app.customer.customerNumber in (:customerNos)")
	public List<ApplicationBase> findAllApplicationNumbersByCustomer(
			@Param("customerNos") final List<String> customerNos);
}

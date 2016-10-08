package com.blackbox.ids.core.repository.correspondence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;

public interface CorrespondenceBaseRepository extends JpaRepository<CorrespondenceBase, Long> {

	/**
	 * Find by source.
	 *
	 * @param source
	 *            the source
	 * @return the correspondence
	 * @throws ApplicationException
	 *             the application exception
	 */
	@Query("SELECT correspondence from CorrespondenceBase correspondence where lower(correspondence.source) = lower(:source)")
	public List<CorrespondenceBase> findBySource(@Param("source") String source) throws ApplicationException;

	/**
	 *
	 * @param source
	 *            the source
	 * @return the correspondence
	 * @throws ApplicationException
	 *             the application exception
	 */
	@Query("SELECT correspondence from CorrespondenceBase correspondence ")
	public List<CorrespondenceBase> findAllRecords() throws ApplicationException;

	@Query("SELECT app.applicationNumber, correspondence.mailingDate, correspondence.documentCode.code, correspondence.pageCount"
			+ " FROM ApplicationBase app, CorrespondenceBase correspondence "
			+ "WHERE app.id = correspondence.application.id" + " and app.customer.customerNumber = :customerNumber")
	public List<Object[]> findRequiredValuesWithBaseTables(@Param("customerNumber") final String customerNumber)
			throws ApplicationException;

	/**
	 * Find correspondence data.
	 *
	 * @return the list
	 */
	@Query("SELECT correspondence.application.applicationNumber,correspondence.application.jurisdiction.code,correspondence.documentCode.code,correspondence from "
			+ "CorrespondenceBase correspondence where correspondence.application.recordStatus = com.blackbox.ids.core.model.enums.MDMRecordStatus.ACTIVE")
	public List<Object[]> findAllCorrespondencesForActiveMDMApplications();

	
	@Query(value = "SELECT correspondence from CorrespondenceBase correspondence ,DocumentOCRDetail ocr , DocumentCode dCode " +
    " where correspondence.documentCode.id = dCode.id and "+
	" dCode.code = ocr.docCode and ocr.isOCR=true")
	public List<CorrespondenceBase> findOCRApplicableRecords();
	
	
	
}

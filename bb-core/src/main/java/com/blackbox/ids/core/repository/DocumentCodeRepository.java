package com.blackbox.ids.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.DocumentCode;

/**
 * A DAO for the entity User is simply created by extending the JpaRepository interface provided by spring and custom
 * repository. The following methods are some of the ones available from former interface: save, delete, deleteAll,
 * findOne and findAll. The magic is that such methods must not be implemented, and moreover it is possible create new
 * query methods working only by defining their signature! Custom interface can be used to define custom query methods
 * that can not be implemented by Spring.
 * @author Nagarro
 */
public interface DocumentCodeRepository extends JpaRepository<DocumentCode, Long> {

	/**
	 * Find by document code.
	 * @param code
	 *            the code
	 * @return the jurisdiction
	 * @throws ApplicationException
	 *             the application exception
	 */
	@Query("SELECT documentCode from DocumentCode documentCode where lower(documentCode.code) = lower(:code)")
	public DocumentCode findByDocumentCode(@Param("code") final String code);

	/**
	 * Find by document code.
	 * @param code
	 *            the code
	 * @return the jurisdiction
	 * @throws ApplicationException
	 *             the application exception
	 */

	public DocumentCode findByCode(String code);

	@Query("SELECT documentCode from DocumentCode documentCode where documentCode.jurisdiction.code =:jurisdictionCode")
	public List<DocumentCode> findByJurisdictionCode(@Param("jurisdictionCode") String jurisdictionCode);

	@Query("SELECT documentCode from DocumentCode documentCode where documentCode.jurisdiction.code in (:countryCodes) ")
	public List<DocumentCode> findDocumentByCountryCodes(@Param("countryCodes") List<String> countryCodes);

	/**
	 * Find by id.
	 * @param id
	 *            the id
	 * @return the document code
	 * @throws ApplicationException
	 *             the application exception
	 */
	public DocumentCode findById(Long id);

}

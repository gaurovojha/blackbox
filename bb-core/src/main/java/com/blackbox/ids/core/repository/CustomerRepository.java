package com.blackbox.ids.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.mstr.Customer;

/**
 * A DAO for the entity Customer is simply created by extending the JpaRepository interface provided by spring and
 * custom repository. The following methods are some of the ones available from former interface: save, delete,
 * deleteAll, findOne and findAll. The magic is that such methods must not be implemented, and moreover it is possible
 * create new query methods working only by defining their signature! Custom interface can be used to define custom
 * query methods that can not be implemented by Spring.
 * @author Nagarro
 */
@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("Select c From Customer c where c.customerNumber = :customerNumber")
	Customer findCustomerByNumber(@Param("customerNumber") final String customerNumber);

	@Query("from Customer c left join fetch c.authenticationData where c.customerNumber = :customerNumber")
	Customer findCustomerAuthenticationData(@Param("customerNumber") final String customerNumber);

	/**
	 * Find all application numbers for all customers.
	 * @param ids
	 *            the ids
	 * @return the list
	 * @throws ApplicationException
	 *             the application exception
	 */
	@Query("SELECT customer.customerNumber,app.applicationNumber from ApplicationBase app, Customer customer where customer.id = app.customer.id "
			+ "and app.recordStatus = com.blackbox.ids.core.model.enums.MDMRecordStatus.ACTIVE and customer.id in (:ids)")
	public List<Object[]> findAllApplicationNumbersForGivenCustomers(@Param("ids") List<Long> ids);

	/**
	 * Find all customers.
	 * @return the list
	 */
	@Query("Select id From Customer")
	List<Long> findAllCustomers();

	/**
	 * Find all application numbers for all customers.
	 * @return the list
	 * @throws ApplicationException
	 *             the application exception
	 */
	@Query("SELECT customer.customerNumber,app.applicationNumber from ApplicationBase app, Customer customer where customer.id = app.customer.id "
			+ "and app.recordStatus = com.blackbox.ids.core.model.enums.MDMRecordStatus.ACTIVE")
	public List<Object[]> findAllApplicationNumbersForAllCustomers();

	/**
	 * Find all customers with given ids.
	 * @return the list
	 */
	@Query("Select c From Customer c where c.customerNumber in (:customer_nos)")
	List<Customer> findAllCustomersByNumber(@Param("customer_nos") List<String> customer_nos);

	/**
	 * Find all customers.
	 * @return the list
	 */
	@Query("Select customerNumber From Customer")
	List<String> findAllCustomersByNumber();

}

package com.blackbox.ids.core.repository.webcrawler;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.model.mstr.AuthenticationData;

/**
 * A DAO for the entity Customer is simply created by extending the JpaRepository
 * interface provided by spring and custom repository. The following methods are some of the ones
 * available from former interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature! 
 * Custom interface can be used to define custom query methods that can not be implemented by Spring. 
 * 
 * @author Nagarro
 */
@Repository
public interface AuthenticationDataRepository extends JpaRepository<AuthenticationData, Long> {

	 /**
 	 * Gets the authentication data from customer number.
 	 *
 	 * @param customerNumber the customer number
 	 * @return the authentication for customer
 	 */
 	@Query("SELECT customer.authenticationData from Customer customer where customer.customerNumber = :customerNumber")
	AuthenticationData findAuthenticationDataFromCustomer(@Param("customerNumber") String customerNumber);
 	
 	@Query("SELECT customer.customerNumber from Customer customer where authenticationData.id = :id")
	List<String> findCustomerNumbersFromAuthenticationId(@Param("id") Long id);
	
}

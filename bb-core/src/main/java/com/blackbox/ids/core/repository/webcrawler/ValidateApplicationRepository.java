package com.blackbox.ids.core.repository.webcrawler;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.blackbox.ids.core.model.webcrawler.ValidateApplication;
import com.blackbox.ids.core.repository.base.BaseRepository;

/**
 * A DAO for the entity User is simply created by extending the JpaRepository interface provided by spring and custom
 * repository. The following methods are some of the ones available from former interface: save, delete, deleteAll,
 * findOne and findAll. The magic is that such methods must not be implemented, and moreover it is possible create new
 * query methods working only by defining their signature! Custom interface can be used to define custom query methods
 * that can not be implemented by Spring.
 *
 * @author abhay2566
 */
public interface ValidateApplicationRepository extends BaseRepository<ValidateApplication, Long> {

	@Query("select va from ValidateApplication va where va.status = com.blackbox.ids.core.model.enums.QueueStatus.INITIATED order by va.customerNumber")
	List<ValidateApplication> findByCustomerNumberOrderByCustomerNumber();

}
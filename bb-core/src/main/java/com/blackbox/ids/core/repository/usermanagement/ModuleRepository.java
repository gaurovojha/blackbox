package com.blackbox.ids.core.repository.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackbox.ids.core.model.usermanagement.Module;

/**
 * A DAO for the entity Module is simply created by extending the JpaRepository
 * interface provided by spring and custom repository. The following methods are some of the ones
 * available from former interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature! 
 * Custom interface can be used to define custom query methods that can not be implemented by Spring. 
 * 
 * @author Nagarro
 */
public interface ModuleRepository extends JpaRepository<Module, Long> {
}

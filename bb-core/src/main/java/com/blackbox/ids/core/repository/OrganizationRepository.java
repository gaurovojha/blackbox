package com.blackbox.ids.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.model.mstr.Organization;

/**
 * A DAO for the entity Organization is simply created by extending the JpaRepository interface provided by spring and
 * custom repository. The following methods are some of the ones available from former interface: save, delete,
 * deleteAll, findOne and findAll. The magic is that such methods must not be implemented, and moreover it is possible
 * create new query methods working only by defining their signature! Custom interface can be used to define custom
 * query methods that can not be implemented by Spring.
 * @author Nagarro
 */
@Repository("organizationRepository")
public interface OrganizationRepository extends JpaRepository<Organization, Long> {}

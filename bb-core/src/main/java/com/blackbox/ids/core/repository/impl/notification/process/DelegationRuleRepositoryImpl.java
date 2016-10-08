package com.blackbox.ids.core.repository.impl.notification.process;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.blackbox.ids.core.repository.notification.process.DelegationRuleCustomerRepository;

public class DelegationRuleRepositoryImpl implements DelegationRuleCustomerRepository {

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;
	
}

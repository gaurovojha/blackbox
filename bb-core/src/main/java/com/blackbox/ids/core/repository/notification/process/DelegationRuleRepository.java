package com.blackbox.ids.core.repository.notification.process;

import java.util.Calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.blackbox.ids.core.model.notification.process.DelegationRule;

public interface DelegationRuleRepository extends JpaRepository<DelegationRule, Long>,
		QueryDslPredicateExecutor<DelegationRule>, DelegationRuleCustomerRepository {

	@Query("select dr.delegateRole.id from DelegationRule dr where dr.active = 'true' and dr.role.id = ?1 and dr.startDate <= ?2 and dr.endDate >= ?2")
	Long getDelegateRoleId(Long roleId, Calendar date);
	
	@Query("select count(dr.delegationRuleId) from DelegationRule dr where dr.active = 'true' and dr.role.id = ?1 and dr.endDate >= ?2")
	int isAcitveRuleExistForRoleId(Long roleId, Calendar date);
	
	@Modifying
	@Query("update DelegationRule dr set dr.active = 'false' where dr.delegationRuleId =?1")
	void deactivateDelegationRuleById(Long delegationRuleId);
}

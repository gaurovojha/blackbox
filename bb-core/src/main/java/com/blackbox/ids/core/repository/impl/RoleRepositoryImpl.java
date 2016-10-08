package com.blackbox.ids.core.repository.impl;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.repository.RoleCustomRepository;

/**
 * Implements custom methods specific to Role entity
 * 
 * @author Nagarro
 *
 */
@Repository("roleRepository")
public class RoleRepositoryImpl implements RoleCustomRepository<Role, Long> {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public Set<Long> findUserIdByRoleId(Long id) {
		Query query = entityManager.createNativeQuery(
				"Select u.bb_user_id from bb_user_role ur, bb_user u where u.bb_user_id = ur.bb_user_id and ur.bb_role_id = :id and u.is_active = :active");
		query.setParameter("id", id);
		query.setParameter("active", "true");
		List<BigInteger> ids = query.getResultList();
		Set<Long> idSet = new HashSet<Long>();
		// list is returning BigInteger so converting it to Long
		for (BigInteger key : ids) {
			Long idValue = key.longValue();
			idSet.add(idValue);
		}
		return idSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findAllRoleNames() {
		return entityManager.createNamedQuery("findAllRoleNames").getResultList();
	}

	@Override
	public boolean deactivateRole(Long id) {
		String update = "update Role r set r.active = :deactivate, r.endDate = :endDate where r.id = :id";
		Query deactivateRoleQuery = entityManager.createQuery(update).setParameter("deactivate", false).setParameter("endDate", Calendar.getInstance()).setParameter("id", id);
		int count = deactivateRoleQuery.executeUpdate();
		if(count == 1) {
			return true;	
		}
		else {
			return false;
			}
	}

	@Override
	public int countUsersByRoleIds(Set<Long> roleIds) {
		Query query = entityManager.createNativeQuery(
				"Select count(u.bb_user_id) from bb_user_role ur, bb_user u where u.bb_user_id = ur.bb_user_id and ur.bb_role_id in :roleIds and u.is_active = :active");
		query.setParameter("roleIds", roleIds);
		query.setParameter("active", "true");
		return (Integer) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getUsersByRoleIds(Set<Long> roleIds) {
		Query query = entityManager.createNativeQuery(
				"Select u.bb_user_id from bb_user_role ur, bb_user u where u.bb_user_id = ur.bb_user_id and ur.bb_role_id in :roleIds and u.is_active = :active");
		query.setParameter("roleIds", roleIds);
		query.setParameter("active", "true");
		
		List<BigInteger> ids = query.getResultList();
		return ids.stream().map(id -> id.longValue()).collect(Collectors.toList());
	}
}
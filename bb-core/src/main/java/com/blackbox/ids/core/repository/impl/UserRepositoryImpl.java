package com.blackbox.ids.core.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.repository.UserCustomRepository;

/**
 * Implements custom methods specific to User entity
 *
 * @author Nagarro
 *
 */
public class UserRepositoryImpl implements UserCustomRepository<User, Long> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public String getEmailId(Long id) {
		List<String> emails;
		Query query = entityManager.createNamedQuery("findEmailById");
		query.setParameter(1, id);
		emails = query.getResultList();
		if (!CollectionUtils.isEmpty(emails)) {
			return emails.get(0);
		}
		return null;
	}

	@Override
	public boolean disableAccess(Long id) {
			String update = "update User u set u.isEnabled = :isEnabled where u.id = :id";
			Query disableUserQuery = entityManager.createQuery(update).setParameter("isEnabled", false)
					.setParameter("id", id);
			disableUserQuery.executeUpdate();
			return true;
	}

	@Override
	public boolean deleteUser(Long id) {
			String update = "update User u set u.isActive = :isActive , u.isEnabled= :isEnabled where u.id = :id";
			Query deleteUserQuery = entityManager.createQuery(update).setParameter("isActive", false);
			deleteUserQuery.setParameter("id", id);
			deleteUserQuery.setParameter("isEnabled", false);
			deleteUserQuery.executeUpdate();
			return true;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getId(String email) {
		List<Long> ids;
		Query query = entityManager.createNamedQuery("findUserIdByEmail");
		query.setParameter(1, email);
		ids = query.getResultList();
		if (!CollectionUtils.isEmpty(ids)) {
			return ids.get(0);
		}
		return null;
	}

	@Override
	public void savePassword(String username, String password) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaUpdate<User> updateCriteria = cb.createCriteriaUpdate(User.class);
		Root<User> root = updateCriteria.from(User.class);
		updateCriteria.set(root.get("password"), password);
		updateCriteria.set(root.get("isFirstLogin"), false);
		updateCriteria.where(cb.equal(root.get("loginId"), username));
		entityManager.createQuery(updateCriteria).executeUpdate();
	}

	@Override
	public String getUserFullName(String userId) {

		Query query = entityManager.createQuery(
				"Select COALESCE(u.person.firstName, ' ') || COALESCE(u.person.middleName, ' ') || COALESCE(u.person.lastName, '') from User u where u.loginId = :userId");
		query.setParameter("userId", userId);
		String fullName = (String)query.getSingleResult();
		/*Object ob[] = new Object[3];
		if (!CollectionUtils.isEmpty(list)) {

			ob = (Object[]) list.get(0);
			for (int i = 0; i < 3; i++) {
				if (!StringUtils.isEmpty(ob[i])) {
					fullName = fullName + " " + (String) ob[i];
				}
			}
		}*/
		return fullName;
	}

	@Override
	public String getFirstNameByLoginId(String userId) {
		String firstName = "";
		Query query = entityManager.createQuery("Select u.person.firstName from User u where u.loginId = :userId");
		query.setParameter("userId", userId);
		List list = query.getResultList();
		if (!CollectionUtils.isEmpty(list)) {
			firstName = (String) list.get(0);
		}
		return firstName;
	}

	@Override
	public String getUserFirstAndLastName(Long id) {

		String name = "";
		Query query = entityManager.createQuery(
				"Select u.person.firstName, u.person.lastName from User u where u.id = :id");
		query.setParameter("id", id);
		List list = query.getResultList();
		Object ob[] = new Object[3];
		if (!CollectionUtils.isEmpty(list)) {

			ob = (Object[]) list.get(0);
			for (int i = 0; i < 2; i++) {
				if (!StringUtils.isEmpty(ob[i])) {
					name = name + " " + (String) ob[i];
				}
			}
		}
		return name;
	}
}

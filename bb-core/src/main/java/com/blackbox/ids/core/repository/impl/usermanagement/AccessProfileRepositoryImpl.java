package com.blackbox.ids.core.repository.impl.usermanagement;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.usermanagement.AccessProfile;
import com.blackbox.ids.core.repository.usermanagement.AccessProfileCustomRepository;

/**
 * Implements custom methods specific to AccessProfile entity
 * @author Nagarro
 */
@Repository("accessProfileRepository")
public class AccessProfileRepositoryImpl implements AccessProfileCustomRepository<AccessProfile, Long> {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public Set<Long> findUserIdByAccessProfileId(Long id) {

		Query query = entityManager.createNativeQuery("Select ur.bb_user_id from BB_USER_ROLE ur, BB_ROLE r "
				+ "where ur.bb_role_id = r.bb_role_id and r.bb_access_profile_id = :id");
		query.setParameter("id", id);
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
	public List<String> findAllAccessProfileNames() {
		List<String> names = entityManager.createNamedQuery("findAllProfileNames").getResultList();
		return names;
	}

	@Override
	public boolean deactivateAccessProfile(Long id) {
		try {
			String update = "update AccessProfile a set a.active = :deactivate, a.endDate = :endDate where a.id = :id";
			Query deactivateAccessProfileQuery = entityManager.createQuery(update).setParameter("deactivate", false)
					.setParameter("endDate", Calendar.getInstance()).setParameter("id", id);
			deactivateAccessProfileQuery.executeUpdate();
			return true;
		} catch (Exception e) {
			throw new ApplicationException(1, "Not able to deactivate accessprofile", e);
		}
	}
}

/**
 *
 */
package com.blackbox.ids.services.impl.common;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dao.EntityUserDAO;
import com.blackbox.ids.core.model.EntityUser;
import com.blackbox.ids.core.model.EntityUser.EntityName;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.services.common.EntityUserService;

/**
 * @author ajay2258
 *
 */
@Service(EntityUserService.BEAN_NAME)
public class EntityUserServiceImpl implements EntityUserService {

	private Logger log = Logger.getLogger(EntityUserService.class);

	@Value("${clear.recordLock.after.hours}")
	private int clearLocksAfter;

	@Autowired
	private EntityUserDAO entityUserDAO;

	@Override
	// @PostConstruct
	@Transactional
	public long deleteAllLocks() throws ApplicationException {
		Calendar clearLockedAcquiredBefore = BlackboxDateUtil.timeAfterHours(Calendar.getInstance(), -clearLocksAfter);
		log.info(String.format("Clearing locks acquired before {0}.", clearLockedAcquiredBefore.getTime()));
		long numLockedRemoved = entityUserDAO.deleteLocksAcquiredBefore(clearLockedAcquiredBefore);
		log.info(String.format("Removed {0} cleared successfully.", numLockedRemoved));
		return numLockedRemoved;
	}

	@Override
	@Transactional
	public boolean checkAndGetLockOnEntity(EntityName entityType, Long recordId) throws ApplicationException {
		boolean lockClaimed = false;
		Long currentUser = BlackboxSecurityContextHolder.getUserId();

		// XXX: Acquire lock if claimed before 8 hours.
		EntityUser entityUser = entityUserDAO.getLockDetailsOnEntity(entityType, recordId);
		if (entityUser != null) {
			lockClaimed = entityUser.getLockedBy().equals(currentUser);
		} else {
			entityUser = new EntityUser(entityType, recordId);
			entityUserDAO.persist(entityUser);
			lockClaimed = true;
		}

		return lockClaimed;
	}

	@Override
	@Transactional
	public long releaseLocksHeldByUser(final long userId) throws ApplicationException {
		log.info(String.format("Releasing locks acquired by user {0}.", userId));
		long numLockedReleased = entityUserDAO.releaseLocksHeldByUser(userId);
		log.info(String.format("Released total {0} locks acquired by user {1}.", numLockedReleased, userId));
		return numLockedReleased;
	}

	@Override
	public EntityUser getLockDetailsOnEntity(EntityName entityType, Long recordId) throws ApplicationException {
		return entityUserDAO.getLockDetailsOnEntity(entityType, recordId);
	}

	@Override
	@Transactional
	public long resetLocksHeldByUser(Long userId, EntityName entityName) {
		if (userId == null || entityName == null) {
			throw new IllegalArgumentException("[Reset entity locks]: Missing required parameters.");
		}
		log.info(String.format("Releasing user {0} locks for entity type {1}.", userId, entityName));
		long numLockedReleased = entityUserDAO.releaseLocks(userId, entityName);
		log.info(String.format("Release total {0} locks for user {1} for entity {2}.", numLockedReleased, userId,
				entityName));
		return numLockedReleased;
	}

}

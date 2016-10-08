/**
 *
 */
package com.blackbox.ids.core.dao.impl;

import java.util.Calendar;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.EntityUserDAO;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.model.EntityUser;
import com.blackbox.ids.core.model.EntityUser.EntityName;
import com.blackbox.ids.core.model.QEntityUser;

/**
 * @author ajay2258
 *
 */
@Repository
public class EntityUserDaoImpl extends BaseDaoImpl<EntityUser, Long> implements EntityUserDAO {

	@Override
	public long deleteLocksAcquiredBefore(Calendar clearLockedAcquiredBefore) {
		QEntityUser entityUser = QEntityUser.entityUser;
		return getJPADeleteClause(entityUser)
				.where(entityUser.lockAcquiredOn.loe(clearLockedAcquiredBefore))
				.execute();
	}

	@Override
	public EntityUser getLockDetailsOnEntity(EntityName entityType, Long recordId) {
		QEntityUser entityUser = QEntityUser.entityUser;
		return getJPAQuery().from(entityUser)
				.where(entityUser.entity.eq(entityType).and(entityUser.recordId.eq(recordId)))
				.uniqueResult(entityUser);
	}

	@Override
	public long releaseLocksHeldByUser(final long lockOwner) {
		QEntityUser entityUser = QEntityUser.entityUser;
		return getJPADeleteClause(entityUser)
				.where(entityUser.lockedBy.longValue().eq(lockOwner))
				.execute();
	}

	@Override
	public long releaseLocks(Long userId, EntityName entityName) {
		QEntityUser entityUser = QEntityUser.entityUser;
		return getJPADeleteClause(entityUser)
				.where(entityUser.lockedBy.eq(userId).and(entityUser.entity.eq(entityName)))
				.execute();
	}

}

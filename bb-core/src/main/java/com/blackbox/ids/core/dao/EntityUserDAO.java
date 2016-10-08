/**
 *
 */
package com.blackbox.ids.core.dao;

import java.util.Calendar;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.model.EntityUser;
import com.blackbox.ids.core.model.EntityUser.EntityName;

/**
 * @author ajay2258
 */
public interface EntityUserDAO extends BaseDAO<EntityUser, Long> {

	long deleteLocksAcquiredBefore(Calendar clearLockedAcquiredBefore);

	EntityUser getLockDetailsOnEntity(EntityName entityType, Long recordId);

	long releaseLocksHeldByUser(final long lockOwner);

	long releaseLocks(Long userId, EntityName entityName);

}

/**
 *
 */
package com.blackbox.ids.services.common;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.EntityUser;
import com.blackbox.ids.core.model.EntityUser.EntityName;

/**
 * The interface type {@code EntityUserService} exposes APIs to perform business over entity class {@code EntityUser}.
 *
 * @author ajay2258
 */
public interface EntityUserService {

	String BEAN_NAME = "entityUserService";

	public long deleteAllLocks() throws ApplicationException;

	public boolean checkAndGetLockOnEntity(EntityName entityType, Long entityId) throws ApplicationException;

	public long releaseLocksHeldByUser(final long userId) throws ApplicationException;

	public EntityUser getLockDetailsOnEntity(final EntityName entityType, final Long entityId)
			throws ApplicationException;

	public long resetLocksHeldByUser(Long userId, EntityName entityName);

}

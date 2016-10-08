package com.blackbox.ids.core.dao.notification;

import java.util.Set;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcess;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;

/**
 * The {@code NotificationProcessDAO} exposes APIs to perform database operations on {@link NotificationProcess} derived
 * entity classes.
 * <p/>
 *
 * @author nagarro
 */
public interface NotificationProcessDAO extends BaseDAO<NotificationProcess, Long> {

	public Long getNotificationProcess(Long id, NotificationProcessType processType,
			EntityName entityName, Set<Long> roleIds);
}

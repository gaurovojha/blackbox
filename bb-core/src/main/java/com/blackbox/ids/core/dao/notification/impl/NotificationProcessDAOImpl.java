
/**
 *
 */
package com.blackbox.ids.core.dao.notification.impl;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.correspondence.impl.CorrespondenceDAOImpl;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dao.notification.NotificationProcessDAO;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcess;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.QNotificationProcess;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ConstructorExpression;

/**
 * The <code>NotificationProcessDAOImpl</code> provides implementation of {@link NotificationProcessDAO} for all type of
 * operations related to Notification related functionality.
 *
 * @author nagarro
 */
@Repository
public class NotificationProcessDAOImpl extends BaseDaoImpl<NotificationProcess, Long>
		implements NotificationProcessDAO {

	private Logger logger = Logger.getLogger(CorrespondenceDAOImpl.class);

	public Long getNotificationProcess(Long id, NotificationProcessType processType,
			EntityName entityName, Set<Long> userRolesId) {
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		JPAQuery query = getJPAQuery().from(notificationProcess);
		query.where(notificationProcess.notificationProcessId.eq(id)
				.and(notificationProcess.notificationProcessType.eq(processType))
				.and(notificationProcess.entityName.eq(entityName)).and(notificationProcess.active.eq(true))
				.and(notificationProcess.roles.any().id.in(userRolesId)));
		return query.uniqueResult(ConstructorExpression.create(Long.class, notificationProcess.entityId));
	}
}

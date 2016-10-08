package com.blackbox.ids.core.dao.webcrawler.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dao.webcrawler.IN417TreatmentQueueDAO;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.QApplicationBase;
import com.blackbox.ids.core.model.webcrawler.N417TreatmentQueue;
import com.blackbox.ids.core.model.webcrawler.QN417TreatmentQueue;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository
public class N417TreatmentQueueDAOImpl extends BaseDaoImpl<N417TreatmentQueue, Long> implements IN417TreatmentQueueDAO {
	
	@Override
	public List<N417TreatmentQueue> getApplicationsList() {
		QN417TreatmentQueue n417Queue = QN417TreatmentQueue.n417TreatmentQueue;
		QApplicationBase app = QApplicationBase.applicationBase;

		JPAQuery query = getJPAQuery().from(n417Queue).innerJoin(n417Queue.application, app).fetch()
				.innerJoin(app.customer).fetch()
				.innerJoin(app.customer.authenticationData).fetch()
				.innerJoin(app.jurisdiction).fetch().distinct();
		query = query.where(n417Queue.status.eq(QueueStatus.INITIATED));
		
		return query.list(n417Queue);
	}

}

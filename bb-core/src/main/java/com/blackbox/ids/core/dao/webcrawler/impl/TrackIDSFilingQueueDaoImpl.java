package com.blackbox.ids.core.dao.webcrawler.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dao.webcrawler.ITrackIDSFilingQueueDao;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.webcrawler.QTrackIDSFilingQueue;
import com.blackbox.ids.core.model.webcrawler.TrackIDSFilingQueue;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Predicate;

@Repository
public class TrackIDSFilingQueueDaoImpl extends BaseDaoImpl<TrackIDSFilingQueue	, Long> implements ITrackIDSFilingQueueDao {
	
	@Override
	public Map<String, List<TrackIDSFilingQueue>> getIdSTrackingQueueByCustomer() {
		QTrackIDSFilingQueue idsQueue = QTrackIDSFilingQueue.trackIDSFilingQueue;
		
		Predicate filter = idsQueue.status.eq(QueueStatus.INITIATED).and(idsQueue.nextRunDate.loe(Calendar.getInstance()));
		JPAQuery query = getJPAQuery().from(idsQueue).where(filter);
		return query.list(idsQueue).stream().collect(Collectors.groupingBy(TrackIDSFilingQueue::getCustomerNumber));
	}
	

}

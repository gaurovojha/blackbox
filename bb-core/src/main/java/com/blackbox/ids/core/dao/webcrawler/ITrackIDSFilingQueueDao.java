package com.blackbox.ids.core.dao.webcrawler;

import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.model.webcrawler.TrackIDSFilingQueue;

/**
 * The {@code ITrackIDSFilingQueueDao} exposes APIs to perform database operations on entity class {@link TrackIDSFilingQueue}.
 */
public interface ITrackIDSFilingQueueDao extends BaseDAO<TrackIDSFilingQueue, Long> {

	/**
	 * Gets the IDS tracking queue group by customer.
	 *
	 * @return the IDS tracking queue group by customer
	 */
	Map<String, List<TrackIDSFilingQueue>> getIdSTrackingQueueByCustomer();
	
}

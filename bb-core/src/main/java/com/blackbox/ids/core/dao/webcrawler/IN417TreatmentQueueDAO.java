package com.blackbox.ids.core.dao.webcrawler;

import java.util.List;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.model.webcrawler.N417TreatmentQueue;

/**
 * The {@code IN417TreatmentQueueDAO} exposes APIs to perform database operations on entity class {@link N417TreatmentQueue}.
 */
public interface IN417TreatmentQueueDAO extends BaseDAO<N417TreatmentQueue, Long> {

	/**
	 * Gets N417 Treatment queue list.
	 *
	 * @return the N417 treatment queue
	 */
	List<N417TreatmentQueue> getApplicationsList();
	
}

package com.blackbox.ids.webcrawler.service.uspto.ids;

/**
 * Service to tracking IDS filing at USPTO. This service will read IDS from tracking queue and tracking filing status at
 * USPTO site.
 */
public interface ITrackIDSFilingService {

	/**
	 * Executes IDS filing tracking.
	 *
	 * @param maxRetryCount the max retry count
	 */
	void execute(int maxRetryCount);

}

package com.blackbox.ids.scheduler.webcrawler;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.webcrawler.service.uspto.ids.ITrackIDSFilingService;

/**
 * Background job to track IDS filing for manual channel.
 */
public class TrackIDSFilingJob extends AbstractWebCrawlerBaseJob {

	/** The ids tracking service. */
	@Autowired
	private ITrackIDSFilingService service;

	@Override
	public void executeJob(WebCrawlerJobAudit webCrawlerJobAudit, int retryCount) {
		service.execute(retryCount);
	}

}

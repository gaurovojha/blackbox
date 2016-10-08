package com.blackbox.ids.scheduler.webcrawler;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.webcrawler.service.uspto.application.ITrackFamilyService;

public class TrackApplicationFamilyJob extends AbstractWebCrawlerBaseJob {
	
	@Autowired
	private ITrackFamilyService trackFamilyService;

	@Override
	public void executeJob(final WebCrawlerJobAudit webCrawlerJobAudit, final int maxRetryCount) {
		trackFamilyService.execute(webCrawlerJobAudit, maxRetryCount);
	}

}

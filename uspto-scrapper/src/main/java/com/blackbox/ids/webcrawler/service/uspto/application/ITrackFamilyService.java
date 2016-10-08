package com.blackbox.ids.webcrawler.service.uspto.application;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;

public interface ITrackFamilyService {
	void execute(WebCrawlerJobAudit webCrawlerJobAudit, final int maxRetryCount);
}

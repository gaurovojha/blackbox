package com.blackbox.ids.scheduler.webcrawler;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.webcrawler.service.uspto.ids.IN417TreatmentService;

public class N417TreatmentJob extends AbstractWebCrawlerBaseJob {
	
	@Autowired
	private IN417TreatmentService service;

	@Override
	public void executeJob(WebCrawlerJobAudit webCrawlerJobAudit, int retryCount) {
		service.execute();
	}

}

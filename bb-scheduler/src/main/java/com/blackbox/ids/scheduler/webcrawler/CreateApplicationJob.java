package com.blackbox.ids.scheduler.webcrawler;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.webcrawler.service.uspto.application.ICreateApplicationService;


public class CreateApplicationJob extends AbstractWebCrawlerBaseJob {
	
	@Autowired
	private ICreateApplicationService applicationService;
	
	@Override
	public void executeJob(WebCrawlerJobAudit webCrawlerJobAudit, int retryCount) {
		applicationService.execute(retryCount);
	}

}

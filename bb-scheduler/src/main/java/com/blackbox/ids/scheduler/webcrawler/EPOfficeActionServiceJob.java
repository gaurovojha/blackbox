package com.blackbox.ids.scheduler.webcrawler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.webcrawler.service.uspto.IBaseCrawlerService;

public class EPOfficeActionServiceJob extends AbstractWebCrawlerBaseJob {

	private Logger logger = Logger.getLogger(EPOfficeActionServiceJob.class);

	@Autowired
	@Qualifier("ePOfficeActionService")
	private IBaseCrawlerService service;

	@Override
	public void executeJob(WebCrawlerJobAudit webCrawlerJobAudit, int retryCount) {
		logger.info("Starting " + this.getClass().getName() + "Job");

		service.execute(webCrawlerJobAudit);

	}

}

package com.blackbox.ids.scheduler.webcrawler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.webcrawler.service.uspto.IBaseCrawlerService;

public class ApplicationValidationJob extends AbstractWebCrawlerBaseJob {

	private Logger logger = Logger.getLogger(ApplicationValidationJob.class);

	@Autowired
	@Qualifier("applicationValidationService")
	private IBaseCrawlerService validationService;

	@Override
	public void executeJob(WebCrawlerJobAudit webCrawlerJobAudit, int retryCount) {

		logger.info("Import Job Started : " + this.getClass().getName());
		validationService.execute(webCrawlerJobAudit);
	}
}
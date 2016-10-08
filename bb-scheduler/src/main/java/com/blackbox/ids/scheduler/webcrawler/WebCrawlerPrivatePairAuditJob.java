package com.blackbox.ids.scheduler.webcrawler;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IPrivatePairAuditWebCrawler;

/**
 * The Class WebCrawlerPrivatePairAuditJob.
 */
public class WebCrawlerPrivatePairAuditJob extends AbstractWebCrawlerBaseJob {

	/** The logger. */
	private static Log logger = LogFactory.getLog(WebCrawlerPrivatePairAuditJob.class);
	
	/** The web crawler private pair audit service. */
	@Autowired
	private IPrivatePairAuditWebCrawler webCrawlerPrivatePairAuditService;
	
	@Override
	public void executeJob(WebCrawlerJobAudit webCrawlerJobAudit, int retryCount) {
		logger.info(MessageFormat.format("About to execute the service for Web Crawler Private pair audit job with crawler id {0}"
				,webCrawlerJobAudit.getId()));
		webCrawlerPrivatePairAuditService.execute(webCrawlerJobAudit);
	}

}

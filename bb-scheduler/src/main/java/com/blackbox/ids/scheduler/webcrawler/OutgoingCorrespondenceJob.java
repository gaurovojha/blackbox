package com.blackbox.ids.scheduler.webcrawler;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IOutgoingCorrespondenceWebCrawlerService;

/**
 * The Class OutgoingCorrespondenceJob.
 */
public class OutgoingCorrespondenceJob extends AbstractWebCrawlerBaseJob {

	/** The logger. */
	private static Log logger = LogFactory.getLog(OutgoingCorrespondenceJob.class);
	
	/** The correspondence web crawler service. */
	@Autowired
	private IOutgoingCorrespondenceWebCrawlerService correspondenceWebCrawlerService;
	
	/**
	 * Execute job.
	 *
	 */
	@Override
	public void executeJob(WebCrawlerJobAudit webCrawlerJobAudit, int retryCount) {
		logger.info(MessageFormat.format("Outgoing Correspondence Job id [{0}] invoked.", webCrawlerJobAudit.getId()));
		correspondenceWebCrawlerService.execute(webCrawlerJobAudit);
	}
}

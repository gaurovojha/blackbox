package com.blackbox.ids.scheduler.webcrawler;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IIFWDownloadDoCWebCrawlerService;

/**
 * The Class IFWDocDownloadJob.
 */
public class IFWDocDownloadJob extends AbstractWebCrawlerBaseJob {

	/** The download doc web crawler service. */
	@Autowired
	private IIFWDownloadDoCWebCrawlerService downloadDoCWebCrawlerService;
	
	@Override
	public void executeJob(WebCrawlerJobAudit webCrawlerJobAudit, int retryCount) {
		downloadDoCWebCrawlerService.execute(webCrawlerJobAudit);
	}

}

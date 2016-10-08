package com.blackbox.ids.webcrawler.service.uspto.correspondence;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;

/**
 * The Interface {@link IIFWDownloadDoCWebCrawlerService}
 * and contains the business method specific method to IFW DOC Download Web Crawler.
 * 
 * @author nagarro
 * 
 *
 */
public interface IIFWDownloadDoCWebCrawlerService {
	
	/**
     * This API scraps the USPTO website and prepares the data depending on the type of the implementation of the job.
     *
     * @param WebCrawlerJobAudit
     *            the meta data for the Current Crawler job run.
     */
	void execute(WebCrawlerJobAudit webCrawlerJobAudit);
	
}
package com.blackbox.ids.webcrawler.service.uspto.correspondence;

import java.io.IOException;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;

/**
 * The Interface {@link IOutgoingCorrespondenceWebCrawlerService}
 * contains the business method specific method to Outgoing Correspondence Web Crawler.
 * 
 * @author nagarro
 * 
 *
 */
public interface IOutgoingCorrespondenceWebCrawlerService {

    /**
     * This API scraps the USPTO website and prepares the data depending on the type of the implementation of the job.
     *
     * @param WebCrawlerJobAudit
     *            the meta data for the Current Crawler job run.
     * @throws IOException 
     */
	void execute(WebCrawlerJobAudit webCrawlerJobAudit);
	
}

package com.blackbox.ids.webcrawler.service.uspto;

import java.util.Map;

import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;

/**
 * 
 * @author Nagarro
 *
 */
public interface IBaseCrawlerService {
	
	/**
	 * Base execution API
	 * @param webCrawlerJobAudit TODO
	 */
	 Map<String , Object> execute(WebCrawlerJobAudit webCrawlerJobAudit);

}

package com.blackbox.ids.webcrawler.service.uspto.correspondence;

import com.blackbox.ids.core.model.mstr.AuthenticationData;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;

/**
 * The Interface {@link IPrivatePairAuditScrapper} is the scrapper service for Private Pair Audit Crawler.
 * @author nagarro
 */
public interface IPrivatePairAuditScrapper {

	void performScrapping(AuthenticationData epfFile, String absolutePath, WebCrawlerJobAudit webCrawlerJobAudit);

}

package com.blackbox.ids.services.crawler;

import com.blackbox.ids.core.model.mstr.AuthenticationData;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IPrivatePairAuditScrapper;

public class MockPrivatePairAuditScrapperImpl implements IPrivatePairAuditScrapper {
	

	@Override
	public void performScrapping(AuthenticationData epfFile, String absolutePath, WebCrawlerJobAudit webCrawlerJobAudit){
		return;
	}

}

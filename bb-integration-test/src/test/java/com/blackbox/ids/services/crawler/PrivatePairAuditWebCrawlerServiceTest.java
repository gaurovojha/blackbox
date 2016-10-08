package com.blackbox.ids.services.crawler;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.correspondence.PairAudit;
import com.blackbox.ids.core.model.enums.WebCrawlerJobStatusEnum;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.repository.correspondence.PairAuditRepository;
import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IPrivatePairAuditWebCrawler;

public class PrivatePairAuditWebCrawlerServiceTest extends AbstractIntegrationTest {

	@Autowired
	private IPrivatePairAuditWebCrawler	privatePairAuditWebCrawler;
	
	@Autowired
	private PairAuditRepository	pairAuditRepository;
	
	@Test
	public void testExecuteRecordsFound(){
		WebCrawlerJobAudit webCrawlerJobAudit = new WebCrawlerJobAudit(WebCrawlerJobStatusEnum.RUNNING,
				BBWebCrawerConstants.EMPTY_STRING, Calendar.getInstance(), null, this.getClass().getSimpleName());
		webCrawlerJobAudit.setId(new Long(1));
		List<PairAudit> pairAuditsListBeforeAudit = pairAuditRepository.findAll();
		int pairAuditEntriesBeforeAudit = pairAuditsListBeforeAudit.size();
		privatePairAuditWebCrawler.execute(webCrawlerJobAudit);
		List<PairAudit> pairAuditsListAfterAudit = pairAuditRepository.findAll();
		int pairAuditEntriesAfterAudit = pairAuditsListAfterAudit.size();
		int entriesAdded = pairAuditEntriesAfterAudit - pairAuditEntriesBeforeAudit;
		Assert.assertEquals(2, entriesAdded);
	}
	
	@Test
	public void testExecuteRecordsNotFound(){
		WebCrawlerJobAudit webCrawlerJobAudit = new WebCrawlerJobAudit(WebCrawlerJobStatusEnum.RUNNING,
				BBWebCrawerConstants.EMPTY_STRING, Calendar.getInstance(), null, this.getClass().getSimpleName());
		webCrawlerJobAudit.setId(new Long(2));
		List<PairAudit> pairAuditsListBeforeAudit = pairAuditRepository.findAll();
		int pairAuditEntriesBeforeAudit = pairAuditsListBeforeAudit.size();
		privatePairAuditWebCrawler.execute(webCrawlerJobAudit);
		List<PairAudit> pairAuditsListAfterAudit = pairAuditRepository.findAll();
		int pairAuditEntriesAfterAudit = pairAuditsListAfterAudit.size();
		Assert.assertEquals(pairAuditEntriesBeforeAudit, pairAuditEntriesAfterAudit);
		
	}
}

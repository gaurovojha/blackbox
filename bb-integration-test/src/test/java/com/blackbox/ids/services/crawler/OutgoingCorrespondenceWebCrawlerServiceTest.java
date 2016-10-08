package com.blackbox.ids.services.crawler;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.dao.correspondence.impl.CorrespondenceStageDAOImpl;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.enums.WebCrawlerJobStatusEnum;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IOutgoingCorrespondenceWebCrawlerService;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class OutgoingCorrespondenceWebCrawlerServiceTest extends AbstractIntegrationTest{

	@Autowired
	private IOutgoingCorrespondenceWebCrawlerService outgoingCorrespondenceWebCrawlerService ;
	
	@Autowired
	private CorrespondenceStageDAOImpl correspondenceStageDao;
	

	@Test
	public void testExecute(){
		WebCrawlerJobAudit webCrawlerJobAudit = new WebCrawlerJobAudit(WebCrawlerJobStatusEnum.RUNNING,
					BBWebCrawerConstants.EMPTY_STRING, Calendar.getInstance(), null, this.getClass().getSimpleName());
		webCrawlerJobAudit.setId(new Long(1));
		List<CorrespondenceStaging> stagingList = correspondenceStageDao.findAll();
		int initialStagingCount = stagingList.size() ;
		outgoingCorrespondenceWebCrawlerService.execute(webCrawlerJobAudit);
		stagingList = correspondenceStageDao.findAll();
		int finalStagingCount = stagingList.size() ;
		int recordsAdded = finalStagingCount - initialStagingCount;
		Assert.assertEquals(6, recordsAdded);
	}
}

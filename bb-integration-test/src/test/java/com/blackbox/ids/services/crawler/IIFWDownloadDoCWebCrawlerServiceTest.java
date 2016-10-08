package com.blackbox.ids.services.crawler;

import java.io.File;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.enums.WebCrawlerJobStatusEnum;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.util.BlackboxUtils;
import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.impl.IFWDownloadDoCWebCrawlerServiceImpl;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

public class IIFWDownloadDoCWebCrawlerServiceTest extends AbstractIntegrationTest{

	@Autowired
	@InjectMocks
	private IFWDownloadDoCWebCrawlerServiceImpl ifwDownloadDocWebCrawlerServiceImpl ;
	
	@Mock
	private IUSPTOLoginService loginService;
	
	private WebDriver webDriver;
	
    @Before
    public void Setup(){
           MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testExecute(){
		
		WebCrawlerJobAudit webCrawlerJobAudit = new WebCrawlerJobAudit(WebCrawlerJobStatusEnum.RUNNING,
				BBWebCrawerConstants.EMPTY_STRING, Calendar.getInstance(), null, this.getClass().getSimpleName());
		webCrawlerJobAudit.setId(new Long(3));
		String completePath = BlackboxUtils.concat(FolderRelativePathEnum.CRAWLER.getAbsolutePath(BBWebCrawerConstants.AUTHENTICATION_FOLDER),
				"crawler",File.separator,"epf",File.separator);
        Mockito.doNothing().when(loginService).login(webDriver,completePath, "India111");
		ifwDownloadDocWebCrawlerServiceImpl.execute(webCrawlerJobAudit);
	}
}

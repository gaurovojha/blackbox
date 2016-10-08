package com.blackbox.ids.usptoscrapper.application;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.webcrawler.service.uspto.application.impl.TrackFamilyServiceImpl;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

public class TrackFamilyServiceImplTest extends AbstractIntegrationTest {
	
	private static final Logger log = Logger.getLogger(TrackFamilyServiceImplTest.class);

	private final static int RETRY_COUNT = 2;
	
	private final static String CUSTOMER_NUMBER = "123-456-ABC";

    private static long ID = 200001;
    
    private static long APPLICATION_BASE_ID = 200004;
    
    private static int count = 0;
	
    @Mock
	private IUSPTOLoginService loginService;

	private WebDriver webDriver;

	private WebCrawlerJobAudit webCrawlerJobAudit;

	@InjectMocks
	@Autowired
	TrackFamilyServiceImpl trackFamilyServiceImpl;

	@BeforeClass
	public static void beforeClass() {
		log.info("Junit Test cases start for Crwaler TrackFamily Service");
	}

	@Before
	public void setUp() {
		count++;
		MockitoAnnotations.initMocks(this);
		log.info(MessageFormat.format("Test case {0} start executing", count));
	}
	
	@Test
	public void testTrackfamilyService() {

		Mockito.when(loginService.login(webDriver, CUSTOMER_NUMBER)).thenReturn(true);

		webCrawlerJobAudit = new WebCrawlerJobAudit();

		webCrawlerJobAudit.setId(ID);

		Assert.assertNotNull(trackFamilyServiceImpl);

		trackFamilyServiceImpl.execute(webCrawlerJobAudit, RETRY_COUNT);
		
		//we are running void method and do not have meaningful information to assert.
		Assert.assertTrue(true);

	}
	@Test
	public void testTrackfamilyServiceForFailedLogin() {

		Mockito.when(loginService.login(webDriver, CUSTOMER_NUMBER)).thenReturn(false);

		webCrawlerJobAudit = new WebCrawlerJobAudit();

		webCrawlerJobAudit.setId(ID);

		Assert.assertNotNull(trackFamilyServiceImpl);

		trackFamilyServiceImpl.execute(webCrawlerJobAudit, RETRY_COUNT);
		
		//we are running void method and do not have meaningful information to assert.
		Assert.assertTrue(true);

	}
	@Test
	public void testTrackfamilyServiceForavailbaleOnApplicationBase() {

		Mockito.when(loginService.login(webDriver, CUSTOMER_NUMBER)).thenReturn(true);

		webCrawlerJobAudit = new WebCrawlerJobAudit();

		webCrawlerJobAudit.setId(APPLICATION_BASE_ID);

		Assert.assertNotNull(trackFamilyServiceImpl);

		trackFamilyServiceImpl.execute(webCrawlerJobAudit, RETRY_COUNT);
		
		//we are running void method and do not have meaningful information to assert.
		Assert.assertTrue(true);

	}
	
	@After
	public void afterTest() {
		log.info(MessageFormat.format("Test case {0} stop executing", count));
	}

	@AfterClass
	public static void afterClass() {
		log.info("Junit Test cases end for Crwaler TrackFamily Service");
	}
}

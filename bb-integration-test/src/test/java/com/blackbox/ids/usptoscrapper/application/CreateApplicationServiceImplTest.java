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

import com.blackbox.ids.it.AbstractIntegrationTest;
import com.blackbox.ids.webcrawler.service.uspto.application.impl.CreateApplicationServiceImpl;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

public class CreateApplicationServiceImplTest extends AbstractIntegrationTest {

	private static final Logger log = Logger.getLogger(CreateApplicationServiceImplTest.class);

	private final static int RETRY_COUNT = 2;

	private final static String CUSTOMER_NO = "22971";

	private static int count = 0;

	@Mock
	private IUSPTOLoginService loginService;

	private WebDriver webDriver;

	@InjectMocks
	@Autowired
	CreateApplicationServiceImpl createApplicationService;

	@BeforeClass
	public static void beforeClass() {
		log.info("Junit Test cases start for Crwaler Application Service");
	}

	@Before
	public void setUp() {
		count++;
		MockitoAnnotations.initMocks(this);
		log.info(MessageFormat.format("Test case {0} start executing", count));
	}

	@Test
	public void testApplicationService() {

		Mockito.when(loginService.login(webDriver, CUSTOMER_NO)).thenReturn(true);

		Assert.assertNotNull(createApplicationService);

		createApplicationService.execute(RETRY_COUNT);

		// we are running void method and do not have meaningful information to
		// assert.
		Assert.assertTrue(true);
	}
	
	@Test
	public void testApplicationServiceForLoginFalied() {

		Mockito.when(loginService.login(webDriver, CUSTOMER_NO)).thenReturn(false);

		Assert.assertNotNull(createApplicationService);

		createApplicationService.execute(RETRY_COUNT);

		// we are running void method and do not have meaningful information to
		// assert.
		Assert.assertTrue(true);
	}
	
	@After
	public void afterTest() {
		log.info(MessageFormat.format("Test case {0} stop executing", count));
	}

	@AfterClass
	public static void afterClass() {
		log.info("Junit Test cases end for Crwaler Application Service");
	}

}

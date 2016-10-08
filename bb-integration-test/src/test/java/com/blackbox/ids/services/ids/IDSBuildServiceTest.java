package com.blackbox.ids.services.ids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.it.AbstractIntegrationTest;

public class IDSBuildServiceTest extends AbstractIntegrationTest{

	@Autowired
	private IDSBuildService idsBuildService;
	
	private static final Logger logger = Logger.getLogger(IDSBuildServiceTest.class);
	
	@Before
	public void setUp() {
		setTestUser();
	}
	
	/************************************************************/
	/**** IDSBuildService - IDS Build TestCases Start *****/
	/************************************************************/
	
	/** Tests the Application Notification Count */
	@Test
	public void testIDSBuildservice_01() {
		logger.info("Test Application Notification Count without skip Notifcation List");	
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setLimit(2L);
		paginationInfo.setOffset(0);
		
		List<Long> skipNotificatioList = Collections.EMPTY_LIST;
		long applicationId = 1L;
		
		SearchResult<NotificationBaseDTO> items = idsBuildService.getNotificationsOfApp(applicationId,paginationInfo,skipNotificatioList);
		
		Assert.assertNotNull(items);
		Assert.assertEquals(1, items.getItems().size());
	}
	
	/** Tests the Application Notification Count with Skip Notification List*/
	@Test
	public void testIDSBuildservice_02() {
		logger.info("Test Application Notification Count with Skip Notification List");	
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setLimit(2L);
		paginationInfo.setOffset(0);
		
		List<Long> skipNotificatioList = new ArrayList<Long>();
		skipNotificatioList.add(100005l);
		long applicationId = 1L;
		
		SearchResult<NotificationBaseDTO> items = idsBuildService.getNotificationsOfApp(applicationId,paginationInfo,skipNotificatioList);
		
		Assert.assertNotNull(items);
		Assert.assertEquals(0, items.getItems().size());
	}
	
	/** Tests the Family Notification Count */
	@Test
	public void testIDSBuildservice_03() {
		logger.info("Test Family Notification Count without skip Notifcation List with limit = 2 records");	
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setLimit(2L);
		paginationInfo.setOffset(0);
		
		List<Long> skipNotificatioList = Collections.EMPTY_LIST;
		long applicationId = 1L;
		
		SearchResult<NotificationBaseDTO> items = idsBuildService.getNotificationsOfFamily(applicationId,paginationInfo,skipNotificatioList);
		
		Assert.assertNotNull(items);
		Assert.assertEquals(2, items.getItems().size());
	}
	
	@Test
	public void testIDSBuildservice_04() {
		logger.info("Test Family Notification Count without skip Notifcation List with limit = 10 records");	
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		
		List<Long> skipNotificatioList = Collections.EMPTY_LIST;
		long applicationId = 1L;
		
		SearchResult<NotificationBaseDTO> items = idsBuildService.getNotificationsOfFamily(applicationId,paginationInfo,skipNotificatioList);
		
		Assert.assertNotNull(items);
		Assert.assertEquals(5, items.getItems().size());
	}
	
	@Test
	public void testIDSBuildservice_05() {
		logger.info("Test Family Notification Count with skip Notifcation List with limit = 10 records");	
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setLimit(10L);
		paginationInfo.setOffset(0);
		
		List<Long> skipNotificatioList = new ArrayList<>();
		skipNotificatioList.add(100007l);
		long applicationId = 1L;
		
		SearchResult<NotificationBaseDTO> items = idsBuildService.getNotificationsOfFamily(applicationId,paginationInfo,skipNotificatioList);
		
		Assert.assertNotNull(items);
		Assert.assertEquals(4, items.getItems().size());
	}
	
	@Test
	public void testIDSBuildservice_06() {
		logger.info("Test pending Notifications");	
		
		List<Long> skipAppNotificatioList = Collections.EMPTY_LIST;
		List<Long> skipFamilyNotificatioList = new ArrayList<>();
		skipFamilyNotificatioList.add(100007l);
		long applicationId = 1L;
		
		 Map<String, Long> items = idsBuildService.countNotifications(applicationId, skipAppNotificatioList, skipFamilyNotificatioList);
		
		Assert.assertNotNull(items);
		Assert.assertEquals(1, items.get("application").longValue());
		Assert.assertEquals(4, items.get("family").longValue());
	}
	
}

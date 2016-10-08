package com.blackbox.ids.services.notification;

import java.text.MessageFormat;
import java.util.Calendar;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.dto.notification.NotificationBusinessRuleDTO;
import com.blackbox.ids.dto.notification.NotificationDTO;
import com.blackbox.ids.it.AbstractIntegrationTest;

public class NotificationServiceTest extends AbstractIntegrationTest {

	@Autowired
	private NotificationService notificationService;

	private static final Logger log = Logger.getLogger(NotificationServiceTest.class);

	private static int count = 0;

	@BeforeClass
	public static void beforeClass() {
		log.info("Junit Test cases start for Notification Service");
	}

	@Before
	public void setUp() {
		count++;
		log.info(MessageFormat.format("Test case {0} start executing", count));
	}

	@Test
	public void testGetAllNotification() {
		Assert.assertNotNull("Notification Service object should not be null", notificationService);
		Assert.assertNotNull("List of notification should not be null", notificationService.getAllNotification());
		Assert.assertTrue("Notification List should contain atleast one notificatioin",
				notificationService.getAllNotification().size() > 0);
	}

	@Test
	public void testGetNotificationById() {
		Assert.assertNotNull("Notification Service object should not be null", notificationService);

		for (NotificationDTO dto : notificationService.getAllNotification()) {
			Assert.assertNotNull("Notification object should not be null",
					notificationService.getNotificationById(dto.getNotificationId()));
		}
	}

	@Test(expected = ApplicationException.class)
	public void testGetNotificationByIdWithExe() {
		log.info("ApplicationException should be thrown while fetching notificaion for id -1.");
		notificationService.getNotificationById(-1l);
	}

	@Test
	public void testGetNotificationMessage() {
		Assert.assertNotNull("Notification Service object should not be null", notificationService);
		Assert.assertNotNull("NPL Duplicate Check Notification message should not be null",
				notificationService.getNotificationMessage(NotificationProcessType.NPL_DUPLICATE_CHECK));
	}

	@Test
	public void testGetNotificationBusinessRule() {
		Assert.assertNotNull("Notification Service object should not be null", notificationService);

		for (NotificationDTO dto : notificationService.getAllNotification()) {
			if (CollectionUtils.isNotEmpty(dto.getBusinessRuleDtos())) {
				Assert.assertNotNull("Notification Business Rule object should not be null",
						notificationService.getNotificationBusinessRule(
								dto.getBusinessRuleDtos().iterator().next().getNotificationBusinessRuleId()));
			}
		}
	}

	@Test
	public void testSaveOrUpdateNotification() {
		Assert.assertNotNull("Notification Service object should not be null", notificationService);

		NotificationDTO dto = notificationService.getNotificationById(getNotificationId());
		dto.setNotificationName("Test Notification");
		dto.setUpdatedByUsename("system");
		long id = notificationService.saveOrUpdateNotification(dto);

		Assert.assertEquals("Checking notification name", "Test Notification",
				notificationService.getNotificationById(id).getNotificationName());
	}

	@Test
	public void testDeleteNotificationBusinessRule() {
		Assert.assertNotNull("Notification Service object should not be null", notificationService);

		NotificationDTO dto = notificationService.getNotificationById(getNotificationId());
		long businessRuleId = dto.getBusinessRuleDtos().iterator().next().getNotificationBusinessRuleId();
		long id = notificationService.deleteNotificationBusinessRule(dto.getNotificationId(), businessRuleId,
				Calendar.getInstance(), "system");

		Assert.assertTrue("Busniess rule of new notification should be null",
				CollectionUtils.isEmpty(notificationService.getNotificationById(id).getBusinessRuleDtos()));
	}

	@Test
	public void testSaveOrUpdateNotificationBusinessRule() {
		Assert.assertNotNull("Notification Service object should not be null", notificationService);

		NotificationDTO dto = notificationService.getNotificationById(getNotificationId());
		NotificationBusinessRuleDTO businessRuleDto = dto.getBusinessRuleDtos().iterator().next();
		businessRuleDto.setNotificationBusinessRuleId(null);
		businessRuleDto.setUpdatedByUsename("system");
		businessRuleDto.setNotificationId(getNotificationId());
		long id = notificationService.saveOrUpdateNotificationBusinessRule(businessRuleDto);

		Assert.assertTrue("Busniess Rule of notification should be greater than 1",
				notificationService.getNotificationById(id).getBusinessRuleDtos().size() > 1);

		businessRuleDto.setNotificationId(id);
		businessRuleDto.setSystemInitiated(true);
		businessRuleDto.getBusinessLevelDto().iterator().next().getLevelRoleDtos().get(0).setRoleId(0);
		long id1 = notificationService.saveOrUpdateNotificationBusinessRule(businessRuleDto);

		Assert.assertTrue("Busniess Rule of notification should be greater than 2",
				notificationService.getNotificationById(id1).getBusinessRuleDtos().size() > 2);
	}
	
	private long getNotificationId() {
		for (NotificationDTO dto : notificationService.getAllNotification()) {
			if (CollectionUtils.isNotEmpty(dto.getBusinessRuleDtos())) {
				return dto.getNotificationId();
			}
		}
		
		return 2l;
	}

	@After
	public void afterTest() {
		log.info(MessageFormat.format("Test case {0} stop executing", count));
	}

	@AfterClass
	public static void afterClass() {
		log.info("Junit Test cases end for Notification Service");
	}
}

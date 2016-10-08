package com.blackbox.ids.services.notification.process;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.it.AbstractIntegrationTest;

public class NotificationProcessServiceTest extends AbstractIntegrationTest {

	private static final Logger log = Logger.getLogger(NotificationProcessServiceTest.class);

	private static int count = 0;

	@Autowired
	private NotificationProcessService notificationProcessService;

	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;

	@BeforeClass
	public static void beforeClass() {
		log.info("Junit Test cases start for Notification Process Service");
	}

	@Before
	public void setUp() {
		count++;
		log.info(MessageFormat.format("Test case {0} start executing", count));
		setTestUser();
	}

	@Test
	public void testCreateSystemIniNotification() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);
		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		Assert.assertTrue("Notification process id should be greater than 0",
				Long.compare(notificationProcessId, 0) > 0);
	}

	@Test
	public void testCreateUserIniNotification() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);
		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l,
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		Assert.assertTrue("Notification process id should be greater than 0",
				Long.compare(notificationProcessId, 0) > 0);
	}

	@Test
	public void testCreateFyiNotification() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		Assert.assertTrue("Notification process id should be greater than 0",
				Long.compare(notificationProcessId, 0) > 0);
	}

	@Test
	public void testCreateFyaNotification() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);
		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		Assert.assertTrue("Notification process id should be greater than 0",
				Long.compare(notificationProcessId, 0) > 0);
	}

	@Test
	public void testAssignNotificatioinToUser() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		Assert.assertTrue("Notification process id should be greater than 0",
				Long.compare(notificationProcessId, 0) > 0);
	}

	@Test
	public void testGetTasksByUser() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		notificationProcessService.assignNotificationToUser(1l, notificationProcessId);

		Assert.assertTrue("No. of notification tasks assign to user id 1 should be equal to 1",
				notificationProcessService.getTasksByUser("1").size() == 1);
	}

	@Test
	public void testGetTaskById() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		notificationProcessService.assignNotificationToUser(1l, notificationProcessId);
		List<Task> tasks = notificationProcessService.getTasksByUser("1");

		Assert.assertNotNull("Task should not be null", notificationProcessService.getTaskById(tasks.get(0).getId()));
	}

	@Test
	public void testCompleteTask() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		notificationProcessService.assignNotificationToUser(1l, notificationProcessId);
		notificationProcessService.completeTask(notificationProcessId, NotificationStatus.COMPLETED);
		Assert.assertTrue("No. of notification tasks assign to user id 1 should be equal to 0",
				notificationProcessService.getTasksByUser("1").size() == 0);
	}

	@Test
	public void testDeleteNotification() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		notificationProcessService.assignNotificationToUser(1l, notificationProcessId);
		notificationProcessService.deleteNotification(notificationProcessId, "For Testing purpose only");
		Assert.assertTrue("No. of notification tasks assign to user id 1 should be equal to 0",
				notificationProcessService.getTasksByUser("1").size() == 0);
	}

	@Test
	public void testUnassignNotificatioin() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		notificationProcessService.assignNotificationToUser(1l, notificationProcessId);
		notificationProcessService.unassignNotificatioin(notificationProcessId);
		Assert.assertTrue("No. of notification tasks assign to user id 1 should be equal to 0",
				notificationProcessService.getTasksByUser("1").size() == 0);
	}

	@Test
	public void testGetNotificationProcessIdByActivitiId() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		String activityId = notificationProcessService.getActivitiIdByNotificationProcessId(notificationProcessId);

		Assert.assertEquals("No. of notification tasks assign to user id 1 should be equal to 0", notificationProcessId,
				notificationProcessService.getNotificationProcessIdByActivitiId(activityId));
	}

	@Test
	public void testCompleteNotificationLevel() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l,
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		notificationProcessService.assignNotificationToUser(1l, notificationProcessId);
		notificationProcessService.completeNotificationLevel(-1l, getRoleIds(), 1, true, NotificationStatus.COMPLETED);
		Assert.assertTrue("No. of notification tasks assign to user id 1 should be equal to 1",
				notificationProcessService.getTasksByUser("1").size() == 1);
	}	

	@Test
	public void testDeleteNotificationForAppId() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, null, null, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		notificationProcessService.assignNotificationToUser(1l, notificationProcessId);
		notificationProcessService.deleteNotificationForAppId(100l, "For Testing purpose only");
		Assert.assertTrue("No. of notification tasks assign to user id 1 should be equal to 0",
				notificationProcessService.getTasksByUser("1").size() == 0);
	}

	@Test
	public void testDeleteNotificationForCorrespondenceId() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, 100l, 100l, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		notificationProcessService.assignNotificationToUser(1l, notificationProcessId);
		notificationProcessService.deleteNotificationForCorrespondenceId(100l, "For Testing purpose only");
		Assert.assertTrue("No. of notification tasks assign to user id 1 should be equal to 0",
				notificationProcessService.getTasksByUser("1").size() == 0);
	}

	@Test
	public void testDeleteNotificationForReferenceId() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, 100l, 100l, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		notificationProcessService.assignNotificationToUser(1l, notificationProcessId);
		notificationProcessService.deleteNotificationForReferenceId(100l, "For Testing purpose only");
		Assert.assertTrue("No. of notification tasks assign to user id 1 should be equal to 0",
				notificationProcessService.getTasksByUser("1").size() == 0);
	}

	@Test(expected = SecurityException.class)
	public void testGetEntityIdByNotificationId() {
		Assert.assertNotNull("Notification Process Service should not be null", notificationProcessService);

		long notificationProcessId = notificationProcessService.createNotification(null, 1l, 1l, 1l, 
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, null);
		Assert.assertEquals("Entity Id must be null", Long.valueOf(1l),
				notificationProcessService.getEntityIdByNotificationId(notificationProcessId));
	}

	@Test
	public void testCreateNotification() {
		List<Long> userIds = new ArrayList<>();
		userIds.add(1l);
		userIds.add(2l);
		userIds.add(3l);
		Long id = notificationProcessService.createNotification(applicationBaseDAO.find(1l), null, null, 1l,
				EntityName.REFERENCE_STAGING_DATA, NotificationProcessType.INPADOC_FAILED, userIds);
		System.out.println(id);
		List<Long> ids = new ArrayList<>();
		ids.add(id);
		notificationProcessService.delegateNotifications(ids, 1l);
	}

	private Set<Long> getRoleIds() {
		Set<Long> roleIds = new HashSet<>();
		roleIds.add(1l);
		roleIds.add(2l);

		return roleIds;
	}

	@After
	public void afterTest() {
		log.info(MessageFormat.format("Test case {0} stop executing", count));
	}

	@AfterClass
	public static void afterClass() {
		log.info("Junit Test cases end for Notification Process Service");
	}
}

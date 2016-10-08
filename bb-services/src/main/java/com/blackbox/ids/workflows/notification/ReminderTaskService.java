package com.blackbox.ids.workflows.notification;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blackbox.ids.services.notification.process.NotificationProcessService;

@Component("reminderTaskService")
public class ReminderTaskService implements JavaDelegate {
	
	@Autowired
	private NotificationProcessService notificationProcessService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		long notificationProcessId = notificationProcessService
				.getNotificationProcessIdByActivitiId(execution.getProcessInstanceId());
		notificationProcessService.unassignNotificatioin(notificationProcessId);
	}
}

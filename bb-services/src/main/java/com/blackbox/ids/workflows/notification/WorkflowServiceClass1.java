package com.blackbox.ids.workflows.notification;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;

import com.blackbox.ids.services.notification.process.NotificationProcessService;

public class WorkflowServiceClass1 implements JavaDelegate {
	
	private NotificationProcessService service;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		service = (NotificationProcessService) Context.getProcessEngineConfiguration().getBeans().get("notificationProcessService");
		//service.assignNotificatioinToUser(1l, 2l);
		//service.completeNotification(2l);
		//int a = service.hashCode();
	}
}

package com.blackbox.ids.workflows.notification;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.blackbox.ids.services.notification.process.NotificationProcessConstant;

public class TaskListenerComplete implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegateTask) {
		Integer level = (Integer) delegateTask.getVariable(NotificationProcessConstant.CURRENT_LEVEL);
		delegateTask.setVariable(NotificationProcessConstant.CURRENT_LEVEL, level + 1);
	}
}

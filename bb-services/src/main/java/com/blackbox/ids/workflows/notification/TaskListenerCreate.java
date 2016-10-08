package com.blackbox.ids.workflows.notification;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskListenerCreate implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegateTask) {
		//delegateTask.setVariable("listener1", "Task Created" + delegateTask.getId());

		//if ("create".equalsIgnoreCase(delegateTask.getEventName())) {
		//}
	}
}

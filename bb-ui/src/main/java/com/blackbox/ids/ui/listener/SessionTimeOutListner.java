/**
 *
 */
package com.blackbox.ids.ui.listener;

import java.util.List;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.core.common.BbxApplicationContextUtil;
import com.blackbox.ids.services.common.EntityUserService;
import com.blackbox.ids.services.notification.process.NotificationProcessService;

/**
 * @author ajay2258
 *
 */
@Component
public class SessionTimeOutListner implements HttpSessionListener {

	// @Value("${session.timeout.duration}")
	private int timeoutInMinutes = 30;

	@Override
	public void sessionCreated(final HttpSessionEvent arg0) {
		arg0.getSession().setMaxInactiveInterval(timeoutInMinutes * 60);
	}

	@Override
	public void sessionDestroyed(final HttpSessionEvent arg0) {

		Object contextFromSession = arg0.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		if (contextFromSession == null) {
			return;
		}
		if (!(contextFromSession instanceof SecurityContext)) {
			return;
		}

		// remove all the locks session gets expired
		Long userId = ((BlackboxUser) ((SecurityContext) contextFromSession).getAuthentication().getPrincipal())
				.getId();
		EntityUserService entityUserService = (EntityUserService) BbxApplicationContextUtil
				.getBean(EntityUserService.BEAN_NAME);
		entityUserService.releaseLocksHeldByUser(userId);

		unAssignTask(userId);
	}

	private void unAssignTask(Long userId) {
		NotificationProcessService notificationProcessService = (NotificationProcessService) BbxApplicationContextUtil
				.getBean(NotificationProcessService.BEAN_NAME);

		List<Task> tasks = notificationProcessService.getTasksByUser(String.valueOf(userId));

		if (CollectionUtils.isNotEmpty(tasks)) {
			for (Task task : tasks) {
				long notificationProcessId = notificationProcessService
						.getNotificationProcessIdByActivitiId(task.getProcessInstanceId());
				notificationProcessService.unassignNotificatioin(notificationProcessId);
			}
		}
	}
}
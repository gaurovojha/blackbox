package com.blackbox.ids.services.notification.process;

import java.util.List;

import com.blackbox.ids.core.model.notification.Notification;

public interface NotificationEmailService {
	void sendEmailOnNotification(Notification notification, List<Long> userIds, List<String> filePaths,
			String applicationNumber);
}

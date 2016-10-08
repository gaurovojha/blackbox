package com.blackbox.ids.scheduler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.services.scheduler.NplReferenceNotificationService;

@Service
public class NplReferenceNotificationScheduler {

	private Logger log = Logger.getLogger(NplReferenceNotificationScheduler.class);

	@Autowired
	private NplReferenceNotificationService nplReferenceNotificationService;

	public final void createNotifications() {
		log.info("Start creating Notifications for Pending NPL References.");
		nplReferenceNotificationService.createNotificationForNPLReference();
		log.info("Finished creating Notifications for Pending NPL References.");
	}

}

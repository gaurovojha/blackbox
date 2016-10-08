package com.blackbox.ids.scheduler.mdm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.services.mdm.ApplicationService;

@Service
public class UpdateAssigneeJob {
	
	private Logger log = Logger.getLogger(UpdateAssigneeJob.class);
	
	@Autowired
	private ApplicationService applicationService;
	
	public final void executeUpdateAssigneeJob()
	{
		log.info("Sending notification for update Assignee");
		try{
			applicationService.sendUpdateAssigneeNotification();
		}
		catch(Exception e)
		{
			log.info("Exception occured which sending the notification:"+e);
		}
		log.info("Notifications successfully sent");
	}

}

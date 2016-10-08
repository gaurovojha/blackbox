package com.blackbox.ids.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.blackbox.ids.services.scheduler.BlackboxOtherSchedulerService;

@Service
public class BlackboxOtherScheduler {
	
	//private Logger log = Logger.getLogger(BlackboxOtherScheduler.class);
	
	@Autowired
	private BlackboxOtherSchedulerService blackboxOtherSchedulerService;
	
	@Scheduled(cron = "${cron.expression.uspto.family.linkage}")
	public final void executeUsptoFamilyLinkage() {
		//log.debug("Start Scheduler");
		blackboxOtherSchedulerService.testScheduler();
		//log.debug("End Scheduler");
	}
}

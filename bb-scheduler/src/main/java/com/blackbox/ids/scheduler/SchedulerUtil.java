package com.blackbox.ids.scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SchedulerUtil {

	private Logger logger = Logger.getLogger(SchedulerUtil.class);

	@Autowired
	@Qualifier("quartzScheduler")
	private Scheduler scheduler;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");

	public SchedulerUtil() {

	}

	public void scheduleJob(Job baseJob, Calendar scheduledTime) {

		logger.info("Scheduling: " + baseJob.getClass().getName() + " at " + sdf.format(scheduledTime.getTime()));

		String jobIdentity = baseJob.getClass().getName();
		JobDetail newJob = JobBuilder.newJob(baseJob.getClass()).withDescription("").withIdentity(jobIdentity).build();

		SimpleTriggerImpl trigger = new SimpleTriggerImpl();
		trigger.setName(baseJob.getClass().getName() + ": Trigger");
		trigger.setStartTime(scheduledTime.getTime());
		trigger.setDescription("");

		try {
			scheduler.scheduleJob(newJob, trigger);
		} catch (SchedulerException e) {
			logger.error("Not Able To Schedule :" + e);
		}

	}
	
	/**
	 * It reschedules the existing Trigger
	 * @param intervalinMin
	 * @param name
	 * @param group
	 * @throws SchedulerException
	 */
	public void rescheduleExistingTrigger(int intervalinMin, String name, String group) throws SchedulerException {

		logger.info("Rescheduling Job after :" + intervalinMin + "Triggger Name:" + name + "Trigger Group" + group);

		TriggerKey key = new TriggerKey(name, group);
		Trigger oldTrigger = scheduler.getTrigger(key);

		TriggerBuilder tb = oldTrigger.getTriggerBuilder();

		ScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(intervalinMin)
				.repeatForever();
		Trigger newTrigger = tb.withSchedule(scheduleBuilder).startNow().build();
		scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
		logger.info("Job Rescheduled ");

	}
}

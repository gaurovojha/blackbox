package com.blackbox.ids.scheduler.email;

import com.blackbox.ids.scheduler.email.config.SchedulerConfig;

public class EmailScheduler {

	private final SchedulerConfig schedulerConfig;

	public EmailScheduler(final SchedulerConfig schedulerConfig)
	{
		this.schedulerConfig = schedulerConfig;
	}

	public SchedulerConfig getSchedulerConfig() {
		return schedulerConfig;
	}
}

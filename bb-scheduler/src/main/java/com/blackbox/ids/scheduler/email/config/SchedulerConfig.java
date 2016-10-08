package com.blackbox.ids.scheduler.email.config;

import java.util.Collection;

public class SchedulerConfig {

	private Collection<SchedulerEntity> schedulerEntities;

	public Collection<SchedulerEntity> getSchedulerEntities() {
		return schedulerEntities;
	}

	public void setSchedulerEntities(final Collection<SchedulerEntity> schedulerEntities) {
		this.schedulerEntities = schedulerEntities;
	}

}

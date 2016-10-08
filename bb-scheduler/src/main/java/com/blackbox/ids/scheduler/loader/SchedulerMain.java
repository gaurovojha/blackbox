package com.blackbox.ids.scheduler.loader;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SchedulerMain {

	private static final Logger LOGGER = Logger.getLogger(SchedulerMain.class);

	public static void main(final String args[]) throws Exception {

		LOGGER.info("Loading blackbox - scheduler.xml");
		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:META-INF/spring/blackbox-scheduler.xml");
	}
}

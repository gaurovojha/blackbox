package com.blackbox.ids.scheduler.email.config;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.scheduler.email.handler.MessageHandler;

public class SchedulerEntity implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerEntity.class);

	private long								scheduleInterval;
	private int									poolSize;
	private Priority							priority;
	private boolean								runnable;
	private Map<TemplateType, MessageHandler>	messageConfig;
	private ExecutorService						threadPoolExecutor;

	@PostConstruct
	private void init() {
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(this, 0, scheduleInterval, TimeUnit.SECONDS);
		threadPoolExecutor = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public void run() {
		try {
			for (final Map.Entry<TemplateType, MessageHandler> entry : messageConfig.entrySet()) {
				final MessageHandler messageHandler = entry.getValue();
				final Collection<Message> messages = messageHandler.queryMessages();
				for (final Message message : messages) {
					threadPoolExecutor.submit(new MessageRunnable(messageHandler, message));
				}
			}
		} catch (final Exception e) { // Required to catch Exception so that scheduler works smoothly
			LOGGER.error(e.getMessage(), e);
		}
	}

	private static class MessageRunnable implements Runnable {
		private final MessageHandler	handler;
		private final Message			message;

		public MessageRunnable(final MessageHandler handler, final Message message)
		{
			this.handler = handler;
			this.message = message;
		}

		@Override
		public void run() {
			try {
				if (message != null && handler.lock(message) == 1) {
					handler.handle(message);
					handler.markAsComplete(message);
				}
			} catch (final Exception e) { // Required to catch Exception so that scheduler works smoothly
				LOGGER.error("Exception occured in email handling: ", e.getMessage());
				handler.markAsError(message);
			}

		}

	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(final Priority priority) {
		this.priority = priority;
	}

	public boolean isRunnable() {
		return runnable;
	}

	public void setRunnable(final boolean runnable) {
		this.runnable = runnable;
	}

	public long getScheduleInterval() {
		return scheduleInterval;
	}

	public void setScheduleInterval(final long scheduleInterval) {
		this.scheduleInterval = scheduleInterval;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(final int poolSize) {
		this.poolSize = poolSize;
	}

	public Map<TemplateType, MessageHandler> getMessageConfig() {
		return messageConfig;
	}

	public void setMessageConfig(final Map<TemplateType, MessageHandler> messageConfig) {
		this.messageConfig = messageConfig;
	}

	public enum Priority {
		MIN(1), NORMAL(5), MAX(10);

		int level;

		private Priority(final int level)
		{
			this.level = level;
		}

		public int getLevel() {
			return level;
		}
	}
}

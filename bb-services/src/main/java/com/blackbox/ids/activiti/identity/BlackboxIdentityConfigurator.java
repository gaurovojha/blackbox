package com.blackbox.ids.activiti.identity;

import org.activiti.engine.cfg.ProcessEngineConfigurator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.interceptor.SessionFactory;

public class BlackboxIdentityConfigurator implements ProcessEngineConfigurator {

	private BlackboxUserManagerFactory userManagerFactory;
	private BlackboxGroupManagerFactory groupManagerFactory;

	@Override
	public void configure(final ProcessEngineConfigurationImpl processEngineConfiguration) {
		final SessionFactory umFactory = getUserManagerFactory();
		processEngineConfiguration.getSessionFactories().put(umFactory.getSessionType(), umFactory);

		final SessionFactory gmFactory = getGroupManagerFactory();
		processEngineConfiguration.getSessionFactories().put(gmFactory.getSessionType(), gmFactory);

	}

	public BlackboxUserManagerFactory getUserManagerFactory() {
		return userManagerFactory;
	}

	public BlackboxGroupManagerFactory getGroupManagerFactory() {
		return groupManagerFactory;
	}

	public void setUserManagerFactory(final BlackboxUserManagerFactory userManagerFactory) {
		this.userManagerFactory = userManagerFactory;
	}

	public void setGroupManagerFactory(final BlackboxGroupManagerFactory groupManagerFactory) {
		this.groupManagerFactory = groupManagerFactory;
	}

	@Override
	public void beforeInit(ProcessEngineConfigurationImpl processEngineConfiguration) {

	}

	@Override
	public int getPriority() {
		return 0;
	}
}

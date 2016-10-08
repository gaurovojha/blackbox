package com.blackbox.ids.activiti.identity;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;

public class BlackboxGroupManagerFactory implements SessionFactory {

	private final BlackboxGroupManager groupManager;

	public BlackboxGroupManagerFactory(final BlackboxGroupManager groupManager) {
		this.groupManager = groupManager;
	}

	@Override
	public Class<?> getSessionType() {
		return GroupIdentityManager.class;
	}

	@Override
	public Session openSession() {
		return groupManager;
	}

}

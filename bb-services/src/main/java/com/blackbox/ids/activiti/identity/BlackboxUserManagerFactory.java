package com.blackbox.ids.activiti.identity;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;

public class BlackboxUserManagerFactory implements SessionFactory {

	private final BlackboxUserManager userManager;

	public BlackboxUserManagerFactory(final BlackboxUserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public Class<?> getSessionType() {
		return UserIdentityManager.class;
	}

	@Override
	public Session openSession() {
		return userManager;
	}

}

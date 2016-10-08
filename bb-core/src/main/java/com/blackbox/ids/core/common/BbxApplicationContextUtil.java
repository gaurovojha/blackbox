/**
 *
 */
package com.blackbox.ids.core.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * The <code>BbxApplicationContextUtil</code> holds the {@link ApplicationContext} to help fetch beans by their name at
 * places where autowiring isn't feasible.
 *
 * @author ajay2258
 */
@Component
public class BbxApplicationContextUtil {

	private static ApplicationContext applicationContext;

	@Autowired
	public void setApplicationContext(final ApplicationContext context) {
		applicationContext = context;
	}

	public static Object getBean(final String name) {
		return applicationContext.getBean(name);
	}
	
	public static <T> T getBean(final Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

}

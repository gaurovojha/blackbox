package com.blackbox.ids.core.cache;

import org.springframework.context.SmartLifecycle;

public interface EntityCacheLoader extends SmartLifecycle {
	
	void load();

}

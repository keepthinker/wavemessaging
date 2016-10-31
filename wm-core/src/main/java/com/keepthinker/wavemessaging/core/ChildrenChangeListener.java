package com.keepthinker.wavemessaging.core;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

public interface ChildrenChangeListener {
	public void added(PathChildrenCacheEvent event);

	public void removed(PathChildrenCacheEvent event);
	
}

package org.auto.io.scanner;

import java.util.LinkedList;
import java.util.List;

import org.auto.io.Resource;

/**
 * NodeFilterCluster能够将一组处理器组成一个过滤器
 *
 * @author XiaohangHu
 * */
public class ResourceHandlerCluster implements ResourceHandler {

	private List<ResourceHandler> handlers = new LinkedList<ResourceHandler>();

	public void addHandler(ResourceHandler handler) {
		this.handlers.add(handler);
	}

	public List<ResourceHandler> getHandlers() {
		return handlers;
	}

	@Override
	public void handle(Resource resource) {
		for (ResourceHandler handler : getHandlers()) {
			handler.handle(resource);
		}
	}

}

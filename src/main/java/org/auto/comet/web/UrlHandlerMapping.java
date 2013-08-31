package org.auto.comet.web;

import java.util.HashMap;
import java.util.Map;

import org.auto.comet.SocketHandler;

/**
 * @author XiaohangHu
 * */
public class UrlHandlerMapping {

	private final Map<String, SocketHandler> urlMap = new HashMap<String, SocketHandler>();

	public SocketHandler getHandler(String uri) {
		return urlMap.get(uri);
	}

	public void putHandler(String uri, SocketHandler controller) {
		this.urlMap.put(uri, controller);
	}

}

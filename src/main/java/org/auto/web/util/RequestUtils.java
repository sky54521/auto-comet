package org.auto.web.util;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author XiaohangHu
 * */
public class RequestUtils {

	public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
	public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";

	private RequestUtils() {
		throw new AssertionError();
	}

	public static String getRequestURI(HttpServletRequest request) {
		String uri = (String) request
				.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE);
		if (uri == null) {
			uri = request.getRequestURI();
		}
		return uri;
	}

	public static String getServletPath(HttpServletRequest request) {
		String servletPath = (String) request
				.getAttribute(INCLUDE_SERVLET_PATH_ATTRIBUTE);
		if (servletPath == null) {
			servletPath = request.getServletPath();
		}
		return servletPath;
	}

}

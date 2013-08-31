package org.auto.web.resource;

import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.auto.io.Resource;

/**
 *
 * @author XiaohangHu
 */
public class ServletContextResource implements Resource {

	/**
	 * 协议名
	 * */
	public static final String RESOURCE_PROTOCOL_NAME = "webroot";

	private String path;

	private ServletContext servletContext;

	public ServletContextResource(ServletContext servletContext, String path) {
		if (null == servletContext) {
			throw new IllegalArgumentException(
					"The servletContext must not be null!");
		}
		if (StringUtils.isBlank(path)) {
			throw new IllegalArgumentException("The path must not be blank!");
		}

		this.servletContext = servletContext;

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		this.path = path;

	}

	@Override
	public InputStream getInputStream() {
		return this.servletContext.getResourceAsStream(this.path);
	}

	@Override
	public String getDescription() {
		return "ServletContext resource [" + this.path + "]";
	}

}

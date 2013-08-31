package org.auto.io.scanner;

import org.apache.commons.lang.StringUtils;
import org.auto.io.ClassPathResource;
import org.auto.io.ResourcePathUtils;

/**
 * 扫描机器，可以将各种扫描器组装成一个功能强大的资源扫描机器,直接提供功能强大的服务。
 *
 * */
public class ResourceScanMachine {

	public static final String RESOURCE_LOCATION_SEPARATOR = ",";

	private ResourcePatternScannerManager resourcePatternScannerManager;

	private String defaultProtocol = ClassPathResource.RESOURCE_PROTOCOL_NAME;

	public String getDefaultProtocol() {
		return defaultProtocol;
	}

	/**
	 * 设置默认协议
	 * */
	public void setDefaultProtocol(String defaultProtocol) {
		this.defaultProtocol = defaultProtocol;
	}

	public ResourcePatternScannerManager getResourcePatternScannerManager() {
		return resourcePatternScannerManager;
	}

	public void setResourcePatternScannerManager(
			ResourcePatternScannerManager resourcePatternScannerManager) {
		this.resourcePatternScannerManager = resourcePatternScannerManager;
	}

	/**
	 * 扫描指定路径匹配模式下的所有资源
	 *
	 * @param resourceLocation资源路径
	 *
	 *            资源路径支持格式：
	 *            "classpath:com/auto/*.xml,file:C:\ProgramFiles\Java\jdk1.6
	 *            .0_14\register.html"
	 *
	 * @param handler
	 *            处理器
	 *
	 * */
	public void scanLocations(String resourceLocations, ResourceHandler handler) {
		if (StringUtils.isBlank(resourceLocations)) {
			throw new IllegalArgumentException(
					"ResourceLocation must not be null");
		}
		if (null == handler) {
			throw new IllegalArgumentException("handler must not be null");
		}
		String[] locations = getResourceLocations(resourceLocations);
		for (String location : locations) {
			scanLocation(location, handler);
		}
	}

	public void scanLocation(String resourceLocation, ResourceHandler handler) {
		if (StringUtils.isBlank(resourceLocation)) {
			throw new IllegalArgumentException(
					"ResourceLocation must not be null");
		}
		if (null == handler) {
			throw new IllegalArgumentException("handler must not be null");
		}
		String protocol = ResourcePathUtils.getProtocol(resourceLocation);
		if (null == protocol) {
			protocol = this.getDefaultProtocol();
		}
		ResourcePatternScanner scanner = this.resourcePatternScannerManager
				.getScanner(protocol);
		if (null == scanner) {
			throw new IllegalArgumentException(
					"Invalid ResourceLocation! Cant't recognition the protocol.");
		}
		scanner.scan(resourceLocation, handler);
	}

	protected String[] getResourceLocations(String resourceLocation) {
		return resourceLocation.split(RESOURCE_LOCATION_SEPARATOR);
	}

}

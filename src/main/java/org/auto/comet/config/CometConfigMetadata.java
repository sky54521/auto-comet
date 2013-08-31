package org.auto.comet.config;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author XiaohangHu
 * */
public class CometConfigMetadata {

	public static final String OBJECT_FACTORY_PROPERTY_NAME = "objectFactory";

	private Integer timeout;

	private Properties properties = new Properties();

	private Set<CometMetadata> cometMetadatas = new HashSet<CometMetadata>();

	public void addProperty(String name, String value) {
		this.properties.put(name, value);
	}

	public String getProperty(String name) {
		return this.properties.getProperty(name);
	}

	public Set<CometMetadata> getCometMetadatas() {
		return cometMetadatas;
	}

	public void addCometMetadata(CometMetadata cometMetadata) {
		if (null != cometMetadata) {
			this.cometMetadatas.add(cometMetadata);
		}
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

}

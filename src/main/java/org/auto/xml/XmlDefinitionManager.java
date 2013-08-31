package org.auto.xml;

import java.util.HashMap;
import java.util.Map;

import org.auto.io.Resource;

/**
 * 用于管理Xml模式定义资源(DTD,XSD)
 *
 * @author XiaohangHu
 * */
public class XmlDefinitionManager {

	/***
	 * key : systemId like [http://www.thunisoft.com/dataSync.xsd]
	 *
	 * value : Resource
	 * */
	private Map<String, Resource> definitionMappings = new HashMap<String, Resource>();

	public Resource getXmlDefinition(String systemId) {
		return definitionMappings.get(systemId);
	}

	public void addDefinitionMapping(String systemId, Resource resource) {
		this.definitionMappings.put(systemId, resource);
	}

	public void addDefinitionMappings(Map<String, Resource> definitionMappings) {
		this.definitionMappings.putAll(definitionMappings);
	}

}

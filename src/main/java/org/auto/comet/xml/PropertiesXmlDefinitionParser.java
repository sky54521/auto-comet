package org.auto.comet.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.auto.io.ClassPathResource;
import org.auto.io.Resource;

/**
 * @author XiaohangHu
 * */
public class PropertiesXmlDefinitionParser {

	public Map<String, Resource> parser(Properties properties) {
		Map<String, Resource> definitionMappings = new HashMap<String, Resource>();
		Set<Entry<Object, Object>> set = properties.entrySet();
		for (Entry<Object, Object> entry : set) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			Resource resource = getResource(value);
			definitionMappings.put(key, resource);
		}
		return definitionMappings;
	};

	protected Resource getResource(String path) {
		// 只支持ClassPath资源
		return new ClassPathResource(path);
	}

}

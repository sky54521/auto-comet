package org.auto.comet.xml.parser;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.auto.comet.config.CometConfigMetadata;
import org.auto.comet.config.CometMetadata;
import org.auto.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author huxh
 * @version 1.0
 */
public class CometConfigParser {

	public static final String COMET_ELEMENT = "comet";
	public static final String PROPERTY_ELEMENT = "property";
	public static final String TIMEOUT_ELEMENT = "timeout";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String VALUE_ATTRIBUTE = "value";

	private CometElementParser cometParser = new CometElementParser();

	/**
	 * 读取document加入到配置中
	 *
	 * @param document
	 * */
	public void addConfig(CometConfigMetadata config, Document document) {
		Element root = document.getDocumentElement();
		if (null == root) {
			return;
		}
		Iterator<Element> iterator = XmlUtil.childElementIterator(root);
		while (iterator.hasNext()) {
			Element element = iterator.next();
			String name = element.getNodeName();
			if (PROPERTY_ELEMENT.equals(name)) {
				parseProperty(config, element);
			} else if (COMET_ELEMENT.equals(name)) {
				CometMetadata cometConfig = this.cometParser.parse(element);
				config.addCometMetadata(cometConfig);
			} else if (TIMEOUT_ELEMENT.equals(name)) {
				parseTimeout(config, element);
			}
		}
	}

	protected void parseTimeout(CometConfigMetadata config, Element element) {
		String value = XmlUtil
				.getElementAttributeTrim(VALUE_ATTRIBUTE, element);
		if (StringUtils.isNotBlank(value)) {
			Integer time = Integer.valueOf(value);
			config.setTimeout(time);
		}
	}

	protected void parseProperty(CometConfigMetadata config, Element element) {
		String name = XmlUtil.getElementAttributeTrim(NAME_ATTRIBUTE, element);
		String value = XmlUtil
				.getElementAttributeTrim(VALUE_ATTRIBUTE, element);
		config.addProperty(name, value);
	}

	/**
	 * 读取document转化为配置
	 *
	 * */
	public CometConfigMetadata parse(Document document) {
		CometConfigMetadata config = new CometConfigMetadata();
		addConfig(config, document);
		return config;
	}

	/**
	 * 读取多个document转化为配置
	 *
	 * */
	public CometConfigMetadata parse(Document[] documents) {
		CometConfigMetadata config = new CometConfigMetadata();
		for (Document document : documents) {
			addConfig(config, document);
		}
		return config;
	}

}

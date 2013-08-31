package org.auto.xml;

import java.io.IOException;
import java.io.InputStream;

import org.auto.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author XiaohangHu
 */
public class XmlDefinitionResolver implements EntityResolver {

	private XmlDefinitionManager xmlDefinitionManager;

	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		if (systemId == null) {
			return null;
		}
		if (xmlDefinitionManager == null) {
			return null;
		}
		Resource resource = xmlDefinitionManager.getXmlDefinition(systemId);
		if (null == resource) {
			return null;
		}
		InputStream inputStream = resource.getInputStream();
		InputSource inputSource = new InputSource(inputStream);
		inputSource.setPublicId(publicId);
		inputSource.setSystemId(systemId);
		return inputSource;

	}

	public void setXmlDefinitionManager(
			XmlDefinitionManager xmlDefinitionManager) {
		this.xmlDefinitionManager = xmlDefinitionManager;
	}

}

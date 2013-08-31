package org.auto.comet.xml;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.auto.io.Resource;
import org.auto.util.PropertiesLoaderUtils;
import org.auto.xml.DocumentLoader;
import org.auto.xml.XmlDefinitionManager;
import org.auto.xml.XmlDefinitionResolver;
import org.auto.xml.XmlDocumentLoader;
import org.w3c.dom.Document;

/**
 * @author XiaohangHu
 * */
public class DocumentResourceReader {

	private String xmlDefinitionMappingsLocation = "META-INF/auto.comet.scehmas";

	private DocumentLoader documentLoader;

	{
		initDocumentLoader();
	}

	private void initDocumentLoader() {
		XmlDocumentLoader documentLoader = new XmlDocumentLoader();
		documentLoader.setValidationMode(XmlDocumentLoader.VALIDATION_NONE);
		documentLoader.setNamespaceAware(true);

		XmlDefinitionResolver delegatingResolver = new XmlDefinitionResolver();
		XmlDefinitionManager xmlDefinitionManager = getXmlDefinitionManager();
		delegatingResolver.setXmlDefinitionManager(xmlDefinitionManager);

		documentLoader.setEntityResolver(delegatingResolver);
		this.documentLoader = documentLoader;
	}

	private XmlDefinitionManager getXmlDefinitionManager() {
		/**
		 * get XML definition from properties file.
		 * */
		XmlDefinitionManager xmlDefinitionManager = new XmlDefinitionManager();
		Properties properties = PropertiesLoaderUtils.loadClassPathProperties(
				xmlDefinitionMappingsLocation, null);
		PropertiesXmlDefinitionParser propertiesXmlDefinitionParser = new PropertiesXmlDefinitionParser();
		xmlDefinitionManager
				.addDefinitionMappings(propertiesXmlDefinitionParser
						.parser(properties));
		return xmlDefinitionManager;
	}

	public Document read(Resource resource) {
		InputStream in;
		in = resource.getInputStream();
		Document document = documentLoader.loadDocument(in);
		return document;
	}

	public Set<Document> read(Set<Resource> resources) {
		Set<Document> documents = new HashSet<Document>();
		for (Resource resource : resources) {
			Document d = read(resource);
			if (null != d) {
				documents.add(d);
			}
		}
		return documents;
	}

}

package org.auto.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author XiaohangHu
 */
public class XmlDocumentLoader implements DocumentLoader {

	/**
	 * Indicates that the validation should be disabled.
	 */
	public static final int VALIDATION_NONE = 0;

	/**
	 * Indicates that DTD validation should be used (we found a "DOCTYPE"
	 * declaration).
	 */
	public static final int VALIDATION_DTD = 1;

	/**
	 * Indicates that XSD validation should be used (found no "DOCTYPE"
	 * declaration).
	 */
	public static final int VALIDATION_XSD = 2;

	/**
	 * JAXP attribute used to configure the schema language for validation.
	 */
	private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	/** 验证方式，默认不验证 */
	protected int validationMode = VALIDATION_NONE;
	/** 是否解析命名空间 */
	protected boolean namespaceAware = false;
	/** 实体解析器,用于处理本地模式文件获取 */
	protected EntityResolver entityResolver;
	/** 错误处理 */
	protected ErrorHandler errorHandler;

	protected DocumentBuilder documentBuilder;

	/**
	 * 从资源读取document
	 * */
	public Document loadDocument(InputStream inputStream)
			throws DocumentLoadException {
		DocumentBuilder builder = this.getDocumentBuilder();
		try {
			return builder.parse(inputStream);
		} catch (SAXParseException e) {
			throw new DocumentLoadException("Line " + e.getLineNumber()
					+ " in XML document is invalid", e);
		} catch (SAXException e) {
			throw new DocumentLoadException("XML document is invalid", e);
		} catch (IOException e) {
			throw new DocumentLoadException(
					"IOException parsing XML document ", e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}

	}

	protected DocumentBuilder getDocumentBuilder() {
		if (null == this.documentBuilder) {
			this.documentBuilder = this.createDocumentBuilder();
		}
		return this.documentBuilder;
	}

	protected DocumentBuilderFactory createDocumentBuilderFactory() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(namespaceAware);

		if (validationMode != VALIDATION_NONE) {
			factory.setValidating(true);

			if (validationMode == VALIDATION_XSD) {
				// Enforce namespace aware for XSD...
				factory.setNamespaceAware(true);
				try {
					factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE,
							XMLConstants.W3C_XML_SCHEMA_NS_URI);
				} catch (IllegalArgumentException e) {
					DocumentLoadException ex = new DocumentLoadException(
							"Unable to validate using XSD: Your JAXP provider ["
									+ factory
									+ "] does not support XML Schema. Are you running on Java 1.4 with Apache Crimson? "
									+ "Upgrade to Apache Xerces (or Java 1.5) for full XSD support.",
							e);
					throw ex;
				}
			}
		}

		return factory;
	}

	protected DocumentBuilder createDocumentBuilder() {
		DocumentBuilderFactory factory = this.createDocumentBuilderFactory();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new DocumentLoadException(
					"Parser configuration exception parsing XML", e);
		}
		EntityResolver entityResolver = this.getEntityResolver();
		if (entityResolver != null) {
			documentBuilder.setEntityResolver(entityResolver);
		}
		ErrorHandler errorHandler = this.getErrorHandler();
		if (errorHandler != null) {
			documentBuilder.setErrorHandler(errorHandler);
		}
		return documentBuilder;
	}

	public int getValidationMode() {
		return validationMode;
	}

	public void setValidationMode(int validationMode) {
		this.validationMode = validationMode;
	}

	public boolean isNamespaceAware() {
		return namespaceAware;
	}

	public void setNamespaceAware(boolean namespaceAware) {
		this.namespaceAware = namespaceAware;
	}

	public EntityResolver getEntityResolver() {
		return entityResolver;
	}

	public void setEntityResolver(EntityResolver entityResolver) {
		this.entityResolver = entityResolver;
	}

	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

}

package org.auto.xml;

import org.apache.commons.logging.Log;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Simple <code>org.xml.sax.ErrorHandler</code> implementation: logs warnings
 * using the given Commons Logging logger instance, and rethrows errors to
 * discontinue the XML transformation.
 *
 * @author Juergen Hoeller
 */
public class SimpleErrorHandler implements ErrorHandler {

	private final Log logger;

	/**
	 * Create a new SimpleSaxErrorHandler for the given Commons Logging logger
	 * instance.
	 */
	public SimpleErrorHandler(Log logger) {
		this.logger = logger;
	}

	public void warning(SAXParseException ex) throws SAXException {
		logger.warn("Ignored XML validation warning", ex);
	}

	public void error(SAXParseException ex) throws SAXException {
		throw ex;
	}

	public void fatalError(SAXParseException ex) throws SAXException {
		throw ex;
	}

}

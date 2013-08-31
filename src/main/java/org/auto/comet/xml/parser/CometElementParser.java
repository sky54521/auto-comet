package org.auto.comet.xml.parser;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auto.comet.config.CometMetadata;
import org.auto.xml.XmlUtil;
import org.w3c.dom.Element;

/**
 *
 * @author huxh
 * @version 1.0
 */
public class CometElementParser {

	private static final Log logger = LogFactory
			.getLog(CometElementParser.class);

	public static final String REQUEST_ATTRIBUTE = "request";

	public static final String HANDLER_ATTRIBUTE = "handler";

	/**
	 * */
	public CometMetadata parse(Element element) {

		CometMetadata cometConfig = new CometMetadata();

		setProperty(cometConfig, element);

		return cometConfig;
	}

	private void setProperty(CometMetadata cometConfig, Element element) {
		String handler = XmlUtil.getElementAttributeTrim(HANDLER_ATTRIBUTE,
				element);
		String request = XmlUtil.getElementAttributeTrim(REQUEST_ATTRIBUTE,
				element);
		if (StringUtils.isNotBlank(request)) {
			cometConfig.setRequest(request);
		}
		if (StringUtils.isNotBlank(handler)) {
			cometConfig.setHandler((handler));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Read comet config [request:\"" + request
					+ "\", handler:\"" + handler + "\"]");
		}
	}
}

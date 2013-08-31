package org.auto.comet.xml;

import org.auto.comet.config.CometConfigMetadata;
import org.auto.comet.xml.parser.CometConfigParser;
import org.auto.io.Resource;
import org.auto.io.scanner.ResourceHandler;
import org.w3c.dom.Document;

/**
 * @author XiaohangHu
 * */
public class XmlConfigResourceHandler implements ResourceHandler {

	private DocumentResourceReader documentResourceReader = new DocumentResourceReader();
	private CometConfigParser autoCometParser = new CometConfigParser();
	private CometConfigMetadata config = new CometConfigMetadata();

	public XmlConfigResourceHandler(CometConfigMetadata config) {
		this.config = config;
	}

	@Override
	public void handle(Resource resource) {
		Document document = documentResourceReader.read(resource);
		autoCometParser.addConfig(config, document);
	}

}

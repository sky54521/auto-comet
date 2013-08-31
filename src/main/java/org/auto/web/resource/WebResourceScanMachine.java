package org.auto.web.resource;

import javax.servlet.ServletContext;

import org.auto.io.ClassPathResource;
import org.auto.io.FileResource;
import org.auto.io.scanner.ClassPathResourceScanner;
import org.auto.io.scanner.FileSystemResourceScanner;
import org.auto.io.scanner.ResourcePatternScannerManager;
import org.auto.io.scanner.ResourceScanMachine;

/**
 *
 * @author huxh
 * */
public class WebResourceScanMachine extends ResourceScanMachine {

	private ServletContext servletContext;

	public WebResourceScanMachine(ServletContext servletContext) {
		if (null == servletContext) {
			throw new IllegalArgumentException(
					"The servletContext must not be null!");
		}
		this.servletContext = servletContext;
		init();
		super.setDefaultProtocol(ServletContextResource.RESOURCE_PROTOCOL_NAME);
	}

	private void init() {
		ResourcePatternScannerManager scannerManager = new ResourcePatternScannerManager();
		scannerManager.addScanner(ClassPathResource.RESOURCE_PROTOCOL_NAME,
				new ClassPathResourceScanner());
		scannerManager.addScanner(FileResource.RESOURCE_PROTOCOL_NAME,
				new FileSystemResourceScanner());
		scannerManager.addScanner(
				ServletContextResource.RESOURCE_PROTOCOL_NAME,
				new ServletContextResourceScanner(servletContext));
		setResourcePatternScannerManager(scannerManager);
	}
}

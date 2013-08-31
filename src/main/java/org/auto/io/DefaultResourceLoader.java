package org.auto.io;

import java.net.MalformedURLException;
import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader {

	protected Resource getClassPathResource(String location) {
		String classPathLocation = location
				.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length());
		return new ClassPathResource(classPathLocation);
	}

	protected Resource getUrlResource(String location)
			throws MalformedURLException {
		URL url = new URL(location);
		return new UrlResource(url);
	}

	public Resource getResource(String location) {
		if (location.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
			return getClassPathResource(location);
		} else {
			try {
				return getUrlResource(location);
			} catch (MalformedURLException ex) {
				throw new ReadResourceException("MalformedURLException ["
						+ location + "]", ex);
			}
		}
	}

	@Override
	public Resource[] getResources(String locationPattern) {
		// TODO Auto-generated method stub
		return null;
	}

}

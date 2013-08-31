package org.auto.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.auto.io.Resource;
import org.auto.io.UrlResource;

/**
 * @author XiaohangHu
 * */
public class PropertiesLoaderUtils {

	/**
	 * Load properties from the given resource.
	 *
	 * @param resource
	 *            the resource to load from
	 * @return the populated Properties instance
	 * @throws IOException
	 *             if loading failed
	 */
	public static Properties loadProperties(Resource resource)
			throws IOException {
		Properties props = new Properties();
		fillProperties(props, resource);
		return props;
	}

	/**
	 * Load properties from the given resource.
	 *
	 * @param resource
	 *            the resource to load from
	 * @return the populated Properties instance
	 * @throws IOException
	 *             if loading failed
	 */
	public static Properties loadProperties(Resource[] resources)
			throws IOException {
		Properties props = new Properties();
		for (Resource resource : resources) {
			fillProperties(props, resource);
		}
		return props;
	}

	/**
	 * Fill the given properties from the given resource.
	 *
	 * @param props
	 *            the Properties instance to fill
	 * @param resource
	 *            the resource to load from
	 * @throws IOException
	 *             if loading failed
	 */
	public static void fillProperties(Properties props, Resource resource)
			throws IOException {
		InputStream is = resource.getInputStream();
		try {
			props.load(is);
		} finally {
			is.close();
		}
	}

	public static Properties loadClassPathProperties(String path,
			ClassLoader classLoader) {
		if (null == classLoader) {
			classLoader = ClassUtils.getDefaultClassLoader();
		}
		if (StringUtils.isBlank(path)) {
			throw new IllegalArgumentException("Path  must not be null!");
		}

		Properties properties = new Properties();
		Enumeration<URL> urls;
		try {
			urls = classLoader.getResources(path);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				Resource resource = new UrlResource(url);
				PropertiesLoaderUtils.fillProperties(properties, resource);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"IOException load class path properties[" + path + "]!", e);
		}
		return properties;
	}

}

package org.auto.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.JarFile;

public abstract class ResourceUtils {

	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	public static final String FILE_URL_PREFIX = "file:";

	public static final String URL_PROTOCOL_FILE = "file";

	public static final String URL_PROTOCOL_JAR = "jar";

	public static final String URL_PROTOCOL_ZIP = "zip";

	public static final String URL_PROTOCOL_VFSZIP = "vfszip";

	public static final String URL_PROTOCOL_WSJAR = "wsjar";

	public static final String URL_PROTOCOL_CODE_SOURCE = "code-source";

	public static final String JAR_URL_SEPARATOR = "!/";

	/**
	 * Return whether the given resource location is a URL: either a special
	 * "classpath" pseudo URL or a standard URL.
	 *
	 * @param resourceLocation
	 *            the location String to check
	 * @return whether the location qualifies as a URL
	 * @see #CLASSPATH_URL_PREFIX
	 * @see java.net.URL
	 */
	public static boolean isUrl(String resourceLocation) {
		if (resourceLocation == null) {
			return false;
		}
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			return true;
		}
		try {
			new URL(resourceLocation);
			return true;
		} catch (MalformedURLException ex) {
			return false;
		}
	}

	public static boolean isJarURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_JAR.equals(protocol)
				|| URL_PROTOCOL_ZIP.equals(protocol)
				|| URL_PROTOCOL_VFSZIP.equals(protocol)
				|| URL_PROTOCOL_WSJAR.equals(protocol) || (URL_PROTOCOL_CODE_SOURCE
				.equals(protocol) && url.getPath().indexOf(JAR_URL_SEPARATOR) != -1));
	}

	public static JarFile getJarFile(String jarFileUrl) throws IOException {
		if (jarFileUrl.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
			try {
				String uriPath = jarFileUrl.replace(" ", "%20");
				URI uri = new URI(uriPath);
				return new JarFile(uri.getSchemeSpecificPart());
			} catch (URISyntaxException ex) {
				return new JarFile(
						jarFileUrl.substring(ResourceUtils.FILE_URL_PREFIX
								.length()));
			}
		} else {
			return new JarFile(jarFileUrl);
		}
	}

}

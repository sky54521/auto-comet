package org.auto.io.scanner;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.auto.io.ClassPathResource;
import org.auto.io.FileResource;
import org.auto.io.JarEntryResource;
import org.auto.io.Resource;
import org.auto.io.ResourcePathUtils;
import org.auto.io.ResourceUtils;
import org.auto.util.ClassUtils;

/**
 * 资源扫描器
 * 
 * @author XiaohangHu
 * */
public class ClassPathResourceScanner extends AbstractPatternResourceScanner {

	/**
	 * 扫描ClassPath
	 * */
	public void scan(String rootDirPath, String subPattern,
			final ResourceHandler handler) {
		Enumeration<URL> urlEnumeration = gerClassPathResources(rootDirPath);
		DefaultFilePatternScanner fileScanner = new DefaultFilePatternScanner();
		FileHandler fileHandler = new FileHandler() {
			@Override
			public void handle(File file) {
				Resource resource = new FileResource(file);
				handler.handle(resource);
			}
		};
		while (urlEnumeration.hasMoreElements()) {
			URL url = (URL) urlEnumeration.nextElement();
			if (ResourceUtils.isJarURL(url)) {
				try {
					scanJarUrl(url, subPattern, handler);
				} catch (IOException e) {
					throw new ScannerException("IOException [" + url.getPath()
							+ "]!", e);
				}
			} else {
				try {
					File file = new File(url.toURI());
					fileScanner.scan(file, subPattern, fileHandler);
				} catch (URISyntaxException e) {
					throw new ScannerException("URISyntaxException [" + url
							+ "]", e);
				}
			}
		}
	}

	protected void scanJarUrl(URL url, String subPattern,
			ResourceHandler handler) throws IOException {
		URLConnection con = url.openConnection();
		JarFile jarFile = null;
		String rootEntryPath = "";
		if (con instanceof JarURLConnection) {
			JarURLConnection jarCon = (JarURLConnection) con;
			jarCon.setUseCaches(false);
			jarFile = jarCon.getJarFile();
			JarEntry jarEntry = jarCon.getJarEntry();
			if (jarEntry != null)
				rootEntryPath = jarEntry.getName();
			scanJarFile(jarFile, rootEntryPath, subPattern, handler);
		} else {
			try {
				String urlFilePath = url.getPath();
				int separatorIndex = urlFilePath
						.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
				if (separatorIndex != -1) {
					String jarFileUrl = urlFilePath
							.substring(0, separatorIndex);
					rootEntryPath = urlFilePath.substring(separatorIndex
							+ ResourceUtils.JAR_URL_SEPARATOR.length());
					jarFile = ResourceUtils.getJarFile(jarFileUrl);
				} else {
					jarFile = new JarFile(urlFilePath);
				}
				scanJarFile(jarFile, rootEntryPath, subPattern, handler);
			} finally {
				if (null != jarFile)
					jarFile.close();
			}
		}

	}

	private void scanJarFile(JarFile jarFile, String rootEntryPath,
			String subPattern, ResourceHandler handler) {

		if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
			// Root entry path must end with slash to allow for proper matching.
			// The Sun JRE does not return a slash here, but BEA JRockit does.
			rootEntryPath = rootEntryPath + "/";
		}

		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryPath = entry.getName();
			if (entryPath.startsWith(rootEntryPath)) {
				String relativePath = entryPath.substring(rootEntryPath
						.length());
				if (getPathMatcher().match(subPattern, relativePath)) {
					Resource resource = new JarEntryResource(jarFile, entry);
					handler.handle(resource);
				}
			}
		}
	}

	private Enumeration<URL> gerClassPathResources(String location) {
		Enumeration<URL> resourceUrls = null;
		try {
			resourceUrls = getClassLoader().getResources(location);
		} catch (IOException e1) {
			throw new ScannerException("IOException get resources [" + location
					+ "] from classLoader!", e1);
		}
		return resourceUrls;
	}

	private ClassLoader getClassLoader() {
		return ClassUtils.getDefaultClassLoader();
	}

	@Override
	public void scan(String locationPattern, ResourceHandler handler) {
		locationPattern = ResourcePathUtils.getReallPath(locationPattern);
		if (super.getPathMatcher().isPattern(locationPattern)) {
			String rootDirPath = determineRootDir(locationPattern);
			String subPattern = locationPattern.substring(rootDirPath.length());
			this.scan(rootDirPath, subPattern, handler);
		} else {
			Resource resource = new ClassPathResource(locationPattern);
			handler.handle(resource);
		}
	}

}

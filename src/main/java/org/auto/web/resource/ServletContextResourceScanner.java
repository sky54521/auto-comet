package org.auto.web.resource;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;

import org.auto.io.Resource;
import org.auto.io.ResourcePathUtils;
import org.auto.io.scanner.AbstractPatternResourceScanner;
import org.auto.io.scanner.ResourceHandler;

/**
 *
 * @author huxh
 * */
public class ServletContextResourceScanner extends
		AbstractPatternResourceScanner {

	private ServletContext servletContext;

	public ServletContextResourceScanner(ServletContext servletContext) {
		if (null == servletContext) {
			throw new IllegalArgumentException(
					"servletContext must not be null!");
		}
		this.servletContext = servletContext;
	}

	protected void doRetrieveMatchingServletContextResources(
			String rootDirPath, String locationPattern, ResourceHandler handler) {

		Set<String> candidates = servletContext.getResourcePaths(rootDirPath);
		if (candidates != null) {
			boolean dirDepthNotFixed = (locationPattern.indexOf("**") != -1);
			for (Iterator<String> it = candidates.iterator(); it.hasNext();) {
				String currPath = (String) it.next();
				if (!currPath.startsWith(rootDirPath)) {
					// Returned resource path does not start with relative
					// directory:
					// assuming absolute path returned -> strip absolute path.
					int dirIndex = currPath.indexOf(rootDirPath);
					if (dirIndex != -1) {
						currPath = currPath.substring(dirIndex);
					}
				}
				if (currPath.endsWith("/")
						&& (dirDepthNotFixed || countOccurrencesOf(currPath,
								"/") <= countOccurrencesOf(locationPattern, "/"))) {
					// Search subdirectories recursively:
					// ServletContext.getResourcePaths
					// only returns entries for one directory level.
					doRetrieveMatchingServletContextResources(currPath,
							locationPattern, handler);
				}
				if (getPathMatcher().match(locationPattern, currPath)) {
					Resource resource = new ServletContextResource(
							servletContext, currPath);
					handler.handle(resource);
				}
			}
		}
	}

	/**
	 * Count the occurrences of the substring in string s.
	 *
	 * @param str
	 *            string to search in. Return 0 if this is null.
	 * @param sub
	 *            string to search for. Return 0 if this is null.
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0
				|| sub.length() == 0) {
			return 0;
		}
		int count = 0, pos = 0, idx = 0;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	@Override
	public void scan(String locationPattern, ResourceHandler handler) {
		if (getPathMatcher().isPattern(locationPattern)) {
			locationPattern = ResourcePathUtils.getReallPath(locationPattern);
			String rootDirPath = determineRootDir(locationPattern);
			doRetrieveMatchingServletContextResources(rootDirPath,
					locationPattern, handler);
		} else {
			Resource resource = new ServletContextResource(servletContext,
					locationPattern);
			handler.handle(resource);
		}

	}

}

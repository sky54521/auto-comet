package org.auto.io.scanner;

import org.auto.io.ResourcePathUtils;
import org.auto.util.AntPathMatcher;
import org.auto.util.PathMatcher;

/**
 *
 * @author XiaohangHu
 * */
public abstract class AbstractPatternResourceScanner implements
		ResourcePatternScanner {

	private PathMatcher pathMatcher = new AntPathMatcher();

	protected String determineRootDir(String locationPattern) {
		return ResourcePathUtils.getRootDir(locationPattern, pathMatcher);
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

}

package org.auto.util;

/**
 * 路径匹配器
 *
 * @see org.springframework.util.PathMatcher
 * @author huxh
 * */
public interface PathMatcher {

	boolean isPattern(String path);

	boolean match(String pattern, String path);

	boolean matchStart(String pattern, String path);

}

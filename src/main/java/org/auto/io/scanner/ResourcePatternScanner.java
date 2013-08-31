package org.auto.io.scanner;

/**
 *
 * @author XiaohangHu
 * */
public interface ResourcePatternScanner {

	/**
	 * 扫描指定路径匹配模式下的所有资源
	 *
	 * @param locationPattern路径匹配模式
	 *
	 * @param handler
	 *            处理器
	 *
	 * */
	void scan(String locationPattern, ResourceHandler handler);

}

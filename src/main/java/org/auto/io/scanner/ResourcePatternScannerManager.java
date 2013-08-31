package org.auto.io.scanner;

import java.util.HashMap;
import java.util.Map;

/**
 * ResourcePatternScannerManager管理扫描器
 *
 * @author XiaohangHu
 * */
public class ResourcePatternScannerManager {

	private Map<String, ResourcePatternScanner> scanners = new HashMap<String, ResourcePatternScanner>();

	/**
	 * @param protocol协议名
	 * @param scanner扫描器
	 * */
	public void addScanner(String protocol, ResourcePatternScanner scanner) {
		this.scanners.put(protocol, scanner);
	}

	/**
	 * 根据协议名获取扫描器
	 *
	 * @param protocol协议名
	 * */
	public ResourcePatternScanner getScanner(String protocol) {
		return this.scanners.get(protocol);
	}

}

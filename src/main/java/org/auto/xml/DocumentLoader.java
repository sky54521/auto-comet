package org.auto.xml;

import java.io.InputStream;

import org.w3c.dom.Document;

/**
 * XML读取接口
 *
 * @author XiaohangHu
 */
public interface DocumentLoader {

	/**
	 * 读取Document对象
	 * */
	Document loadDocument(InputStream inputStream) throws DocumentLoadException;

}

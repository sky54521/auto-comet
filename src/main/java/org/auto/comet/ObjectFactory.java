package org.auto.comet;

import javax.servlet.ServletContext;

/**
 *
 * ObjectFactory
 *
 * 用于获取SocketHandler等对象。
 *
 * @author XiaohangHu
 * */
public interface ObjectFactory {

	void init(ServletContext servletContext);

	Object getObject(String objectName);

}

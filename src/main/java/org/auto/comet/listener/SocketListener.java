package org.auto.comet.listener;

import java.util.EventListener;

/**
 * @author XiaohangHu
 * */
public interface SocketListener extends EventListener {

	/**
	 * 当真正关闭连接时触发
	 * */
	void onReallyClose(SocketEvent event);

	/**
	 * 当客户端发起一次request(即建立异步连接)时触发
	 * */
	void onRequestConnection(SocketEvent event);
}

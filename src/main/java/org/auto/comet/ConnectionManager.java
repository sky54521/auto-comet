package org.auto.comet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ConnectionManager {

	/**
	 * 检查超时
	 * */
	void checkPushTimeout();

	/**
	 * 创建新链接
	 * */
	void creatConnection(PushSocket socket);

	/**
	 * 断开链接
	 * */
	void disconnect(String connectionId, SocketHandler handler,
			HttpServletRequest request);

	/**
	 * 接收消息
	 * */
	void receiveMessage(String connectionId, HttpServletRequest request,
			HttpServletResponse response);
}

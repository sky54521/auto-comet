package org.auto.comet;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * PushSocket
 * 
 * 
 * @author XiaohangHu
 * */
public interface PushSocket extends Socket {

	Serializable getId();

	void setId(Serializable id);

	/**
	 * 接待取消息请求
	 * 
	 * @throws IOException
	 * */
	void receiveRequest(HttpServletRequest request, HttpServletResponse response);

	void setTimeout(long timeout);

	void setSocketManager(SocketManager socketManager);

	/**
	 * 处理推送超时，超时推送代表客户端长时间没有发送连接请求
	 * 
	 * 超时会发生一个连接异常。
	 * 
	 * @param pushTimeout
	 *            超时时间
	 * @return 是否超时
	 */
	boolean checkPushTimeOut(long pushTimeout);
}

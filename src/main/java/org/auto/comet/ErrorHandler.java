package org.auto.comet;

/**
 * SocketErrorHandler
 *
 * 通信错误处理接口
 *
 * @author XiaohangHu
 * */
public interface ErrorHandler {

	/**
	 * @param socket
	 *            can getUserMessages from the socket
	 * @param e
	 *            : PushException
	 * */
	public void error(Socket socket, PushException e);

}

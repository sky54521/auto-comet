package org.auto.comet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author XiaohangHu
 * */
public interface SocketHandler {

	/**
	 * <p>
	 * Handle a connection request.
	 * </p>
	 *
	 * @param socket
	 *            You can use socket send message.
	 * @param request
	 *            You can get parameter from the request.
	 * @return <code>true</code> if you accept the connection
	 * */
	boolean accept(Socket socket, HttpServletRequest request);

	/**
	 * Handle a disconnection request.
	 *
	 * */
	void quit(Socket socket, HttpServletRequest request);

}

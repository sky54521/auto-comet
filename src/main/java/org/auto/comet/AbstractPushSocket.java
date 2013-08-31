package org.auto.comet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.auto.comet.listener.SocketEvent;
import org.auto.comet.listener.SocketListener;
import org.auto.comet.support.JsonProtocolUtils;
import org.auto.comet.web.listener.AsyncAdapter;
import org.auto.json.JsonArray;
import org.auto.json.JsonObject;

/**
 * PushSocket
 * 
 * states:
 * 
 * 是否在等待: isWaiting
 * 
 * 是否有消息: hasMessage
 * 
 * 是否已经关闭: isClosed
 * 
 * @author XiaohangHu
 * */
public class AbstractPushSocket implements Socket, PushSocket {

	private Serializable id;

	private static final JsonObject CLOSE_MESSAGE;

	/** 消息队列 */
	private List<Object> messages;

	/** 是否已经预关闭 */
	private boolean close = false;

	/** 异步超时时间，默认一小时 */
	private long asyncTimeout = 3600000;

	/**
	 * 记录上一次推送的时间。客户端长时间没有轮询，应该发生一个异常。
	 * */
	private Long lastPushTime = getNowTimeInMillis();

	private AsyncContext asyncContext;

	private SocketManager socketManager;

	private List<SocketListener> listeners = new LinkedList<SocketListener>();

	private ErrorHandler errorHandler;

	static {
		CLOSE_MESSAGE = JsonProtocolUtils.getCloseCommend();
	}
	{
		messages = new LinkedList<Object>();
	}

	public AbstractPushSocket() {
		super();
	}

	public AbstractPushSocket(Serializable id) {
		this.id = id;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public long getTimeout() {
		return asyncTimeout;
	}

	public void setTimeout(long timeout) {
		this.asyncTimeout = timeout;
	}

	private long getNowTimeInMillis() {
		return System.currentTimeMillis();
	}

	/** 添加监听器 */
	public void addListener(SocketListener listener) {
		listeners.add(listener);
	}

	public SocketManager getSocketManager() {
		return socketManager;
	}

	public void setSocketManager(SocketManager socketManager) {
		this.socketManager = socketManager;
	}

	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	/** 重置最后推送时间 */
	private void resetLastPushTime() {
		this.lastPushTime = getNowTimeInMillis();
	}

	/** 获取最后一次推送的时间 */
	protected Long getLastPushTime() {
		return lastPushTime;
	}

	/** 异步等待消息 */
	private void waitMessage(HttpServletRequest request) {
		try {
			AsyncContext ac = request.startAsync();
			ac.setTimeout(this.asyncTimeout);
			ac.addListener(new AsyncAdapter() {
				@Override
				public void onError(AsyncEvent asyncevent) throws IOException {
					close();
					PushException e = new PushException("Async context error!");
					fireError(e);
				}

				@Override
				public void onTimeout(AsyncEvent asyncevent) throws IOException {
					close();
					PushException e = new AsyncTimeoutException(
							"Async context timeout! wait message more then ["
									+ asyncTimeout + "]ms");
					fireError(e);
				}
			});
			this.asyncContext = ac;
		} catch (Exception e) {
			PushException pe = new PushException(
					"StartAsync exception! May be the servlet or filter is not async.",
					e);
			this.fireError(pe);
		} catch (Throwable te) {
			throw new PushException(
					"StartAsync exception! May be the servlet or filter is not async.",
					te);
		}
	}

	private boolean isCloseMessage(Object msg) {
		// 用==提高比较效率
		return CLOSE_MESSAGE == msg;
	}

	private void complete() {
		this.asyncContext.complete();
		this.asyncContext = null;
	}

	/**
	 * 真正关闭连接
	 * */
	private void reallyClose() {
		socketManager.removeSocket(id);
		fireReallyClose();
	}

	/**
	 * 触发真正关闭连接事件
	 * */
	protected void fireReallyClose() {
		SocketEvent event = new SocketEvent(this);
		for (SocketListener listener : listeners) {
			listener.onReallyClose(event);
		}
	}

	/**
	 * 触发异常处理
	 * */
	protected void fireError(PushException e) {
		ErrorHandler handler = this.getErrorHandler();
		if (null != handler) {
			handler.error(this, e);
		} else {
			throw e;
		}
	}

	/** ~~~~~~~~~~~~~~~~~~~~~~~推送消息~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
	/**
	 * 将消息用指定的writer发送
	 * */
	private void pushMessage(List<Object> messages, PrintWriter writer) {
		JsonArray array = new JsonArray();
		boolean isClose = false;
		for (Object message : messages) {
			if (isCloseMessage(message)) {
				isClose = true;
			}
			array.add(message);
		}
		writer.write(array.toString());
		writer.flush();
		// 如果发送的消息中有关闭消息，则真正关闭连接
		if (isClose) {
			reallyClose();
		}
		// 重置最后推送时间
		resetLastPushTime();
	}

	private void pushMessage(List<Object> messages, ServletResponse response) {
		try {
			pushMessage(messages, response.getWriter());
		} catch (IOException e) {
			PushException pe = new PushException("IOException push message", e);
			this.fireError(pe);
		}
	}

	private void pushMessage(Object message, ServletResponse response)
			throws IOException {
		List<Object> msgs = new LinkedList<Object>();
		msgs.add(message);
		pushMessage(msgs, response);
	}

	public List<String> getCachedData() {
		List<String> userMessages = new LinkedList<String>();
		for (Object message : messages) {
			if (message instanceof String && !this.isCloseMessage(message)) {
				userMessages.add((String) message);
			}
		}
		// 清空缓存
		this.messages.clear();
		return userMessages;
	}

	/** ~~~~~~~~~~~~~~~~~~~~~~~状态获取~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
	/**
	 * 是在等待
	 * */
	private boolean isWaiting() {
		return null != this.asyncContext;
	}

	/**
	 * 是否有消息要发送
	 * */
	private boolean hasMessage() {
		return !this.messages.isEmpty();
	}

	/**
	 * 是否已经关闭
	 * */
	private boolean isClosed() {
		return close;
	}

	/**
	 * 接待取消息请求
	 * 
	 * @throws IOException
	 * */
	public void receiveRequest(HttpServletRequest request,
			HttpServletResponse response) {
		// if (isClosed()) {
		// PushException e = new PushException("Use a closed pushSocked!");
		// this.fireError(e);
		// }
		if (this.hasMessage()) {
			// 如果有消息则直接将消息推送
			pushMessage(this.messages, response);
			// 发送后清空缓冲区
			this.messages.clear();
		} else {
			// 如果没有消息则等待消息
			this.waitMessage(request);
		}
	}

	public void sendObjectMessage(Object message) {
		if (isClosed()) {
			PushException e = new PushException("Use a closed pushSocked!");
			this.fireError(e);
			return;
		}
		// 如果不是等待状态，将消息缓存
		if (!isWaiting()) {
			this.messages.add(message);
			return;
		}
		// 如果是等待状态，发送消息
		ServletResponse response = this.asyncContext.getResponse();
		try {
			pushMessage(message, response);
		} catch (IOException e) {
			PushException pe = new PushException("IOException push message", e);
			this.fireError(pe);
			return;
		}
		complete();
	}

	@Override
	public void send(String message) {
		sendObjectMessage(message);
	}

	/**
	 * 关闭连接
	 * */
	public void close() {
		this.sendObjectMessage(CLOSE_MESSAGE);
		this.close = true;
	}

	/**
	 * 处理推送超时，超时推送代表客户端长时间没有发送连接请求
	 * 
	 * 超时会发生一个连接异常。
	 * 
	 * @param pushTimeout
	 *            超时时间
	 * @return 是否超时
	 */
	public boolean checkPushTimeOut(long pushTimeout) {
		if (this.isWaiting()) {
			return false;
		}
		Long lastTime = this.getLastPushTime();
		long now = this.getNowTimeInMillis();
		long sent = now - lastTime;
		if (sent > pushTimeout) {
			this.close = true;// 关闭连接
			PushException e = new PushTimeoutException(
					"Push timeout! The client has no connection more than["
							+ pushTimeout + "]ms");
			this.socketManager.removeSocket(id);
			fireError(e);
			return true;
		}
		return false;
	}

}

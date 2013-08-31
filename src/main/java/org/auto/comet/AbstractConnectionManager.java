package org.auto.comet;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auto.comet.web.DispatchException;

/**
 * 该类用于处理各种通信请求，并管理socket
 * 
 * @author XiaohangHu
 * */
public abstract class AbstractConnectionManager implements ConnectionManager,
		SocketManager {

	private static final String RANDOM_ALGORITHM = "SHA1PRNG";

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	/** 默认为1分钟 */
	private long pushTimeout = 60000l;

	/** 异步超时时间，默认一小时 */
	private long asyncTimeout = 3600000;

	protected Map<Serializable, PushSocket> socketStore = new ConcurrentHashMap<Serializable, PushSocket>();

	private SecureRandom random;
	{
		try {
			random = SecureRandom.getInstance(RANDOM_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Can't find secure random["
					+ RANDOM_ALGORITHM + "]", e);
		}
	}

	public AbstractConnectionManager() {
	}

	public AbstractConnectionManager(long pushTimeout) {
		this.pushTimeout = pushTimeout;
	}

	public long getPushTimeout() {
		return pushTimeout;
	}

	public void setPushTimeout(long pushTimeout) {
		this.pushTimeout = pushTimeout;
	}

	public long getAsyncTimeout() {
		return asyncTimeout;
	}

	public void setAsyncTimeout(long asyncTimeout) {
		this.asyncTimeout = asyncTimeout;
	}

	private void processPushTimeOut(PushSocket socket) {
		socket.checkPushTimeOut(this.pushTimeout);
	}

	/**
	 * 推送超时处理
	 * */
	public void checkPushTimeout() {
		if (this.logger.isInfoEnabled()) {
			this.logger.info("Check push timeout, Socket count:"
					+ socketStore.size());
		}
		Collection<PushSocket> sockets = this.socketStore.values();
		for (PushSocket socket : sockets) {// 检查所有的socket是否超时
			processPushTimeOut(socket);
		}
	}

	/**
	 * 接收消息
	 * */
	public void receiveMessage(String connectionId, HttpServletRequest request,
			HttpServletResponse response) {
		PushSocket socket = this.getSocket(connectionId);
		if (null == socket) {
			throw new PushException("Cant't find socket by connectionId:"
					+ connectionId);
		}
		socket.receiveRequest(request, response);
	}

	/**
	 * 创建新链接
	 * */
	public void creatConnection(PushSocket socket) {
		String id = this.getConnectionId();
		socket.setId(id);
		socket.setTimeout(this.asyncTimeout);
		socket.setSocketManager(this);
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("creatConnection [" + id + "]");
		}
		this.addSocket(socket);
	}

	/**
	 * 断开链接
	 * */
	public void disconnect(String connectionId, SocketHandler handler,
			HttpServletRequest request) {
		if (null == connectionId) {
			throw new IllegalArgumentException(
					"Disconnect. ConnectionId must not be null!");
		}
		if (null == handler) {
			throw new DispatchException("Cant't find handler");
		}
		PushSocket socket = getSocket(connectionId);
		if (null == socket) {
			if (this.logger.isWarnEnabled()) {
				this.logger
						.warn("Disconnect. Can't find socket by connectionId ["
								+ connectionId + "]");
			}
			return;
		}
		handler.quit(socket, request);
		socket.close();
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("disconnect [" + connectionId + "]");
		}
	}

	private String getRandom() {
		long l = random.nextLong();
		l = l >= 0L ? l : -l;
		return Long.toString(l, 36);
	}

	/**
	 * 返回一个不重复的安全随机id
	 * */
	protected String getConnectionId() {
		String id = getRandom();
		while (hasSocket(id)) {
			id = getRandom();
		}
		return id;
	}

	protected PushSocket getSocket(Serializable id) {
		return socketStore.get(id);
	}

	protected PushSocket removeSocket(PushSocket socket) {
		Serializable id = socket.getId();
		return removeSocket(id);
	}

	protected void addSocket(PushSocket socket) {
		socketStore.put(socket.getId(), socket);
	}

	protected boolean hasSocket(Serializable id) {
		Socket socket = this.getSocket(id);
		return null != socket;
	}

	public PushSocket removeSocket(Serializable id) {
		return socketStore.remove(id);
	}
}

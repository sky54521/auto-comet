package org.auto.comet.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auto.comet.ConcurrentConnectionManager;
import org.auto.comet.ConcurrentPushSocket;
import org.auto.comet.ObjectFactory;
import org.auto.comet.Protocol;
import org.auto.comet.PushSocket;
import org.auto.comet.SocketHandler;
import org.auto.comet.ConnectionManager;
import org.auto.comet.config.CometConfigMetadata;
import org.auto.comet.support.JsonProtocolUtils;
import org.auto.comet.support.ObjectFactoryBuilder;
import org.auto.comet.xml.XmlConfigResourceHandler;
import org.auto.web.resource.WebResourceScanMachine;
import org.auto.web.util.RequestUtils;

/**
 * 连接转发servlet
 * 
 * 该类用于处理所有的连接请求
 * 
 * @author XiaohangHu
 * */
public class SocketDispatcherServlet extends AbstractDispatcherServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = -3671690949937300581L;

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	public static final String INIT_PARAMETER_CONFIG_LOCATION = "dispatcherConfigLocation";

	/** 默认为1分钟 */
	private long pushTimeout = 60000l;

	private UrlHandlerMapping urlHandlerMapping;
	private ConnectionManager connectionManager;

	@Override
	public final void init() throws ServletException {
		getServletContext().log(
				"Initializing " + getClass().getSimpleName() + " '"
						+ getServletName() + "'");
		super.init();
		CometConfigMetadata cometConfig = readCometConfig();
		initHandlerMapping(cometConfig);
		initConnectionManager(cometConfig);
		startTimer(connectionManager);
		if (this.logger.isInfoEnabled()) {
			this.logger.info("SocketDispatcherServlet '" + getServletName()
					+ "': initialization started");
		}
	}

	/**
	 * 开始定时任务
	 * */
	public void startTimer(ConnectionManager socketManager) {
		Timer timer = new Timer(true);
		long period = pushTimeout / 2l;
		timer.schedule(new CheckPushTimeoutTimerTask(socketManager),
				pushTimeout, period);
	}

	protected CometConfigMetadata readCometConfig() {
		ServletConfig config = getServletConfig();
		String dispatcherConfigLocation = config
				.getInitParameter(INIT_PARAMETER_CONFIG_LOCATION);
		if (StringUtils.isBlank(dispatcherConfigLocation)) {
			dispatcherConfigLocation = getDefaultDispatcherConfigLocation();
		}
		WebResourceScanMachine webResourceScanMachine = new WebResourceScanMachine(
				this.getServletContext());
		CometConfigMetadata cometConfig = new CometConfigMetadata();

		// 扫描将配置元数据放入cometConfig中
		webResourceScanMachine.scanLocations(dispatcherConfigLocation,
				new XmlConfigResourceHandler(cometConfig));

		return cometConfig;
	}

	protected void initHandlerMapping(CometConfigMetadata cometConfig) {
		ObjectFactory objectFactory = ObjectFactoryBuilder.creatObjectFactory(
				cometConfig, getServletContext());

		UrlHandlerMappingBuilder mappingBuilder = new UrlHandlerMappingBuilder(
				objectFactory);

		urlHandlerMapping = mappingBuilder.buildHandlerMapping(cometConfig);

	}

	protected void initConnectionManager(CometConfigMetadata cometConfig)
			throws ServletException {
		ConcurrentConnectionManager connectionManager = new ConcurrentConnectionManager(
				pushTimeout);
		Integer timeout = cometConfig.getTimeout();
		if (null != timeout) {
			// 将分钟转化为毫秒
			long asyncTimeout = (long) (60000l * timeout);
			connectionManager.setAsyncTimeout(asyncTimeout);
		}
		this.connectionManager = connectionManager;
	}

	public String getDefaultDispatcherConfigLocation() {
		return "/WEB-INF/dispatcher-servlet.xml";
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String synchronizValue = getSynchronizValue(request);

		if (null == synchronizValue) {// 同步值为空则为接收消息
			receiveMessage(connectionManager, request, response);
		} else if (Protocol.CONNECTION_VALUE.equals(synchronizValue)) {// 创建连接请求
			creatConnection(connectionManager, request, response);
		} else if (Protocol.DISCONNECT_VALUE.equals(synchronizValue)) {// 断开连接请求
			disconnect(connectionManager, request, response);
		}
	}

	/**
	 * 接收消息
	 * */
	private static void receiveMessage(ConnectionManager socketManager,
			HttpServletRequest request, HttpServletResponse response) {
		String connectionId = getConnectionId(request);
		socketManager.receiveMessage(connectionId, request, response);
	}

	/**
	 * 创建连接
	 * */
	private void creatConnection(ConnectionManager connectionManager,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SocketHandler service = getSocketHandler(request);
		if (null == service) {
			noHandlerFound(request, response);
			return;
		}

		PushSocket socket = new ConcurrentPushSocket();
		boolean accept = service.accept(socket, request);
		String commend = null;
		if (accept) {// 如果接受连接请求则创建连接
			connectionManager.creatConnection(socket);
			commend = JsonProtocolUtils.getConnectionCommend(socket.getId());
		} else {// 如果拒绝连接请求
			commend = JsonProtocolUtils.getConnectionCommend(null);
		}
		PrintWriter write = response.getWriter();
		// 返回生成的连接id
		write.write(commend);
		write.flush();//本次创建连接用的http请求，对应的响应完成（tcp链接没有断开）
	}

	/**
	 * 断开连接
	 * */
	private void disconnect(ConnectionManager socketManager,
			HttpServletRequest request, HttpServletResponse response) {
		SocketHandler handler = getSocketHandler(request);
		if (null == handler) {
			noHandlerFound(request, response);
			return;
		}
		String connectionId = getConnectionId(request);
		socketManager.disconnect(connectionId, handler, request);
	}

	protected SocketHandler getSocketHandler(HttpServletRequest request) {
		String uri = RequestUtils.getServletPath(request);
		return urlHandlerMapping.getHandler(uri);
	}

	/**
	 * No handler found -> set appropriate HTTP response status.
	 * 
	 * @param request
	 *            current HTTP request
	 * @param response
	 *            current HTTP response
	 */
	protected void noHandlerFound(HttpServletRequest request,
			HttpServletResponse response) {
		String uri = RequestUtils.getServletPath(request);
		if (logger.isWarnEnabled()) {
			String message = "No mapping handler found for HTTP request with URI ["
					+ uri
					+ "] in DispatcherServlet with name '"
					+ getServletName() + "'";
			logger.warn(message);
		}
		try {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private static String getSynchronizValue(HttpServletRequest request) {
		// StringUtils.trim();
		return request.getParameter(Protocol.SYNCHRONIZE_KEY);
	}

	private static String getConnectionId(HttpServletRequest request) {
		return request.getParameter(Protocol.CONNECTIONID_KEY);
	}

}

class CheckPushTimeoutTimerTask extends TimerTask {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	private ConnectionManager socketManager;

	CheckPushTimeoutTimerTask(ConnectionManager socketManager) {
		this.socketManager = socketManager;
	}

	@Override
	public void run() {
		// 异常处理，防止守护线程死亡！
		try {
			socketManager.checkPushTimeout();
		} catch (Throwable e) {
			logger.error("Push timeout Exception!", e);
		}
	}
}

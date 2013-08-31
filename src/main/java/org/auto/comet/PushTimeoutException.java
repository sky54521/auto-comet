package org.auto.comet;

/**
 *
 * 推送超时异常
 *
 * 代表客户端长时间没有轮询。大约为1分钟，会发生一个异常。
 *
 * @author XiaohangHu
 * */
public class PushTimeoutException extends PushException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4953949710626671131L;

	public PushTimeoutException() {
		super();
	}

	public PushTimeoutException(String message) {
		super(message);
	}

	public PushTimeoutException(Throwable throwable) {
		super(throwable);
	}

	public PushTimeoutException(String message, Throwable throwable) {
		super(message, throwable);
	}

}

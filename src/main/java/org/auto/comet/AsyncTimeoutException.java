package org.auto.comet;

/**
 * 异步超时异常
 *
 * 一个连接长时间没有被服务器端使用，会发生一个异常。建议超时设置与session一致，大约为1小时。
 *
 * @author XiaohangHu
 * */
public class AsyncTimeoutException extends PushException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4953949710626671131L;

	public AsyncTimeoutException() {
		super();
	}

	public AsyncTimeoutException(String message) {
		super(message);
	}

	public AsyncTimeoutException(Throwable throwable) {
		super(throwable);
	}

	public AsyncTimeoutException(String message, Throwable throwable) {
		super(message, throwable);
	}

}

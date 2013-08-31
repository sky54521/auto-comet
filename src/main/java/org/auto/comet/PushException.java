package org.auto.comet;

/**
 * 推送异常
 *
 * comet会将推送时的所有底层异常包装为运行时的PushException
 *
 * @author XiaohangHu
 * */
public class PushException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4953949710626671131L;

	public PushException() {
		super();
	}

	public PushException(String message) {
		super(message);
	}

	public PushException(Throwable throwable) {
		super(throwable);
	}

	public PushException(String message, Throwable throwable) {
		super(message, throwable);
	}

}

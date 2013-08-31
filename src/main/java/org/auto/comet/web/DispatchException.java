package org.auto.comet.web;

public class DispatchException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 4324289825948938207L;

	public DispatchException() {
		super();
	}

	public DispatchException(String message) {
		super(message);
	}

	public DispatchException(Throwable throwable) {
		super(throwable);
	}

	public DispatchException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
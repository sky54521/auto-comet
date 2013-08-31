package org.auto.comet.listener;

import org.auto.comet.PushSocket;

/**
 *
 * @author XiaohangHu
 * */
public class SocketEvent {

	private PushSocket socket;

	public SocketEvent(PushSocket socket) {
		this.socket = socket;
	}

	public PushSocket getPushSocket() {
		return this.socket;
	}
}

package org.auto.comet;

import java.io.Serializable;

public interface SocketManager {

	/**
	 * 根据Socket id删除Socket
	 * */
	PushSocket removeSocket(Serializable id);

}

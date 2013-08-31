package org.auto.comet.support;

import java.io.Serializable;

import org.auto.comet.Protocol;
import org.auto.json.JsonObject;

/**
 * 
 * @author XiaohangHu
 */
public class JsonProtocolUtils {

	public static JsonObject getCloseCommend() {
		JsonObject commend = new JsonObject();
		commend.put(Protocol.SYNCHRONIZE_KEY, Protocol.DISCONNECT_VALUE);
		return commend;
	}

	public static String getConnectionCommend(Serializable socketId) {
		JsonObject commend = new JsonObject();
		commend.put(Protocol.CONNECTIONID_KEY, socketId);
		return commend.toString();
	}

	// suppress default constructor for noninstantiability
	private JsonProtocolUtils() {
		throw new AssertionError();
	}

}

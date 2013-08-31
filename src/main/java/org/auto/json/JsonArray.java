package org.auto.json;

import java.util.ArrayList;

/**
 * @author XiaohangHu
 * */
public class JsonArray extends ArrayList<Object> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		JsonSerializer serializer = new JsonSerializer();
		return serializer.toJsonString(this);
	}

}

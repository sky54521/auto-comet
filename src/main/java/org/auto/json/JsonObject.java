package org.auto.json;

import java.util.HashMap;

/**
 * @author XiaohangHu
 * */
public class JsonObject extends HashMap<Object, Object> {

	/**
	 *
	 */
	private static final long serialVersionUID = -1493269365377697547L;

	@Override
	public Object put(Object name, Object value) {
		if (null == name) {
			throw new IllegalArgumentException("name must not be null!");
		}
		return super.put(name, value);
	}

	/**
	 * 转化为json格式字符串
	 * */
	@Override
	public String toString() {
		JsonSerializer serializer = new JsonSerializer();
		return serializer.toJsonString(this);
	}

}

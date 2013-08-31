package org.auto.json;

import java.util.Collection;

/**
 * JsonUtils can transform the three to each other:
 *
 * 1.jsonString
 *
 * 2.jsonObject | jsonArray
 *
 * 3.javaBean | javaBeans
 *
 * @author XiaohangHu
 * */
public class JsonUtils {

	/**
	 * javaBeans to jsonString
	 *
	 * @param javaBeans
	 *
	 * @return jsonString
	 *
	 * */
	public String toJsonString(Collection<?> javaBeans) {
		// TODO ...
		return null;
	}

	/**
	 * javaBean to jsonString
	 *
	 * @param javaBean
	 *
	 * @return jsonString
	 *
	 * */
	public String toJsonString(Object javaBean) {
		// TODO ...
		return null;
	}

	/**
	 * jsonObject to jsonString
	 *
	 * @param jsonObject
	 *
	 * @return jsonString
	 *
	 * */
	public String toJsonString(JsonObject jsonObject) {
		// TODO ...
		return null;
	}

	/**
	 * jsonArray to jsonString
	 *
	 * @param jsonArray
	 *
	 * @return jsonString
	 *
	 * */
	public String toJsonString(JsonArray jsonArray) {
		// TODO ...
		return null;
	}

	/**
	 * jsonString to javaBean
	 *
	 * @param jsonString
	 * @param beanClass
	 *
	 * @return javaBean
	 *
	 * */
	public <T> T toJavaBean(String jsonString, Class<T> beanClass) {
		// TODO ...
		return null;
	}

	// suppress default constructor for noninstantiability
	private JsonUtils() {
		throw new AssertionError();
	}

}

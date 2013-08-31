package org.auto.json;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * JsonSerializer
 *
 * object to jsonString
 *
 * @author XiaohangHu
 * */
public class JsonSerializer {

	/**
	 * javaBeans to jsonString
	 *
	 * @param collection
	 *
	 * @return jsonString
	 *
	 * */
	@SuppressWarnings("rawtypes")
	public String toJsonString(Collection collection) {
		StringBuilder builder = new StringBuilder();
		appendValue(builder, collection);
		return builder.toString();
	}

	/**
	 * Object to jsonString
	 *
	 * @param object
	 *
	 * @return jsonString
	 *
	 * */
	public String toJsonString(Object object) {
		StringBuilder builder = new StringBuilder();
		appendValue(builder, object);
		return builder.toString();
	}

	/**
	 * map to jsonString
	 *
	 * @param map
	 *            map like a javaBean
	 *
	 * @return jsonString
	 *
	 * */
	@SuppressWarnings("rawtypes")
	public String toJsonString(Map map) {
		StringBuilder builder = new StringBuilder();
		appendValue(builder, map);
		return builder.toString();
	}

	/**
	 * jsonObject to jsonString
	 *
	 * @param jsonObject
	 *
	 * @return jsonString
	 *
	 * */
	@SuppressWarnings("rawtypes")
	public String toJsonString(JsonObject jsonObject) {
		return toJsonString((Map) jsonObject);
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
		return toJsonString((Collection<?>) jsonArray);
	}

	@SuppressWarnings("rawtypes")
	private void appendValue(StringBuilder builder, Object value) {
		if (null == value) {
			builder.append(JsonProtocol.NULL);
			return;
		}
		if (value instanceof String) {
			appendValue(builder, (String) value);
			return;
		}
		if (value instanceof Boolean) {
			builder.append(value);
			return;
		}
		if (value instanceof Number) {
			builder.append(value);
			return;
		}
		if (value instanceof Character) {
			appendValue(builder, (Character) value);
			return;
		}
		if (value instanceof Date) {
			appendValue(builder, (Date) value);
			return;
		}
		if (value instanceof Map) {
			appendValue(builder, (Map) value);
			return;
		}
		if (value instanceof Collection) {
			appendValue(builder, (Collection) value);
			return;
		}
		appendJavaBeanValue(builder, value);

	}

	/**
	 * @param value
	 *            must not be null
	 * */
	private void appendValue(StringBuilder builder, String value) {
		builder.append(JsonProtocol.STRING_BEGIN);
		JsonStringUtils.escape(value, builder);
		builder.append(JsonProtocol.STRING_END);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	private void appendValue(StringBuilder builder, Character value) {
		builder.append(JsonProtocol.CHAR_BEGIN);
		builder.append(value);
		builder.append(JsonProtocol.CHAR_END);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	private void appendValue(StringBuilder builder, Date value) {
		builder.append(value.getTime());
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	@SuppressWarnings("rawtypes")
	private void appendValue(StringBuilder builder, Collection value) {
		builder.append(JsonProtocol.ARRAY_BEGIN);
		Iterator iterator = value.iterator();
		if (iterator.hasNext()) {
			String elementString = toJsonString(iterator.next());
			builder.append(elementString);
		}
		while (iterator.hasNext()) {
			builder.append(JsonProtocol.ARRAY_ELEMENTS_SEPARATOR);
			String elementString = toJsonString(iterator.next());
			builder.append(elementString);
		}

		builder.append(JsonProtocol.ARRAY_END);
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void appendValue(StringBuilder builder, Map value) {
		builder.append(JsonProtocol.OBJECT_BEGIN);
		Set<java.util.Map.Entry> entrySet = value.entrySet();
		Iterator<java.util.Map.Entry> iterator = entrySet.iterator();
		if (iterator.hasNext()) {
			appendEntrySet(builder, iterator.next());
		}
		while (iterator.hasNext()) {
			builder.append(JsonProtocol.OBJECT_MEMBERS_SEPARATOR);
			appendEntrySet(builder, iterator.next());
		}
		builder.append(JsonProtocol.OBJECT_END);
	}

	@SuppressWarnings("rawtypes")
	private void appendEntrySet(StringBuilder builder, java.util.Map.Entry entry) {
		Object key = entry.getKey();
		appendValue(builder, key.toString());
		builder.append(JsonProtocol.PAIR_SEPARATOR);
		appendValue(builder, entry.getValue());
	}

	/**
	 * @param value
	 *            must not be null
	 * */
	@SuppressWarnings("rawtypes")
	private void appendJavaBeanValue(StringBuilder builder, Object value) {
		try {
			Map map = PropertyUtils.describe(value);
			map.remove("class");
			appendValue(builder, map);
		} catch (Exception e) {
			throw new IllegalArgumentException("object ["
					+ value.getClass().getName() + ":" + value
					+ "] can't be described!");
		}
	}

}

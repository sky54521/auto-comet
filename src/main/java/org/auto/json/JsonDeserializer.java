package org.auto.json;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/**
 * JsontDerializer
 * 
 * jsongString to Object
 * 
 * @author XiaohangHu
 * */
public class JsonDeserializer {

	public Object toObject(String jsonString, Class<?> beanType) {
		if (StringUtils.isEmpty(jsonString))
			return null;
		jsonString = jsonString.trim();
		return toJavaBean(jsonString, beanType);
	}

	/**
	 * jsonString to javaBean
	 * 
	 * @param jsonString
	 *            not be null. must begins with "{", and extends with "}"
	 * @param beanClass
	 * 
	 * @return javaBean
	 * 
	 * */
	private Object toJavaBean(String jsonString, Class<?> beanType) {
		jsonString = JsonStringUtils.removeBeginAndEnd(jsonString);
		Object bean = newInstance(beanType);
		putPairs(jsonString, bean);
		return bean;
	}

	/**
	 * @param jsonString
	 *            must begins with "{", and extends with "}"
	 * */
	private JsonObject toJsonObject(String jsonString) {
		jsonString = JsonStringUtils.removeBeginAndEnd(jsonString);
		JsonObject object = new JsonObject();
		putPairs(jsonString, object);
		return object;
	}

	/**
	 * @param jsonString
	 *            must begins with "[", and extends with "]"
	 * */
	private JsonArray toJsonArray(String jsonString) {
		jsonString = JsonStringUtils.removeBeginAndEnd(jsonString);
		JsonArray object = new JsonArray();
		addElements(jsonString, object);
		return object;
	}

	/**
	 * @param jsonString
	 *            must begins with "[", and extends with "]" /**
	 * @param collectionType
	 * @param elementsType
	 * */
	private <T extends Collection<E>, E> Collection<E> toCollection(
			String jsonString, Class<? extends Collection<E>> collectionType) {
		jsonString = JsonStringUtils.removeBeginAndEnd(jsonString);
		Collection<E> collection = newInstance(collectionType);
		addElements(jsonString, collection);
		return collection;
	}

	/**
	 * @param jsonString
	 *            must begins with "[", and extends with "]"
	 * @param collectionType
	 * @param elementsType
	 * */
	private <T extends Collection<E>, E> Collection<E> toCollection(
			String jsonString, Class<? extends Collection<E>> collectionType,
			Class<E> elementsType) {
		jsonString = jsonString.trim();
		jsonString = JsonStringUtils.removeBeginAndEnd(jsonString);
		Collection<E> collection = newInstance(collectionType);
		addElements(jsonString, collection, elementsType);
		return collection;
	}

	private void putPairs(String pairsString, Object object) {
		// TODO something wrong like "{age:8,father:{age:34,high:'170cm'}}"
		String[] pairs = pairsString
				.split(JsonProtocol.OBJECT_MEMBERS_SEPARATOR);
		if (object instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = ((Map<String, Object>) object);
			for (String pairString : pairs) {
				Pair pair = toPair(pairString);
				putPari(pair, map);
			}
		} else {
			for (String pairString : pairs) {
				Pair pair = toPair(pairString);
				putPari(pair, object);
			}
		}
	}

	/**
	 * @param pairString
	 *            must not be bull. eg: "name":"XiaohangHu"
	 * */
	private Pair toPair(String pairString) {
		pairString = pairString.trim();
		int separatorInext = -1;
		String name = null;
		if (pairString.startsWith(JsonProtocol.STRING_BEGIN)) {
			int endOfNameInext = pairString.indexOf(JsonProtocol.STRING_END, 1);
			if (endOfNameInext == -1) {
				throw new IllegalArgumentException("Illegal pair ["
						+ pairString + "]. Pair must be a String!");
			}
			name = pairString.substring(1, endOfNameInext);
			separatorInext = pairString.indexOf(JsonProtocol.PAIR_SEPARATOR,
					endOfNameInext);
		} else {
			separatorInext = pairString.indexOf(JsonProtocol.PAIR_SEPARATOR);
			name = pairString.substring(0, separatorInext);
			name = name.trim();
		}
		if (separatorInext == -1) {
			throw new IllegalArgumentException("Illegal pair [" + pairString
					+ "]. Pair must like [string:value]");
		}
		String valueString = pairString.substring(separatorInext + 1,
				pairString.length());
		Pair pair = new Pair();
		pair.setName(name);
		pair.setValue(valueString);
		return pair;
	}

	/**
	 * @param pair
	 *            pair's name pair's value not be bull.
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void putPari(Pair pair, Object bean) {
		String name = pair.getName();
		String valueString = pair.getValue();
		try {
			Object value = null;
			Class type = PropertyUtils.getPropertyType(bean, name);
			if (Collection.class.isAssignableFrom(type)) {// 如果是集合
				Class elementsType = getFirstParameterFirstGeneric(bean, name);
				type = getCollectionType(type);
				if (null != elementsType) {
					value = toCollection(valueString, type, elementsType);
				} else {
					value = toCollection(valueString, type);
				}
			} else {
				value = this.getValue(valueString, type);
			}
			PropertyUtils.setProperty(bean, name, value);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	private Class<? extends Collection> getCollectionType(
			Class<? extends Collection> type) {
		if (Modifier.isAbstract(type.getModifiers())) {// 如果为接口或者抽象类
			if (Set.class.isAssignableFrom(type)) {
				return HashSet.class;// 默认用HashSet
			} else if (List.class.isAssignableFrom(type)) {
				return ArrayList.class;// 默认用ArrayList
			}
		}
		return type;
	}

	/**
	 * @param pair
	 *            pair's name pair's value not be bull.
	 * */
	private void putPari(Pair pair, Map<String, Object> map) {
		String name = pair.getName();
		String valueString = pair.getValue();

		Object value = getValue(valueString);
		map.put(name, value);
	}

	@SuppressWarnings("unchecked")
	private <T> void addElements(String jsonString, Collection<T> connection) {
		String[] elements = jsonString
				.split(JsonProtocol.ARRAY_ELEMENTS_SEPARATOR);
		for (String element : elements) {
			T value = (T) getArrayValue(element);
			connection.add(value);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> void addElements(String jsonString, Collection<T> connection,
			Class type) {
		String[] elements = jsonString
				.split(JsonProtocol.ARRAY_ELEMENTS_SEPARATOR);
		for (String element : elements) {
			T value = (T) getArrayValue(element, type);
			connection.add(value);
		}
	}

	/**
	 * @param valueString
	 *            special array value: "" == null
	 * 
	 * */
	@SuppressWarnings("rawtypes")
	private Object getArrayValue(String valueString, Class type) {
		valueString = valueString.trim();
		if ("".equals(valueString)) {
			return null;
		}
		return getValue(valueString, type);
	}

	/**
	 * @param valueString
	 *            special array value: "" == null
	 * 
	 * */
	private Object getArrayValue(String valueString) {
		valueString = valueString.trim();
		if ("".equals(valueString)) {
			return null;
		}
		return getValue(valueString);
	}

	/**
	 * get value by class type
	 * 
	 * @param valueString
	 *            value must not be null
	 * */
	private Object getValue(String valueString, Class<?> type) {
		valueString = valueString.trim();

		if (JsonStringUtils.isNullValue(valueString)) {
			return null;
		}
		if (JsonStringUtils.isStringValue(valueString)) {
			valueString = JsonStringUtils.removeBeginAndEnd(valueString);
		}
		if (JsonStringUtils.isCharValue(valueString)) {
			valueString = JsonStringUtils.removeBeginAndEnd(valueString);
		}
		if (JsonStringUtils.isObjectValue(valueString)) {
			return toJavaBean(valueString, type);
		}
		// if (JsonStringUtils.isArrayValue(valueString)) {
		// return toCollection(valueString, type);
		// }
		return ConvertUtils.convert(valueString, type);
	}

	/**
	 * value ::= string | number | object | array | true | false | null
	 * 
	 * @param valueString
	 *            value must not be null
	 * */
	private Object getValue(String valueString) {

		valueString = valueString.trim();

		if (JsonStringUtils.isStringValue(valueString)) {// string
			valueString = JsonStringUtils.removeBeginAndEnd(valueString);
			return valueString;
		}
		if (JsonStringUtils.isObjectValue(valueString)) {// object
			return toJsonObject(valueString);
		}
		if (JsonStringUtils.isArrayValue(valueString)) {// array
			return toJsonArray(valueString);
		}
		if (JsonStringUtils.isCharValue(valueString)) {
			valueString = JsonStringUtils.removeBeginAndEnd(valueString);
			if (valueString.length() == 1) {
				char c = (char) valueString.getBytes()[0];
				return Character.valueOf(c);
			}
			return valueString;
		}
		if (JsonStringUtils.isNullValue(valueString)) {// null
			return null;
		}
		if (JsonStringUtils.isTrueValue(valueString)) {// true
			return Boolean.TRUE;
		}
		if (JsonStringUtils.isFalseValue(valueString)) {// false
			return Boolean.FALSE;
		}
		try {
			return Integer.valueOf(valueString);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Illegal json value ["
					+ valueString + "]", e);
		}
	}

	@SuppressWarnings("rawtypes")
	private static Class getFirstParameterFirstGeneric(Object bean,
			String propertyName) {
		PropertyDescriptor ptor;
		try {
			ptor = PropertyUtils.getPropertyDescriptor(bean, propertyName);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		Method method = ptor.getWriteMethod();
		Type[] gptypes = method.getGenericParameterTypes();
		if (null == gptypes || gptypes.length != 1) {
			return null;
		}
		Type type = gptypes[0];// 方法的第一个参数
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type[] types = parameterizedType.getActualTypeArguments();
			if (null != types && types.length == 1) {
				Type t = types[0];
				if (t instanceof Class) {
					return (Class) t;
				}
			}
		}
		return null;
	}

	private static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Is it an abstract class["
					+ clazz.getName() + "]?", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(
					"the constructor can't access by class[" + clazz.getName()
							+ "]", e);
		}
	}

	class Pair {

		private String name;
		private String value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}
}

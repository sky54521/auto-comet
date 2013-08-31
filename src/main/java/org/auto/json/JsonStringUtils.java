package org.auto.json;

/**
 *
 * @author XiaohangHu
 * */
class JsonStringUtils {

	static String removeBeginAndEnd(String value) {
		return value.substring(1, value.length() - 1);
	}

	/**
	 * @param value
	 *            Must not be null
	 * */
	static boolean isStringValue(String value) {
		return value.startsWith(JsonProtocol.STRING_BEGIN);
	}

	/**
	 * @param value
	 *            Must not be null
	 * */
	static boolean isObjectValue(String value) {
		return value.startsWith(JsonProtocol.OBJECT_BEGIN);
	}

	/**
	 * @param value
	 *            Must not be null
	 * */
	static boolean isCharValue(String value) {
		return value.startsWith(JsonProtocol.CHAR_BEGIN);
	}

	/**
	 * @param value
	 *            Must not be null
	 * */
	static boolean isArrayValue(String value) {
		return value.startsWith(JsonProtocol.ARRAY_BEGIN);
	}

	/**
	 * @param value
	 *            Must not be null
	 * */
	static boolean isNullValue(String value) {
		return JsonProtocol.NULL.equals(value);
	}

	/**
	 * @param value
	 *            Must not be null
	 * */
	static boolean isTrueValue(String value) {
		return JsonProtocol.TRUE.equals(value);
	}

	/**
	 * @param value
	 *            Must not be null
	 * */
	static boolean isFalseValue(String value) {
		return JsonProtocol.FALSE.equals(value);
	}

	/**
	 * @param s
	 *            Must not be null.
	 * @param sb
	 */
	static void escape(String s, StringBuilder sb) {
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				// Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if ((ch >= '\u0000' && ch <= '\u001F')
						|| (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}// for
	}

	// suppress default constructor for noninstantiability
	private JsonStringUtils() {
		throw new AssertionError();
	}

}

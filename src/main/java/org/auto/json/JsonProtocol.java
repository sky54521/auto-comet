package org.auto.json;

/**
 *
 * @see http://www.json.org/
 *
 *      object ::= {} | {members}
 *
 *      members ::= pair | pair, members
 *
 *      pair ::= string: value
 *
 *      array ::= [] | [elements]
 *
 *      elements ::= value | value, elements
 *
 *      value ::= string | number | object | array | true | false | null
 *
 *      string ::= "" | "chars"
 *
 *      chars ::= char | char chars
 *
 *      char ::= any-Unicode-character-except-"-or-\-or-control-character | \" |
 *      \\ | \/ | \b | \f | \n | \r | \t | \\u four-hex-digits
 *
 *      number ::= int | int frac | int exp | int frac exp
 *
 *      int ::= digit | digit1-9 digits | -digit | -digit1-9 digits
 *
 *      frac ::= .digits
 *
 *      exp ::= e digits
 *
 *      digits ::= digit | digit digits
 *
 *      e ::= e | e+ | e- | E | E+ | E-
 *
 * @author XiaohangHu
 *
 * */
public interface JsonProtocol {

	public static final String OBJECT_BEGIN = "{";
	public static final String OBJECT_END = "}";

	public static final String OBJECT_MEMBERS_SEPARATOR = ",";

	public static final String PAIR_SEPARATOR = ":";

	public static final String ARRAY_BEGIN = "[";
	public static final String ARRAY_END = "]";

	public static final String ARRAY_ELEMENTS_SEPARATOR = ",";

	public static final String STRING_BEGIN = "\"";
	public static final String STRING_END = "\"";

	public static final String CHAR_BEGIN = "'";
	public static final String CHAR_END = "'";

	public static final String NULL = "null";
	public static final String TRUE = "true";
	public static final String FALSE = "false";

}

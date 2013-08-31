package org.auto.xml;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XML工具
 *
 * @author XiaohangHu
 */
public class XmlUtil {

	/**
	 * 获得Element类型子节点的迭代器
	 *
	 * @param parentNode
	 *            父节点
	 * @return 类型子节点的迭代器
	 *
	 * */
	public static Iterator<Element> childElementIterator(Node parentNode) {
		return new ElementIterator(parentNode);
	}

	/**
	 * 获得指定名称的Element类型子节点的迭代器
	 *
	 * @param parentNode
	 *            父节点
	 * @param nodeName
	 *            节点名称
	 * @return 类型子节点的迭代器
	 *
	 * */
	public static Iterator<Element> childElementIterator(Node parentNode,
			String nodeName) {
		return new ElementByNameIterator(parentNode, nodeName);
	}

	/**
	 * 获得element的属性值
	 *
	 * @param attributeName
	 *            属性名称
	 * @param element
	 *            Element
	 * */
	public static String getElementAttributeTrim(String attributeName,
			Element element) {
		if (null == element)
			return null;
		String value = element.getAttribute(attributeName);
		return StringUtils.trim(value);
	}

	// suppress default constructor for noninstantiability
	private XmlUtil() {
		throw new AssertionError();
	}
}

/**
 * Element迭代器,非线程安全的。
 * */
class ElementIterator implements Iterator<Element> {

	private Node parentNode;
	private NodeList childNodes;
	private int index = 0;
	/** 仅仅为remove方法记录上一个被读过的元素 */
	private Element lastElement = null;

	public ElementIterator(Node parentNode) {
		this.parentNode = parentNode;
		this.childNodes = parentNode.getChildNodes();
	}

	/** 这种构造方式，remove方法是无效的。 */
	public ElementIterator(NodeList nodes) {
		this.childNodes = nodes;
	}

	public boolean hasNext() {
		while (this.index < this.getLength()) {
			Node node = this.childNodes.item(this.index);
			if (this.isEligibleNode(node)) {
				return true;
			} else {
				this.index++;
			}
		}
		return false;
	}

	public Element next() {
		if (this.hasNext()) {
			Element element = (Element) this.childNodes.item(this.index);
			this.lastElement = element;
			this.index++;
			return element;
		}
		return null;
	}

	/**
	 * @deprecated 当使用构造器ElementIterator(NodeList nodes)的时侯，该方法是无效的。
	 * */
	public void remove() {
		if (null == this.lastElement || null == this.parentNode)
			throw new IllegalStateException();
		this.parentNode.removeChild(lastElement);
		this.lastElement = null;
	}

	private int getLength() {
		return this.childNodes.getLength();
	}

	protected boolean isEligibleNode(Node node) {
		return isElement(node);
	}

	protected boolean isElement(Node node) {
		return node instanceof Element;
	}
}

class ElementByNameIterator extends ElementIterator {

	private String nodeName;

	public ElementByNameIterator(Node parentNode, String nodeName) {
		super(parentNode);
		this.nodeName = nodeName;
	}

	/** 这种构造方式，remove方法是无效的。 */
	public ElementByNameIterator(NodeList nodes, String nodeName) {
		super(nodes);
		this.nodeName = nodeName;
	}

	protected boolean isEligibleNode(Node node) {
		if (isElement(node)) {
			if (null == nodeName)
				return true;
			String name = node.getNodeName();
			if (nodeName.equals(name)) {
				return true;
			}
		}
		return false;
	}
}

package org.auto.util.mutableTree;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author huxh
 * */
public class MutableTreeUtils {

	/**
	 * 深拷贝节点数组。
	 *
	 * @author nodeList
	 * @author cloner 用户数据拷贝器，为空则不对用户数据做拷贝
	 *
	 *
	 * */
	public static List<DefaultMutableTreeNode> cloneNodeList(
			List<DefaultMutableTreeNode> nodeList, UserObjectCloner<?> cloner) {
		if (null == nodeList) {
			return null;
		}
		List<DefaultMutableTreeNode> result = new LinkedList<DefaultMutableTreeNode>();
		if (null == cloner)
			for (DefaultMutableTreeNode node : nodeList) {
				DefaultMutableTreeNode newNode = cloneNode(node);
				result.add(newNode);
			}
		else {
			for (DefaultMutableTreeNode node : nodeList) {
				DefaultMutableTreeNode newNode = cloneNode(node, cloner);
				result.add(newNode);
			}
		}
		return result;
	}

	/**
	 * 迭代深度拷贝节点。
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static DefaultMutableTreeNode cloneNode(DefaultMutableTreeNode node,
			UserObjectCloner cloner) {
		if (null == node) {
			return null;
		}
		DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) node.clone();
		Enumeration<DefaultMutableTreeNode> children = node.children();
		while (children.hasMoreElements()) {
			DefaultMutableTreeNode childNode = children.nextElement();
			DefaultMutableTreeNode newChildNode = cloneNode(childNode, cloner);
			Object userObject = newChildNode.getUserObject();
			newChildNode.setUserObject(cloner.clone(userObject));
			newNode.add(newChildNode);
		}
		return newNode;
	}

	/**
	 * 迭代深度拷贝节点，不拷贝userObject。
	 * */
	@SuppressWarnings("unchecked")
	public static DefaultMutableTreeNode cloneNode(DefaultMutableTreeNode node) {
		if (null == node) {
			return null;
		}
		DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) node.clone();
		Enumeration<DefaultMutableTreeNode> children = node.children();
		while (children.hasMoreElements()) {
			DefaultMutableTreeNode childNode = children.nextElement();
			DefaultMutableTreeNode newChildNode = cloneNode(childNode);
			newNode.add(newChildNode);
		}
		return newNode;
	}

	/**
	 * 过滤节点
	 *
	 * */
	@SuppressWarnings("unchecked")
	public static DefaultMutableTreeNode filterNode(
			DefaultMutableTreeNode node, MutableTreeNodeFilter filter) {
		if (null == node) {
			return null;
		}
		Enumeration<DefaultMutableTreeNode> enumeration = node
				.breadthFirstEnumeration();
		/** 去掉节点自己 */
		if (enumeration.hasMoreElements()) {
			enumeration.nextElement();
		}
		List<DefaultMutableTreeNode> removeNodes = new LinkedList<DefaultMutableTreeNode>();
		while (enumeration.hasMoreElements()) {
			DefaultMutableTreeNode treeNode = enumeration.nextElement();
			boolean filt = filter.filter(treeNode);
			if (filt) {
				removeNodes.add(treeNode);
			}
		}
		for (DefaultMutableTreeNode removeNode : removeNodes) {
			removeNode.removeFromParent();
		}
		return node;
	}

	/**
	 * 过滤节点集合
	 *
	 * */
	public static List<DefaultMutableTreeNode> filterNodeList(
			List<DefaultMutableTreeNode> nodeList, MutableTreeNodeFilter filter) {
		if (null == nodeList) {
			return null;
		}
		Iterator<DefaultMutableTreeNode> it = nodeList.iterator();
		while (it.hasNext()) {
			DefaultMutableTreeNode node = it.next();
			if (filter.filter(node)) {
				it.remove();
			} else {
				filterNode(node, filter);
			}
		}
		return nodeList;
	}

	private MutableTreeUtils() {
		throw new AssertionError();
	}

}

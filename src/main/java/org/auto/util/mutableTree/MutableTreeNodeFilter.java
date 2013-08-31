package org.auto.util.mutableTree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * DefaultMutableTreeNode过滤器
 *
 * @author huxh
 * */
public interface MutableTreeNodeFilter {

    /**
     * @return 返回true，则过滤掉节点
     * */
    boolean filter(DefaultMutableTreeNode node);

}

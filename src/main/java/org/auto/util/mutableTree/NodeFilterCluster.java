package org.auto.util.mutableTree;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * NodeFilterCluster能够将一组过滤器组成一个过滤器
 *
 * 默认为强的过滤:过滤器中任意一个返回true则结果返回true
 *
 * @author huxh
 * */
public class NodeFilterCluster implements MutableTreeNodeFilter {

    private Collection<MutableTreeNodeFilter> filters;
    /** 默认为强的过滤 */
    private boolean forced = true;

    public NodeFilterCluster() {
        this.filters = new LinkedList<MutableTreeNodeFilter>();
    }

    public NodeFilterCluster(Collection<MutableTreeNodeFilter> filters) {
        this.filters = filters;
    }

    /**
     * 添加一个过滤器
     * */
    public void add(MutableTreeNodeFilter filter) {
        this.filters.add(filter);
    }

    /**
     * 过滤器中任意一个返回true则过滤结果返回true
     * */
    public boolean filter(DefaultMutableTreeNode node) {
        if (this.forced) {
            return forcedFilter(node);
        } else {
            return weakFilter(node);
        }
    }

    /**
     * 过滤器中任意一个返回true则过滤结果返回true
     * */
    protected boolean forcedFilter(DefaultMutableTreeNode node) {
        for (MutableTreeNodeFilter filter : filters) {
            boolean result = filter.filter(node);
            if (result) {
                return result;
            }
        }
        return false;
    }

    /**
     * 过滤器中任意一个返回true则过滤结果返回true
     * */
    protected boolean weakFilter(DefaultMutableTreeNode node) {
        if (filters.isEmpty()) {
            return false;
        }
        for (MutableTreeNodeFilter filter : filters) {
            boolean result = filter.filter(node);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    public boolean isForced() {
        return forced;
    }

    /**
     * 设置是否为“强过滤”，是则过滤器中任意一个返回true结果返回true
     * */
    public void setForced(boolean forced) {
        this.forced = forced;
    }

}

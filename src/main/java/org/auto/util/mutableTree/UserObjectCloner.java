package org.auto.util.mutableTree;

/**
 * 用户数据拷贝器
 *
 * @author huxh
 * */
public interface UserObjectCloner<T> {

    /**
     * 拷贝一个对象
     * */
    T clone(T object);

}

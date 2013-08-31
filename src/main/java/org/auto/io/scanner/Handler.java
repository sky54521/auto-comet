package org.auto.io.scanner;

/**
 * @author XiaohangHu
 * */
public interface Handler<T> {

	/**
	 * 对数据处理
	 * */
	void handle(T t);
}

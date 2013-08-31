package org.auto.id;

import java.io.Serializable;

/**
 * Id生成器
 *
 * @author XiaohangHu
 * */
public interface IdentifierGenerator<T extends Serializable> {

	/**
	 * Generate a new identifier.
	 *
	 * @return a new identifier
	 */
	public T generate();

}

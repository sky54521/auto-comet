package org.auto.comet;

/**
 * 协议相关常量
 *
 * @author XiaohangHu
 * */
public interface Protocol {

	/** 请求参数名：连接ID */
	String CONNECTIONID_KEY = "_C_COMET";
	
	/** 请求参数名：同步 */
	String SYNCHRONIZE_KEY = "_S_COMET";
		/** 同步值：创建连接 */
		String CONNECTION_VALUE = "C";
			/** 异步连接中断(comet连接还没断) **/
			String Suspend_Value = "S";
		/** 同步值：断开连接 */
		String DISCONNECT_VALUE = "D";
	

}

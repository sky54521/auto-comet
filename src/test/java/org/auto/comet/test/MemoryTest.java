package org.auto.comet.test;

import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.AsyncContext;

//import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.InputBuffer;
import org.apache.catalina.connector.Request;
import org.apache.catalina.core.AsyncContextImpl;
import org.auto.comet.ConcurrentPushSocket;
import org.auto.comet.PushSocket;

/**
 * org.apache.catalina.connector.Request
 * 对象占用内存非常多，导致comet连接占用内存非常多（平均一个连接占用内存8KB+）
 * 
 * @author xiaohanghu
 */
public class MemoryTest {

	private static final MemoryMXBean memoryMXBean = ManagementFactory
			.getMemoryMXBean();

	protected static Map<Serializable, Object> socketStore = new ConcurrentHashMap<Serializable, Object>();

	// protected static Map<Serializable, PushSocket> socketStore =
	// Collections.EMPTY_MAP;

	public static void main(String[] args) throws IOException {

		for (int i = 0; i < 8000; i++) {
			// Request request = new Request();
			// socketStore.put("asdfsdfsadf" + i, new ConcurrentPushSocket());
			// AsyncContext asyncContext = new AsyncContextImpl(request);
			// socketStore.put("asdfsdfsadfa" + i, asyncContext);
			// socketStore.put("asdfsdfsadfa" + i, request);// 占内存非常多
			// request.finishRequest();
			InputBuffer inputBuffer = new InputBuffer();
			socketStore.put("asdfsdfsadfa" + i, new InputBuffer());// 罪魁祸首
			inputBuffer.close();
		}
		MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
		long usedBytes = memoryUsage.getUsed();

		System.out.println("used :" + usedBytes / 1024 / 1024 + " MB");

	}
}

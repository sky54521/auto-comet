package org.auto.id;

/**
 * 本地自增id生成器 这里的Local代表本jvm.只有同一个生成器的实例,能保证其生成的id唯一.
 *
 * @author XiaohangHu
 * */
public class LocalIncrementGenerator extends IncrementGenerator {

	/**
	 * 当前的值,默认起始值:0
	 * */
	private Long localNumber = 10L;

	/**
	 * 增量,默认:1
	 * */
	private int incremental = 1;

	/**
	 * @param startNumber
	 *            起始值
	 * */
	public LocalIncrementGenerator() {
		super();
	}

	/**
	 * @param startNumber
	 *            起始值
	 * */
	public LocalIncrementGenerator(Long startNumber) {
		super();
		setStartNumber(startNumber);
	}

	/**
	 * @param startNumber
	 *            起始值
	 * @param incremental
	 *            增量
	 * */
	public LocalIncrementGenerator(Long startNumber, Integer incremental) {
		super();
		setStartNumber(startNumber);
		setIncremental(incremental);
	}

	public synchronized Long generate() {
		Long result = getLocalNumber();
		int incremental = getIncremental();
		setLocalNumber(result + incremental);
		return result;
	}

	private Long getLocalNumber() {
		return localNumber;
	}

	private void setLocalNumber(Long localNumber) {
		this.localNumber = localNumber;
	}

	private Integer getIncremental() {
		return incremental;
	}

	private void setStartNumber(Long startNumber) {
		if (null != startNumber) {
			this.localNumber = startNumber;
		}
	}

	private void setIncremental(Integer incremental) {
		if (null != incremental) {
			this.incremental = incremental;
		}
	}

}

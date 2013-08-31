package org.auto.io;

import java.io.InputStream;

/**
 *
 * @author XiaohangHu
 */
public class InputStreamResource implements Resource {

	private final InputStream inputStream;

	private final String description;

	/**
	 * Create a new InputStreamResource.
	 *
	 * @param inputStream
	 *            the InputStream to use
	 */
	public InputStreamResource(InputStream inputStream) {
		this(inputStream, "resource loaded through InputStream");
	}

	/**
	 * Create a new InputStreamResource.
	 *
	 * @param inputStream
	 *            the InputStream to use
	 * @param description
	 *            where the InputStream comes from
	 */
	public InputStreamResource(InputStream inputStream, String description) {
		if (inputStream == null) {
			throw new IllegalArgumentException("InputStream must not be null");
		}
		this.inputStream = inputStream;
		this.description = (description != null ? description : "");
	}

	@Override
	public InputStream getInputStream() throws ReadResourceException {
		return this.inputStream;
	}

	@Override
	public String getDescription() {
		return "InputStream resource[" + this.description + "]";
	}

}

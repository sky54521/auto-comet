package org.auto.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author XiaohangHu
 * */
public class FileResource implements Resource {

	/**
	 * 协议名
	 * */
	public static final String RESOURCE_PROTOCOL_NAME = "file";

	private File file;

	public FileResource(File file) {
		this.file = file;
	}

	@Override
	public InputStream getInputStream() throws ReadResourceException {
		try {
			return new FileInputStream(this.file);
		} catch (FileNotFoundException e) {
			throw new ReadResourceException("File not found [" + file + "]", e);
		}
	}

	@Override
	public String getDescription() {
		return "FileResource [" + file + "]";
	}

}

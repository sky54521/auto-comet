package org.auto.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author XiaohangHu
 */
public class JarEntryResource implements Resource {

	private JarFile jarFile;
	private JarEntry jarEntry;

	public JarEntryResource(JarFile jarFile, JarEntry jarEntry) {
		this.jarFile = jarFile;
		this.jarEntry = jarEntry;
	}

	@Override
	public InputStream getInputStream() throws ReadResourceException {
		try {
			return jarFile.getInputStream(jarEntry);
		} catch (IOException e) {
			throw new ReadResourceException(
					"IOException get stream from [jarFile:" + jarFile
							+ ",jarEntry:" + jarEntry + "]", e);
		}
	}

	@Override
	public String getDescription() {
		return "JarEntryResource [jarFile:" + jarFile + ",jarEntry:" + jarEntry
				+ "]";
	}
}

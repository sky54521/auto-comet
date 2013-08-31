package org.auto.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author XiaohangHu
 * */
public class UrlResource implements Resource {

	/**
	 * Original URL, used for actual access.
	 */
	private final URL url;

	public UrlResource(URL url) {
		this.url = url;
	}

	/**
	 * This implementation opens an InputStream for the given URL. It sets the
	 * "UseCaches" flag to <code>false</code>, mainly to avoid jar file locking
	 * on Windows.
	 *
	 * @see java.net.URL#openConnection()
	 * @see java.net.URLConnection#setUseCaches(boolean)
	 * @see java.net.URLConnection#getInputStream()
	 */
	public InputStream getInputStream() throws ReadResourceException {
		URLConnection con;
		try {
			con = this.url.openConnection();
			con.setUseCaches(false);
			return con.getInputStream();
		} catch (IOException e) {
			throw new ReadResourceException("IOException read "
					+ getDescription(), e);
		}
	}

	/**
	 * This implementation returns a description that includes the URL.
	 */
	public String getDescription() {
		return "URL resource [" + this.url + "]";
	}

}

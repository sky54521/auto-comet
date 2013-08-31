package org.auto.io.scanner;

import java.io.File;

import org.auto.io.FileResource;
import org.auto.io.Resource;

/**
 * 资源扫描器
 *
 * @author XiaohangHu
 * */
public class FileSystemResourceScanner extends AbstractPatternResourceScanner {

	private FilePatternScanner fileScanner;
	{
		fileScanner = new DefaultFilePatternScanner();
	}

	@Override
	public void scan(String locationPattern, final ResourceHandler handler) {
		fileScanner.scan(locationPattern, new FileHandler() {
			@Override
			public void handle(File file) {
				Resource resource = new FileResource(file);
				handler.handle(resource);
			}

		});
	}

}

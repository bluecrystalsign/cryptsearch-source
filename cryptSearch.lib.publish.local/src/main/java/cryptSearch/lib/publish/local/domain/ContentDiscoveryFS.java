package cryptSearch.lib.publish.local.domain;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import cryptSearch.lib.publish.local.message.ContentDiscoveryWork;
import cryptSearch.lib.publish.local.message.ContentUri;
import cryptSearch.lib.publish.local.util.PublishLocalFactory;

public class ContentDiscoveryFS implements ContentDiscovery {
	private String[] initialPath;
	private String[] fileExtension;
	public String[] getInitialPath() {
		return initialPath;
	}
	public String[] getFileExtension() {
		return fileExtension;
	}
	public ContentDiscoveryFS(String initialPath[], String[] fileExtension) {
		super();
		this.initialPath = initialPath;
		this.fileExtension = fileExtension;
	}

	public List<ContentUri> discoverFiles(PublishLocalFactory plf) {
		String[] initialPath = getInitialPath();
		List<ContentUri> fileLst = new ArrayList<ContentUri>();

		for (String nextPath : initialPath) {

			ContentDiscoveryWork cdw = plf.getContentDiscoveryWork(nextPath, getFileExtension());
			try {
				ContentUri ret = discoverContent(cdw);
				fileLst.add(ret);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return fileLst;
	}
	private ContentUri discoverContent(ContentDiscoveryWork cdw)
			throws Exception {
		List<String> ret = new ArrayList<String>();
		List<String> fileLst2 = discoverFullPath(cdw.getPath());
		for (String path : fileLst2) {
			if (filterExtension(path, cdw.getFileExtension())) {
				ret.add(path);
			}
		}
		return new ContentUri(ret);
	}
	private List<String> discoverFullPath(String initialPath)
			throws IOException {
		List<String> fileLst = new ArrayList<String>();
		Path dir = FileSystems.getDefault().getPath(initialPath);
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
		for (Path path : stream) {
			String fullPath = initialPath + File.separator + path.getFileName();
			fileLst.add(fullPath);
			if ((new File(fullPath)).isDirectory()) {
				List<String> fileLst2 = discoverFullPath(fullPath);
				if (!fileLst2.isEmpty()) {
					fileLst.addAll(fileLst2);
				}
			}
		}
		stream.close();
		return fileLst;
	}

	private boolean filterExtension(String path, String[] fileExtension) {
		for (String next : fileExtension) {
			if (path.endsWith(next)) {
				return true;
			}
		}
		return false;
	}

	
}

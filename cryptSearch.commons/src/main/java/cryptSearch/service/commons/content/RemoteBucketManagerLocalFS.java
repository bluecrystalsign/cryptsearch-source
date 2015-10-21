package cryptSearch.service.commons.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cryptSearch.service.commons.Messages;

public class RemoteBucketManagerLocalFS implements RemoteBucketManager {
	private static final int MAX_DATA_FILE = 100 * 1000;
	private final String BASE_PATH = Messages.getString("FileManager.8"); //$NON-NLS-1$
	private final String DATA_PATH = Messages.getString("FileManager.1"); //$NON-NLS-1$
	private final String INDEX_PATH = Messages.getString("FileManager.4"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see cryptSearch.service.commons.content.BucketManager#getDataPath()
	 */
	public File getDataPath() {
		return new File(BASE_PATH + File.separator + DATA_PATH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.BucketManager#getDataFile(java.lang
	 * .String)
	 */
	public File getDataFile(String filePath) {
		return new File(BASE_PATH + File.separator + DATA_PATH + File.separator
				+ filePath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cryptSearch.service.commons.content.BucketManager#getIndexPath()
	 */
	public File getIndexPath() {
		return new File(BASE_PATH + File.separator + INDEX_PATH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.BucketManager#getIndexPath(java.lang
	 * .String)
	 */
	public File getIndexPath(String indexName) {
		return new File(BASE_PATH + File.separator + INDEX_PATH
				+ File.separator + indexName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.BucketManager#clearIndexPath(java
	 * .lang.String)
	 */
	public File clearIndexPath(String indexName) {
		File f = getIndexPath(indexName);
		if (f.exists()) {
			deleteDirectory(f);
		}
		f.mkdirs();
		return f;
	}

	public File loadIndexPath(String indexName) {
		File f = getIndexPath(indexName);
		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.BucketManager#deleteDirectory(java
	 * .io.File)
	 */
	public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cryptSearch.service.commons.content.BucketManager#createDirs()
	 */
	public void init(boolean delete) {
		if (getDataPath().exists()) {
			if (delete) {
				deleteDirectory(getDataPath());
			}
			getDataPath().mkdirs();
		}
		if (getIndexPath().exists()) {
			if (delete) {
				deleteDirectory(getIndexPath());
			}
			getIndexPath().mkdirs();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.BucketManager#loadEncryptedContent
	 * (java.lang.String)
	 */
	public byte[] loadEncryptedContent(String next) throws Exception {
		File dataFile = getDataFile(next);
		byte[] dataContent = loadContent(dataFile);
		System.out.println("De: " + dataFile.getAbsolutePath());
		return dataContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.BucketManager#loadContent(java.io
	 * .File)
	 */
	public byte[] loadContent(File dest) throws Exception {
		InputStream is = new FileInputStream(dest);
		byte[] b = new byte[is.available()];
		is.read(b);
		is.close();
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.BucketManager#storeEncrypted(int,
	 * byte[])
	 */
	public void storeEncrypted(int i, byte[] encContent)
			throws FileNotFoundException, IOException {
		OutputStream os = new FileOutputStream(getDataPath() + File.separator
				+ i);
		os.write(encContent);
		os.close();
	}

	public int getNextContentIndex() {
		for (int i = 0; i < MAX_DATA_FILE; i++) {
			File f = new File(getDataPath() + File.separator + i);
			if (!f.exists()) {
				return i;
			}
		}
		return 0;

	}

}

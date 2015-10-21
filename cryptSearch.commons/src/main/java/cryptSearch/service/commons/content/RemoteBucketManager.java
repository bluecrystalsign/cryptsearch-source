package cryptSearch.service.commons.content;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface RemoteBucketManager {

	public abstract File getDataPath();

	public abstract File getDataFile(String filePath);

	public abstract File getIndexPath();

	public abstract File getIndexPath(String indexName);

	public abstract File clearIndexPath(String indexName);
	public abstract File loadIndexPath(String indexName);

	public abstract boolean deleteDirectory(File path);

	public abstract void init(boolean delete);

	public abstract byte[] loadEncryptedContent(String next) throws Exception;

	public abstract byte[] loadContent(File dest) throws Exception;

	public abstract void storeEncrypted(int i, byte[] encContent)
			throws FileNotFoundException, IOException;

	public abstract int getNextContentIndex();

}
package cryptSearch.service.commons.content;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import cryptSearch.service.commons.domain.ContentRef;
import cryptSearch.service.commons.domain.TermsCache;

public interface LocalBucketManager {

	public abstract File getKsPath();

	public abstract File getKsFile(String filePath);

	public abstract File getCachePath();

	public abstract File getCacheFile(String filePath);

	public abstract File getRefByNamePath();

	public abstract File getRefByNameFile(String filePath);

	public abstract File getClearPath();

	public abstract File getClearFile(String filePath);

	public abstract void createDirs();

	public abstract boolean deleteClearContent();

	public abstract void saveClearContent(ContentRef contentRef, byte[] clearContent) throws Exception;

	public abstract Map<Integer, ContentRef> loadRefByName(String indexName)
			throws Exception;

	public abstract byte[] loadContent(String next)
			throws FileNotFoundException, IOException;

	public abstract File createCacheFile(String indexName);
	public void clearCache();

	public abstract void writeToFile(byte[] bytes, File f) throws Exception;
	public int getNextCacheFile(String filePath);
}
package cryptSearch.service.commons.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import cryptSearch.service.commons.Messages;
import cryptSearch.service.commons.domain.ContentRef;
import cryptSearch.service.commons.domain.TermsCache;
import cryptSearch.service.commons.persist.RefByNamePersistXStream;

public class LocalBucketManagerLocalFS implements LocalBucketManager {
	private static final int MAX_FRAGMENTS = 10*1000;
	private final String BASE_PATH = Messages.getString("FileManager.0"); //$NON-NLS-1$
	private final String KS_PATH = Messages.getString("FileManager.2"); //$NON-NLS-1$
	private final String CACHE_PATH = Messages.getString("FileManager.3"); //$NON-NLS-1$
	private final String REF_PATH = Messages.getString("FileManager.6"); //$NON-NLS-1$
	private final String CLEAR_PATH = Messages.getString("FileManager.7"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see cryptSearch.service.commons.content.LocalBucketManager#getKsPath()
	 */
	public File getKsPath() {
		return new File(BASE_PATH + File.separator + KS_PATH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.LocalBucketManager#getKsFile(java
	 * .lang.String)
	 */
	public File getKsFile(String filePath) {
		return new File(BASE_PATH + File.separator + KS_PATH + File.separator
				+ filePath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.LocalBucketManager#getCachePath()
	 */
	public File getCachePath() {
		return new File(BASE_PATH + File.separator + CACHE_PATH);
	}

	
	public void clearCache() {
		File f = new File(BASE_PATH + File.separator + CACHE_PATH);
		deleteDirectory(f);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.LocalBucketManager#getCacheFile(java
	 * .lang.String)
	 */
	public File createCacheFile(String filePath) {
//		File basePath = new File(BASE_PATH + File.separator + CACHE_PATH
//				+ File.separator + filePath);
//		if(!basePath.exists()){
//			basePath.mkdir();
//		}
//		
//		File nextFile =  new File(basePath.getAbsoluteFile() + File.separator + String.valueOf(0) +  Messages.getString("FileManager.5"));
//		if(basePath.isDirectory()){
//			for(int i = 0; i < MAX_FRAGMENTS; i++){
//				nextFile =  new File(basePath.getAbsoluteFile() + File.separator + String.valueOf(i) + Messages.getString("FileManager.5"));
//				if(!nextFile.exists()){
//					break;
//				}
//			}
//		}

		int next = getNextCacheFile(filePath);
		File basePath = new File(BASE_PATH + File.separator + CACHE_PATH
				+ File.separator + filePath);
		File nextFile =  new File(basePath.getAbsoluteFile() + File.separator + String.valueOf(next) + Messages.getString("FileManager.5"));
		
		return nextFile; //$NON-NLS-1$
	}

	public int getNextCacheFile(String filePath) {
		File basePath = new File(BASE_PATH + File.separator + CACHE_PATH
				+ File.separator + filePath);
		if(!basePath.exists()){
			basePath.mkdir();
		}
		int i = 0;
		File nextFile =  new File(basePath.getAbsoluteFile() + File.separator + String.valueOf(0) +  Messages.getString("FileManager.5"));
		if(basePath.isDirectory()){
			for(; i < MAX_FRAGMENTS; i++){
				nextFile =  new File(basePath.getAbsoluteFile() + File.separator + String.valueOf(i) + Messages.getString("FileManager.5"));
				if(!nextFile.exists()){
					break;
				}
			}
		}
		
		return i; //$NON-NLS-1$
	}

	
	public File getCacheFile(String filePath) {
		File basePath = new File(BASE_PATH + File.separator + CACHE_PATH
				+ File.separator + filePath);
		if(!basePath.exists()){
			basePath.mkdir();
		}
		
		File nextFile =  new File(basePath.getAbsoluteFile() + File.separator + String.valueOf(0) +  Messages.getString("FileManager.5"));
		return nextFile; //$NON-NLS-1$
	}

	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.LocalBucketManager#getRefByNamePath()
	 */
	public File getRefByNamePath() {
		return new File(BASE_PATH + File.separator + REF_PATH);
	}

	/*
	 * (non-Javadoc)
	 * fwrite
	 * @see
	 * cryptSearch.service.commons.content.LocalBucketManager#getRefByNameFile
	 * (java.lang.String)
	 */
	public File getRefByNameFile(String filePath) {
		return new File(BASE_PATH + File.separator + REF_PATH + File.separator
				+ filePath + Messages.getString("FileManager.5")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.LocalBucketManager#getClearPath()
	 */
	public File getClearPath() {
		return new File(BASE_PATH + File.separator + CLEAR_PATH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.LocalBucketManager#getClearFile(java
	 * .lang.String)
	 */
	public File getClearFile(String filePath) {
		return new File(BASE_PATH + File.separator + CLEAR_PATH
				+ File.separator + filePath); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cryptSearch.service.commons.content.LocalBucketManager#createDirs()
	 */
	public void createDirs() {

		if (!getKsPath().exists()) {
			getKsPath().mkdirs();
		}
		if (!getCachePath().exists()) {
			getCachePath().mkdirs();
		}

		if (!getRefByNamePath().exists()) {
			getRefByNamePath().mkdirs();
		}

		if (!getClearPath().exists()) {
			getClearPath().mkdirs();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.LocalBucketManager#deleteClearContent
	 * ()
	 */
	public boolean deleteClearContent() {
		if (getClearPath().exists()) {
			File[] files = getClearPath().listFiles();
			boolean isDel = true;
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					isDel = deleteDirectory(files[i]);
				} else {
					isDel = files[i].delete();
				}
				if (!isDel) {
					return false;
				}
			}
		}
		return (true);
	}

	private boolean deleteDirectory(File path) {
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
	 * @see
	 * cryptSearch.service.commons.content.LocalBucketManager#saveClearContent
	 * (java.util.Map, java.lang.String, byte[])
	 */
	public void saveClearContent(ContentRef contentRef, byte[] clearContent)
			throws Exception {
		String destOrig = contentRef.getName();
			File destFile = new File(destOrig);
			File dest = getClearFile(destFile.getName());
			System.out.println("\t" + dest.getAbsolutePath());
			saveContent(dest, clearContent);
	}



	private void saveContent(File dest, byte[] clearContent) throws Exception {
		OutputStream os = new FileOutputStream(dest);
		os.write(clearContent);
		os.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.LocalBucketManager#loadRefByName(
	 * java.lang.String)
	 */
	public Map<Integer, ContentRef> loadRefByName(String indexName)
			throws Exception {
		RefByNamePersistXStream xs = new RefByNamePersistXStream();
		xs.init(this);
		Map<Integer, ContentRef> refByName = xs.load(indexName);
		return refByName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.content.LocalBucketManager#loadContent(java
	 * .lang.String)
	 */
	public byte[] loadContent(String next) throws FileNotFoundException,
			IOException {
		InputStream is = new FileInputStream(next);
		byte[] content = new byte[is.available()];
		is.read(content);
		is.close();
		return content;
	}

	
	
	public void writeToFile(byte[] bytes, File f) throws Exception {
		
			if( !f.exists()){
				createFolder(f.getParent());
//				File parent = f.getParentFile();
//				parent.mkdirs();
//				
//				f.createNewFile();
			}
			OutputStream os = new FileOutputStream(f);
			os.write(bytes);
			os.close();
	
		
	}

	public void createFolder(String path){
		File f = new File(path);
		f.mkdirs();
	}
}

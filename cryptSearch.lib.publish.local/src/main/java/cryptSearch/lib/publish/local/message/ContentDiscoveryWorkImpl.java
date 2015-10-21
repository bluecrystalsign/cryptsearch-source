package cryptSearch.lib.publish.local.message;


public class ContentDiscoveryWorkImpl implements ContentDiscoveryWork {
	private String path;
	private String[] fileExtension;

	public ContentDiscoveryWorkImpl(String path, String[] fileExtension) {
		super();
		this.path = path;
		this.fileExtension = fileExtension;
	}

	/* (non-Javadoc)
	 * @see cryptSearch.lib.publish.local.message.ContentDiscoveryWork#getFileExtension()
	 */
	public String[] getFileExtension() {
		return fileExtension;
	}

	/* (non-Javadoc)
	 * @see cryptSearch.lib.publish.local.message.ContentDiscoveryWork#getPath()
	 */
	public String getPath() {
		return path;
	}
	
}

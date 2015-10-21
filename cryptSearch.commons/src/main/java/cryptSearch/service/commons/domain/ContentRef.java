package cryptSearch.service.commons.domain;

public class ContentRef {
	private String name;
	private byte[] sha256;
	public ContentRef() {
		super();
	}
	public ContentRef(String name, byte[] sha256) {
		super();
		this.name = name;
		this.sha256 = sha256;
	}
	public String getName() {
		return name;
	}
	public byte[] getSha256() {
		return sha256;
	}
	

}

package cryptSearch.lib.publish.local.domain;

import cryptSearch.service.commons.crypto.CipherKey;

public class GenerateContent {
	private String[] fsExt;
	private String[] path;
	private CipherKey ck;
	private String term;
	private String indexName;
	
	
	public String[] getFsExt() {
		return fsExt;
	}
	public String[] getPath() {
		return path;
	}
	public CipherKey getCk() {
		return ck;
	}
	public String getTerm() {
		return term;
	}
	
	
	public String getIndexName() {
		return indexName;
	}
	public GenerateContent(String[] fsExt, String[] path, CipherKey ck,
			String term, String indexName) {
		super();
		this.fsExt = fsExt;
		this.path = path;
		this.ck = ck;
		this.term = term;
		this.indexName = indexName;
	}


}

package cryptSearch.lib.publish.local.message;

import java.util.Set;

public class IndexBuilderWork {
	private String url;
	private Set<String> encSet;
	public String getUrl() {
		return url;
	}
	public Set<String> getEncSet() {
		return encSet;
	}
	public IndexBuilderWork(String url, Set<String> encSet) {
		super();
		this.url = url;
		this.encSet = encSet;
	}
	
	
}

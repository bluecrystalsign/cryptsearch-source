package cryptSearch.lib.publish.local.message;

import java.util.HashSet;
import java.util.Set;


public class Tokens {
	public Tokens(Set<String> tokens, int i) {
		super();
//		this.url = url;
		this.tokens = tokens;
		this.i = i;
		this.encTokens = new HashSet<String>();
	}

//	private String url;
	private Set<String> tokens;
	private Set<String> encTokens;
	private int i;
	
	public Set<String> getEncTokens() {
		return encTokens;
	}
	public int getI() {
		return i;
	}
//	public String getUrl() {
//		return url;
//	}
	public Set<String> getTokens() {
		return tokens;
	}

//	@Override
//	public String toString() {
//		return "TokenizationResult [url=" + url + ", tokens=" + tokens + ", i="
//				+ i + "]";
//	}
}

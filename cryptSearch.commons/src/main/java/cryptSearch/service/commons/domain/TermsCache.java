package cryptSearch.service.commons.domain;

public class TermsCache {
	private Integer lenght;
	private Integer count;
	public Integer getLenght() {
		return lenght;
	}
	public Integer getCount() {
		return count;
	}
	public TermsCache(Integer lenght, Integer count) {
		super();
		this.lenght = lenght;
		this.count = count;
	}
	public TermsCache() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

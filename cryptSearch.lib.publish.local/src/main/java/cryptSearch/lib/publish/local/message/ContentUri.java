package cryptSearch.lib.publish.local.message;

import java.util.List;

public class ContentUri {
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		for(String next : fileLst){
			str.append(next);	
			str.append("\n");	
		}
		
		return str.toString();
	}

	private List<String> fileLst;

	public List<String> getFileLst() {
		return fileLst;
	}

	public ContentUri(List<String> fileLst) {
		super();
		this.fileLst = fileLst;
	}
	
}

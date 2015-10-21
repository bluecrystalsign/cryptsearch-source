package cryptSearch.lib.search.remote.util;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

public interface RemoteIndexSearcher {
	public IndexSearcher getSearcher() throws IOException;
	public StandardAnalyzer getAnalyzer();
	public void searchTerm(int hitsPerPage, RemoteIndexSearcher indexer2,
			List<ScoreDoc[]> hitsList, String next)
			throws ParseException, IOException;

}
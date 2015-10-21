package cryptSearch.lib.search.remote.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;

import cryptSearch.service.commons.content.RemoteBucketManager;

public class IndexSearcherLucene implements RemoteIndexSearcher {

	private String indexName;
	private RemoteBucketManager bucketManager;
	private IndexSearcher searcher;
	private StandardAnalyzer analyzer;

	public IndexSearcherLucene(String indexName,
			RemoteBucketManager remoteBucketManager) {
		super();
		this.indexName = indexName;
		this.bucketManager = remoteBucketManager;
		analyzer = new StandardAnalyzer(Version.LUCENE_40);
	}

	public IndexSearcher getSearcher() throws IOException {
		if(searcher == null){
			searcher = createIndexSearcher(indexName);
		}
		return searcher;
	}

	public StandardAnalyzer getAnalyzer() {
		return analyzer;
	}

	public void searchTerm(int hitsPerPage, RemoteIndexSearcher indexer2,
			List<ScoreDoc[]> hitsList, String next)
			throws ParseException, IOException {
		Query q = new QueryParser(Version.LUCENE_40, "content", indexer2.getAnalyzer())
				.parse(next);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				hitsPerPage, true);
		(indexer2.getSearcher()).search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		hitsList.add(hits);
	}

	private IndexSearcher createIndexSearcher(String indexName)
			throws IOException {
		File indexPath = bucketManager.getIndexPath(indexName);
		Directory index = MMapDirectory.open(indexPath);
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher sear = new IndexSearcher(reader);
		return sear;
	}

}

package cryptSearch.lib.search.remote.worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;

import cryptSearch.lib.search.remote.util.IndexSearcherLucene;
import cryptSearch.lib.search.remote.util.RemoteIndexSearcher;
import cryptSearch.lib.search.remote.util.SearchRemoteFactory;
import cryptSearch.service.commons.content.RemoteBucketManager;
import cryptSearch.service.commons.content.RemoteBucketManagerLocalFS;

public class SearchRemoteWorkerImpl implements SearchRemoteWorker {
	/* (non-Javadoc)
	 * @see cryptSearch.lib.search.remote.RemoteSearchWorker#indexDump(java.lang.String[])
	 */
	
	private RemoteBucketManager bucketManager;
	private RemoteIndexSearcher indexer;
	
	public SearchRemoteWorkerImpl(RemoteBucketManager bucketManager, RemoteIndexSearcher indexer2) {
		super();
		this.bucketManager = bucketManager;
		this.indexer = indexer2;
	}
	
	public SearchRemoteWorkerImpl(SearchRemoteFactory searchRemoteFactory,
			String indexName) {
		this.bucketManager = searchRemoteFactory.getRemoteBucketManager();
		this.indexer = searchRemoteFactory.getIndexerSearcher(indexName, searchRemoteFactory.getRemoteBucketManager());
	}

	public Map<String, List<String>> indexDump(String indexName, String termsCount) throws Exception {
		Directory index = MMapDirectory.open(bucketManager.getIndexPath(termsCount));
		IndexReader reader = DirectoryReader.open(index);
		return displayIndex(reader, Integer.parseInt(indexName));
	}

	/* (non-Javadoc)
	 * @see cryptSearch.lib.search.remote.RemoteSearchWorker#searchEncryptedIndex(java.lang.String, java.util.Set)
	 */
	public List<String> searchEncryptedIndex(String indexName,
			Set<String> encryptTerm, RemoteBucketManager remoteBucketManager) throws IOException, ParseException {
		List<String> retList = new ArrayList<String>();
		int hitsPerPage = 100;
//		IndexSearcher searcher = createIndexSearcher(indexName);

		RemoteIndexSearcher searcher = new IndexSearcherLucene(indexName, remoteBucketManager);
		List<ScoreDoc[]> hitsList = new ArrayList<ScoreDoc[]>();
//		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		for (String next : encryptTerm) {
			searcher.searchTerm(hitsPerPage, indexer, hitsList, next);
		}
		printHits(retList, indexer.getSearcher(), hitsList);

		return retList;
	}

	private void printHits(List<String> retList, IndexSearcher searcher,
			List<ScoreDoc[]> hitsList) throws IOException {
		for (ScoreDoc[] next : hitsList) {
			if (next.length > 0) {
				List<String> displayResults = displayResults(searcher, next);
				retList.addAll( displayResults );
				break;
			}
		}
	}



	private List<String> displayResults(IndexSearcher searcher, ScoreDoc[] hits)
			throws IOException {
		List<String> retList = new ArrayList<String>();
		if (hits.length > 0) {
			System.out.println("Encontrados " + hits.length + " documentos.");
			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				try {
					System.out.println((i + 1) + ". " + d.get("url"));
					retList.add(d.get("url"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return retList;
	}

	private Map<String, List<String>> displayIndex(IndexReader reader, int j) throws IOException {
		Map<String, List<String>> ret= new HashMap<String, List<String>>();
		for (int i = 0; i < reader.maxDoc(); i++) {
			List<String> ret2 = new ArrayList<String>();
			Document doc = reader.document(i);

			String content = doc.get("content");
			String[] terms = content.split(" ");

//			System.out.println("\n" + doc.get("url"));
//			int len = (j > terms.length) ? terms.length : j;
//			for (int i2 = 0; i2 < len; i2++) {
//				System.out.print(terms[i2] + " ");
//			}
//			System.out.println("");

			int len = (j > terms.length) ? terms.length : j;
			for (int i2 = 0; i2 < len; i2++) {
				ret2.add(terms[i2]);
//				System.out.print(terms[i2] + " ");
			}
			ret.put(doc.get("url"), ret2);
		}
		return ret;
	}
	public byte[] loadEncryptedContent(String next) throws Exception {
		RemoteBucketManager bucketManager;
		bucketManager = new RemoteBucketManagerLocalFS(); 
		byte[] dataContent = bucketManager.loadEncryptedContent(next);
		return dataContent;
	}

}

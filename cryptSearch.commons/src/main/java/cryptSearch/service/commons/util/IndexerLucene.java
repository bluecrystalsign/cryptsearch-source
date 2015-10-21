package cryptSearch.service.commons.util;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;

import cryptSearch.service.commons.content.RemoteBucketManager;

public class IndexerLucene  {

	private Directory index;
	private StandardAnalyzer analyzer;
	private IndexWriter w;
	private Document doc;
	private TextField fieldContent;
	private StringField fieldUrl;
	private IndexSearcher searcher;
	private String indexName;

	private RemoteBucketManager bucketManager;
	public StandardAnalyzer getAnalyzer() {
		return analyzer;
	}

	public IndexerLucene(String indexName, RemoteBucketManager remoteBucketManager) {
		super();
		
		bucketManager = remoteBucketManager;
		try {
			
			File indexPath = bucketManager.clearIndexPath(indexName);
			index = MMapDirectory.open(indexPath);
			this.indexName = indexName;
//			this.searcher = createIndexSearcher(indexName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		analyzer = new StandardAnalyzer(Version.LUCENE_40);
		doc = new Document();
		fieldContent = new TextField("content", "",
				Field.Store.YES);
		doc.add(fieldContent);
		fieldUrl = new StringField("url", "", Field.Store.YES);
		doc.add(fieldUrl);
		
//		fieldFileId = new StringField("id", "", Field.Store.YES);
//		doc.add(fieldFileId);

	}

//	public IndexSearcher getSearcher() throws IOException {
//		if(searcher == null){
//			searcher = createIndexSearcher(indexName);
//		}
//		return searcher;
//	}

	/* (non-Javadoc)
	 * @see cryptSearch.lib.publish.remote.util.Indexer#finish()
	 */
//	public void finish() throws IOException {
//		w.close();
//	}

	/* (non-Javadoc)
	 * @see cryptSearch.lib.publish.remote.util.Indexer#addDocToIndex(java.lang.String, java.util.Set, java.lang.String)
	 */
//	public void addDocToIndex(String url, Set<String> encSet) throws Exception {
//		addContent(w, url, encSet);
//	}
	/* (non-Javadoc)

	 * @see cryptSearch.lib.publish.remote.util.Indexer#create()
	 */
//	public void create() throws IOException {
//		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40,
//				analyzer);
//		config.setUseCompoundFile(false);
//		w = new IndexWriter(index, config);
//	}
	

//	private void addContent(IndexWriter w, String next, Set<String> indexable)
//			throws Exception {
//		fieldContent.setStringValue(createIndexable(indexable));
//		fieldUrl.setStringValue(next);
////		fieldFileId.setStringValue(id);
//		w.addDocument(doc);
//	}

//	private String createIndexable(Set<String> indexable) {
//		StringBuffer sb = new StringBuffer();
//		for (String next : indexable) {
//			sb.append(next);
//			sb.append(" ");
//		}
//		return sb.toString();
//	}
	
//	private IndexSearcher createIndexSearcher(String indexName)
//			throws IOException {
//		File indexPath = bucketManager.getIndexPath(indexName);
//		Directory index = MMapDirectory.open(indexPath);
//		IndexReader reader = DirectoryReader.open(index);
//		IndexSearcher sear = new IndexSearcher(reader);
//		return sear;
//	}

}

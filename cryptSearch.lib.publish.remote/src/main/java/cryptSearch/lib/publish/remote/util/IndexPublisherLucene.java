package cryptSearch.lib.publish.remote.util;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;

import cryptSearch.service.commons.content.RemoteBucketManager;

public class IndexPublisherLucene implements RemoteIndexPublisher {
	private StandardAnalyzer analyzer;
	private Directory index;
	private IndexWriter w;
	private TextField fieldContent;
	private StringField fieldUrl;
	private Document doc;
	private RemoteBucketManager bucketManager;


	public IndexPublisherLucene() {
		super();
	}

	public IndexPublisherLucene(String indexName, 
			RemoteBucketManager remoteBucketManager, 
			boolean recreate) throws Exception {
		super();
		init(indexName, remoteBucketManager, recreate);

	}

	public void init(String indexName, RemoteBucketManager remoteBucketManager, boolean recreate) throws IOException {
		analyzer = new StandardAnalyzer(Version.LUCENE_40);
		doc = new Document();
		fieldContent = new TextField("content", "",
				Field.Store.YES);
		doc.add(fieldContent);
		fieldUrl = new StringField("url", "", Field.Store.YES);
		doc.add(fieldUrl);
		bucketManager = remoteBucketManager;
		File indexPath = null;
		if(recreate){
			indexPath = bucketManager.clearIndexPath(indexName);
		} else {
			indexPath = bucketManager.loadIndexPath(indexName);
		}
		index = MMapDirectory.open(indexPath);
	}

	public void create() throws IOException {
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40,
				analyzer);
		config.setUseCompoundFile(false);
		w = new IndexWriter(index, config);
	}

	public void addDocToIndex(String url, Set<String> encSet) throws Exception {
		addContent(w, url, encSet);
	}

	private void addContent(IndexWriter w, String next, Set<String> indexable)
			throws Exception {
		String createIndexable = createIndexable(indexable);
		fieldContent.setStringValue(createIndexable);
		fieldUrl.setStringValue(next);
		w.addDocument(doc);
	}
	private String createIndexable(Set<String> indexable) {
		StringBuffer sb = new StringBuffer();
		for (String next : indexable) {
			sb.append(next);
			sb.append(" ");
		}
		return sb.toString();
	}
	public void finish() throws IOException {
		w.close();
	}

}

package cryptSearch.service.commons.persist;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.domain.TermsCache;

public class CachePersistXStream implements CachePersist {
	private static final int MAX_CACHE_FILES = 100 * 1000;
	private XStream xs;
	private LocalBucketManager bucket;

	public void init(LocalBucketManager bucket, boolean delete) {
		xs = new XStream(new StaxDriver());
		xs.alias("term", TermsCache.class);
		this.bucket = bucket;
		if (delete) {
			this.bucket.clearCache();
		}
	}

	public List<TermsCache>[] loadFromCache(String indexName) throws Exception {
		int nextCache = this.bucket.getNextCacheFile(indexName);

		List<TermsCache>[] retTerms = (List<TermsCache>[]) new ArrayList[nextCache];

		for (int i = 0; i < nextCache; i++) {
			InputStream is = new FileInputStream(bucket.getCacheFile(indexName));
			byte[] b = new byte[is.available()];
			is.read(b);
			List<TermsCache> unmarshall = unmarshall(new String(b));
			is.close();
			retTerms[i] = unmarshall;
		}
		return retTerms;
	}

	public void persistCache(String indexName,
			Map<Integer, Set<String>> sumTokens) throws Exception {
		List<TermsCache> cache = new ArrayList<TermsCache>();
		for (Integer nextInt : sumTokens.keySet()) {
			cache.add(new TermsCache(nextInt, sumTokens.get(nextInt).size()));
		}
		writeToFile(cache, bucket.createCacheFile(indexName));
	}

	private String marshall(List<TermsCache> ks) {
		return xs.toXML(ks);
	}

	private List<TermsCache> unmarshall(String s) {
		return (List<TermsCache>) xs.fromXML(s);
	}

	private void writeToFile(List<TermsCache> term, File f) throws Exception {
		byte[] bytes = marshall(term).getBytes();

		this.bucket.writeToFile(bytes, f);

	}

}

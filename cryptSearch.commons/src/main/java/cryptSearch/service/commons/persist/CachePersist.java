package cryptSearch.service.commons.persist;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.domain.TermsCache;

public interface CachePersist {
	public void init(LocalBucketManager bucket, boolean delete);
	
	void persistCache(String indexName, Map<Integer, Set<String>> sumTokens)
			throws Exception;

	public List<TermsCache>[] loadFromCache(String indexName)  throws Exception;
}

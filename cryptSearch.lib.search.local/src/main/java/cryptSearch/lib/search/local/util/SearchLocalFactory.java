package cryptSearch.lib.search.local.util;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.persist.CachePersist;
import cryptSearch.service.commons.util.CacheHandler;

public interface SearchLocalFactory {
//	public SearchLocalWorker getLocalSearchWorker();
	public LocalBucketManager getLocalBucketManager();
	public CachePersist getCachePersist();
	public SearchCryptoService getCryptoService();
	public CacheHandler getCacheHandler(boolean delete); 

}
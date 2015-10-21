package cryptSearch.lib.search.remote.util;

import cryptSearch.lib.search.remote.worker.SearchRemoteWorker;
import cryptSearch.service.commons.content.RemoteBucketManager;

public interface SearchRemoteFactory {
	public SearchRemoteWorker getRemoteSearchWorker(RemoteBucketManager rbm, RemoteIndexSearcher indexer);
	public RemoteBucketManager getRemoteBucketManager();
	public RemoteIndexSearcher getIndexerSearcher(String indexName, RemoteBucketManager remoteBucketManager);
}
package cryptSearch.lib.search.remote.util;

import cryptSearch.lib.search.remote.worker.SearchRemoteWorker;
import cryptSearch.lib.search.remote.worker.SearchRemoteWorkerImpl;
import cryptSearch.service.commons.content.RemoteBucketManager;
import cryptSearch.service.commons.content.RemoteBucketManagerLocalFS;


public class SearchRemoteFactoryImpl implements SearchRemoteFactory {
	private static SearchRemoteFactory factory;
	
	public static SearchRemoteFactory getInstance(){
		if(factory== null){
			factory = new SearchRemoteFactoryImpl();
		}
		return factory;
	}

	
	private SearchRemoteFactoryImpl() {
		super();
	}


	public SearchRemoteWorker getRemoteSearchWorker(RemoteBucketManager rbm, RemoteIndexSearcher indexer) {
		return new SearchRemoteWorkerImpl(rbm, indexer);
	}

	public RemoteBucketManager getRemoteBucketManager() {
		return new RemoteBucketManagerLocalFS(); 
	}
	
	public RemoteIndexSearcher getIndexerSearcher(String indexName, RemoteBucketManager remoteBucketManager){
		return new IndexSearcherLucene(indexName, remoteBucketManager);
	}

}

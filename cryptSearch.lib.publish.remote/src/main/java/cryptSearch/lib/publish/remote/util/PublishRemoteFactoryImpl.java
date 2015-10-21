package cryptSearch.lib.publish.remote.util;

import cryptSearch.service.commons.content.RemoteBucketManager;
import cryptSearch.service.commons.content.RemoteBucketManagerLocalFS;

public class PublishRemoteFactoryImpl implements PublishRemoteFactory {
	private static PublishRemoteFactoryImpl factory;
	private PublishRemoteFactoryImpl() {
		super();
	}
	public static PublishRemoteFactory getInstance(){
		if(factory == null){
			factory = new PublishRemoteFactoryImpl();
		}
		return factory;
	}
	public RemoteIndexPublisher getIndexer(String indexName, 
			RemoteBucketManager remoteBucketManager, 
			boolean recreate) throws Exception {
		return new IndexPublisherLucene(indexName, remoteBucketManager, recreate);
	}

	public RemoteBucketManager getRemoteBucketManager() {
		return new RemoteBucketManagerLocalFS();
	}

}

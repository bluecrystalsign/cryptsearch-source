package cryptSearch.lib.publish.remote.util;

import cryptSearch.service.commons.content.RemoteBucketManager;

public interface PublishRemoteFactory {

	public RemoteIndexPublisher getIndexer(String indexName, 
			RemoteBucketManager remoteBucketManager, 
			boolean recreate) throws Exception;
	public RemoteBucketManager getRemoteBucketManager();
}
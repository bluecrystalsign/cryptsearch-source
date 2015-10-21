package cryptSearch.lib.publish.remote.worker;

import java.io.IOException;
import java.util.Set;

import cryptSearch.lib.publish.remote.util.RemoteIndexPublisher;
import cryptSearch.lib.publish.remote.util.PublishRemoteFactory;


public class PublishRemoteWorkerImpl implements PublishRemoteWorker {
	private  String indexName;	
	private  RemoteIndexPublisher index;
	private PublishRemoteFactory prf;
	
	public PublishRemoteWorkerImpl(PublishRemoteFactory prf) {
		super();
		this.prf = prf;
	}

	/* (non-Javadoc)
	 * @see cryptSearch.lib.publish.remote.PublishRemoteWorker#getIndexName()
	 */
	public  String getIndexName() {
		return indexName;
	}

	/* (non-Javadoc)
	 * @see cryptSearch.lib.publish.remote.PublishRemoteWorker#setIndexName(java.lang.String)
	 */
	public  void initIndex(String indexName, boolean recreate) throws Exception {
		this.indexName = indexName;
		index = prf.getIndexer(indexName, prf.getRemoteBucketManager(), recreate);
			index.create();
	}
	
	/* (non-Javadoc)
	 * @see cryptSearch.lib.publish.remote.PublishRemoteWorker#addDocToIndex(java.lang.String, java.util.Set, java.lang.String)
	 */
	public void addDocToIndex(String url, Set<String> encSet) throws Exception {
		index.addDocToIndex(url, encSet);
	}
	/* (non-Javadoc)
	 * @see cryptSearch.lib.publish.remote.PublishRemoteWorker#finish()
	 */
	public void finish() throws IOException {
		index.finish();
	}

	public void updateIndex(String url, Set<String> encSet)  throws Exception {
		index.addDocToIndex(url, encSet);
		
	}
	

}

package cryptSearch.lib.publish.remote.worker;

import java.io.IOException;
import java.util.Set;

public interface PublishRemoteWorker {

	public abstract String getIndexName();

	public abstract void initIndex(String indexName, boolean recreate) throws Exception;

	public abstract void addDocToIndex(String url, Set<String> encSet)
			throws Exception;

	public abstract void finish() throws IOException;

	public abstract void updateIndex(String contentRefence, Set<String> set) throws Exception;


}
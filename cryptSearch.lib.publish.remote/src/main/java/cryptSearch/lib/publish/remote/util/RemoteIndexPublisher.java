package cryptSearch.lib.publish.remote.util;

import java.io.IOException;
import java.util.Set;

public interface RemoteIndexPublisher {

	void create() throws IOException;

	void addDocToIndex(String url, Set<String> encSet) throws Exception;

	void finish() throws IOException;

}
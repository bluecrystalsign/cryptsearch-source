package cryptSearch.lib.search.remote.worker;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.queryparser.classic.ParseException;

import cryptSearch.service.commons.content.RemoteBucketManager;

public interface SearchRemoteWorker {

	public abstract Map<String, List<String>> indexDump(String indexName, String termsCount) throws Exception;

	public abstract List<String> searchEncryptedIndex(String indexName,
			Set<String> encryptTerm, RemoteBucketManager remoteBucketManager) throws IOException, ParseException;
	public byte[] loadEncryptedContent(String next) throws Exception;
}
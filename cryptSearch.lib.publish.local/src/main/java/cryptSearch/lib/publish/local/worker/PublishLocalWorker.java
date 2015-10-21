package cryptSearch.lib.publish.local.worker;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

public interface PublishLocalWorker {

	public abstract Map<String, Set<String>> createEncryptedTokens(String indexName)
			throws Exception, IOException, SAXException, TikaException;

	public abstract Map<String, Set<String>> createEncryptedTokensToUpdate(
			String indexName) throws Exception;

}
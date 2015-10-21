package cryptSearch.lib.search.local.worker;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Set;

import javax.crypto.NoSuchPaddingException;

public interface SearchLocalWorker {

	public abstract Set<String> encryptTerm(String term, String indexName) throws Exception, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException;

}
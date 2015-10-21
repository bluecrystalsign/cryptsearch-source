package cryptSearch.service.commons.persist;

import java.io.File;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.crypto.KeyStore;

public interface KeyPersist {
	public void init(LocalBucketManager bucket);
	
//	public String marshall(KeyStore ks);
//	
//	public KeyStore unmarshall(String s);
	
	public void persist(KeyStore ks, String f) throws Exception;
	public KeyStore load(String f) throws Exception;
}

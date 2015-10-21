package cryptSearch.service.commons.util;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.content.RemoteBucketManager;
import cryptSearch.service.commons.crypto.CryptoService;

public interface CommonsFactory {

	RemoteBucketManager getRemoteBucketManager();

	LocalBucketManager getLocalBucketManager();
	public CryptoService getCryptoService();
}
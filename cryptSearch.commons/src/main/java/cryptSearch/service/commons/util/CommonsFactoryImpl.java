package cryptSearch.service.commons.util;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.content.LocalBucketManagerLocalFS;
import cryptSearch.service.commons.content.RemoteBucketManager;
import cryptSearch.service.commons.content.RemoteBucketManagerLocalFS;
import cryptSearch.service.commons.crypto.CryptoService;
import cryptSearch.service.commons.crypto.CryptoServiceAes;

public class CommonsFactoryImpl implements CommonsFactory {
	private static CommonsFactory factory;
	
	private CommonsFactoryImpl() {
		super();
	}

	public static CommonsFactory getInstance(){
		if(factory == null){
			factory = new CommonsFactoryImpl();
		}
		return factory;
	}
	
	public RemoteBucketManager getRemoteBucketManager() {
		return new RemoteBucketManagerLocalFS();
	}

	public LocalBucketManager getLocalBucketManager() {
		return new LocalBucketManagerLocalFS();
	}
	
	public CryptoService getCryptoService() {
		return new CryptoServiceAes(new RemoteBucketManagerLocalFS(), new LocalBucketManagerLocalFS());
	}


}

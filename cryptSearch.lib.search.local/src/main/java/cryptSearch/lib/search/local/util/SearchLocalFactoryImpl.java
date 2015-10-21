package cryptSearch.lib.search.local.util;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.content.LocalBucketManagerLocalFS;
import cryptSearch.service.commons.persist.CachePersist;
import cryptSearch.service.commons.persist.CachePersistXStream;
import cryptSearch.service.commons.util.CacheHandler;
import cryptSearch.service.commons.util.CacheHandlerImpl;
import cryptSearch.service.commons.util.CommonsFactory;
import cryptSearch.service.commons.util.CommonsFactoryImpl;


public class SearchLocalFactoryImpl implements SearchLocalFactory {
	private CommonsFactory cFactory;
	private static SearchLocalFactory factory;
	
	public static SearchLocalFactory getInstance(){
		if(factory== null){
			factory = new SearchLocalFactoryImpl();
		}
		return factory;
	}

	private SearchLocalFactoryImpl() {
		super();
		cFactory = CommonsFactoryImpl.getInstance();
	}

	public LocalBucketManager getLocalBucketManager() {
		
		return new LocalBucketManagerLocalFS();
	}
	public CachePersist getCachePersist() 
	{
		return new CachePersistXStream();
	}

	public SearchCryptoService getCryptoService() {
		return new SearchCryptoServiceAes(cFactory);
	}

	public CacheHandler getCacheHandler(boolean delete) {
		return new CacheHandlerImpl(getCachePersist(), getLocalBucketManager(), delete);
	}

}

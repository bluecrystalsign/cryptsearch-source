package cryptSearch.lib.publish.local.util;

import cryptSearch.lib.publish.local.domain.ContentDiscovery;
import cryptSearch.lib.publish.local.domain.ContentDiscoveryFS;
import cryptSearch.lib.publish.local.message.ContentDiscoveryWork;
import cryptSearch.lib.publish.local.message.ContentDiscoveryWorkImpl;
import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.content.LocalBucketManagerLocalFS;
import cryptSearch.service.commons.persist.CachePersist;
import cryptSearch.service.commons.persist.CachePersistXStream;
import cryptSearch.service.commons.persist.RefByNamePersist;
import cryptSearch.service.commons.persist.RefByNamePersistXStream;
import cryptSearch.service.commons.util.CommonsFactory;
import cryptSearch.service.commons.util.CommonsFactoryImpl;


public class PublishLocalFactoryImpl implements PublishLocalFactory {
	private CommonsFactory commonsFactory = null;
	private static PublishLocalFactory factory;
	
	public static PublishLocalFactory getInstance(){
		if(factory== null){
			factory = new PublishLocalFactoryImpl();
		}
		return factory;
	}
	private PublishLocalFactoryImpl() {
		super();
		commonsFactory = CommonsFactoryImpl.getInstance();
	}

	public CommonsFactory getCommonsFactory() {
		return commonsFactory;
	}
	public ContentLoader getContentLoader() {
		return new ContentLoaderFileSystem();
	}

	public Tokenizer getTokenizer() {
		return new TokenizerLuceneStandardAnalyzer();
	}

	public ContentDiscovery getContentDiscovery(String initialPath[], String[] filters)
	{
		return new ContentDiscoveryFS(initialPath, filters);
	}
	
	public RefByNamePersist getRefByNamePersist() 
	{
		return new RefByNamePersistXStream();
	}
	
	public LocalBucketManager getLocalBucketManager(){
		return new LocalBucketManagerLocalFS();
		
	}
	
	public CachePersist getCachePersist() 
	{
		return new CachePersistXStream();
	}
	
	public ContentDiscoveryWork getContentDiscoveryWork(String path, String[] filter) 
	{
		return new ContentDiscoveryWorkImpl(path,
				filter);
	}

	public PublishCryptoService getCryptoService() {
		return new PublishCryptoServiceAes(commonsFactory);
	}
	
	
}

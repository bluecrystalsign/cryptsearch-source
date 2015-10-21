package cryptSearch.lib.publish.local.util;

import cryptSearch.lib.publish.local.domain.ContentDiscovery;
import cryptSearch.lib.publish.local.message.ContentDiscoveryWork;
import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.persist.CachePersist;
import cryptSearch.service.commons.persist.RefByNamePersist;
import cryptSearch.service.commons.util.CommonsFactory;

public interface PublishLocalFactory {
	public ContentLoader getContentLoader();
	public Tokenizer getTokenizer();
	public ContentDiscovery getContentDiscovery(String initialPath[], String[] filters);
	public RefByNamePersist getRefByNamePersist();
	public CachePersist getCachePersist();
	public ContentDiscoveryWork getContentDiscoveryWork(String path, String[] filter);
	public PublishCryptoService getCryptoService(); 
	public CommonsFactory getCommonsFactory();
	public LocalBucketManager getLocalBucketManager();

}
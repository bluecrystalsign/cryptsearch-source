package cryptSearch.service.commons.persist;

import java.util.Map;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.domain.ContentRef;

public interface RefByNamePersist {

	void init(LocalBucketManager bucket);

	void persist(Map<Integer, ContentRef> refByName, String name, boolean overwrite) throws Exception;
	public Map<Integer, ContentRef> load(String f)  throws Exception;


}

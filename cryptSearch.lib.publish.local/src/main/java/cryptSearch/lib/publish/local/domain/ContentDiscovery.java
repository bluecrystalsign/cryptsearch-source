package cryptSearch.lib.publish.local.domain;

import java.util.List;

import cryptSearch.lib.publish.local.message.ContentUri;
import cryptSearch.lib.publish.local.util.PublishLocalFactory;

public interface ContentDiscovery {
	public List<ContentUri> discoverFiles(PublishLocalFactory plf);
}

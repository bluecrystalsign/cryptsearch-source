package cryptSearch.service.commons.util;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.domain.TermsCache;
import cryptSearch.service.commons.persist.CachePersist;

public class CacheHandlerImpl implements CacheHandler {
	private List<TermsCache>[] cache;
	private CachePersist persist;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.lib.publish.local.util.CacheHandler#loadCache(java.lang.String
	 * )
	 */
	public void loadCache(String indexName) throws Exception {
		cache = persist.loadFromCache(indexName);
	}

	public CacheHandlerImpl(CachePersist persist,
			LocalBucketManager localBucketManager, boolean delete) {
		super();
		this.persist = persist;
		persist.init(localBucketManager, delete);
	}

	public SortedSet<Integer> getSorted(int i) {
		SortedSet<Integer> sorted = new TreeSet<Integer>();
		for (TermsCache next : cache[i]) {
			sorted.add(next.getLenght());
		}
		return sorted;
	}

	public int getCountByLength(int length, int i) {
		for (TermsCache next : cache[i]) {
			if (next.getLenght() == length) {
				return next.getCount();
			}
		}
		return -1;
	}

	public int getTotalLength(int maxCache) {
		int prefixLen = 0;
		int maxLen = cache.length < maxCache ? cache.length : maxCache;
		for (int i = 0; i < maxLen; i++) {
			SortedSet<Integer> sorted = getSorted(i);

			for (int nextInt : sorted) {
				int count = getCountByLength(nextInt, i);
				prefixLen += count * (nextInt + 1);
			}
		}
		return prefixLen;
	}

	public void loadFromCache(String indexName) throws Exception {
		cache = persist.loadFromCache(indexName);

	}

	public int getCount() {
		return cache.length;
	}

	public void addSorted(int i, SortedSet<Integer> sorted) {
		for (TermsCache next : cache[i]) {
			sorted.add(next.getLenght());
		}
	}

	public boolean isCacheLength(int length, int i) {
		for (TermsCache next : cache[i]) {
			if (next.getLenght() == length) {
				return true;
			}
		}
		return false;
	}

	public boolean isCacheLengthGE(int length, int i) {
		for (TermsCache next : cache[i]) {
			if (next.getLenght() >= length) {
				return true;
			}
		}
		return false;
	}
}

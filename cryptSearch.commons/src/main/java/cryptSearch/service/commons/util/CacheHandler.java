package cryptSearch.service.commons.util;

import java.util.SortedSet;

import cryptSearch.service.commons.domain.TermsCache;

public interface CacheHandler {

	public abstract void loadCache(String indexName) throws Exception;

	public abstract SortedSet<Integer> getSorted(int i);
	
	public int getCountByLength(int length, int i);
	
	public int getTotalLength(int i);

	public abstract void loadFromCache(String indexName) throws Exception;
	
	public int getCount();

	public abstract void addSorted(int i, SortedSet<Integer> sorted);
	
	public boolean isCacheLength(int length, int i);

	public boolean isCacheLengthGE(int length, int i);}
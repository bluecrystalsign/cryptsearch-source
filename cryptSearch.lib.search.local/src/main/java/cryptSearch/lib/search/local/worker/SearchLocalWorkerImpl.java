package cryptSearch.lib.search.local.worker;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.crypto.NoSuchPaddingException;

import com.thoughtworks.xstream.core.util.OrderRetainingMap;

import cryptSearch.lib.search.local.util.SearchCryptoService;
import cryptSearch.lib.search.local.util.SearchLocalFactory;
import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.crypto.CipherKey;
import cryptSearch.service.commons.domain.TermsCache;
import cryptSearch.service.commons.persist.CachePersist;
import cryptSearch.service.commons.util.CacheHandler;
import cryptSearch.service.commons.util.Converter;

public class SearchLocalWorkerImpl implements SearchLocalWorker {
	private static final int NO_WC = 0;
	private static final int STARTS_WITH = 1;
	// private List<TermsCache>[] cache;
	private CacheHandler cache;
	private SearchCryptoService cryptoService;
	private CachePersist persist;

	public SearchLocalWorkerImpl(SearchLocalFactory slFactory) {
		super();
		cryptoService = slFactory.getCryptoService();
		LocalBucketManager localBucketManager = slFactory
				.getLocalBucketManager();
		persist = slFactory.getCachePersist();
		persist.init(localBucketManager, false);
		cache = slFactory.getCacheHandler(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.lib.search.local.LocalSearchWorker#encryptTerm(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	public Set<String> encryptTerm(String term, String indexName)
			throws Exception, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException {
		Set<String> encryptTerm;
		loadCache(indexName);
		CipherKey cipherKey = cryptoService.loadStreamKeyStore(indexName);

		encryptTerm = encryptTerm(term, cipherKey);
		return encryptTerm;
	}

	private void loadCache(String indexName) throws Exception {
		cache.loadFromCache(indexName);
	}

	private Set<String> encryptTerm(String term, CipherKey ck) throws Exception {
		String checkTerm = term;
		int maxLengthTerm = term.length();
		int minLengthTerm = term.length();

		SortedSet<Integer> sorted = new TreeSet<Integer>();
		
		Set<String> ret = new HashSet<String>();
		int prefixLength = 0;
		int cacheCount = cache.getCount();
		for (int counter = 0; counter < cacheCount; counter++) {
			int operation = NO_WC;
			cache.addSorted(counter, sorted);

			if (term.endsWith("*")) {
				checkTerm = term.substring(0, term.length() - 1);
				minLengthTerm = term.length() - 1;
				operation = STARTS_WITH;
				maxLengthTerm = sorted.last();
			} else if (term.endsWith("?")) {
				checkTerm = term.substring(0, term.length() - 1);
				operation = STARTS_WITH;
			}

			switch (operation) {
			case NO_WC:
				if (!cache.isCacheLength(checkTerm.length(), counter)) {
					return null;
				}
				break;
			case STARTS_WITH:
				if (!cache.isCacheLengthGE(checkTerm.length(), counter)) {
					return null;
				}
				break;

			default:
				return null;
			}

			prefixLength += cache.getTotalLength(counter);
			byte[] contentToSearch = createContentToSearch(checkTerm, ck,
					sorted, operation, minLengthTerm, maxLengthTerm,
					prefixLength);

//			prefixLength += getCacheTotalLength(nextCache);

			Set<String> tempSet = createSetToSearch(checkTerm.length(),
					contentToSearch, sorted, operation, minLengthTerm,
					maxLengthTerm);
			ret.addAll(tempSet);
		}

		return ret;
	}

	private byte[] createContentToSearch(String term, CipherKey ck,
			SortedSet<Integer> sorted, int operation, int minLengthTerm,
			int maxLengthTerm, int prefixLength) throws InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		byte[] contentToSearch;
		String prefix = "";
		int prefixLen = 0;
		int length = term.length();
		for (int nextInt : sorted) {
			if (nextInt < minLengthTerm) {
				int count = cache.getCountByLength(nextInt, 0);
				prefixLen = count * (nextInt + 1);
				prefix += new String(new byte[prefixLen]);
			} else if (nextInt == minLengthTerm) {
				prefixLen = prefix.length();
				int fillerLength = minLengthTerm - length + 1;
				int size = cache.getCountByLength(nextInt, 0);
				for (int i = 0; i < size; i++) {
					prefix += term + new String(new byte[fillerLength]);
				}
				if (operation == NO_WC) {
					break;
				}
			} else if (nextInt > minLengthTerm && nextInt <= maxLengthTerm
					&& operation == STARTS_WITH) {
				// prefixLen = prefix.length();
				int size = cache.getCountByLength(nextInt, 0);
				String termStr = term + new String(new byte[nextInt - length]);
				for (int i = 0; i < size; i++) {
					prefix += termStr + " ";
				}
			} else {
				break;
			}
		}

		byte[] toEncrypt = prefix.getBytes();
		byte[] toEncrypt2 = new byte[prefixLength + toEncrypt.length];
		for (int i = 0; i < toEncrypt.length; i++) {
			toEncrypt2[i + prefixLength] = toEncrypt[i];
		}
		System.out.println("-----------");
		System.out.println(Converter.bytesToHex(toEncrypt) + "-"
				+ toEncrypt.length);
		System.out.println(Converter.bytesToHex(toEncrypt2) + "-"
				+ toEncrypt2.length);
		byte[] encContent = cryptoService.encrypt(toEncrypt2, ck);
		System.out.println(Converter.bytesToHex(encContent) + "-"
				+ encContent.length);
		contentToSearch = new byte[encContent.length
				- (prefixLen + prefixLength)];

		for (int j = 0; j < contentToSearch.length; j++) { // Remove prefix from
															// search string
			contentToSearch[j] = encContent[prefixLen + j];

		}
		System.out.println(Converter.bytesToHex(contentToSearch));
		return contentToSearch;
	}

//	private int getCacheLength(int length, int i) {
//		for (TermsCache next : cache[i]) {
//			if (next.getLenght() == length) {
//				return next.getCount();
//			}
//		}
//		return -1;
//	}

	private Set<String> createSetToSearch(int termLen, byte[] contentToSearch,
			SortedSet<Integer> sorted, int operation, int minLengthTerm,
			int maxLengthTerm) {
		Set<String> ret = new HashSet<String>();
		int accIndex = 0;
		for (Integer nextInt : sorted) {
			if (nextInt >= minLengthTerm) {

				accIndex = createBlock(termLen, contentToSearch, operation,
						ret, accIndex, nextInt);
			}
			if (maxLengthTerm == nextInt) {
				break;
			}
		}
		return ret;
	}

	private int createBlock(int termLen, byte[] contentToSearch, int operation,
			Set<String> ret, int accIndex, Integer nextInt) {
		int chunkListLen = cache.getCountByLength(nextInt, 0);
		int beginIndex = 0;
		for (int i = 0; i < chunkListLen; i++) {
			beginIndex = (i * (nextInt + 1) + accIndex);
			byte[] byteTemp = new byte[termLen];
			for (int j = 0; j < byteTemp.length; j++) {
				byteTemp[j] = contentToSearch[beginIndex + j];
			}
			String searchHex = Converter.bytesToHex(byteTemp);
			if (operation == STARTS_WITH) {
				searchHex += "*";
			}
			ret.add(searchHex);
		}
		accIndex = beginIndex + nextInt + 1;
		return accIndex;
	}

}

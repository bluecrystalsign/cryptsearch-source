package cryptSearch.lib.publish.local.worker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import cryptSearch.lib.publish.local.domain.ContentDiscovery;
import cryptSearch.lib.publish.local.domain.ContentDiscoveryFS;
import cryptSearch.lib.publish.local.message.ContentUri;
import cryptSearch.lib.publish.local.message.Tokens;
import cryptSearch.lib.publish.local.util.PublishCryptoService;
import cryptSearch.lib.publish.local.util.PublishLocalFactory;
import cryptSearch.service.commons.crypto.CipherKey;
import cryptSearch.service.commons.domain.ContentRef;
import cryptSearch.service.commons.persist.CachePersist;
import cryptSearch.service.commons.persist.RefByNamePersist;
import cryptSearch.service.commons.util.CacheHandler;

public class PublishLocalWorkerImpl implements PublishLocalWorker {

	private PublishLocalFactory plf;
	// private CommonsFactory cf;
	private String[] initialPath;
	private String[] filters;
	private CacheHandler cacheHandler;

	public PublishLocalWorkerImpl(PublishLocalFactory plf,
			String[] initialPath, String[] filters, CacheHandler cacheHandler) {
		super();
		this.plf = plf;
		this.initialPath = initialPath;
		this.filters = filters;
		this.cacheHandler = cacheHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.lib.publish.local.worker.PublishLocalWorker#createEncryptedTokens
	 * (java.lang.String[], java.lang.String[], java.lang.String)
	 */
	public Map<String, Set<String>> createEncryptedTokens(String indexName)
			throws Exception {

		Map<Integer, Set<String>> tokensByLength = new HashMap<Integer, Set<String>>();
		Map<String, Tokens> tokenByDocumnent = new HashMap<String, Tokens>();
		Map<Integer, ContentRef> refByName = new HashMap<Integer, ContentRef>();

		initOuptupBucket(true);

		List<ContentUri> cdr = discover(plf.getContentDiscovery(initialPath,
				filters));
		PublishCryptoService cryptoService = plf.getCryptoService();
		int i = 0;
		for (ContentUri nextCdr : cdr) {
			for (String nextPath : nextCdr.getFileLst()) {

				String parsed = plf.getContentLoader().parseContentToString(
						nextPath);
				CipherKey cbcCipherKey = cryptoService
						.createCihperKeyBlock(indexName + "_" + i);
				byte[] sha256 = cryptoService.persistEncrypted(nextPath,
						cbcCipherKey, i);
				refByName.put(i, new ContentRef(nextPath, sha256));
				Set<String> tokenizeString = plf.getTokenizer().tokenizeString(
						parsed);
				for (String nextToken : tokenizeString) {
					sumTokensAdd(nextToken, tokensByLength);
				}
				Tokens tr = new Tokens(tokenizeString, i);
				tokenByDocumnent.put(nextPath, tr);
				i++;
			}
		}
		CipherKey ctrCipherKey = cryptoService.createCihperKeyStream(indexName);
		Map<String, String> encMap = cryptoService.encryptIndex(tokensByLength,
				ctrCipherKey, 0);
		// 0 -> the index will be created, so there's no prefix.

		for (String next : encMap.keySet()) {
			System.out.println("[" + next + "]->[" + encMap.get(next) + "]");
		}

		persistCache(indexName, tokensByLength, true);
		persistRefByName(indexName, refByName, true);
		Map<String, Set<String>> tokensToIndex = buildIndex(encMap,
				tokenByDocumnent);
		return tokensToIndex;
	}

	public Map<String, Set<String>> createEncryptedTokensToUpdate(
			String indexName) throws Exception {
		Map<Integer, Set<String>> tokensByLength = new HashMap<Integer, Set<String>>();
		Map<String, Tokens> tokenByDocumnent = new HashMap<String, Tokens>();
		Map<Integer, ContentRef> refByName = new HashMap<Integer, ContentRef>();

		initOuptupBucket(false);
		initCacheHandler(indexName);

		List<ContentUri> cdr = discover(plf.getContentDiscovery(initialPath,
				filters));
		PublishCryptoService cryptoService = plf.getCryptoService();

		int nextContentIndex = cryptoService.getNextContentIndex();
		for (ContentUri nextCdr : cdr) {
			for (String nextPath : nextCdr.getFileLst()) {

				String parsed = plf.getContentLoader().parseContentToString(
						nextPath);
				CipherKey cbcCipherKey = cryptoService
						.createCihperKeyBlock(indexName + "_"
								+ nextContentIndex);
				byte[] sha256 = cryptoService.persistEncrypted(nextPath,
						cbcCipherKey, nextContentIndex);
				refByName.put(nextContentIndex,
						new ContentRef(nextPath, sha256));
				Set<String> tokenizeString = plf.getTokenizer().tokenizeString(
						parsed);
				for (String nextToken : tokenizeString) {
					sumTokensAdd(nextToken, tokensByLength);
				}
				Tokens tr = new Tokens(tokenizeString, nextContentIndex);
				tokenByDocumnent.put(nextPath, tr);
				nextContentIndex++;
			}
		}

		// Create encrypted index

		int maxCache = cacheHandler.getCount();
		Map<String, Set<String>> tokensToIndex =  new HashMap<String, Set<String>>();
//		for (int i = 0; i < maxCache; i++) {
			CipherKey ctrCipherKey = cryptoService
					.loadStreamKeyStore(indexName);
			int prefixLength = cacheHandler.getTotalLength(maxCache);

			Map<String, String> encMap = cryptoService.encryptIndex(
					tokensByLength, ctrCipherKey, prefixLength);

			for (String next : encMap.keySet()) {
				System.out
						.println("[" + next + "]->[" + encMap.get(next) + "]");
			}
			persistCache(indexName, tokensByLength, false);
			persistRefByName(indexName, refByName, false);
			Map<String, Set<String>> tokens = buildIndex(encMap,
					tokenByDocumnent);
			tokensToIndex.putAll(tokens);
//		}
		return tokensToIndex;
	}

	// private int countPrefix() {
	// int prefixLen = 0;
	// SortedSet<Integer> sorted = cacheHandler.getSorted();
	//
	// for (int nextInt : sorted) {
	// int count = cacheHandler.getCacheLength(nextInt);
	// prefixLen += count * (nextInt + 1);
	// }
	// return prefixLen;
	// }

	private void initCacheHandler(String indexName) throws Exception {
		cacheHandler.loadCache(indexName);

	}

	private void persistRefByName(String indexName,
			Map<Integer, ContentRef> refByName, boolean overwrite)
			throws Exception {
		RefByNamePersist pers = plf.getRefByNamePersist();
		pers.init(plf.getCommonsFactory().getLocalBucketManager());
		pers.persist(refByName, indexName, overwrite);

	}

	private void persistCache(String indexName,
			Map<Integer, Set<String>> sumTokens, boolean delete)
			throws Exception {
		CachePersist pers = plf.getCachePersist();
		pers.init(plf.getCommonsFactory().getLocalBucketManager(), delete);
		pers.persistCache(indexName, sumTokens);
	}

	private List<ContentUri> discover(ContentDiscovery cd) {
		List<ContentUri> ret = null;
		if (cd instanceof ContentDiscoveryFS) {
			// ret = discoverFiles((ContentDiscoveryFS) cd);
			ret = cd.discoverFiles(plf);
		}
		return ret;
	}

	private void initOuptupBucket(boolean delete) {
		plf.getCommonsFactory().getRemoteBucketManager().init(delete);

	}

	private void sumTokensAdd(String nextStr,
			Map<Integer, Set<String>> tokensByLength) {
		if (!tokensByLength.containsKey(nextStr.length())) {
			tokensByLength.put(nextStr.length(), new HashSet<String>());
		}
		tokensByLength.get(nextStr.length()).add(nextStr);

	}

	private Map<String, Set<String>> buildIndex(Map<String, String> encMap,
			Map<String, Tokens> tokenList) throws Exception {
		Map<String, Set<String>> tokensToIndex = new HashMap<String, Set<String>>();

		for (String next : tokenList.keySet()) {
			Tokens tokens = tokenList.get(next);
			Set<String> encTokens = tokens.getEncTokens();
			for (String nextToEnc : tokens.getTokens()) {
				String encTerm = encMap.get(nextToEnc);
				encTokens.add(encTerm);
			}

			tokensToIndex.put(String.valueOf(tokens.getI()), encTokens);
		}
		return tokensToIndex;
	}

}

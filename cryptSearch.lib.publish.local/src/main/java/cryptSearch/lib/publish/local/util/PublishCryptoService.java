package cryptSearch.lib.publish.local.util;

import java.util.Map;
import java.util.Set;

import cryptSearch.service.commons.crypto.CipherKey;

public interface PublishCryptoService {

	CipherKey createCihperKeyBlock(String string) throws Exception;

	byte[] persistEncrypted(String nextPath, CipherKey cbcCipherKey, int i) throws Exception;

	CipherKey createCihperKeyStream(String indexName) throws Exception;

	Map<String, String> encryptIndex(Map<Integer, Set<String>> tokensByLength,
			CipherKey ctrCipherKey, int prefixLength) throws Exception;
	public CipherKey loadStreamKeyStore(String indexName) throws Exception;
	public int getNextContentIndex() throws Exception;

}

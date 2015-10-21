package cryptSearch.lib.publish.local.util;

import java.util.Map;
import java.util.Set;

import cryptSearch.service.commons.crypto.CipherKey;
import cryptSearch.service.commons.crypto.CryptoService;
import cryptSearch.service.commons.util.CommonsFactory;

public class PublishCryptoServiceAes implements PublishCryptoService {

	private CryptoService serv;

	public PublishCryptoServiceAes(CommonsFactory cf) {
		super();
		serv = cf.getCryptoService();
	}

	public CipherKey createCihperKeyBlock(String string) throws Exception {
		return serv.createCihperKeyBlock(string);
	}


	public byte[] persistEncrypted(String nextPath, CipherKey cbcCipherKey, int i) throws Exception {
		return serv.persistEncrypted(nextPath, cbcCipherKey, i);
		
	}

	public CipherKey createCihperKeyStream(String indexName) throws Exception {
		return serv.createCihperKeyStream(indexName);
	}

	public Map<String, String> encryptIndex(
			Map<Integer, Set<String>> tokensByLength, CipherKey ctrCipherKey, int prefixLength) throws Exception {
		return serv.encryptIndex(tokensByLength, ctrCipherKey, prefixLength);
	}

	public CipherKey loadStreamKeyStore(String indexName) throws Exception {
		return serv.loadStreamKeyStore(indexName);	
	}
	
	public int getNextContentIndex() throws Exception{
		return serv.getNextContentIndex();
	}
}

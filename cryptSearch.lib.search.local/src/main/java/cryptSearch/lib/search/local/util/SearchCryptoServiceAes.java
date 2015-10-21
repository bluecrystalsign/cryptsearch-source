package cryptSearch.lib.search.local.util;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.NoSuchPaddingException;

import cryptSearch.service.commons.crypto.CipherKey;
import cryptSearch.service.commons.crypto.CryptoService;
import cryptSearch.service.commons.util.CommonsFactory;

public class SearchCryptoServiceAes implements SearchCryptoService {

	private CryptoService serv;

	public SearchCryptoServiceAes(CommonsFactory cf) {
		super();
		this.serv = cf.getCryptoService();
	}

	public CipherKey loadStreamKeyStore(String indexName) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, Exception {
		return serv.loadStreamKeyStore(indexName);
	}

	public byte[] encrypt(byte[] bytes, CipherKey ck) throws InvalidKeyException, InvalidAlgorithmParameterException, IOException {
		return serv.encrypt(bytes, ck);
	}

	public byte[] decryptBlock(String ksname, byte[] content) throws Exception {
		return serv.decryptBlock(ksname, content);
	}

	public boolean verifyIntegrity(byte[] sha256Orig, byte[] clearContent) throws NoSuchAlgorithmException {
		return serv.verifyIntegrity(sha256Orig, clearContent);
	}

}

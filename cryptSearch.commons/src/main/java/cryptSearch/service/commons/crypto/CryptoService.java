package cryptSearch.service.commons.crypto;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;
import java.util.Set;

import javax.crypto.NoSuchPaddingException;

public interface CryptoService {

	CipherKey createCihperKeyBlock(String string) throws Exception;

	byte[] persistEncrypted(String nextPath, CipherKey cbcCipherKey, int i) throws Exception;

	CipherKey createCihperKeyStream(String indexName) throws Exception;

	Map<String, String> encryptIndex(Map<Integer, Set<String>> tokensByLength,
			CipherKey ctrCipherKey, int prefixLength) throws Exception;
	CipherKey loadStreamKeyStore(String indexName) throws Exception, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException;

	byte[] encrypt(byte[] bytes, CipherKey ck) throws InvalidKeyException, InvalidAlgorithmParameterException, IOException;
	public byte[] decryptBlock(String ksname, byte[] content) throws Exception;
	public byte[] calcSha256(byte[] content) throws NoSuchAlgorithmException;

	boolean verifyIntegrity(byte[] sha256Orig, byte[] clearContent) throws NoSuchAlgorithmException;
	
	public int getNextContentIndex() throws Exception;
}

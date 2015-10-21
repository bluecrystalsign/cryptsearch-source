package cryptSearch.lib.search.local.util;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.NoSuchPaddingException;

import cryptSearch.service.commons.crypto.CipherKey;

public interface SearchCryptoService {

	CipherKey loadStreamKeyStore(String indexName) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, Exception;

	byte[] encrypt(byte[] bytes, CipherKey ck) throws InvalidKeyException, InvalidAlgorithmParameterException, IOException;

	public byte[] decryptBlock(String ksname, byte[] content) throws Exception;

	boolean verifyIntegrity(byte[] sha256Orig, byte[] clearContent) throws NoSuchAlgorithmException;
}
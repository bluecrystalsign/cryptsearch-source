package cryptSearch.service.commons.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.content.RemoteBucketManager;
import cryptSearch.service.commons.persist.KeyPersist;
import cryptSearch.service.commons.persist.KeyPersistXStream;
import cryptSearch.service.commons.util.CommonsFactory;
import cryptSearch.service.commons.util.Converter;

public class CryptoServiceAes implements CryptoService {
	private static final String AES_CBC_PKCS7_PADDING = "AES/CBC/PKCS7Padding";
	private static final String AES_EXT = ".aes";
	private static final int AES_KEY_SIZE = 256;
	private static final String BC = "BC";
	private static final String AES_CTR_NO_PADDING = "AES/CTR/NoPadding";
	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static final String AES = "AES";

	private RemoteBucketManager remoteBucketManager;
	private LocalBucketManager localBucketManager;

	public CryptoServiceAes(RemoteBucketManager remotebucketManager,
			LocalBucketManager localbucketManager) {
		super();
		this.remoteBucketManager = remotebucketManager;
		this.localBucketManager = localbucketManager;
	}

	// public CryptoServiceAes(CommonsFactory factory) {
	// super();
	// remoteBucketManager = factory.getRemoteBucketManager();
	// localBucketManager = factory.getLocalBucketManager();
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.crypto.CryptoService#persistEncrypted(java
	 * .lang.String, cryptSearch.service.commons.crypto.CipherKey, int)
	 */
	public byte[] persistEncrypted(String next, CipherKey cipherKey, int i)
			throws Exception {
		byte[] content = localBucketManager.loadContent(next);

		SecretKeySpec key = new SecretKeySpec(cipherKey.getSecretKey()
				.getEncoded(), AES);
		byte[] encContent = encrypt(content, key, cipherKey.getIvSpec(),
				cipherKey.getCipher());

		remoteBucketManager.storeEncrypted(i, encContent);
		
		return calcSha256(content);
	}

	public int getNextContentIndex(){
		return remoteBucketManager.getNextContentIndex();
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.crypto.CryptoService#encryptIndex(java.util
	 * .Map, cryptSearch.service.commons.crypto.CipherKey)
	 */
	public Map<String, String> encryptIndex(Map<Integer, Set<String>> map,
			CipherKey cipherKey, int prefixLen) throws Exception {
		Map<String, String> ret = new HashMap<String, String>();
		SortedSet<Integer> setInt = new TreeSet<Integer>();
		for (Integer nextInt : map.keySet()) {
			setInt.add(nextInt);
		}

		String clear = appendWithSpaces(map, setInt);
		String[] listOfTokens = clear.split(" ");
		clear = addPrefix(prefixLen) + clear;
		System.out.println("["+clear+"]");
		System.out.println("["+ Converter.bytesToHex(clear.getBytes())+"]"+clear.length());
		byte[] encWordWithPrefix = encrypt(clear.getBytes(), cipherKey);
		System.out.println("["+ Converter.bytesToHex(encWordWithPrefix)+"]"+encWordWithPrefix.length);
		byte[] encWord = removePrefix(encWordWithPrefix, prefixLen);
		System.out.println("["+ Converter.bytesToHex(encWord)+"]"+encWord.length);
		int prevSize = 0;
		for (int i = 0; i < listOfTokens.length; i++) {
			int wordSize = listOfTokens[i].length();
			byte[] b = new byte[wordSize];
			for (int j = 0; j < b.length; j++) {
				b[j] = encWord[prevSize + j];
			}
			prevSize += (wordSize + 1);
			ret.put(listOfTokens[i], bytesToHex(b));
		}

		return ret;
	}

	private byte[] removePrefix(byte[] encWordWithPrefix, int prefixLen) {
		int encWordLenWithPref = encWordWithPrefix.length;
		byte[] encWord = new  byte[encWordLenWithPref-prefixLen];
		for(int i = 0; i < encWord.length; i++ ){
			int prefI = prefixLen+i;
			encWord[i] = encWordWithPrefix[prefI];
		}
		return encWord;
	}

	private String addPrefix(int prefixLen) {
		char [] base = new char[prefixLen];
		
		int pos = 0;
	    while (pos < base.length) {
	        base[pos] = ' ';
	        pos++;
	    }
		return new String(base);
	}

	public byte[] calcSha256(byte[] content) throws NoSuchAlgorithmException {
		String algorithm = "SHA256";
		return calcSha(content, algorithm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.crypto.CryptoService#createCihperKeyStream
	 * (java.lang.String)
	 */
	public CipherKey createCihperKeyStream(String args) throws Exception {
		String filePath = args + AES_EXT;

		SecretKey secretKey = createSecretKey();

		byte[] ivBytes = createIvBytes();

		storeKey(filePath, secretKey, ivBytes, AES_CTR_NO_PADDING);

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = createAesStream();
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		CipherKey ck = new CipherKeyImpl(secretKey, ivSpec, cipher);

		return ck;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cryptSearch.service.commons.crypto.CryptoService#createBlock()
	 */
	public Cipher createBlock() throws NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException {
		return Cipher.getInstance(AES_CBC_PKCS7_PADDING, BC);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.crypto.CryptoService#createCihperKeyBlock
	 * (java.lang.String)
	 */
	public CipherKey createCihperKeyBlock(String args) throws Exception {
		String filePath = args + AES_EXT;

		SecretKey secretKey = createSecretKey();

		byte[] ivBytes = createIvBytes();

		storeKey(filePath, secretKey, ivBytes, AES_CBC_PKCS7_PADDING);

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = createBlock();
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		CipherKey ck = new CipherKeyImpl(secretKey, ivSpec, cipher);

		return ck;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cryptSearch.service.commons.crypto.CryptoService#createAesStream()
	 */
	public Cipher createAesStream() throws NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException {
		return Cipher.getInstance(AES_CTR_NO_PADDING, BC);
	}

	private void storeKey(String filePath, SecretKey secretKey, byte[] ivBytes,
			String keyType) throws Exception {
		byte[] keyBytes = secretKey.getEncoded();
		KeyStore ks = new KeyStoreImpl(keyBytes, ivBytes, keyType);
		KeyPersist kp = new KeyPersistXStream();
		kp.init(localBucketManager);
		kp.persist(ks, filePath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.crypto.CryptoService#loadStreamKeyStore(java
	 * .lang.String, java.lang.String)
	 */
	public CipherKey loadStreamKeyStore(String indexName) throws Exception,
			NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException {
		CipherKey cipherKey;
		KeyPersist kp = new KeyPersistXStream();
		kp.init(localBucketManager);
		KeyStore ks = kp.load(indexName + ".aes");
		SecretKey secretKey = new SecretKeySpec(ks.getK(), AES);
		SecretKeySpec key = new SecretKeySpec(secretKey.getEncoded(), AES);
		IvParameterSpec ivSpec = new IvParameterSpec(ks.getI());
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = createAesStream();

		cipherKey = new CipherKeyImpl(secretKey, ivSpec, cipher);
		return cipherKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.crypto.CryptoService#decryptBlock(java.lang
	 * .String, byte[])
	 */
	public byte[] decryptBlock(String ksname, byte[] content) throws Exception {
		CipherKey ck = loadBlockKeyStore(ksname);
		return decrypt(content, ck);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cryptSearch.service.commons.crypto.CryptoService#loadBlockKeyStore(java
	 * .lang.String)
	 */
	public CipherKey loadBlockKeyStore(String ksname) throws Exception,
			NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException {
		CipherKey cipherKey;
		KeyPersist kp = new KeyPersistXStream();
		kp.init(localBucketManager);
		KeyStore ks = kp.load(ksname);
		SecretKey secretKey = new SecretKeySpec(ks.getK(), AES);
		IvParameterSpec ivSpec = new IvParameterSpec(ks.getI());
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = createBlock();

		cipherKey = new CipherKeyImpl(secretKey, ivSpec, cipher);
		return cipherKey;
	}

	private String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	private byte[] createIvBytes() {
		byte[] ivBytes;
		ivBytes = new byte[16];
		SecureRandom sr = new SecureRandom();
		sr.nextBytes(ivBytes);
		return ivBytes;
	}

	private SecretKey createSecretKey() throws NoSuchAlgorithmException {
		SecretKey secretKey;
		KeyGenerator keyGen = KeyGenerator.getInstance(AES);
		keyGen.init(AES_KEY_SIZE);
		secretKey = keyGen.generateKey();
		return secretKey;
	}

	private byte[] decrypt(SecretKeySpec key, IvParameterSpec ivSpec,
			Cipher cipher, byte[] cipherText) throws InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		ByteArrayOutputStream bOut;
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

		bOut = new ByteArrayOutputStream();
		CipherOutputStream cOut = new CipherOutputStream(bOut, cipher);
		cOut.write(cipherText);
		cOut.close();
		byte[] outBytes = bOut.toByteArray();
		bOut.close();
		return outBytes;
	}

	private String appendWithSpaces(Map<Integer, Set<String>> map,
			SortedSet<Integer> setInt) {
		String ret = "";
		Iterator<Integer> iterator = setInt.iterator();
		StringBuffer sb = new StringBuffer();
		while (iterator.hasNext()) {
			Integer nextInt = iterator.next();
			Set<String> nextSet = map.get(nextInt);
			if (!map.isEmpty()) {
				// int len = map.size() * (nextSet.iterator().next().length() +
				// 1);

				for (String next : nextSet) {
					sb.append(next);
					sb.append(" ");
				}
			}
		}
		ret = sb.toString();
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cryptSearch.service.commons.crypto.CryptoService#encrypt(byte[],
	 * cryptSearch.service.commons.crypto.CipherKey)
	 */
	public byte[] encrypt(byte[] input, CipherKey cipherKey)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			IOException {
		SecretKeySpec key = new SecretKeySpec(cipherKey.getSecretKey()
				.getEncoded(), AES);
		return encrypt(input, key, cipherKey.getIvSpec(), cipherKey.getCipher());
	}

	private byte[] decrypt(byte[] input, CipherKey cipherKey)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			IOException {
		SecretKeySpec key = new SecretKeySpec(cipherKey.getSecretKey()
				.getEncoded(), AES);
		return decrypt(key, cipherKey.getIvSpec(), cipherKey.getCipher(), input);
	}

	// private synchronized byte[] encrypt(byte[] input, SecretKeySpec key,
	// IvParameterSpec ivSpec, Cipher cipher) throws InvalidKeyException,
	// InvalidAlgorithmParameterException, IOException {
	private byte[] encrypt(byte[] input, SecretKeySpec key,
			IvParameterSpec ivSpec, Cipher cipher) throws InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		byte[] cipherText;
		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		ByteArrayInputStream bIn = new ByteArrayInputStream(input);
		CipherInputStream cIn = new CipherInputStream(bIn, cipher);
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		int i = 0;
		try {
			int ch;

			while ((ch = cIn.read()) >= 0) {

				bOut.write(ch);
				i++;
			}
		} catch (Exception e) {
			System.out.println(i + "/" + input.length);
			e.printStackTrace();
			System.exit(0);
		}

		cipherText = bOut.toByteArray();
		cIn.close();
		return cipherText;
	}

	private byte[] calcSha(byte[] content, String algorithm)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.reset();
		md.update(content);

		byte[] output = md.digest();
		return output;
	}

	public boolean verifyIntegrity(byte[] sha256Orig, byte[] content) throws NoSuchAlgorithmException {
		byte[] sha256 = calcSha256(content);
		return Arrays.equals(sha256, sha256Orig);
	}
}

package cryptSearch.service.commons.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CipherKeyImpl implements CipherKey {
	private SecretKey secretKey;
	private IvParameterSpec ivSpec;
	private Cipher cipher;
	/* (non-Javadoc)
	 * @see cryptSearch.service.commons.crypto.CipherKey#getSecretKey()
	 */
	public SecretKey getSecretKey() {
		return secretKey;
	}
	/* (non-Javadoc)
	 * @see cryptSearch.service.commons.crypto.CipherKey#getIvSpec()
	 */
	public IvParameterSpec getIvSpec() {
		return ivSpec;
	}
	/* (non-Javadoc)
	 * @see cryptSearch.service.commons.crypto.CipherKey#getCipher()
	 */
	public Cipher getCipher() {
		return cipher;
	}
	public CipherKeyImpl(SecretKey secretKey, IvParameterSpec ivSpec, Cipher cipher) {
		super();
		this.secretKey = secretKey;
		this.ivSpec = ivSpec;
		this.cipher = cipher;
	}

}

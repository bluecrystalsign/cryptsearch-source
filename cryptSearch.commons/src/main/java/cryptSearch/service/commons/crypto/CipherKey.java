package cryptSearch.service.commons.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public interface CipherKey {

	public abstract SecretKey getSecretKey();

	public abstract IvParameterSpec getIvSpec();

	public abstract Cipher getCipher();

}
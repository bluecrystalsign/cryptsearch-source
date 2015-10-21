package cryptSearch.service.commons.crypto;

public class KeyStoreImpl implements KeyStore {
	private byte[] k;
	private byte[] i;
	private String t;
	/* (non-Javadoc)
	 * @see cryptSearch.service.commons.crypto.KeyStore#getK()
	 */
	public byte[] getK() {
		return k;
	}
	/* (non-Javadoc)
	 * @see cryptSearch.service.commons.crypto.KeyStore#getI()
	 */
	public byte[] getI() {
		return i;
	}
	
	/* (non-Javadoc)
	 * @see cryptSearch.service.commons.crypto.KeyStore#getT()
	 */
	public String getT() {
		return t;
	}
	public KeyStoreImpl() {
		super();
	}
	public KeyStoreImpl(byte[] k, byte[] i, String t) {
		super();
		this.k = k;
		this.i = i;
		this.t = t;
	}



}

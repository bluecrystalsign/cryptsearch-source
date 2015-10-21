package cryptSearch.service.commons.persist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.crypto.KeyStore;
import cryptSearch.service.commons.crypto.KeyStoreImpl;

public class KeyPersistXStream implements KeyPersist {
	private XStream xs;
	private LocalBucketManager bucket;

	public void init(LocalBucketManager bucket){
		xs = new XStream(new StaxDriver());
		xs.alias("ks", KeyStoreImpl.class);
		this.bucket = bucket;
	}
	
	private String marshall(KeyStore ks){
		return xs.toXML(ks);
	}
	
	private KeyStore unmarshall(String s){
		return (KeyStore) xs.fromXML(s);
	}

	public void persist(KeyStore ks, String filePath) throws Exception {
		OutputStream os = new FileOutputStream(bucket.getKsFile(filePath));
		os.write(marshall(ks).getBytes());
		os.close();
		
	}

	public KeyStore load(String f)  throws Exception {
		
		File ksFile = bucket.getKsFile(f);
		InputStream is = new FileInputStream(ksFile.getAbsolutePath());
		byte[] b = new byte[is.available()];
		is.read(b);
		return unmarshall(new String(b));
	}
}

package cryptSearch.service.commons.persist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.domain.ContentRef;

public class RefByNamePersistXStream implements RefByNamePersist {
	private XStream xs;
	private LocalBucketManager bucket;

	public void init(LocalBucketManager bucket) {
		xs = new XStream(new StaxDriver());
		this.bucket = bucket;
		
	}

	public void persist(Map<Integer, ContentRef> refByName, String name, boolean overwrite) throws Exception {
		if(!overwrite){
			Map<Integer, ContentRef> refByNameOld = load(name);
			refByName.putAll(refByNameOld);
		}
		File refByNameFile = bucket.getRefByNameFile(name);
		OutputStream os = new FileOutputStream(refByNameFile);
		byte[] bytes = marshall(refByName).getBytes();
		os.write(bytes);
		os.close();
		
	}
	
	public Map<Integer, ContentRef> load(String name)  throws Exception {
		File refByNameFile = bucket.getRefByNameFile(name);
		InputStream is = new FileInputStream(refByNameFile);
		byte[] b = new byte[is.available()];
		is.read(b);
		return unmarshall(new String(b));
	}
	
	private Map<Integer, ContentRef> unmarshall(String s){
		return (Map<Integer, ContentRef>) xs.fromXML(s);
	}
	
	private String marshall(Map<Integer, ContentRef> refByName){
		return xs.toXML(refByName);
	}

}

package cryptSearch.lib.publish.local.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.Tika;

public class ContentLoaderFileSystem implements ContentLoader{

	public String parseContentToString(String res) throws Exception {
		InputStream stream = new FileInputStream(new File(res));
		Tika tika = new Tika();
		try {
			return tika.parseToString(stream);
		} finally {
			stream.close();
		}
	}

}

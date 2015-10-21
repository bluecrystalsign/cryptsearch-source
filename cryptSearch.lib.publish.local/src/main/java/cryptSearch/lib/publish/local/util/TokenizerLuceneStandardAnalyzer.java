package cryptSearch.lib.publish.local.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class TokenizerLuceneStandardAnalyzer implements Tokenizer {

	/* (non-Javadoc)
	 * @see cryptSearch.lib.publish.local.util.Tokenizer#tokenizeString(java.lang.String)
	 */
	public Set<String> tokenizeString(String string) {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
	    Set<String> result = new HashSet<String>();
	    try {
	      TokenStream stream  = analyzer.tokenStream(null, new StringReader(string));
	      stream.reset();
	      while (stream.incrementToken()) {
	        result.add(stream.getAttribute(CharTermAttribute.class).toString());
	        
	      }
	    } catch (IOException e) {
	      // not thrown b/c we're using a string reader...
	      throw new RuntimeException(e);
	    }
	    return result;
	  }
}

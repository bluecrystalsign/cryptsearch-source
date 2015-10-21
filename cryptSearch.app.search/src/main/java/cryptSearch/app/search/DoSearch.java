package cryptSearch.app.search;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cryptSearch.lib.search.local.util.SearchCryptoService;
import cryptSearch.lib.search.local.util.SearchLocalFactory;
import cryptSearch.lib.search.local.util.SearchLocalFactoryImpl;
import cryptSearch.lib.search.local.worker.SearchLocalWorker;
import cryptSearch.lib.search.local.worker.SearchLocalWorkerImpl;
import cryptSearch.lib.search.remote.util.SearchRemoteFactory;
import cryptSearch.lib.search.remote.util.SearchRemoteFactoryImpl;
import cryptSearch.lib.search.remote.worker.SearchRemoteWorker;
import cryptSearch.lib.search.remote.worker.SearchRemoteWorkerImpl;
import cryptSearch.service.commons.content.LocalBucketManager;
import cryptSearch.service.commons.content.RemoteBucketManager;
import cryptSearch.service.commons.domain.ContentRef;
import cryptSearch.service.commons.util.CommonsFactory;
import cryptSearch.service.commons.util.CommonsFactoryImpl;
import cryptSearch.service.commons.util.Timelapse;

public class DoSearch {

	private SearchLocalFactory searchLocalFactory;
	private SearchRemoteFactory searchRemoteFactory;
	private CommonsFactory cf;

	public DoSearch() {
		super();
		searchLocalFactory = SearchLocalFactoryImpl.getInstance();
		cf = CommonsFactoryImpl.getInstance();
		searchRemoteFactory = SearchRemoteFactoryImpl.getInstance(); 
	}

	public static void main(String[] args) {
		(new DoSearch()).start(args);

	}

	private void start(String[] args) {
		String indexName = args[1];
		
		SearchRemoteWorker rs = new SearchRemoteWorkerImpl(searchRemoteFactory, indexName);
		
		try {
			if (args[0].compareTo("-dump") == 0) {
				String termsCount = args[2];
				System.out.println("Index dump. Mostrar " + indexName
						+ " termos por entrada do indice " + termsCount);

				Map<String, List<String>> hitList = rs.indexDump(indexName, termsCount);
				for(String next : hitList.keySet()){
					System.out.println("\nURL: "+next);
					List<String> hit = hitList.get(next);
					for(String next2 : hit){
						System.out.print(next2+ " ");
					}
				}
				System.out.println("");
				
			} else {
				String term = args[0];
				execSearch(term, indexName, rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private  void execSearch(String term, String indexName, SearchRemoteWorker rsw) {
		try {
			SearchLocalWorker lsw = new SearchLocalWorkerImpl(searchLocalFactory );

			Timelapse stepTime = new Timelapse();
			stepTime.print("Inicio da busca: " + term);
			Set<String> encryptTerm = lsw.encryptTerm(term, indexName);
			System.out.println("Termo criptografado gerado");
			for(String next : encryptTerm){
				System.out.println("["+encryptTerm+"]");
				
			}

			stepTime.print("Busca passo 2 com " + encryptTerm.size()
					+ " termos");

			RemoteBucketManager bucket = searchRemoteFactory.getRemoteBucketManager();
			
			List<String> hitsList = rsw.searchEncryptedIndex(indexName,
					encryptTerm, bucket);

			decrypt(rsw, hitsList, indexName);
			
			stepTime.print("Fim da busca");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private  void decrypt(SearchRemoteWorker rsw, List<String> hitsList, String indexName) throws Exception {
		System.out.println("Decriptando...");
						
		LocalBucketManager lb = cf.getLocalBucketManager();
		lb.deleteClearContent();
		Map<Integer, ContentRef> refByName = lb.loadRefByName(indexName);
		for(String next : hitsList){
			byte[] clearContent = loadClearContent(rsw, indexName, next, lb);
			
			Integer ndx = Integer.valueOf(next);
			ContentRef contentRef = refByName.get(ndx);
			byte[] sha256Orig = contentRef.getSha256();
			if (verifyIntegrity(sha256Orig, clearContent)) {

			lb.saveClearContent(contentRef, clearContent);
			} else {
				throw new Exception("Content integrity violated: ("+ndx+") "+contentRef.getName());
			}
		}
		
	}

	private boolean verifyIntegrity(byte[] sha256Orig, byte[] clearContent) throws NoSuchAlgorithmException {
		SearchCryptoService cryptoService = searchLocalFactory.getCryptoService();
		return cryptoService.verifyIntegrity(sha256Orig, clearContent);
	}

	private  byte[] loadClearContent(SearchRemoteWorker rsw, String indexName, String next, LocalBucketManager lb)
			throws Exception {
		byte[] dataContent = rsw.loadEncryptedContent(next);
		byte[] clearContent = decryptContent(indexName, next, dataContent, lb);
		return clearContent;
	}

	private  byte[] decryptContent(String indexName, String next,
			byte[] dataContent, LocalBucketManager lb) throws Exception {
//		File ksFile = lb.getKsFile(indexName+ "_"+ next+ ".aes");
		
		SearchCryptoService cryptoService = searchLocalFactory.getCryptoService();
//		byte[] clearContent = cryptoService.decryptBlock(ksFile.getAbsolutePath(), dataContent);
		byte[] clearContent = cryptoService.decryptBlock(indexName+ "_"+ next+ ".aes", dataContent);
		return clearContent;
	}
}

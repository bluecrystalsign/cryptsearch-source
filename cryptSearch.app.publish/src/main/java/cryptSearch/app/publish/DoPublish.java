package cryptSearch.app.publish;

import java.io.File;
import java.util.Map;
import java.util.Set;

import cryptSearch.lib.publish.local.util.PublishLocalFactory;
import cryptSearch.lib.publish.local.util.PublishLocalFactoryImpl;
import cryptSearch.lib.publish.local.worker.PublishLocalWorker;
import cryptSearch.lib.publish.local.worker.PublishLocalWorkerImpl;
import cryptSearch.lib.publish.remote.util.PublishRemoteFactory;
import cryptSearch.lib.publish.remote.util.PublishRemoteFactoryImpl;
import cryptSearch.lib.publish.remote.worker.PublishRemoteWorker;
import cryptSearch.lib.publish.remote.worker.PublishRemoteWorkerImpl;
import cryptSearch.service.commons.util.CacheHandler;
import cryptSearch.service.commons.util.CacheHandlerImpl;
import cryptSearch.service.commons.util.Timelapse;

public class DoPublish {

	public DoPublish() {
		super();
	}

	public static void main(String[] args) {
		System.out.println("*** Inicio da execução");
		try {
			if(args[0].compareTo("-update")==0){
				new DoPublish().update(args);
			} else {
				new DoPublish().create(args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("*** Fim da execução");

	}

	private void update(String[] args) throws Exception {
		System.out.println("Atualizando o indice");
		Timelapse totalTime = new Timelapse();
		if (invalidParemsUpdate(args)) {
			printUsage(args);
			return;
		}

		String[] filters = { "pdf", "doc", "xls", "ppt" , "docx"};
		String[] initialPath = { args[args.length - 1] };

		String indexName = args[1];

		PublishLocalFactory plf = PublishLocalFactoryImpl.getInstance();
		CacheHandler cacheHandler = new CacheHandlerImpl(plf.getCachePersist(), 
				plf.getLocalBucketManager(), false);
		
		PublishLocalWorker plw = new PublishLocalWorkerImpl(plf, initialPath, filters, cacheHandler);
		Map<String, Set<String>> tokensToIndex = plw.createEncryptedTokensToUpdate(indexName);

		PublishRemoteFactory prf = PublishRemoteFactoryImpl.getInstance();
		PublishRemoteWorker prw = new PublishRemoteWorkerImpl(prf);
		prw.initIndex(indexName, false);
		for (String contentRefence : tokensToIndex.keySet()) {
			prw.updateIndex(contentRefence, tokensToIndex.get(contentRefence));
		}

		prw.finish();
		
		
		totalTime.print("Tempo total");

	}

	private void create(String[] args) throws Exception {
		Timelapse totalTime = new Timelapse();
		if (invalidParemsCreate(args)) {
			printUsage(args);
			return;
		}
		String[] filters = { "pdf", "doc", "xls", "ppt" , "docx"};
		String[] initialPath = { args[args.length - 1] };

		String indexName = args[0];

		PublishLocalFactory plf = PublishLocalFactoryImpl.getInstance();
		
		PublishLocalWorker plw = new PublishLocalWorkerImpl(plf, initialPath, filters, null);
		Map<String, Set<String>> tokensToIndex = plw.createEncryptedTokens(indexName);

		PublishRemoteFactory prf = PublishRemoteFactoryImpl.getInstance();
		PublishRemoteWorker prw = new PublishRemoteWorkerImpl(prf);
		prw.initIndex(indexName, true);
		for (String contentRefence : tokensToIndex.keySet()) {
			prw.addDocToIndex(contentRefence, tokensToIndex.get(contentRefence));
		}

		prw.finish();

		totalTime.print("Tempo total");

	}

	private boolean invalidParemsCreate(String[] args) {
		if (args.length >= 2) {
			File f = new File(args[args.length - 1]);
			return !(f.exists() && f.isDirectory() && f.canRead());
		}
		return true;
	}

	private boolean invalidParemsUpdate(String[] args) {
		if (args.length >= 3) {
			File f = new File(args[args.length - 1]);
			return !(f.exists() && f.isDirectory() && f.canRead());
		}
		return true;
	}

	private void printUsage(String[] args) {
		System.out.println("Execute da linha de comando");
		System.out
				.println("Criando uma novo indice passar como parâmetro o nome do indice e o diretório que deve ser indexado.");
		System.out
		.println("Atualizando um indice passar como parâmetro \"-update\",  o nome do indice e o diretório que deve ser indexado.");
		for (String next : args) {
			System.out.println(">> " + next);
		}

	}

}

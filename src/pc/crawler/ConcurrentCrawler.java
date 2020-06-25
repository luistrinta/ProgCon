package pc.crawler;


import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

import pc.util.UnexpectedException;



/**
 * Concurrent crawler.
 *
 */
public class ConcurrentCrawler extends SequentialCrawler {
	
	private AtomicInteger counter = new AtomicInteger();
//  // usamos este tipo de Set por ser "thread safe" visto que utiliza um HashMap Concorrewnte de forma a nao perder/alterar indevidamente informações ao ser realizado o fork
	private  Set<String> map = ConcurrentHashMap.newKeySet();
	
  public static void main(String[] args) throws IOException {
    int threads = args.length > 0 ?  Integer.parseInt(args[0]) : 16;
    String url = args.length > 1 ? args[1] : "http://localhost:8123";
    ConcurrentCrawler cc = new ConcurrentCrawler(threads);
    cc.setVerboseOutput(false);
    cc.crawl(url);
    cc.stop();
  }

 
 
  
  /**
   * The fork-join pool.
   */
  private final ForkJoinPool pool;

  /**
   * Constructor.
   * @param threads number of threads.
   * @throws IOException if an I/O error occurs
   */
  public ConcurrentCrawler(int threads) throws IOException {
    pool = new ForkJoinPool(threads);
  }

  @Override
  public void crawl(String root) {
    long t = System.currentTimeMillis();
    log("Starting at %s", root);
    pool.invoke(new TransferTask(0, root));
    t = System.currentTimeMillis() - t;
    log("Done "+counter.get()+" transfers in %d ms" ,t);
  }

  /**
   * Stop the crawler.
   */
  public void stop() {
    pool.shutdown();
  }
  
  @SuppressWarnings("serial")
  private class TransferTask extends RecursiveTask<Void> {

    final int rid;
    final String path;
    TransferTask(int rid, String path) {
	this.rid = rid;
      this.path = path;
    }

    @Override
    protected Void compute() {
      try {	
        	
    	  URL url = new URL(path);
    		  List<String>  links = performTransfer(rid,url);
    		 List<RecursiveTask<Void>> tasks = new LinkedList<>();	 
        for(String link : links) {
        	String newURL = new URL(url, new URL(url,link).getPath()).toString();
        	
        	if(!map.contains(newURL)){
        		if(map.add(newURL)) {
        		TransferTask  t = new TransferTask(counter.getAndIncrement(),newURL); 
        		tasks.add(t);
        		t.fork();
        		}
        	}
        }
        
      for(RecursiveTask<Void> t : tasks) {
   	   t.join();
       }
       
      } 
      catch (Exception e) {
        throw new UnexpectedException(e);
      }
      return null;
    }

  }
}

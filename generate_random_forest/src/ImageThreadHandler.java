import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Manages all image loading threads
 * @author Admin
 *
 */
public class ImageThreadHandler{

	private int threadNumber;
	private Queue<String> queue;
	private ArrayList<Thread> threads;
	//cache will store offsets for each pixel to help with speed
	OffsetCache cache;

	public ImageThreadHandler(int threadNumber, Queue<String> queue){
		this.threadNumber = threadNumber;
		this.queue = queue;
		this.cache = new OffsetCache();
		this.threads = new ArrayList<Thread>();
	}
	
	/**
	 * Spawns all threads to load images
	 * 
	 */
	public void startThreads(){
		for(int i=0; i<this.threadNumber; i++){
			threads.add(new ImageThread(queue, cache, i));
		}
		
		for(Thread t : threads){
			t.start();
		}
		
		for(Thread t : threads){
			try {
				t.join();
			} catch (InterruptedException e) {
				System.out.println("Image Reading Threads Failed");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	

}

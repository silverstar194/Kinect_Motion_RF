import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Admin
 *
 */
public class MainLoadImages {
	
	/**
	 * Reads in files for calc. their attributes
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */

	public static void main(String[] args) throws FileNotFoundException{
		
		//init. mapping of (r, g, b) to body parts
		MasterConstants.initMap();
		DataHandler getImages = new DataHandler();
		getImages.listFilesAndFilesSubDirectories(MasterConstants.imageDirectory+"/random", MasterConstants.FILETYPE, "c");
		
		
		Queue<String> queue = new ConcurrentLinkedQueue<String>();
		queue.addAll(getImages.imageFiles);
		ImageThreadHandler loadImagesOnThread = new ImageThreadHandler(MasterConstants.CORES*4, queue);
		loadImagesOnThread.startThreads();

	}

}


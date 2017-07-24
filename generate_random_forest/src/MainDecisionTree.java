import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Admin
 *
 */
public class MainDecisionTree {
	
	public static void main(String[] args){
		//init. mapping of (r, g, b) to body parts
		MasterConstants.initMap();
		DataHandler getImages = new DataHandler();
		getImages.listFilesAndFilesSubDirectories(MasterConstants.imageDirectory+"/random", MasterConstants.FILETYPE, "c", MasterConstants.EVERY_NTH_FRAME);
		
		
		Queue<String> queue = new ConcurrentLinkedQueue<String>();
		queue.addAll(getImages.imageFiles);
		ImageThreadHandler loadImagesOnThread = new ImageThreadHandler(MasterConstants.CORES*4, queue);
		loadImagesOnThread.startThreads();
		

		//build tree for belly vs all
		DataSet dataTrain = new DataSet(MasterConstants.GLOBAL_IMAGES, MasterConstants.HEAD);
		
		DecisionTree tree = new DecisionTree(dataTrain, dataTrain);
		
		
		
		
	}

}

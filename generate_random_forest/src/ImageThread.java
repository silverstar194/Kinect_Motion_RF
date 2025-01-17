import java.util.Queue;

/**
 * @author Admin
 *
 */
public class ImageThread extends Thread{
	
	private Queue<String> queue;
	private OffsetCache cache;
	private int threadNumber;
	public ImageThread(Queue<String> queue, OffsetCache cache, int threadNumber){
		this.cache = cache;
		this.queue = queue;
		this.threadNumber = threadNumber;
	}

	/**
	 * Handles creating running and destroying a image loading thread
	 * 
	 */
	private void runImageThread(){
		//runs until queue is empty
		while(true){
		
			String colorImagePath = queue.poll();

			//queue in empty, all images loaded
			if(colorImagePath == null){
				return;
			}

			String depthImagePath = colorImagePath.replace("_c_", "_d_");
			depthImagePath = depthImagePath.replace("color", "depth");

			Image image = new Image(colorImagePath, depthImagePath, cache);
			image.loadImage();
			image.calculateImageAttributes();
//			image.outputImageToDebug();

			//release memory for image data as we have computes attributes already
			image.unloadFrameImages();

			//stores image to master list for analysis
			MasterConstants.GLOBAL_IMAGES.add(image);
			System.out.println("File "+colorImagePath+ " on Thread #"+threadNumber + " Pixels: "+image.attributes.size());
//			image.printBodyParts();
		}

	}

	/**
	 * Required method to start thread
	 * 
	 */

	public void run(){
		runImageThread();
	}

}

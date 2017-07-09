import java.io.FileNotFoundException;

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
		
		DataHandler getImages = new DataHandler();
		
		//cache will store offsets for each pixel to help with speed
		OffsetCache cache = new OffsetCache();
		
		int count = 0;
		
		getImages.listFilesAndFilesSubDirectories(MasterConstants.imageDirectory+"/random", MasterConstants.FILETYPE, "c");
		for(String e : getImages.imageFiles){
			//computes depth and color paths
			String colorImagePath = e;
			String depthImagePath = e.replace("_c_", "_d_");
			depthImagePath = depthImagePath.replace("color", "depth");


			Image image = new Image(colorImagePath, depthImagePath, cache);
			image.loadImage();
			image.calculateImageAttributes();
			
			//release memory for image data as we have computes attributes already
			image.unloadFrameImages();

			//stores image to master list for analysis
			MasterConstants.images.put(e.substring(e.length()-"120_17_Male_normal_trimmed/depth/120_17_d_0001.png".length(), e.length()).replace("/", ""), image);
			System.out.println("Files in #"+count++);
		}

	}

}


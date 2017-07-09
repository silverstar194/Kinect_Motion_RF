import java.util.HashMap;

/**
 * @author Admin
 *
 */
public class MasterConstants {

	//This defines all images paths we can draw from for data
	static final HashMap<String, Image> images = new HashMap<>();

	//This is the root directory that holds images
	static final String imageDirectory = "/Users/Admin/Desktop/TestKinect/sample_trimmed";

	//This helps with speed by setting ArrayList to large size to start with
	static final int ESTIMATED_IMAGE_AMOUNT = 1000;

	//Image type for validation
	static final String FILETYPE = "png";

	//Number of attributes for each pixel
	static final int NUM_ATTRIBUTES = 30;

	//This holds the pattern in which attributes are calculated from
	//It is a radial pattern
	static int[][][][][] ATTRIBUTES_DIFF;

	//width of image
	static final int IMG_WIDTH = 640;
	
	//height of image
	static final int IMG_HEIGHT = 480;
	
	//how many attributes in each ring
	static final int NUM_PER_RING = 5;
	
	//how many pixels away first ring should be
	static final int INNER_RING = 7;
	
	//how many pixels are rings apart
	static final int INCR_RING = 7;
	
	//how many pixels away final ring it
	static final int OUTER_RING = 42;
	
	
}

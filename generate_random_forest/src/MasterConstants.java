import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Admin
 *
 */
public class MasterConstants {

	//This defines all images paths we can draw from for data
	static final Queue<Image> GLOBAL_IMAGES = new ConcurrentLinkedQueue<>();


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

	static final HashMap<Integer, Short> MAP_COLORS_TO_PARTS = new HashMap<>();
	
	static int CORES = Runtime.getRuntime().availableProcessors();

	/**
	 * This maps tuples to values for body parts
	 * 
	 * (254, 254, 0)  (234,234,0) (212,212,0) == left leg -> 1
	 * (127,0,0) (117,0,0) (105,0,0) (234,0,0) == right leg -> 2
	 * (126,0,254) (116,0,254) == right shoulder -> 3
	 * (254,127,0) (239,234,0)== belly -> 4
	 * (254,0,0) == head -> 5
	 * (0,254,0) (0,234,0) (0 212 0) == right arm -> 6
	 * (0,0,254) (0,0,234) (239,234,0) == left arm -> 7
	 */
	public static void initMap(){
		
		//background
		MAP_COLORS_TO_PARTS.put(0, (short)0);
		
		//left leg
		MAP_COLORS_TO_PARTS.put(2542540, (short)0x800);
		MAP_COLORS_TO_PARTS.put(2342340, (short)0x800);
		MAP_COLORS_TO_PARTS.put(2122120, (short)0x800);
		
		//right leg
		MAP_COLORS_TO_PARTS.put(12700, (short)0x1000);
		MAP_COLORS_TO_PARTS.put(11700, (short)0x1000);
		MAP_COLORS_TO_PARTS.put(10500, (short)0x1000);
		MAP_COLORS_TO_PARTS.put(23400, (short)0x1000);
		
		//right shoulder
		MAP_COLORS_TO_PARTS.put(1260254, (short)0x1800);
		MAP_COLORS_TO_PARTS.put(1160254, (short)0x1800);
		
		//belly
		MAP_COLORS_TO_PARTS.put(2541270, (short)0x2000);
		MAP_COLORS_TO_PARTS.put(2392340, (short)0x2000);
		
		//head
		MAP_COLORS_TO_PARTS.put(25400, (short)0x2800);
		
		//right arm
		MAP_COLORS_TO_PARTS.put(2540, (short)0x3000);
		MAP_COLORS_TO_PARTS.put(2340, (short)0x3000);
		MAP_COLORS_TO_PARTS.put(2120, (short)0x3000);
		
		//left arm
		MAP_COLORS_TO_PARTS.put(254, (short)0x3800);
		MAP_COLORS_TO_PARTS.put(234, (short)0x3800);
		MAP_COLORS_TO_PARTS.put(2392340, (short)0x3800);
		
	}



}

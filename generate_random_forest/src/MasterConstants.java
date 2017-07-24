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
	static final String imageDirectory = "/Users/Admin/Desktop/TestKinect/sample/";

	//This helps with speed by setting ArrayList to large size to start with
	static final int ESTIMATED_IMAGE_AMOUNT = 1000;

	//Image type for validation
	static final String FILETYPE = "png";

	//Number of attributes for each pixel
	static final int NUM_ATTRIBUTES = 30;

	//This holds the pattern in which attributes are calculated from
	//It is a radial pattern
	static int[][][][][] ATTRIBUTES_DIFF;

	//width of image untrimmed
	static int IMG_WIDTH = 640;

	//height of image untrimmed
	static int IMG_HEIGHT = 480;

	//how many attributes in each ring
	static final int NUM_PER_RING = 5;

	//how many pixels away first ring should be
	static final int INNER_RING = 7;

	//how many pixels are rings apart
	static final int INCR_RING = 7;

	//how many pixels away final ring it
	static final int OUTER_RING = 42;

	//creats a map between body parts and binary values
	static final HashMap<Integer, Short> MAP_COLORS_TO_PARTS = new HashMap<>();
	
	//total cores JVM can see and I can use
	static int CORES = Runtime.getRuntime().availableProcessors();
	
	//number of random splits to consider
	static int NUM_TO_USE_FOR_INFO_GAIN = 20;
	
	//cut off for how deep the tree can go
	static int DEPTH_CUT_OFF = 5;
	
	//cut off for minimum instances before distribution
	//this is computed to be .25% of total instances
	static int INSTANCE_CUT_OFF = (int)(20*500*.0025);
			
	//target number of pixels from each frame
	static int TARGET_SAMPLE_SIZE = 2000;
	
	//mod % EVERY_NTH_FRAME tell which frames to use
	//I don't want to use every one because they are vastly the same
	static int EVERY_NTH_FRAME = 1;
	
//	//how often to sample pixel in frame
//	static double RANDOM_PERCENT = .7;
//	
	//how often to sample pixel in frame
		static double RANDOM_PERCENT = .995;

	//set up constants for bit-mapping of body part
	static short BACKGROUND = 0;
	static short LEFT_LEG = 1;
	static short RIGHT_LEG = 2;
	static short RIGHT_SHOULDER = 3;
	static short BELLY = 4;
	static short HEAD = 5;
	static short RIGHT_ARM = 6;
	static short LEFT_ARM = 7;

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
		MAP_COLORS_TO_PARTS.put(0, MasterConstants.BACKGROUND);
		
		//left leg
		MAP_COLORS_TO_PARTS.put(2542540, MasterConstants.LEFT_LEG);
		MAP_COLORS_TO_PARTS.put(2342340, MasterConstants.LEFT_LEG);
		MAP_COLORS_TO_PARTS.put(2122120,MasterConstants.LEFT_LEG);
		
		//right leg
		MAP_COLORS_TO_PARTS.put(12700, MasterConstants.RIGHT_LEG);
		MAP_COLORS_TO_PARTS.put(11700, MasterConstants.RIGHT_LEG);
		MAP_COLORS_TO_PARTS.put(10500, MasterConstants.RIGHT_LEG);
		MAP_COLORS_TO_PARTS.put(23400, MasterConstants.RIGHT_LEG);
		
		//right shoulder
		MAP_COLORS_TO_PARTS.put(1260254, MasterConstants.RIGHT_SHOULDER);
		MAP_COLORS_TO_PARTS.put(1160254, MasterConstants.RIGHT_SHOULDER);
		
		//belly
		MAP_COLORS_TO_PARTS.put(2541270, MasterConstants.BELLY);
		MAP_COLORS_TO_PARTS.put(2392340, MasterConstants.BELLY);
		
		//head
		MAP_COLORS_TO_PARTS.put(25400, MasterConstants.HEAD);
		
		//right arm
		MAP_COLORS_TO_PARTS.put(2540, MasterConstants.RIGHT_ARM);
		MAP_COLORS_TO_PARTS.put(2340, MasterConstants.RIGHT_ARM);
		MAP_COLORS_TO_PARTS.put(2120, MasterConstants.RIGHT_ARM);
		
		//left arm
		MAP_COLORS_TO_PARTS.put(254, MasterConstants.LEFT_ARM);
		MAP_COLORS_TO_PARTS.put(234,  MasterConstants.LEFT_ARM);
		MAP_COLORS_TO_PARTS.put(2392340,  MasterConstants.LEFT_ARM);
		
	}



}

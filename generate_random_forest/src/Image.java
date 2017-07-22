import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferUShort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

/**
 * @author Admin
 *
 */
public class Image {

	//holds file path for color image
	private String absolutePathColor;

	//holds file path for depth image
	private String absolutePathDepth;

	//data from read in image, this is only used to calculate attributes and the discarded to save memory
	private BufferedImage imagePixalArrayColor;
	private BufferedImage imagePixalArrayDepth;

	//attributes of the image that are used to classify in random forest
	public HashMap<XYPoint, short[]> attributes;

	//cache is used to avoid redundant offset calc. of offsets
	//this is shared between images
	//Come back and check this for concurrency
	OffsetCache offsetCacheForCalulations;

	/**
	 * Image holds the color and depth data as well as the computed attributes
	 * 
	 * @param absolutePathColor
	 * @param absolutePathDepth
	 * @param offsetCacheForCalulations
	 */
	public Image(String absolutePathColor, String absolutePathDepth, OffsetCache offsetCacheForCalulations){

		if(!absolutePathColor.contains(MasterConstants.FILETYPE) || !absolutePathDepth.contains(MasterConstants.FILETYPE)){
			System.out.println("Fle type violated "+absolutePathColor);
			System.out.println("Fle type violated "+absolutePathDepth);
			System.exit(1);
		}
		this.absolutePathColor = absolutePathColor;
		this.absolutePathDepth = absolutePathDepth;
		this.offsetCacheForCalulations =  offsetCacheForCalulations;
		this.attributes = new HashMap<>();
	}
	
	/**
	 * Copy constructor for subsets
	 * 
	 */
	public Image(Image oldImage){
		this.attributes = (HashMap<XYPoint, short[]>) oldImage.attributes.clone();
	}

	/**
	 * Loads in images from file path
	 * Fails if not correct file path
	 * 
	 */
	public void loadImage(){

		try {
			//read is thread safe
			this.imagePixalArrayColor = ImageIO.read(Files.newInputStream(Paths.get(absolutePathColor)));
			this.imagePixalArrayDepth = ImageIO.read(Files.newInputStream(Paths.get(absolutePathDepth)));
		} catch (IOException e) {
			System.out.println("Failed to load image "+this.absolutePathColor);
			System.out.println("Failed to load image "+this.absolutePathDepth);
			System.exit(1);
		}
	}

	/**
	 * Removes image data for garage collection to save space
	 * 
	 */
	public void unloadFrameImages(){
		this.imagePixalArrayColor = null;
		this.imagePixalArrayDepth = null;
		this.absolutePathColor = null;
		this.absolutePathDepth = null;
	}

	/**
	 * Calculates the image attributes by fetching offsets for each pixel from the cache
	 * Attribute  = pixelValue - pixelValueAtOffset
	 * Attributes have range of 0 - 2047 this requires 11-bits so we can get away with using a short (16-bits)
	 * The upper 5-bits will be used to hold info about what body part it is
	 */
	public void calculateImageAttributes(){

		//fetch in the gray image date as a raw short data
		DataBufferUShort buffer = (DataBufferUShort)imagePixalArrayDepth.getRaster().getDataBuffer();
		short[] arrayUShort = buffer.getData();

		//Loop through each pixel
		for(int x=0; x<MasterConstants.IMG_WIDTH; x++){
			for(int y=0; y<MasterConstants.IMG_HEIGHT; y++){
				
				//get body part value of current point
				Color rgb = new Color(imagePixalArrayColor.getRGB(x, y));
				int bodyPartValue = Integer.parseInt(""+rgb.getRed()+rgb.getGreen()+rgb.getBlue());
				
				//issue here with value parsing
				System.out.println("computing"+((arrayUShort[x + y * MasterConstants.IMG_WIDTH] & 0xffff) >> 5) +""+bodyPartValue);
				
				//checks is pixel is background
				if(((arrayUShort[x + y * MasterConstants.IMG_WIDTH] & 0xffff) >> 5) == 2047 || bodyPartValue == 0){
					continue;
				}
				
				//skips pixels that are not background randomly to have a good sample but not excessive data
				if(randomBoolean()){
					continue;
				}
				


				//Fetches the correct offsets from calculation or cache
				Offset offsets = offsetCacheForCalulations.getOffSets(x, y);

				//creates the current XY point as a key
				XYPoint xyPointKey = new XYPoint(x, y);


				short bodyPart = -1;
				//if the bodyPart was not a "common color use surrounding pixels to guess part"
				if(MasterConstants.MAP_COLORS_TO_PARTS.get(bodyPartValue) == null){
					//use context
					bodyPart = useContentToGetBodyPart(x, y);
					
					if(bodyPart == -1){
						System.out.print("Something when very wrong with useContentToGetBodyPart(x ,y)");
						System.exit(-1);
					}
					
					//if background
					if(bodyPart == 0){
						continue;
					}
				}else{
					bodyPart = (short)MasterConstants.MAP_COLORS_TO_PARTS.get(bodyPartValue);
				}
	
				//loop through attributes
				for(XYPoint xy : offsets.XYPoints){

					//fetch data at xy points
					int xyPointValue =  ((arrayUShort[x + y * MasterConstants.IMG_WIDTH] & 0xffff) >> 5);
					int yxOffsetValue =  ((arrayUShort[xy.x + xy.y * MasterConstants.IMG_HEIGHT] & 0xffff) >> 5);

					//get or create the attribute array
					short[] attributes = null;
					if(!this.attributes.containsKey(xyPointKey)){
						attributes = new short[MasterConstants.NUM_ATTRIBUTES];
					}else{
						attributes = this.attributes.get(xyPointKey);
					}

					//cal. attribute and store
					//Casting to short will get least-significant 16-bit which is fine as out range is 0-2047
					short grayScalePart = (short)(xyPointValue - yxOffsetValue);

					//OR bitmask for bodyPart with grayScalePart part to set bits 12-15 indicating body part
					attributes[xy.counter] =  (short)(bodyPart | grayScalePart);
					
					this.attributes.put(xyPointKey, attributes);

				}

			}
		}

	}

	/**
	 * Expand outward from a unknown pixel color until we find one we know.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private short useContentToGetBodyPart(int x, int y){
		int[][] directions = {{1,0}, {0,1}, {1,1}, {-1,-1}, {-1,0}, {0,-1}, {1,-1}, {-1,1}};

		Queue<XYPoint> queue = new LinkedList<>();
		queue.add(new XYPoint(x,y));

		while(!queue.isEmpty()){
			XYPoint point = queue.poll();

			Color rgb = new Color(imagePixalArrayColor.getRGB(point.x, point.y));
			int bodyPartValue = Integer.parseInt(""+rgb.getRed()+rgb.getGreen()+rgb.getBlue());

			//check for background that is not a perfect black
			//size >200 prevents overflow in searching for (0, 0, 0)
			//if its gone that far its not likely to be anything by background
			if(bodyPartValue == 0 || queue.size() > 200){
				return 0;
			}

			//look up body part
			if(MasterConstants.MAP_COLORS_TO_PARTS.get(bodyPartValue) != null){
				return MasterConstants.MAP_COLORS_TO_PARTS.get(bodyPartValue);
			}

			//add all surrounding pixels
			for(int i=0; i<directions.length; i++){
				int xNew = x+directions[i][0];
				int yNew = y+directions[i][1];
				
				//check within image
				if(xNew > -1 && xNew < MasterConstants.IMG_WIDTH && yNew > -1 && yNew < MasterConstants.IMG_HEIGHT){
					queue.add(new XYPoint(xNew, yNew));
				}

			}

		}

		//this should never happen, caught as error
		return -1;
	}
	
	/**
	 * Get pixel attribute value with some bitwise calcs.
	 * 
	 * @param xValue
	 * @param yValue
	 * @param attributeIndex
	 * @return
	 */
	int getAttributeValue(int xValue, int yValue, int attributeIndex){
		
		short[] attributesForXYPoint = this.attributes.get(new XYPoint(xValue, yValue));
		
		if(attributesForXYPoint == null){
			System.out.println("Error reading attributes for: "+new XYPoint(xValue, yValue));
			System.exit(1);
		}
		
		return (attributesForXYPoint[attributeIndex] & 0b1111111111100000);
	}
	
	/**
	 * Check if pixel has attributes or is a background pixel
	 * 
	 * @param xValue
	 * @param yValue
	 * @return
	 */
	boolean attributeExistAt(int xValue, int yValue){
		short[] attributesForXYPoint = this.attributes.get(new XYPoint(xValue, yValue));
		
		if(attributesForXYPoint == null){
			return false;
		}
		return true;
	}
	
	/**
	 * Get pixel label with bitwise calcs
	 * 
	 * @param xValue
	 * @param yValue
	 * @return
	 */
	int getInstanceLabel(int xValue, int yValue){
		//attributes for a pixel hold a label in there upset 12-16 bits
		return (this.attributes.get(new XYPoint(xValue, yValue))[0] & 0b0000000000011100);
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}

		if (!(other instanceof Image)){
			return false;
		}

		Image other_cast = (Image) other;

		return other_cast.absolutePathColor.equals(this.absolutePathColor) && other_cast.absolutePathDepth.equals(this.absolutePathDepth);
	}
	
	/**
	 * Use this to randomly select pixels from image.
	 * This gives us a random sample, instead of using them all. That would be alot
	 * 
	 * @return
	 */
	private boolean randomBoolean(){
	    return Math.random() < MasterConstants.RANDOM_PERCENT;
	}

}

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

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
	private HashMap<XYPoint, short[]> attributes;

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
	 * Loads in images from file path
	 * Fails if not correct file path
	 * 
	 */
	public void loadImage(){

		try {
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
		imagePixalArrayColor =null;
		imagePixalArrayDepth =null;
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
		
		//this writes out the all colors to file for k-clustering analysis
//		PrintWriter writer = null;
//		try {
//			writer = new PrintWriter(new FileOutputStream("colors.txt", true));
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		//Loop through each pixel
		for(int x=0; x<MasterConstants.IMG_WIDTH; x++){
			for(int y=0; y<MasterConstants.IMG_HEIGHT; y++){

				//checks is pixel is background
				if(((arrayUShort[x + y * MasterConstants.IMG_WIDTH] & 0xffff) >> 5) == 2047){
					continue;
				}

				//Fetches the correct offsets from calculation or cache
				Offset offsets = offsetCacheForCalulations.getOffSets(x, y);

				//tracks attribute we are at to index into attributes array
				int attributeNumber = 0;
				XYPoint xyPointKey = new XYPoint(x, y);

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

					attributes[attributeNumber++] = (short)(xyPointValue - yxOffsetValue);

					Color rgb = new Color(imagePixalArrayColor.getRGB(x, y));

					//write out for k-clustering
//					writer.println(rgb.getRed() +","+rgb.getGreen()+","+rgb.getBlue());

					this.attributes.put(xyPointKey, attributes);

				}

			}
		}

//		writer.close();
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

}
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Admin
 * Logic behind concurrent threads: all the threads can read from elements just fine as they are not modified.
 * Originally I cached points on the first time they were used but... this isn't going to work with multi. threads easily
 * Will revist later.
 * 
 * EDIT: Ended up just preloading all values and sharing between threads. Too time intensive for performance gained for true cache
 */
public class OffsetCache {
	
	boolean cacheLock;

	//stores the computed offsets at all the X,Y points
	private Offset[][] attributesDiffForXYPoint;
	
	public OffsetCache(){
		this.attributesDiffForXYPoint=  new Offset[MasterConstants.IMG_WIDTH][MasterConstants.IMG_HEIGHT];

		//just cache them all to start with
		for(int i=0; i<MasterConstants.IMG_WIDTH; i++){
			for(int j=0; j<MasterConstants.IMG_HEIGHT; j++){
				cachePoint(i, j);
			}
		}
	}
	
	/**
	 * Checks is the offsets are already stored if not stored computes them
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Offset getOffSets(int x, int y){ 
		return this.attributesDiffForXYPoint[x][y];
	}
	
	/**
	 * Computes the offsets for storage in cache
	 * 
	 */
	private void cachePoint(int x, int y){
		this.attributesDiffForXYPoint[x][y] = new Offset(x, y);
	}
	
}

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Admin
 *
 */
public class OffsetCache {

	//stores the computed offsets at all the X,Y points
	private Offset[][] attributesDiffForXYPoint;
	
	public OffsetCache(){
		this.attributesDiffForXYPoint=  new Offset[MasterConstants.IMG_WIDTH][MasterConstants.IMG_HEIGHT];
	}
	
	/**
	 * Checks is the offsets are already stored if not stored computes them
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Offset getOffSets(int x, int y){ 
		
		//stored?
		if(this.attributesDiffForXYPoint[x][y] == null){
			cachePoint(x,y);
		}
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

import java.util.ArrayList;

/**
 * @author Admin
 *
 */
public class Offset {

	//points calulated around XYPoint
	public ArrayList<XYPoint> XYPoints;

	public Offset(int x, int y){
		XYPoints = new ArrayList<>();
		fillXYOffsets(x, y);
	}

	/**
	 * Computes all the offsets surrounding a pixel.
	 * The offsets form a radial pattern.
	 */
	private void fillXYOffsets(int x , int y){

		for(int k=0; k<MasterConstants.NUM_PER_RING; k++){
			for(int r=MasterConstants.INNER_RING; r<MasterConstants.OUTER_RING; r+=MasterConstants.INCR_RING){
				
				//this will produce a nice distributes shape of points to look at
				int xValue = (int)Math.floor(x+Math.sin(2*Math.PI/MasterConstants.NUM_PER_RING*k+r)*r);
				int yValue = (int)Math.floor(y+Math.cos(2*Math.PI/MasterConstants.NUM_PER_RING*k+r)*r);

				//if its out of the image is is discarded
				if(yValue >= MasterConstants.IMG_HEIGHT || yValue < 0 || xValue >= MasterConstants.IMG_HEIGHT || xValue < 0){
					continue;
				}
				this.XYPoints.add(new XYPoint(xValue, yValue));
			}
		}

	}

}

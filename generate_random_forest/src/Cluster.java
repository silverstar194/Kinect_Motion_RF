import java.util.ArrayList;

/**
 * @author Admin
 *
 */
public class Cluster {
	
	//holds all the XYZ currently assigned to this cluser
	public ArrayList<XYZPoint> pointsInCluster;
	
	//3D centroid of the cluster computed from mean x, mean y mean z values
	// Might chance to median later if many extreme values skew center
	public XYZPoint clusterCenter;
	
	//tell us if center is moving at still
	public boolean converged = false;
	
	public int custerId;
	
	public Cluster(int custerId){
		pointsInCluster = new ArrayList<>();
		this.custerId = custerId;
	}
	/**
	 * Sets the new center for cluster after points are assigned
	 * @param clusterCenter
	 * @param custerId
	 */
	public void setCenter(XYZPoint clusterCenter, int custerId){
		this.clusterCenter = clusterCenter;
	}
	
	
	/**
	 * Finds the center of the cluster by averaging all x, y, z points
	 * Note: This isn't great if we have many extreme values that skew center
	 *  might chance to median later if this does not perform well
	 */
	public void calucateCenter(){
		
		int xsum = 0;
		int ysum = 0;
		int zsum = 0;
		for(XYZPoint point : pointsInCluster){
			xsum += point.x;
			ysum += point.y;
			zsum += point.z;
		}
		int numberOfPoints = pointsInCluster.size();


		
		if(clusterCenter.x - (xsum/numberOfPoints) < 1 && clusterCenter.y - (ysum/numberOfPoints) < 1 && clusterCenter.z - (zsum/numberOfPoints) < 1){
			this.converged = true;
		}else{
			this.clusterCenter = new XYZPoint(xsum/numberOfPoints, ysum/numberOfPoints, zsum/numberOfPoints);
		}

	}
	
	@Override
	public String toString(){
		String points = "";
		for(XYZPoint p : pointsInCluster){
			points += p.x +","+p.y +"," +p.z +","+p.getPopularElement()+"\n";
		}
		
		return points;
	}

}

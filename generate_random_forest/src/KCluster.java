import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Admin
 *
 */
public class KCluster {

	//all xyPoints for analysis
	private ArrayList<XYZPoint> xyzPoints;
	
	//holds ranges in which to randomly start points and how many clusters we want to form
	private int rangeX, rangeY, rangeZ, clusterNum;
	private ArrayList<Cluster> clusters;

	/**
	 * This uses k-clustering to cluster similar colors in order to tell the colors of body parts.
	 * 
	 * @param xyzPoints all points to cluster
	 * @param rangeX range from 0 - rangeX
	 * @param rangeY range from 0 - rangeY
	 * @param rangeZ range from 0 - rangeZ
	 * @param clusterNum number of cluster to cluster into
	 */
	public KCluster(ArrayList<XYZPoint> xyzPoints, int rangeX, int rangeY, int rangeZ, int clusterNum){
		this.xyzPoints = xyzPoints;
		this.rangeX = rangeX;
		this.rangeY = rangeY;
		this.rangeZ = rangeZ;
		this.clusterNum = clusterNum;
		this.clusters = new ArrayList<Cluster>();
	}

	/**
	 * Places the cluster centers as random points to start algo.
	 * Uniform Distribution
	 */
	private void plotRandomClusterCenters(){
		clusters = new ArrayList<>();
		for(int i=0; i<clusterNum; i++){
			Cluster c = new Cluster(i);
			int x = (int)(Math.random() * rangeX); 
			int y = (int)(Math.random() * rangeY);
			int z = (int)(Math.random() * rangeZ);
			c.clusterCenter = (new XYZPoint(x, y, z));
			
			//clear old clusters
			clusters.add(c);
		}
	}

	/**
	 * Computes the closest center to each point and assigns it to that cluster 
	 * Distance is geometric (not squarerooted to increase speed)
	 */
	private void assignPoints(){

		//clears out the old points from clusers
		for(Cluster c : clusters){
			c.pointsInCluster = new ArrayList<XYZPoint>();
		}

		//loops through all points
		for(XYZPoint p : xyzPoints){
			double smallestDistance = Double.MAX_VALUE;
			Cluster clusterWithSmallerDistance = null;
			for(Cluster c : clusters){
	
				//distance by straight line distance formula
				double distance = Math.pow((p.x - c.clusterCenter.x), 2) + 	Math.pow((p.y - c.clusterCenter.y), 2)  + Math.pow((p.z - c.clusterCenter.z), 2);

				if(smallestDistance > distance){
					smallestDistance = distance;
					clusterWithSmallerDistance = c;
				}
			}
			clusterWithSmallerDistance.pointsInCluster.add(p);
			p.cluserId.add(clusterWithSmallerDistance.custerId);
		}

	}
	
	/**
	 * Runs clustering 50 times w/ random restarts and takes the mode choice for each point
	 * 
	 * @return a list of clusters
	 */

	public ArrayList<Cluster> runClustering(){

		for(int i=0; i<15; i++){
			plotRandomClusterCenters();
			
			boolean allConverged = false;
			while(!allConverged){
				assignPoints();
				for(Cluster c : clusters){
					
					c.calucateCenter();

					
					for(Cluster x : clusters){
						allConverged = true;
						
						//checks if all the clusters have converged if not keep going
						if(!x.converged){
							allConverged = false;
							break;
						}
					}
				}

			}
			System.out.println(i);
		}
		
		//this assigns the points based on the cluster they fell most frequently into
		for(Cluster c : clusters){
			c.pointsInCluster = new ArrayList<>();
			for(XYZPoint point : this.xyzPoints){
				if(point.getPopularElement() == c.custerId){
					c.pointsInCluster.add(point);
				}
			}
		}
		
		return clusters;
	}
	

}

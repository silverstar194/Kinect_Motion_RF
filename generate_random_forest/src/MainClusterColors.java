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
public class MainClusterColors {
	
	/**
	 * Runs k-clustering on all the colors found in a set of frames (as found in colors-u.txt) to sort them into their color groups
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException{
		
		//read in colors (x, y, z)
		Scanner sc = new Scanner(new File("colors-u.txt"));
		ArrayList<XYZPoint> points = new ArrayList<>();
		while(sc.hasNextLine()){
			String[] tokens =sc.nextLine().split(",");
			XYZPoint point = new XYZPoint(Integer.valueOf(tokens[0]), Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2]));
			points.add(point);
		}
		sc.close();
		
		//specify points, ranges and number of clusters
		KCluster clusterRun = new KCluster(points, 255, 255, 255, 7);

		//get back the points in their cluster
		ArrayList<Cluster> clusters = clusterRun.runClustering();

		
		//save the points and their cluster id to file for later use
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream("colors-clustered.txt", true));

			for(Cluster c : clusters){
				System.out.print(c);
				writer.print(c.toString());
			}
			writer.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Error Custering Colors from File");
			e1.printStackTrace();
		}

	}

}

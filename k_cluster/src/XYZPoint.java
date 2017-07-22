import java.util.ArrayList;

/**
 * Used for k-clusting and rgb in 3D space
 * 
 * @author Admin
 *
 */
public class XYZPoint {

	//3D Coordinates
	public int x, y, z;
	public ArrayList<Integer> cluserId;

	public  XYZPoint(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		cluserId = new ArrayList<Integer>();
	}

	/**
	 * 
	 * Computes the most popular cluster by median
	 * https://stackoverflow.com/questions/8545590/find-the-most-popular-element-in-int-array
	 * 
	 *
	 */
	public int getPopularElement(){
		
		ArrayList<Integer> a = this.cluserId;
		int count = 1, tempCount;
		int popular = a.get(0);
		int temp = 0;
		for (int i = 0; i < (a.size() - 1); i++)
		{
			temp = a.get(i);
			tempCount = 0;
			for (int j = 1; j < a.size(); j++)
			{
				if (temp == a.get(j))
					tempCount++;
			}
			if (tempCount > count)
			{
				popular = temp;
				count = tempCount;
			}
		}
		return popular;
	}

}

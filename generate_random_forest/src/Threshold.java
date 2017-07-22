/**
 * 
 * Controls what attribute and where it splits at
 * Grr no tuples
 * @author Admin
 *
 */
public class Threshold {
	
	public int attributeIndex;
	public int splitPoint;
	
	public Threshold(int attributeIndex, int splitPoint){
		this.attributeIndex = attributeIndex;
		this.splitPoint = splitPoint;
	}

}

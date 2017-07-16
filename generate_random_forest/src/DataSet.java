import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Admin
 *
 */
public class DataSet {
	//holds all instances of images for classification
	public Queue<Image> data;
	//precomputed dataSize for better performance
	//ConcurrentLinkedQueue requires passing through all data for size attribute
	public int dataSize;
	
	//values I do not need to recheck as I have already split on them
	public boolean[][] checkedSplitAttributes;
	
	//TODO
	public List<Integer> labels;
	
	//this is recomputed when dataset is constructed for better speed
	public boolean allSameLabel;
	
	public DataSet(List<Integer> labels, Queue<Image> data){
		this.labels = labels;
		this.data = data;
		this.checkedSplitAttributes = new boolean[MasterConstants.NUM_ATTRIBUTES][2047];
	}
	
	public DataSet(){
		this.data = new ConcurrentLinkedQueue<Image>();
	}

	
	
}

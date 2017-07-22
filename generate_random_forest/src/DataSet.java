import java.util.ArrayList;
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
	
	//ConcurrentLinkedQueue requires passing through all data for size attribute
	//so lets store it and compute in parts for better performance
	public int dataSize;
	
	//values I do not need to recheck as I have already split on them
	public boolean[][] checkedSplitAttributes;
	
	//allows to classify one body part vs all others
	public int labelForOneVsAll;

	//holds all labels for classification in set
	public List<Label> labels;
	
	//this is recomputed when dataset is constructed for better speed
	public boolean allSameLabel;
	
	public DataSet(Queue<Image> data, short labelForOneVsAll){
		this.data = data;
		this.checkedSplitAttributes = new boolean[MasterConstants.NUM_ATTRIBUTES][2048];
		
		//which label for label vs all
		this.labelForOneVsAll = labelForOneVsAll;
		
		//add label and NOT label to lables
		labels = new ArrayList<Label>();
		labels.add(new Label(labelForOneVsAll, false));
		labels.add(new Label(labelForOneVsAll, true));
		
		//computes by traversal this is costly
		int count = 0;
		for(Image i : data){
			count += i.attributes.size();
		}
		this.dataSize = count;
		
		//when I start with loaded images they cannot all be same
		allSameLabel = false;
	}
	
	/**
	 * Constructor for cloning dataset for data subsets at each subtree
	 * 
	 * @param dataSetOld
	 */
	
	public DataSet(DataSet dataSetOld){
		this.data = new ConcurrentLinkedQueue<Image>();
		this.labels = dataSetOld.labels;
		//note dataSize is incremented as each pixel is added to data
	}
	
	/**
	 * Check to see if there are still attributes left to split on.
	 * 
	 * @return boolean
	 */
	public boolean allAttributesGone(){
		for(int i=0; i<this.checkedSplitAttributes.length; i++){
			for(int j=0; j<this.checkedSplitAttributes[i].length; j++){
				if(!checkedSplitAttributes[i][j]){return false;}
			}
		}
		return true;
	}
	
}

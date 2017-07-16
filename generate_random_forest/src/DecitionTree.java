import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Admin
 *
 */
public class DecitionTree {
	//root of tree we are building
	private DecTreeNode root;

	//ordered list of class labels
	private List<Integer> labels; 

	//dataset used for training
	private DataSet train; 

	//dataset used for tuning
	private DataSet tune; 

	//used for cross validation after trimming by occam's razor
	private DataSet crossValidate;

	/**
	 * Build a decision tree given a training set then prune it using a tuning set.
	 * 
	 * @param train: the training set
	 * @param tune: the tuning set
	 */
	DecitionTree(DataSet train, DataSet tune, DataSet crossValidate) {
		this.train = train;
		this.tune = tune;
		this.crossValidate = crossValidate;
	}

	//thing I added
	private DecTreeNode buildTree(DataSet train, int label){

		///(no label, no attribute index, no attribute split value, no parent attribute, by default termination node)
		DecTreeNode node = new DecTreeNode(-1, -1, -1, -1, true);

		//if not instances left to work with
		if(train.data.isEmpty()){node.label = label; System.out.println("Instances Empty"); return node;}

		//if all the examples are the same at leaf
		if(!train.allSameLabel){node.label = label; System.out.println("All Same"); return node;}

		//no attributes left
		if(this.train.data.isEmpty()){node.label = majorityClass(train); System.out.println("Attributes Empty"); return node;}

		//find the best attribute to split on based on information gain
		InfoGain gain =  new InfoGain(train);

		//
		String[] attributeSplitInfo = gain.maxInfoGain().split(",");
		int bestAttributeIndex = Integer.parseInt(attributeSplitInfo[0]);
		int bestAttributeSplitNum = Integer.parseInt(attributeSplitInfo[1]);

		//System.out.println(q);

		DecTreeNode tree = new DecTreeNode(-1, bestAttributeIndex, bestAttributeSplitNum,  -1, false);
		//tree.value = train.attributeValues.get(attributes.get(q)).get(0);

		//System.out.println(attributes.size());
		//binary split on attribute value
		//below and above split value
		for(int i=0; i<2; i++){
			DataSet subset = null;
			if(i == 0){
				//split above 
				subset = subsetAbove(train, bestAttributeIndex, bestAttributeSplitNum);
			}

			if(i == 1){
				subset = subsetBelow(train, bestAttributeIndex, bestAttributeSplitNum);
			}

			//one I have subset, do same thing here
			DecTreeNode subtree = buildTree(subset, majorityClass(train));

			subtree.attributeSplitNum = bestAttributeSplitNum;
			subtree.attribute = "bestAttributeIndex "+ bestAttributeIndex+" bestAttributeSplitNum "+bestAttributeSplitNum;
			subtree.parentAttributeSplitNum = tree.attributeSplitNum;

			//add child to tree, building tree up from bottom up!
			tree.addChild(subtree);
			tree.terminal = false;
		}
		System.out.println("RETURNED");
		return tree;

	}


	/**
	 * This subsets dataset based on the attribute used to split data.
	 * This returns the upper half.
	 * This method also checks if all same label to prevent additional loop later on
	 * 
	 */

	private DataSet subsetAbove(DataSet trainSet, int attributeIndex, int attributeSplitNum){
		//ArrayList<Instance> instancesVar = new ArrayList<>(trainData.instances);
		//System.out.println("Train Data Size: "+trainData.attributes.size());

		boolean allSameLabel = true;
		int firstLabel = trainSet.data.peek().getInstanceLabel(0, 0);

		DataSet outputSet = new DataSet();
		for(Image image : trainSet.data){
			for(int xValue=0; xValue<MasterConstants.IMG_WIDTH; xValue++){
				for(int yValue=0; yValue<MasterConstants.IMG_WIDTH; yValue++){

					//checks if we added to next dataset
					if((image.getAttributeValue(xValue, yValue, attributeIndex) >  attributeSplitNum)){
						outputSet.data.add(image);
						//System.out.println(i.attributes.get(getAttributeIndex(X, train)) + " " +v);

						//makes check for is same label, note this check is nested in other check!!
						if(!(image.getInstanceLabel(xValue, yValue) == firstLabel)){
							allSameLabel = false;;
						}	
					}


				}
			}
		}

		
		//clone old attribute array
		boolean[][] checkedSplitAttributesNew = trainSet.checkedSplitAttributes.clone();
		for (int i = 0; i < checkedSplitAttributesNew.length; i++) {
			checkedSplitAttributesNew[i] = checkedSplitAttributesNew[i].clone();
		}
		
		//mark attributes I will never have to split on again as done
		for(int i=attributeSplitNum; i>0; i--){
			checkedSplitAttributesNew[attributeIndex][i] = true;
		}
		
		//add marked attributes in new array
		outputSet.checkedSplitAttributes = checkedSplitAttributesNew;
		
		//same labels as classification hasn't changed
		outputSet.labels = trainSet.labels;

		//sets if labels all the same
		outputSet.allSameLabel = allSameLabel;
		return outputSet;
	}

	private DataSet subsetBelow(DataSet trainSet, int attributeIndex, int attributeSplitNum){
		//ArrayList<Instance> instancesVar = new ArrayList<>(trainData.instances);
		//System.out.println("Train Data Size: "+trainData.attributes.size());
		
		boolean allSameLabel = true;
		int firstLabel = trainSet.data.peek().getInstanceLabel(0, 0);

		DataSet outputSet = new DataSet();
		for(Image image : trainSet.data){
			for(int xValue=0; xValue<MasterConstants.IMG_WIDTH; xValue++){
				for(int yValue=0; yValue<MasterConstants.IMG_WIDTH; yValue++){

					if((image.getAttributeValue(xValue, yValue, attributeIndex) <=  attributeSplitNum)){
						outputSet.data.add(image);
						//System.out.println(i.attributes.get(getAttributeIndex(X, train)) + " " +v);
						
						//makes check for is same label, note this check is nested in other check!!
						if(!(image.getInstanceLabel(xValue, yValue) == firstLabel)){
							allSameLabel = false;;
						}
					}
				}
			}
		}
		
		
		//clone old attribute array
		boolean[][] checkedSplitAttributesNew = trainSet.checkedSplitAttributes.clone();
		for (int i = 0; i < checkedSplitAttributesNew.length; i++) {
			checkedSplitAttributesNew[i] = checkedSplitAttributesNew[i].clone();
		}
		
		//mark attributes I will never have to split on again as done
		for(int i=0; i<attributeSplitNum; i++){
			checkedSplitAttributesNew[attributeIndex][i] = true;
		}
		
		//add marked attributes in new array
		outputSet.checkedSplitAttributes = checkedSplitAttributesNew;
		
		
		//same labels as classification hasn't changed
		outputSet.labels = trainSet.labels;
		
		//sets if labels all the same
		outputSet.allSameLabel = allSameLabel;
		return outputSet;
	}



	/**
	 * Returns the majorityClass
	 * Using a full loop through dataset is permissible as the set should be greatly reduced
	 * as there are no attributes left to split on
	 * @param data
	 * @return
	 */
	private int majorityClass(DataSet dataSet){

		HashMap<Integer, Integer> countLabel = new HashMap<>();

		for(Image image : dataSet.data){
			for(int xValue=0; xValue<MasterConstants.IMG_WIDTH; xValue++){
				for(int yValue=0; yValue<MasterConstants.IMG_WIDTH; yValue++){
					if(!countLabel.containsKey(image.getInstanceLabel(xValue, yValue))){
						countLabel.put(image.getInstanceLabel(xValue, yValue), 1);
					}else{
						int countOfLabel = countLabel.get(image.getInstanceLabel(xValue, yValue));
						countLabel.replace(image.getInstanceLabel(xValue, yValue),  countOfLabel++);
					}

				}

			}
		}

		int maxValue = Integer.MIN_VALUE;
		int maxIndex = -1;
		for(Integer key : countLabel.keySet()){
			if(maxValue < countLabel.get(key)){
				maxValue = countLabel.get(key);
				maxIndex = key;
			}
		}

		return maxIndex;
	}


	/**
	 * Prints the subtree of the node with each line prefixed by 4 * k spaces.
	 */
	public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < k; i++) {
			sb.append("    ");
		}
		String value;
		if (parent == null) {
			value = "ROOT";
		} else {
			value = parent.attribute;
		}
		sb.append(value);
		if (p.terminal) {
			sb.append(" (" + p.label + ")");
			System.out.println(sb.toString());
		} else {
			sb.append(" {" + p.attribute + "?}");
			System.out.println(sb.toString());
			for (DecTreeNode child : p.children) {
				printTreeNode(child, p, k + 1);
			}
		}
	}


}

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Admin
 *
 */
public class DecisionTree {
	//root of tree we are building
	private DecTreeNode root;

	//dataset used for training
	private DataSet train; 

	//used for cross validation
	private DataSet crossValidate;

	/**
	 * Build a decision tree given a training set then prune it using a tuning set.
	 * 
	 * @param train: the training set
	 * @param tune: the tuning set
	 */
	DecisionTree(DataSet train, DataSet crossValidate) {
		this.train = train;
		this.crossValidate = crossValidate;
		
		//kicks off the recursion
		root = buildTree(train, -1, 0);
		
		//prints the tree once done
		printTreeNode(root, null, 4);
	}

	/**
	 * Core of the program builds the decision tree from dataset
	 * 
	 * 
	 * @return tree for classification
	 */
	private DecTreeNode buildTree(DataSet train, int label, int depth){
		
		//see how deep tree is
		System.out.println("Running Depth "+depth+"...");
		
		///(no label, no attribute index, no attribute split value, no parent attribute, by default termination node)
		DecTreeNode node = new DecTreeNode( -1, -1, true);

		//so tree won't be too deep
		if(depth > 20){System.out.println("Max depth Reached");return node;}
		
		//if no instances left to work with
		if(train.dataSize == 0){node.label = label; System.out.println("Instances Empty"); return node;}

		//if all the examples are the same at leaf
		if(train.allSameLabel){node.label = label; System.out.println("All Same"); return node;}

		//no attributes left
		if(this.train.allAttributesGone()){node.label = majorityClass(train); System.out.println("Attributes Empty"); return node;}

		//find the best attribute to split on based on information gain
		System.out.println("Starting Info Gain.."+train.data.size() +" "+ train.dataSize);
		
		//this uses information gain at random splits
		InfoGain gain =  new InfoGain(train);

		//java has no tuples (at least no easily) so I use string instead to pass two values
		String[] attributeSplitInfo = gain.maxInfoGain().split(",");
		int bestAttributeIndex = Integer.parseInt(attributeSplitInfo[0]);
		int bestAttributeSplitNum = Integer.parseInt(attributeSplitInfo[1]);

		//create new node at split
		DecTreeNode tree = new DecTreeNode(bestAttributeIndex, bestAttributeSplitNum, false);

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

			//one I have subset, do same thing one new subsets
			//cannot use majority class here!! N
			DecTreeNode subtree = buildTree(subset, majorityClass(train), depth++);

			//once subset is done add where it split to node
			subtree.attributeSplitNum = bestAttributeSplitNum;
			
			//to track nodes across threads
			subtree.id = generateId();
			subtree.parentId = tree.id;
					
			//for print to file later on
			subtree.attribute = i+" bestAttributeIndex "+ bestAttributeIndex+" bestAttributeSplitNum "+bestAttributeSplitNum;
			
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
	
		boolean allSameLabel = true;
		int firstLabel = -1;
		int removedCount = 0;
		
		
		DataSet outputSet = new DataSet(trainSet);
		for(Image image : trainSet.data){
			Image clonedImage = new Image(image);
			outputSet.data.add(clonedImage);
			for(int xValue=0; xValue<MasterConstants.IMG_WIDTH; xValue++){
				for(int yValue=0; yValue<MasterConstants.IMG_WIDTH; yValue++){

					//checks if add to next dataset
					if(image.attributeExistAt(xValue, yValue) && !(image.getAttributeValue(xValue, yValue, attributeIndex) >  attributeSplitNum)){
						clonedImage.attributes.remove(new XYPoint(xValue, yValue));
						removedCount++;
					}else if(image.attributeExistAt(xValue, yValue)){

						if(firstLabel == -1){
							firstLabel = image.getInstanceLabel(xValue, yValue);
						}else{
							//makes check for is same label, note this check is nested in other check!!
							if(!(image.getInstanceLabel(xValue, yValue) == firstLabel)){
								allSameLabel = false;;
							}	

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
		
		outputSet.dataSize = trainSet.dataSize - removedCount;
		
		outputSet.allSameLabel = allSameLabel;

		return outputSet;
	}

	private DataSet subsetBelow(DataSet trainSet, int attributeIndex, int attributeSplitNum){
		//ArrayList<Instance> instancesVar = new ArrayList<>(trainData.instances);
		//System.out.println("Train Data Size: "+trainData.attributes.size());

		boolean allSameLabel = true;
		int firstLabel = -1;
		int removedCount = 0;

		DataSet outputSet = new DataSet(trainSet);
		for(Image image : trainSet.data){
			Image clonedImage = new Image(image);
			outputSet.data.add(clonedImage);
			for(int xValue=0; xValue<MasterConstants.IMG_WIDTH; xValue++){
				for(int yValue=0; yValue<MasterConstants.IMG_WIDTH; yValue++){

					if(image.attributeExistAt(xValue, yValue) && !(image.getAttributeValue(xValue, yValue, attributeIndex) <=  attributeSplitNum)){
						clonedImage.attributes.remove(new XYPoint(xValue, yValue));
						removedCount++;
						//System.out.println(i.attributes.get(getAttributeIndex(X, train)) + " " +v);
					}else if(image.attributeExistAt(xValue, yValue)){
						if(firstLabel == -1){
							firstLabel = image.getInstanceLabel(xValue, yValue);
						}else{
							//makes check for is same label, note this check is nested in other check!!
							if(!(image.getInstanceLabel(xValue, yValue) == firstLabel)){
								allSameLabel = false;
							}	
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

		//sets if labels all the same
		outputSet.allSameLabel = allSameLabel;
		
		outputSet.dataSize = trainSet.dataSize - removedCount;
		
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
					if(!image.attributeExistAt(xValue, yValue)){
						continue;
					}
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
	
	/**
	 * Generate a string ID for each node to keep track of them
	 * 
	 * @return
	 */
	private String generateId(){
		return UUID.randomUUID().toString();
	}


}

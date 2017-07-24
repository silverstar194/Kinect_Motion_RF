import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Admin
 *
 */
public class DecisionTree {
	//root of tree we are building
	private DecTreeNode root;
	
	private ArrayList<String> nodesInTree;

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
		this.nodesInTree = new ArrayList<>();
		//kicks off the recursion
		buildTree(train, 0, "root", null);
		for(String s : nodesInTree){
			System.out.println(s);
		}

	}

	/**
	 * Core of the program builds the decision tree from dataset
	 * 
	 * 
	 * @return tree for classification
	 */
	private void buildTree(DataSet train, int depth, String parentNodeID, LabelDistribution label){

		//see how deep tree is
		System.out.println("Running Depth "+depth+"...");

		//so tree won't be too deep
		if(depth > MasterConstants.DEPTH_CUT_OFF){nodesInTree.add(writeOutNode(train, parentNodeID)); System.out.println("Max depth Reached"); return;}

		//if data set becomes too small to work with
		if(train.dataSize < MasterConstants.INSTANCE_CUT_OFF){nodesInTree.add(writeOutNode(train, parentNodeID)); System.out.println("Instances Empty"); return;}

		//if all the examples are the same at leaf
		//this should be pretty rare
		if(train.allSameLabel){nodesInTree.add(writeOutNode(label, parentNodeID));  System.out.println("All Same");  return;}

		//no attributes left
		//this should be impossible as there are more then 20 attributes
		//and I only allow 20 deep
		if(train.allAttributesGone()){nodesInTree.add(writeOutNode(train, parentNodeID)); System.out.println("Attributes Empty");  return;}

		//find the best attribute to split on based on information gain
		System.out.println("Starting Info Gain.."+train.data.size() +" "+ train.dataSize);

		SinglePassInfoGain gain = new SinglePassInfoGain(train);


		//java has no tuples (at least no easily) so I use string instead to pass two values
		String[] attributeSplitInfo = gain.maxInfoGain().split(",");


		int bestAttributeIndex = Integer.parseInt(attributeSplitInfo[0]);
		int bestAttributeSplitNum = Integer.parseInt(attributeSplitInfo[1]);

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


			//save the data so tree can be reconstructed
			String nodeId = generateId();
			String splitData = ""+i+ " "+bestAttributeIndex +" "+bestAttributeSplitNum;
			String dataOut = parentNodeID +" "+nodeId + " "+splitData;
			
			this.nodesInTree.add(dataOut);
			
			//one I have subset, do same thing one new subsets
			//compute the label only if if might make a empty instance label
			if(MasterConstants.INSTANCE_CUT_OFF*3 > subset.dataSize){
				buildTree(subset, depth+1, nodeId, findDistribtuion(subset));
			}else{
				buildTree(subset, depth+1, nodeId, null);
			}
		}

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
					if(image.attributeExistAt(xValue, yValue) && image.getAttributeValue(xValue, yValue, attributeIndex) <  attributeSplitNum){
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
		for(int i=attributeSplitNum; i>=0; i--){
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
	 * Returns a distribution of classes
	 * Using a full loop through dataset is permissible as the set should be greatly reduced
	 * as there are no attributes left to split on
	 * @param data
	 * @return
	 */
	private LabelDistribution findDistribtuion(DataSet dataSet){

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
						countLabel.replace(image.getInstanceLabel(xValue, yValue),  countOfLabel+1);
					}

				}

			}
		}


		return new LabelDistribution(countLabel);
	}

	
	private String writeOutNode(DataSet data, String parentNode){
		return ("leaf "+parentNode + " "+generateId()+" "+findDistribtuion(data).toString());
	}
	
	private String writeOutNode(LabelDistribution label, String parentNode){
		return ("leaf "+parentNode + " "+generateId()+" "+label.toString());
	}

	/**
	 * Generate a string ID for each node to keep track of them
	 * 
	 * @return
	 */
	private String generateId(){
		return UUID.randomUUID().toString().substring(0,8);
	}


}

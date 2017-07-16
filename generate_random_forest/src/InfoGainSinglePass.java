/**
 * @author Admin
 *
 */
import java.util.List;

/**
 * @author Admin
 * I loop through data three times here... Thats slow! Need to reduce to one loop
 *
 *
 */
public class InfoGainSinglePass {

	private  DataSet dataSet;
	private boolean[][] checkedSplitAttributes;

	InfoGainSinglePass(DataSet dataSet){
		this.dataSet = dataSet;
		//2047 comes from 2048 different shades of gray
		this.checkedSplitAttributes = new boolean[MasterConstants.NUM_ATTRIBUTES][2047];
	}

	public String maxInfoGain(){

		double max = Integer.MIN_VALUE;
		int bestAttributeIndex = -1;
		int bestAttributeSplitNum = -1;

		//System.out.println(this.data.attributeValues.keySet());
		//loop through attributes and find one that provides the largest information gain
		for(int attributeIndex=0; attributeIndex<MasterConstants.NUM_ATTRIBUTES; attributeIndex++){
			for(int attributeSplitNum=0; attributeSplitNum<2047; attributeSplitNum++){
				if(!this.checkedSplitAttributes[attributeIndex][attributeSplitNum]){
					double calcEntropy = conditionalEntrpy(attributeIndex, attributeSplitNum);
					//System.out.println("Best: "+entry.getKey());
					if(max < calcEntropy){
						max = calcEntropy;
						bestAttributeIndex = attributeIndex;
						bestAttributeSplitNum = attributeSplitNum;
					}
				}
			}
			//note which I can never split on again here after I found best split
		}

		//System.out.println(bestAttributeIndex+","+attributeSplitNum);
		//this for now because java has no tuples...
		return (bestAttributeIndex+","+bestAttributeSplitNum);
	}


	private double conditionalEntrpy(int attributeIndex, int attributeSplitNum){


		double sum = 0;

		//loop through all values of the attribute (0-2047)
		for(short i=0; i<2048; i++){
			//System.out.println(v);
			//calculate prob of instance with value v
			//H(Y = v | X) for all pixels
			double count = 0;
			for(Image image : this.dataSet.data){
				for(int xValue=0; xValue<MasterConstants.IMG_WIDTH; xValue++){
					for(int yValue=0; yValue<MasterConstants.IMG_HEIGHT; yValue++){
						if(image.getAttributeValue(xValue, yValue, attributeIndex) == i){
							count += 1;
						}
					}
				}

			}

			//calc conditional probs.
			double HValue = conditionalEntrpyGivenV(attributeIndex, attributeSplitNum, i);

			sum += HValue * (count/(dataSet.data.size()*MasterConstants.IMG_WIDTH*MasterConstants.IMG_HEIGHT));
		}

		//System.exit(1);
		//System.out.println(sum);
		return sum;
	}

	//H(Y | X = v)
	private double conditionalEntrpyGivenV(int attributeIndex, int attributeSplitNum, int valueOfAttribute){
		List<Integer> labels = dataSet.labels;

		double sum = 0;
		for(int y : labels){		
			double prob = probGiven(attributeIndex, attributeSplitNum, valueOfAttribute, y);

			//case of prob == zero
			if(prob == 0){
				return 0;
			}

			sum += -(prob*(Math.log(prob)/Math.log(2)));
		}

		//		System.out.println(sum);
		return sum;
	}

	//given attribute/index and value of index, the prob of a win/yield
	///P(Y = y | X = v)
	private double probGiven(int attributeIndex, int attributeSplitNum, int valueOfAttribute, int label){


		//count instances with that index == valueOfIndex
		double count = 0.0;
		double total = 0.0;

		//loop all pixels
		for(Image image : this.dataSet.data){
			for(int xValue=0; xValue<MasterConstants.IMG_WIDTH; xValue++){
				for(int yValue=0; yValue<MasterConstants.IMG_HEIGHT; yValue++){

					//given correct value of attribute
					if(image.getAttributeValue(xValue, yValue, attributeIndex) == valueOfAttribute){
						total += 1.0;
					}

					//if that the label matches too
					if(image.getAttributeValue(xValue, yValue, attributeIndex) == valueOfAttribute && image.getInstanceLabel(xValue, yValue) == label){
						//System.out.println("added to match");		
						count += 1.0;
					}
				}
			}
		}

		//edge case
		if(total == 0){
			return 0;
		}
		//System.out.println(count/total);
		return count/total;
	}

}


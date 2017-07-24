import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author Admin
 * Used this "easier to understand" implementation to cross check acc. with "single pass" implementation 
 * Both of them do the same thing single pass is just much more efficient
 *
 */
public class InfoGain {

	private  DataSet dataSet;
	private boolean[][] checkedSplitAttributes;

	InfoGain(DataSet dataSet){
		this.dataSet = dataSet;
		this.checkedSplitAttributes = new boolean[MasterConstants.NUM_ATTRIBUTES][2048];
	}

	public String maxInfoGain(){

		double max = Integer.MIN_VALUE;
		int bestAttributeIndex = -1;
		int bestAttributeSplitPoint = -1;


		//loop through attributes and find one that provides the largest information gain

		ArrayList<Threshold> threadholdsToSplitOn = generateRandomThreadholds(MasterConstants.NUM_TO_USE_FOR_INFO_GAIN);

		if(threadholdsToSplitOn.isEmpty()){System.out.println("Error Getting Random Split"); System.exit(1);}


		for(Threshold t : threadholdsToSplitOn){
			double calcEntropy = conditionalEntrpy(t.attributeIndex, t.splitPoint);

			
			if(max < calcEntropy){
				max = calcEntropy;
				bestAttributeIndex = t.attributeIndex;
				bestAttributeSplitPoint = t.splitPoint;
			}

		}

		//System.out.println(bestAttributeIndex+","+attributeSplitNum);
		//this for now because java has no tuples...


		return (bestAttributeIndex+","+bestAttributeSplitPoint);
	}


	private double conditionalEntrpy(int attributeIndex, int attributeSplitNum){
		//		System.out.println("attributeIndex: " + attributeIndex + " attributeSplitNum: "+attributeSplitNum);

		HashMap<Integer, Integer>  count = new HashMap<Integer, Integer>();
		double sum = 0;
		double total = 0;

		//System.out.println(v);
		//calculate prob of instance with value v
		//H(Y = v | X) for all pixels
		for(Image image : this.dataSet.data){
			for(int xValue=0; xValue<MasterConstants.IMG_WIDTH; xValue++){
				for(int yValue=0; yValue<MasterConstants.IMG_HEIGHT; yValue++){

					if(image.attributeExistAt(xValue, yValue)){
						total++;
						if(!count.containsKey(image.getAttributeValue(xValue, yValue, attributeIndex))){
							count.put(image.getAttributeValue(xValue, yValue, attributeIndex), 1);
							//System.out.println("Added: "+image.getAttributeValue(xValue, yValue, attributeIndex));
						}else{
							int value = count.get(image.getAttributeValue(xValue, yValue, attributeIndex));
							count.put(image.getAttributeValue(xValue, yValue, attributeIndex), value+1);
							//System.out.println("Incr.: "+image.getAttributeValue(xValue, yValue, attributeIndex) +" "+value+1);
						}

					}
				}
			}
		}

		for(Integer key : count.keySet()){
			//calc conditional probs.
			double HValue = conditionalEntrpyGivenV(attributeIndex, attributeSplitNum, key);
			sum += HValue * (count.get(key)/total);

		}

		//System.exit(1);

		return sum;
	}

	//H(Y | X = v)
	private double conditionalEntrpyGivenV(int attributeIndex, int attributeSplitNum, int valueOfAttribute){
		List<Label> labels = dataSet.labels;

		double sum = 0;
		for(Label label : labels){
			double prob = probGiven(attributeIndex, attributeSplitNum, valueOfAttribute, label);

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
	private double probGiven(int attributeIndex, int attributeSplitNum, int valueOfAttribute, Label label){


		//count instances with that index == valueOfIndex
		double count = 0.0;
		double total = 0.0;

		//loop all pixels
		for(Image image : this.dataSet.data){
			for(int xValue=0; xValue<MasterConstants.IMG_WIDTH; xValue++){
				for(int yValue=0; yValue<MasterConstants.IMG_HEIGHT; yValue++){


					//given correct value of attribute
					if(image.attributeExistAt(xValue, yValue) && image.getAttributeValue(xValue, yValue, attributeIndex) == valueOfAttribute){
						total += 1.0;
					}

					//if that the label matches too
					if(image.attributeExistAt(xValue, yValue)  && image.getAttributeValue(xValue, yValue, attributeIndex) == valueOfAttribute 
							&& ((image.getInstanceLabel(xValue, yValue) == label.label && !label.not) || (image.getInstanceLabel(xValue, yValue) != label.label && label.not))){
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

		return count/total;
	}

	private ArrayList<Threshold> generateRandomThreadholds(int numberThreadholds){
		ArrayList<Threshold> threadsHolds = new ArrayList<>();
		Random generator = new Random(1); 
		int count = 0;
		while(threadsHolds.size()<numberThreadholds && count < 10000){
			int randomAttribute  = generator.nextInt(MasterConstants.NUM_ATTRIBUTES);
			//generate
			int threadholdValue =  generator.nextInt(2048);
			if(!this.checkedSplitAttributes[randomAttribute][threadholdValue]){
				threadsHolds.add(new Threshold(randomAttribute, threadholdValue));
			}
			count++;
		}

		return threadsHolds;
	}

}
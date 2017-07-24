import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author Admin
 * I loop through data three times here... Thats slow! Need to reduce to one loop if possible
 *
 *
 */
public class SinglePassInfoGain {

	private  DataSet dataSet;
	private boolean[][] checkedSplitAttributes;

	SinglePassInfoGain(DataSet dataSet){
		this.dataSet = dataSet;
		//2047 comes from 2048 different shades of gray
		this.checkedSplitAttributes = dataSet.checkedSplitAttributes;
	}

	public String maxInfoGain(){
		double min = Integer.MAX_VALUE;
		int bestAttributeIndex = -1;
		int bestAttributeSplitNum = -1;
		ArrayList<Threshold> threadholdsToSplitOn = generateRandomThreadholds(MasterConstants.NUM_TO_USE_FOR_INFO_GAIN);

		if(threadholdsToSplitOn.isEmpty()){System.out.println("Error Getting Random Split"); System.exit(1);}

		for(Threshold t : threadholdsToSplitOn){

			double calcEntropy = conditionalEntrpy(t.attributeIndex, t.splitPoint);

			if(min > calcEntropy){
				min = calcEntropy;
				bestAttributeIndex = t.attributeIndex;
				bestAttributeSplitNum = t.splitPoint;
			}


		}

		//System.out.println(bestAttributeIndex+","+attributeSplitNum);
		//this for now because java has no tuples...
		System.out.println("Info. Gain Value: "+min);
		return (bestAttributeIndex+","+bestAttributeSplitNum);

	}


	private double conditionalEntrpy(int attributeIndex, int attributeSplitNum){
		double sum = 0;
		double total = 0;

		HashMap<Integer, Integer> countOfValues = new HashMap<>();

		for(Image image : this.dataSet.data){
			for(int xValue=0; xValue<MasterConstants.IMG_WIDTH; xValue++){
				for(int yValue=0; yValue<MasterConstants.IMG_HEIGHT; yValue++){

					if(image.attributeExistAt(xValue, yValue)){
						total++;
						if(!countOfValues.containsKey(image.getAttributeValue(xValue, yValue, attributeIndex))){
							countOfValues.put(image.getAttributeValue(xValue, yValue, attributeIndex), 1);
						}else{
							int currentValue = countOfValues.get(image.getAttributeValue(xValue, yValue, attributeIndex));
							countOfValues.put(image.getAttributeValue(xValue, yValue, attributeIndex), currentValue+1);
							//System.out.println(image.getAttributeValue(xValue, yValue, attributeIndex));
						}

					}

				}
			}

		}

		//get conditional entroy map

		HashMap<Integer, Double> conditonalEntopy = conditionalEntrpyGivenVMap(attributeIndex, attributeSplitNum);
		//loop through all values of the attribute (0-2047)
		//H(Y = v | X) for all pixels


		for(int i=0; i<2048; i++){
			//calc conditional probs.
			if(conditonalEntopy.get(i).isNaN()){
				continue;
			}
			double HValue = conditonalEntopy.get(i);

			//get count from map if there value was present
			double count = 0;
			if(countOfValues.get(i) != null){
				count = countOfValues.get(i);
			}

			sum += HValue * (count/total);
		}

		//System.exit(1);

		//		System.out.println(sum + " Index: "+attributeIndex +" SplitValue: "+attributeSplitNum);
		return sum;
	}

	//H(Y | X = v)
	private HashMap<Integer, Double> conditionalEntrpyGivenVMap(int attributeIndex, int attributeSplitNum){
		List<Label> labels = dataSet.labels;

		ArrayList<HashMap<Integer, Double>>  probGivenMaps = new ArrayList<>();

		for(Label y : labels){
			probGivenMaps.add((probGivenMap(attributeIndex, attributeSplitNum, y)));
		}

		HashMap<Integer, Double> output = new HashMap<>();
		for(HashMap<Integer, Double> map : probGivenMaps){

			for(int key : map.keySet()){

				if(!output.containsKey(key)){
					output.put(key, -(map.get(key)*(Math.log(map.get(key))/Math.log(2))));
				}else{
					//catch math.log(0) errors
					if(map.get(key) == 0){
						continue;
					}

					//catches division error
					if(map.get(key) == 1){
						continue;
					}
					double newValue = output.get(key) + -(map.get(key)*(Math.log(map.get(key))/Math.log(2)));
					//					System.out.println("Map Key: "+map.get(key) + "Log: "+Math.log(map.get(key)) + "Div. "+Math.log(map.get(key))/Math.log(2));
					//					System.out.println("\t"+newValue+"\n");
					output.put(key, newValue);
				}
			}
		}


		return output;
	}

	//given attribute/index and value of index, the prob of a win/yield
	///P(Y = y | X = v)
	private HashMap<Integer, Double> probGivenMap(int attributeIndex, int attributeSplitNum, Label label){


		HashMap<Integer, Double> countOfTotal = new HashMap<>();
		HashMap<Integer, Double> countOfAttribute = new HashMap<>();

		//loop all pixels
		//count all attribute values
		for(Image image : this.dataSet.data){
			for(int xValue=0; xValue<MasterConstants.IMG_WIDTH; xValue++){
				for(int yValue=0; yValue<MasterConstants.IMG_HEIGHT; yValue++){
					//given correct value of attribute
					if(image.attributeExistAt(xValue, yValue)){
						//System.out.println("Total: "+attributeIndex+" "+attributeSplitNum);
						if(!countOfTotal.containsKey(image.getAttributeValue(xValue, yValue, attributeIndex))){
							countOfTotal.put(image.getAttributeValue(xValue, yValue, attributeIndex), 1.0);
						}else{
							double countOfTotalCurr = countOfTotal.get(image.getAttributeValue(xValue, yValue, attributeIndex))+1;
							countOfTotal.put(image.getAttributeValue(xValue, yValue, attributeIndex), countOfTotalCurr);
						}
					}

					//if that the label matches too and (image.label == label and label_used or image.label != label and !label_used)
					if(image.attributeExistAt(xValue, yValue) &&  
							((image.getInstanceLabel(xValue, yValue) == label.label && !label.not) || 
									(image.getInstanceLabel(xValue, yValue) != label.label && label.not))){

						if(!countOfAttribute.containsKey(image.getAttributeValue(xValue, yValue, attributeIndex))){
							countOfAttribute.put(image.getAttributeValue(xValue, yValue, attributeIndex), 1.0);
						}else{
							double countOfAttributeCurr = countOfAttribute.get(image.getAttributeValue(xValue, yValue, attributeIndex))+1;
							countOfAttribute.put(image.getAttributeValue(xValue, yValue, attributeIndex),  countOfAttributeCurr);
						}
					}
				}
			}
		}

		//compute prob. from counts
		HashMap<Integer, Double> output = new HashMap<>();
		for(int key=0; key<2048; key++){
			if(countOfTotal.get(key) == null || countOfAttribute.get(key) == null){
				output.put(key, 0.0);
			}else{
				double result = countOfAttribute.get(key)/countOfTotal.get(key);
				output.put(key, result);
			}
		}

		return output;
	}

	private ArrayList<Threshold> generateRandomThreadholds(int numberThreadholds){
		ArrayList<Threshold> threadsHolds = new ArrayList<>();
		Random generator = new Random(); 
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

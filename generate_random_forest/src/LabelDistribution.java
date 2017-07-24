import java.util.HashMap;

/**
 * @author Admin
 *
 */
public class LabelDistribution {
	
	public int label, notlabel;
	public HashMap<Integer, Integer> countLabels;
	
	public  LabelDistribution(HashMap<Integer, Integer> countLabels){
		this.label = 0;
		this.notlabel = 0;
		this.countLabels = countLabels;
	}
	
	
	public String toString(){
		String s = "[";
		
		for(Integer i : countLabels.keySet()){
			s += i +":"+countLabels.get(i)+", ";
		}
		
		//if not labels
		if(s.length()==1){
			return "";
		}
		
		return s.substring(0,s.length()-2)+"]";
	}
	
	

}

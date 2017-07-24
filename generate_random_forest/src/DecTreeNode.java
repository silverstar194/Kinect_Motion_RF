import java.util.ArrayList;
import java.util.List;

/**
 * Possible class for internal organization of a decision tree. Included to show standardized output
 * method, print().
 * 
 * Do not modify. If you use, create child class DecTreeNodeImpl that inherits the methods.
 * 
 */
public class DecTreeNode {
	//what class the path leads to
	LabelDistribution label; // for leafs
	
	//attribute index to split on
	int attributeIndex;
	
	//Attribute split at number (i.e. above or below)
	int attributeSplitNum;
	
	//for printing attributeIndex + attributeSplitNum
	String attribute;
	
	//marks as leaf if end of path
	boolean terminal;
	
	//holds the children of split point
	List<DecTreeNode> children;
	
	String id;
	String parentId;

	DecTreeNode(int attributeIndex, int attributeSplitNum, boolean terminal) {
		this.attributeIndex = attributeIndex;
		this.attributeSplitNum =  attributeSplitNum;
		
		//for pretty print
		this.attribute = "attributeIndex: "+attributeIndex+" attributeSplitNum: "+attributeSplitNum;
		this.terminal = terminal;
		
		if (this.terminal) {
			children = null;
		} else {
			children = new ArrayList<DecTreeNode>();
		}
	}

	/**
	 * Add child to the node.
	 * 
	 * For printing to be consistent, children should be added in order of the attribute values as
	 * specified in the dataset.
	 */
	public void addChild(DecTreeNode child) {
		if (children != null) {
			children.add(child);
		}
	}

}

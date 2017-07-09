import java.util.Objects;

/**
 * @author Admin
 *
 */
public class XYPoint {

	//Coordinates in image
	public int x, y;

	public XYPoint(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * Overrides hashCode so it can be used as a hashtable key
	 */
	@Override
	public int hashCode(){
		//prime number for more even distribution
		//as each x and y pair are unique this should not have collisions
	    return Objects.hashCode(this.x*37 + this.y);
	}
	
	@Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XYPoint)){
            return false;
        }

        XYPoint other_cast = (XYPoint) other;

        return other_cast.x == this.x && other_cast.y == this.y;
    }

	
	
}

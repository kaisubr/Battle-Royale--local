/**
 *Kailash Subramanian, Gallatin
 */

/**
 * There are 8 different possible Orientations in this World.
 */
public enum Orientation {
	//construct orientations, with raw values 0 to 7
	NORTH(7), NORTHEAST(0), EAST(1), SOUTHEAST(2), SOUTH(3), SOUTHWEST(4), WEST(5), NORTHWEST(6);

	private int orient;
	
	/**
	 * An Orientation has 8 possible values
	 * @param rawOrient the raw orientation value
	 */
	Orientation(int rawOrient) {
		orient = rawOrient;
	}
	
	/**
	 * return the raw orientation value
	 * @return the raw orientation value
	 */
	public int getRawOrientation() {
		return orient;
	}
	  
	/**
	 * return a String representation of the orientation
	 * @return a String representation of the orientation
	 */
	public String toString() {
		return "orient"+orient;
	}
}

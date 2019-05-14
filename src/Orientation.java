
public enum Orientation {
	//construct orientations, with raw values 0 to 7
	NORTH(7), NORTHEAST(0), EAST(1), SOUTHEAST(2), SOUTH(3), SOUTHWEST(4), WEST(5), NORTHWEST(6);

	private int orient;
	Orientation(int rawOrient) {
		orient = rawOrient;
	}
	
	public int getRawOrientation() {
		return orient;
	}
	
	public String toString() {
		return "orient"+orient;
	}
}

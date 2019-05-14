/**
 *Kailash Subramanian, Gallatin
 */

/**
 * A SimpleCoordinate exists in a 2D space, unlike a regular Coordinate that exists in a 3D (isometric) space
 */
public class SimpleCoordinate {
	private int x, y;
	private SimpleCoordinate from;
	/**
	 * Represents a simple coordinate on the 2D space, with (0, 0) being the top left of a map.
	 * @param i x coordinate
	 * @param j y coordinate
	 */
	public SimpleCoordinate(int i, int j) {
		x = (i);
		y = (j);
	}
	
	/**
	 * Constructs SimpleCoordinate from Coordinate
	 * @param c a Coordinate version
	 */
	public SimpleCoordinate (Coordinate c) {
		x = (int) c.getIsoX();
		y = (int) c.getIsoY();
	}
	
	/**
	 * Consructs simple coordinate at (i, j)
	 * @param i the x coord. 
	 * @param j the y coord.
	 * @param previous the previous/parent coordinate
	 */
	public SimpleCoordinate(int i, int j, SimpleCoordinate previous) {
		x = (i);
		y = (j);
		from = previous;
	}
	
	/**
	 * Constructs SimpleCoordinate from Coordinate
	 * @param c the Coordinate
	 * @param previous the previous/parent coordinate
	 */
	public SimpleCoordinate(Coordinate c, SimpleCoordinate previous) {
		x = (int) c.getIsoX();
		y = (int) c.getIsoY();
		from = previous;
	}
	
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the x
	 */
	/**
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Gets previous (parent coordinate)
	 * @return previous
	 */
	public SimpleCoordinate getFrom() {
		// TODO Auto-generated method stub
		return from;
	}
}

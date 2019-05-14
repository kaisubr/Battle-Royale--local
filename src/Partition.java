/**
 *Kailash Subramanian, Gallatin
 */
import java.awt.Rectangle;

/**
 * THis class creates a Partition as a rectangular area in a 2D array
 */
public class Partition {
	private int x0, y0, x1, y1;
	private static int id;
	
	/**
	 * A partition is a rectangular space. Top left corner = (x0, y0); bottom right = (x1, y1)
	 * @param x0 the top left x0
	 * @param x1 the bottom right x1
	 * @param y0 the top left y0 
	 * @param y1 the bottom right y1
	 */
	public Partition(int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
		id++;
	}

	/**
	 * return x0
	 * @return x0 
	 */
	public int getX0() {
		return x0;
	} 

	/**
	 * return y0
	 * @return y0
	 */
	public int getY0() {
		return y0;
	}
	
	/**
	 * return x1
	 * @return x1
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * return y1
	 * @return y1
	 */
	public int getY1() {
		return y1;
	}
	
	/**
	 * return the height of partition
	 * @return the height of partition
	 */
	public int height() {
		return (y1 - y0);
	}
	
	/**
	 * return width of partition
	 * @return width of partition
	 */
	public int width() {
		return (x1 - x0);
	}
	
	/**
	 * return the rectangular form of partition
	 * @return the rectangular form of partition
	 */
	public Rectangle getRectangularPartition() {
		Rectangle r = new Rectangle();
		r.setBounds(x0, y0, width(), height());
		return r;
	}
	
	/**
	 * return the String representation of partition
	 * @return the String representation of partition
	 */
	public String toString() {
		return ("Part." + id + " (" + x0 + "," + y0 + ") -> (" + x1 + "," + y1 + ")");
	}
}

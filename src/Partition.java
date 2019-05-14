import java.awt.Rectangle;

public class Partition {
	private int x0, y0, x1, y1;
	private static int id;
	
	/**
	 * A partition is a rectangular space. Top left corner = (x0, y0); bottom right = (x1, y1)
	 * @param x0
	 * @param x1
	 * @param y0
	 * @param y1
	 */
	public Partition(int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
		id++;
	}

	public int getX0() {
		return x0;
	}

	public int getY0() {
		return y0;
	}
	
	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}
	
	public int height() {
		return (y1 - y0);
	}
	
	public int width() {
		return (x1 - x0);
	}
	
	public Rectangle getRectangularPartition() {
		Rectangle r = new Rectangle();
		r.setBounds(x0, y0, width(), height());
		return r;
	}
	
	public String toString() {
		return ("Part." + id + " (" + x0 + "," + y0 + ") -> (" + x1 + "," + y1 + ")");
	}
}

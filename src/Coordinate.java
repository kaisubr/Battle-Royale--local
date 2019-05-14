/**
 *Kailash Subramanian, Gallatin
 */

/**
 * This class holds both isometric and Cartesian x/y coordinates on the map.
 * 
 * Cartesian coordinates are always relative to the content-pane unless 
 * explicitly otherwise mentioned, i.e. in the Bullet class, where a Point is 
 * used to relay the differentiation.
 */
public class Coordinate implements Comparable {

	private double isoX, isoY;
	private double cartX, cartY;
	
	/**
	 * Given the isometric coordinates, this constructs a Coordinate.
	 * @param x x-value
	 * @param y y-value
	 */
	public Coordinate(double x, double y) {
		isoX = x;
		isoY = y;
		isoToCart();
	} 
	
	/**
	 * Converts isometric to cartesian coordinates
	 */
	private void isoToCart() {
		//cartX = (isoX - isoY) / 1.5;
		//cartY = isoX/3.0 + isoY / 1.5;
		//cartX = (2 * isoY + isoX)/2;
		//cartY = (2 * isoY - isoX)/2;
		
		cartX = (isoY - isoX) * World.W/2;// +World.TOP_CARTX;
		cartY = (isoY + isoX) * World.W/4;// -World.TOP_CARTY;
	}

	/**
	 * Converts Cartesian to isometric coordinates
	 */
	private void cartToIso() {
		// TODO Auto-generated method stub
		isoX = cartX - cartY;
		isoY = (cartX+cartY)/2;
	}
	
	/**
	 * Sets the iso value of point
	 * @param isoX the isometric x tile
	 * @param isoY the isometric y tile
	 */
	public void setIso(double isoX, double isoY) {
		this.isoX = isoX;
		this.isoY = isoY;
		isoToCart();
	}

	/**
	 * Sets the cartesian values of point
	 * @param cartX the absolute cartesian x
	 * @param cartY the absolute cartesian y
	 */
	public void setCart(double cartX, double cartY) {
		this.cartX = cartX;
		this.cartY = cartY;
		cartToIso();
	}

	/**
	 * return the iso-x
	 * @return the iso-x
	 */
	public double getIsoX() {
		return isoX;
	}

	/**
	 * return the iso-y
	 * @return the iso-y
	 */
	public double getIsoY() {
		return isoY;
	}

	/**
	 * return the cart-x
	 * @return the cart-x
	 */
	public double getCartX() {
		return cartX;
	}

	/**
	 * return the cart-y
	 * @return the cart-y
	 */
	public double getCartY() {
		return cartY;
	}
	
	/**
	 * Generates coordinate from a cartesian point
	 * @param cX cart-x
	 * @param cY cart-y
	 * @return the Coordinate generated
	 */
	public static Coordinate fromCart(double cX, double cY) {
		
		double iX = (cY/(World.W/4) - cX/(World.W/2)) / 2;// cX/(World.W/2); // = cX - cY;// cX + cY;
		double iY = (cY/(World.W/4) + cX/(World.W/2)) / 2; // = (cX+cY)/2;// (cY-cX)/2.0f;
		
		////System.out.println(cX + ", " + cY + " = " + iX + ", " + iY);
		return new Coordinate(iX, iY);
	}
	
	/**
	 * A String representation of a Coordinate
	 * @return String representation of a Coordinate
	 */
	public String toString() {
		return "Cart(" + cartX + ", " + cartY + ") Iso(" + isoX + ", " + isoY + ")";
	}

	/**
	 * If 2 coordinates have the same absolute cart-x and cart-y, they are equal.
	 * @return if coordinates are equal (0), or not (1).
	 */
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return (((Coordinate)o).cartX == cartX && ((Coordinate)o).cartY == cartY)? 0 : 1;
	}
}


/**
 * This class holds both isometric and Cartesian x/y coordinates on the map.
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
	
	private void isoToCart() {
		//cartX = (isoX - isoY) / 1.5;
		//cartY = isoX/3.0 + isoY / 1.5;
		//cartX = (2 * isoY + isoX)/2;
		//cartY = (2 * isoY - isoX)/2;
		
		cartX = (isoY - isoX) * World.W/2;// +World.TOP_CARTX;
		cartY = (isoY + isoX) * World.W/4;// -World.TOP_CARTY;
	}

	private void cartToIso() {
		// TODO Auto-generated method stub
		isoX = cartX - cartY;
		isoY = (cartX+cartY)/2;
	}
	
	public void setIso(double isoX, double isoY) {
		this.isoX = isoX;
		this.isoY = isoY;
		isoToCart();
	}

	public void setCart(double cartX, double cartY) {
		this.cartX = cartX;
		this.cartY = cartY;
		cartToIso();
	}

	public double getIsoX() {
		return isoX;
	}

	public double getIsoY() {
		return isoY;
	}

	public double getCartX() {
		return cartX;
	}

	public double getCartY() {
		return cartY;
	}
	
	public static Coordinate fromCart(double cX, double cY) {
		
		double iX = (cY/(World.W/4) - cX/(World.W/2)) / 2;// cX/(World.W/2); // = cX - cY;// cX + cY;
		double iY = (cY/(World.W/4) + cX/(World.W/2)) / 2; // = (cX+cY)/2;// (cY-cX)/2.0f;
		
		//System.out.println(cX + ", " + cY + " = " + iX + ", " + iY);
		return new Coordinate(iX, iY);
	}
	
	public String toString() {
		return "Cart(" + cartX + ", " + cartY + ") Iso(" + isoX + ", " + isoY + ")";
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return (((Coordinate)o).cartX == cartX && ((Coordinate)o).cartY == cartY)? 0 : 1;
	}
}

/**
 *Kailash Subramanian, Gallatin
 */ 
import java.awt.Rectangle;

/**
 * A GameEntity exists in the World and has clearly defined bounds
 */
public class GameEntity {
	private Rectangle hbx;
	private Coordinate c;
	
	/**
	 * constructs a game entity in the world
	 * @param c the entity's coordinates
	 * @param w the width of entity
	 * @param h the height of entity
	 */
	public GameEntity(Coordinate c, int w, int h) {
		this.c= c;
		hbx = new Rectangle();
		hbx.setBounds((int)c.getCartX(), (int)c.getCartY(), w, h);
	}
	
	/**
	 * @return the htibox
	 */
	public Rectangle getHitbox() {
		return hbx;
	}
	
	/** 
	 * @return the coordinates
	 */
	public Coordinate getCoordinates() {
		return c;
	}
}

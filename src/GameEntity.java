import java.awt.Rectangle;

/**
 * A GameEntity exists in the World and has clearly defined bounds
 */
public class GameEntity {
	private Rectangle hbx;
	private Coordinate c;
	
	public GameEntity(Coordinate c, int w, int h) {
		this.c= c;
		hbx = new Rectangle();
		hbx.setBounds((int)c.getCartX(), (int)c.getCartY(), w, h);
	}
	
	public Rectangle getHitbox() {
		return hbx;
	}
	
	public Coordinate getCoordinates() {
		return c;
	}
}

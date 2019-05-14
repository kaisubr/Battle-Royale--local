import java.util.ArrayList;

import javax.swing.JLabel;

public abstract class Soldier extends GameEntity {
	
	protected int id;
	public static int total;
	protected int hp;
	protected Coordinate c;
	protected ContentPaneHandler cph;
	protected boolean isAnimating;
	protected boolean alive;
	
	/**
	 * Construct a soldier on the panel at the location posX, posY.
	 * @param cph The ContentPaneHandler to assist in drawing.
	 * @param posX The Cartesian x-coordinate
	 * @param posY
	 */
	public Soldier(ContentPaneHandler cph, Coordinate c, int w, int h) {
		super(c, w, h);
		id = total++;

		this.cph = cph;
		this.c = c;
		hp = 100;
		alive = true;
		isAnimating = false;
		
		
		draw();
	}
	
	/**
	 * Draws the soldier at the isometric coordinate. 
	 * This should not be called more than once; rather animationCycle().
	 */
	public abstract void draw();

	/**
	 * Runs a single animation cycle as long as not currently animating.
	 */
	public abstract void animationCycle();

	/**
	 * Sets coordinates by a Cartesian amount. 
	 * 
	 * Note: The player's screen position will not update until the 
	 * ContentPaneHandler repaints the GamePanel. To force an update
	 * of the screen position, see forceMove().
	 * 
	 * @param dx pixels right
	 * @param dy pixels down
	 */
	public abstract void deltaSet(double dx, double dy);
	
	/**
	 * Forcefully updates screen location of the Soldier to c.
	 */
	public abstract void forceMove();

	/**
	 * Gets the JLabel image corresponding to Soldier. This JLabel also has rectangular bounds.
	 * @return JLabel corresponding to this Soldier 
	 */
	public abstract JLabel getImg();

	public void deltaHp(int i) {
		hp += i;
	}

}

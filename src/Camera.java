/**
 *Kailash Subramanian, Gallatin
 */
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/**
 * Represents the Camera, centered on the player
 */
public class Camera {
	
	private JFrame fr;
	private ContentPaneHandler cph;
	
	/**
	 * Constructs a Camera
	 * @param fr the JFrame 
	 * @param cph the content-pane handler
	 */
	public Camera(JFrame fr, ContentPaneHandler cph) {
		this.fr = fr;
		this.cph = cph;
	}
	
	/**
	 * Centers the camera at the Cartesian coordinate
	 * @param c a coordinate on the isometric world
	 */
	public void center(Coordinate c) {
		cph.absoluteTranslate(fr.getWidth()/2 - World.W/2 - (int)c.getCartX(), 
				fr.getHeight()/2 - World.H/2 - (int)c.getCartY());
	}

	/**
	 * Returns the JFrame
	 * @return the JFrame
	 */
	public JFrame getFrame() {
		return fr;
	}
	
	/**
	 * return the content-pane handler
	 * @return the content-pane handler
	 */
	public ContentPaneHandler getContentPaneHandler() {
		return cph;
	}
}

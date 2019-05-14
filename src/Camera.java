import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Camera {
	
	private JFrame fr;
	private ContentPaneHandler cph;
	
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

	public JFrame getFrame() {
		return fr;
	}
	
	public ContentPaneHandler getContentPaneHandler() {
		return cph;
	}
}

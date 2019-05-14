/**
 *Kailash Subramanian, Gallatin
 */
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import javafx.scene.paint.Color;

/**
 * The main game panel. Changes to this panel should occur only through 
 * the specified content-pane handler.
 */
public class GamePanel extends JLayeredPane { /*JPane*/
	
	private int x, y;
	
	/**
	 * Constrcuts a panel
	 * @param m the layout-manager of the panel
	 */
	public GamePanel(LayoutManager m) {
		super();
		x = y = 0;
	}
	
	/**
	 * Paints the component
	 * @param g the graphics
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.translate(x, y);
	}
	
	/** 
	 * sets the x/y shift and calls repaint
	 * @param x the x units
	 * @param y the y units
	 */
	public void shift(int x, int y) {
		this.x = x;
		this.y = y;
		repaint();
	}

}

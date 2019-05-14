import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import javafx.scene.paint.Color;

public class GamePanel extends JLayeredPane { /*JPane*/
	
	private int x, y;
	
	public GamePanel(LayoutManager m) {
		super();
		x = y = 0;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.translate(x, y);
	}
	
	public void shift(int x, int y) {
		this.x = x;
		this.y = y;
		repaint();
	}

}

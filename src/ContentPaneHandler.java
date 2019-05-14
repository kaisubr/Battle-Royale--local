import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Timer;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ContentPaneHandler {
	private static GamePanel c;
	private int topX, topY;
	private Camera cam; 
	private Player pl;
	private InputManager im;
	
	private boolean run;
	private boolean isRepainting;
	//changing this might affect player.speed
	private int FRAME_DELAY = 16; //ms	Set to 33 for 30 fps, 16 for 60 fps, 6 for 144 fps
	private PartitionGenerator pag;
	private ArrayList<Soldier> soldiers;
	
	public ContentPaneHandler(GamePanel c) {
		this.c = c;
		topX = topY = 0;
	}
	
	public static void add(JComponent co, double x, double y) {
		c.add(co);
		co.setLocation((int)x, (int)y);
		co.setSize(co.getPreferredSize());
		c.repaint();
	}
	
	public void add(JComponent co, double sx, double sy, double x, double y) {
		c.add(co);
		co.setLocation((int)x, (int)y);
		co.setSize((int)sx, (int)sy);
		c.repaint();
	}
	
	public void add(JComponent co, double sx, double sy, double x, double y, boolean repaint) {
		c.add(co);
		co.setLocation((int)x, (int)y);
		co.setSize((int)sx, (int)sy);
		if (repaint) c.repaint();
	}
	
	public void add(JComponent co, double sx, double sy, double x, double y, boolean repaint, int zIndex) {
		c.add(co, zIndex);
		co.setLocation((int)x, (int)y);
		co.setSize((int)sx, (int)sy);
		if (repaint) c.repaint();
	}
	
	public void remove(JComponent co) {
		c.remove(co);
	}
	
	public void absoluteTranslate(int x, int y) {
		c.shift(x, y);
	}
	
	public void deltaTranslate(int dx, int dy) {
		topX += dx;
		topY += dy;
		absoluteTranslate(topX, topY);
	}
	
	public void repaint() {

		isRepainting = true;
		
		c.repaint();
		if (cam != null) {
			//ensures player visible
			pl.forceMove();
			im.setPosition();

			cam.center(pl.c);
			for (Bullet b : World.getQueueBullets()) {
				b.forceStepMove();
			}
			
			for (Soldier s : soldiers) {
				s.forceMove();
			}
		}
		
		isRepainting = false;
			
	}

	public void setCamera(Camera camera) {
		// TODO Auto-generated method stub
		cam = camera;
	}

	public void setPlayer(Player player) {
		// TODO Auto-generated method stub
		pl = player;
	}
	
	public void setInputManager(InputManager im) {
		this.im = im;
	}
	
	public Player getPlayer() {
		return pl;
	}
	
	public InputManager getInputManager() {
		return im;
	}
	
	public void startGameLoop() {
		ActionListener repaint = new ActionListener() {
			public void actionPerformed(ActionEvent aev) {
				//System.out.println("Frame");
				repaint();
			}
		};
		
		Timer loop = new Timer(FRAME_DELAY, repaint);
		loop.start();
	}

	public void setPartitionGenerator(PartitionGenerator pag) {
		// TODO Auto-generated method stub
		this.pag = pag;
	}

	public PartitionGenerator getPartitionGenerator() {
		// TODO Auto-generated method stub
		return pag;
	}
	
	public boolean isRepainting() {
		return isRepainting;
	}

	public void setSoldiers(ArrayList<Soldier> soldiers) {
		// TODO Auto-generated method stub
		this.soldiers = soldiers;
	}
}

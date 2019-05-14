/**
 *Kailash Subramanian, Gallatin
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Timer;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Handles all activity done upon the content pane of the main GamePanel
 */
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
	private Timer loop;
	
	/**
	 * Constructs a content-pane handler
	 * @param c the game panel
	 */
	public ContentPaneHandler(GamePanel c) {
		this.c = c;
		topX = topY = 0;
	} 
	
	/**
	 * Adds component with solely component and location
	 * @param co component
	 * @param x location x
	 * @param y location y
	 */
	public static void add(JComponent co, double x, double y) {
		c.add(co);
		co.setLocation((int)x, (int)y);
		co.setSize(co.getPreferredSize());
		c.repaint();
	}
	
	/**
	 * Adds component to panel
	 * @param co component
	 * @param sx size x
	 * @param sy size y
	 * @param x location x
	 * @param y location y
	 */
	public void add(JComponent co, double sx, double sy, double x, double y) {
		c.add(co);
		co.setLocation((int)x, (int)y);
		co.setSize((int)sx, (int)sy);
		c.repaint();
	}
	
	/**
	 * @param co component
	 * @param sx size x
	 * @param sy size y
	 * @param x location x
	 * @param y location y
	 * @param repaint repaints if true
	 */
	public void add(JComponent co, double sx, double sy, double x, double y, boolean repaint) {
		c.add(co);
		co.setLocation((int)x, (int)y);
		co.setSize((int)sx, (int)sy);
		if (repaint) c.repaint();
	}
	
	/**
	 * Adds component to panel
	 * @param co component
	 * @param sx size x
	 * @param sy size y
	 * @param x location x
	 * @param y location y
	 * @param repaint
	 * @param zIndex a higher z-index means it is placed on top of components with lower z-indices
	 */
	public void add(JComponent co, double sx, double sy, double x, double y, boolean repaint, int zIndex) {
		c.add(co, zIndex);
		co.setLocation((int)x, (int)y);
		co.setSize((int)sx, (int)sy);
		if (repaint) c.repaint();
	}
	
	/**
	 * Removes component to panel
	 * @param co a component
	 */
	public void remove(JComponent co) {
		c.remove(co);
	}
	
	/**
	 * Jump-translates the panel
	 * @param x to location x
	 * @param y to location y
	 */
	public void absoluteTranslate(int x, int y) {
		c.shift(x, y);
	}
	
	/**
	 * Shifts the panel
	 * @param dx by x units
	 * @param dy by y units
	 */
	public void deltaTranslate(int dx, int dy) {
		topX += dx;
		topY += dy;
		absoluteTranslate(topX, topY);
	}
	
	/**
	 * Repaints anything that has moved
	 */
	public void repaint() {

		if (loop == null || !loop.isRunning()) return;
		
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

	/**
	 * Sets the camera
	 * @param camera the camera
	 */
	public void setCamera(Camera camera) {
		// TODO Auto-generated method stub
		cam = camera;
	}

	/**
	 * Sets the player
	 * @param player the player
	 */
	public void setPlayer(Player player) {
		// TODO Auto-generated method stub
		pl = player;
	}
	
	/**
	 * Sets the input manager
	 * @param im the input manager
	 */
	public void setInputManager(InputManager im) {
		this.im = im;
	}
	
	/**
	 * return the player
	 * @return the player
	 */
	public Player getPlayer() {
		return pl;
	}
	
	/**
	 * @return the input manager
	 */
	public InputManager getInputManager() {
		return im;
	}
	
	/**
	 * Starts the repaint loop
	 */
	public void startGameLoop() {
		ActionListener repaint = new ActionListener() {
			public void actionPerformed(ActionEvent aev) {
				////System.out.println("Frame");
				if (!pl.alive) stopGameLoop(false);
				else if (soldiers.size() == 1) stopGameLoop(true);
				else repaint();
				
			}
		};
		
		loop = new Timer(FRAME_DELAY, repaint);
		loop.start();
	}

	/**
	 * Stops the game loop
	 * @param win if the player had won
	 */
	public void stopGameLoop(boolean win) {
		// TODO Auto-generated method stub
		//System.out.println("stopped");
		
		if (!win) 
			JOptionPane.showMessageDialog(c,
			    "You placed " + (soldiers.size()-1) + " out of " + Setup.players + ". You lost, but got " + pl.eliminations + " eliminations.\nCome back and try different modes!", 
			    "Game over! ", JOptionPane.INFORMATION_MESSAGE);
		else 
			JOptionPane.showMessageDialog(c,
				    "Congratulations! You beat all " + Setup.players + " enemies. You won, and got " + pl.eliminations + " eliminations.\nCome back and try different modes!", 
				    "WINNER WINNER CHICKEN DINNER ;)", JOptionPane.INFORMATION_MESSAGE);
		
		
		loop.stop();
		System.exit(0);
	}

	/**
	 * Sets  the partition generator
	 * @param pag the partition generator
	 */
	public void setPartitionGenerator(PartitionGenerator pag) {
		// TODO Auto-generated method stub
		this.pag = pag;
	}

	/**
	 * return the partition generator
	 * @return the partition generator
	 */
	public PartitionGenerator getPartitionGenerator() {
		// TODO Auto-generated method stub
		return pag;
	}
	
	/**
	 * return if currently repainting
	 * @return if currently repainting
	 */
	public boolean isRepainting() {
		return isRepainting;
	}

	/**
	 * sets the list of soldiers (including Player)
	 * @param soldiers the list of soldiers (including Player)
	 */
	public void setSoldiers(ArrayList<Soldier> soldiers) {
		// TODO Auto-generated method stub
		this.soldiers = soldiers;
	}
}

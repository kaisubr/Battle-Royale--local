import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

public class Player extends Soldier {
	
	
	private JLabel img;
	private JProgressBar healthLbl;
	
	private Orientation orientation;
	private ImageIcon playerImage; //use img to control player, not this.
	
	private Timer mveAnim;
	private int frame = 0;
	private boolean moving = false;
	
	private InputManager im;
	private double speed = 1.5; //also try fps30,sp2; fps16s2; fps11s1
	
	
	//private double x, y;
	
	public Player(ContentPaneHandler cph, Coordinate c) {
		super(cph, c, World.W, World.H);
		this.im = cph.getInputManager();
		//draw() called by Soldier to initialize img
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
		//File f = new File("src/assets/player/Male_3_Idle0.png");
		try {
//			ImageIcon i = new ImageIcon(f.toString());
//			//Image ii = i.getImage().getScaledInstance(W, H, Image.SCALE_SMOOTH);
//			Rectangle size = new Rectangle(0, 256, 256, 256);
//			//comment out //.getScaledInstance(...) if green lines appear
//			Image ii = World.cropImage(f, size);//.getScaledInstance(World.W, World.H, Image.SCALE_SMOOTH);
//			img = new JLabel(new ImageIcon(ii));
//			//l.setBorder(new LineBorder(Color.GREEN));
			
			orientation = Orientation.SOUTHEAST;
			ImageIcon ii = createImageIcon(orientation);
			playerImage = ii;
			
			img = new JLabel(ii);
			img.setBorder(new LineBorder(Color.BLUE));
			cph.add(img, 
					World.W/2,
					World.H/2,
					c.getCartX(), 
					c.getCartY(), 
					false);

			healthLbl = new JProgressBar(JProgressBar.HORIZONTAL);
			healthLbl.setMaximum(100);
			healthLbl.setMinimum(0);
			healthLbl.setValue(100);
			healthLbl.setBorder(new LineBorder(Color.BLACK));
			cph.add(healthLbl, 
					World.W/2,
					10,
					c.getCartX(), 
					c.getCartY(), 
					false, 3);
			
			animationCycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ImageIcon createImageIcon(Orientation o) throws IOException {
		//URL imgURL = Player.class.getResource("/assets/player/Male_" + o.getRawOrientation() + "_Run" + frame + ".png");
		//File f = new File(imgURL.getFile().replaceAll("%20", " "));
		
		//InputStream fsr = Player.class.getResourceAsStream("/assets/player/Male_" + o.getRawOrientation() + "_Run" + frame + ".png");
		String streamRsrc = "/assets/player/Male_" + o.getRawOrientation() + "_Run" + frame + ".png";
		//File f = new File("src/assets/player/Male_" + o.getRawOrientation() + "_Run" + frame + ".png");
		//ImageIcon i = new ImageIcon(ImageIO.read(fsr));
		
		//Image ii = i.getImage().getScaledInstance(W, H, Image.SCALE_SMOOTH);
		Rectangle size = new Rectangle(0, 256, 256, 256); //0,256,256,256
		//comment out //.getScaledInstance(...) if green lines appear
		//Image ii = World.cropImage(ImageIO.read(fsr), size);//.getScaledInstance(World.W, World.H, Image.SCALE_SMOOTH);
		BufferedImage ii = World.cropToBufferedImage(ImageIO.read(quickReadFsr(streamRsrc)), size);
		
		return new ImageIcon(ii);
	}
	
	public InputStream quickReadFsr(String streamRsrc) {
		return Player.class.getResourceAsStream(streamRsrc);
	}

	@Override
	public void animationCycle() {
		// TODO Auto-generated method stub
		ActionListener task = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (moving) frame++;
				else frame = 9;
				
				frame %= 9;
			}
		};
		
		mveAnim = new Timer(75, task);
		mveAnim.start();
	}

	@Override
	public void deltaSet(double dx, double dy) {
		// TODO Auto-generated method stub
		dx *= -speed;// * (3/Math.sqrt(3)); //kinda a hacky way to get the fromCart() method to understand left/right
		dy *= -speed;// * (1/Math.sqrt(3));
		Coordinate oldC = c;
		Coordinate newC = Coordinate.fromCart(c.getCartX() + dx, c.getCartY() + dy);
//		if (!CollisionManager.isCollision(this, oldC,  null, null, orientation, true, cph.getPartitionGenerator())) {
//			c = newC;
//		} else {
//			
//		}
		//https://gamedev.stackexchange.com/questions/64173/how-do-i-detect-collision-with-isometric-walls
		if (CollisionManager.isWalkableBasic(getImg().getBounds(), newC, cph.getPartitionGenerator())) {
			c = newC;
			System.out.println("walkable");
		} else {
			//pop back - applies a exponential spline (2^x) to make a smoother transition to pop back
			Coordinate popC = new Coordinate(Math.round(oldC.getIsoX()), Math.round(oldC.getIsoY()));
			Coordinate popHalf = Coordinate.fromCart((popC.getCartX() + oldC.getCartX())/2, (popC.getCartY() + oldC.getCartY())/2);
			c = popHalf;
			System.out.println("not walkable");
		}
		
		//x = c.getCartX(); y = c.getCartY();
		//cph.repaint();
	}
	
	public void orientationSet(InputManager manager) throws IOException {
		boolean[] keys = manager.getKeys();
		//when mouse is clicked, orientation should "snap" to mouse for firing (eg. quickscope, which is possible since it is kinda a top-view game)
		if (keys[KeyEvent.VK_W] && keys[KeyEvent.VK_D]) {
			orientation = Orientation.EAST;
		} else if (keys[KeyEvent.VK_W] && keys[KeyEvent.VK_A]) {
			orientation = Orientation.NORTH;
		} else if (keys[KeyEvent.VK_S] && keys[KeyEvent.VK_D]) {
			orientation = Orientation.SOUTH;
		} else if (keys[KeyEvent.VK_S] && keys[KeyEvent.VK_A]) {
			orientation = Orientation.WEST;
		} else if (keys[KeyEvent.VK_W]) {
			orientation = Orientation.NORTHEAST;
		} else if (keys[KeyEvent.VK_A]) {
			orientation = Orientation.NORTHWEST;
		} else if (keys[KeyEvent.VK_S]) {
			orientation = Orientation.SOUTHWEST;
		} else if (keys[KeyEvent.VK_D]) {
			orientation = Orientation.SOUTHEAST;
		} 
		
		playerImage = createImageIcon(orientation);
	}
	
	public void setMoving(boolean mve) {
		moving = mve;
	}
	
	@Deprecated
	public void isoMove(int i, int j) {
		c = new Coordinate(i, j);
		//x = c.getCartX(); y = c.getCartY();
		//cph.repaint();
	}
	
	@Override
	public JLabel getImg() {
		return img;
	}
	
	public JProgressBar getHealthLbl() {
		return healthLbl;
	}

	@Override
	public void forceMove() {
		// TODO Auto-generated method stub
		img.setIcon(playerImage);
		img.setLocation((int)c.getCartX(), (int)c.getCartY());
		healthLbl.setValue(hp);
		healthLbl.setLocation((int)c.getCartX(), (int)c.getCartY() - 10);
	}

}

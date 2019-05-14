/**
 *Kailash Subramanian, Gallatin
 */
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 * An Enemy is trying to defeat the Player and other Enemies.
 */
public class Enemy extends Soldier {

	private static final double MIN_ATTACK_RADIUS = 350;
	private AlphaImageLabel img;
	private float opacity;
	private Timer deathAnimation, mveAnim ;
	
	private Soldier target;
	private int map[][];
	private Pathfinder pathfinder;
	private List<SimpleCoordinate>  route;
	private int routeFrame;

	private JProgressBar healthLbl;
	private int fitnessFireRate;
	
	/**
	 * Constructs Enemy. All enemies have different firing rates, and thus 
	 * different levels of difficulty.
	 *  
	 * @param cph the content-pane handler
	 * @param c the Coordinate
	 */
	public Enemy(ContentPaneHandler cph, Coordinate c) {
		super(cph, c, World.W, World.H);
		
		target = acquireTarget(World.getSoldiers());
		
		fitnessFireRate= (int) (150 + (Math.random() * 500));
		
		initNewPathfinder(target.c);
		
		//draw() called by Soldier to initialize img
	}

	/**
	 * Acquires target (closest to this enemy)
	 * @param soldiers the list of soldiers
	 * @return the closest target
	 */
	private Soldier acquireTarget(ArrayList<Soldier> soldiers) {
		// TODO Auto-generated method stub
		if (Setup.mode == Setup.SURVIVAL) return cph.getPlayer();
		
		Soldier s = cph.getPlayer();
		double sDist = distanceTo(s);
		
		for (Soldier so : soldiers) {
			double soDist = distanceTo(so);
			if (soDist < sDist) {
				s = so;
				sDist = soDist;
			}
		}
		
		return s;
	}

	/**
	 * Constructs a standard Enemy.
	 * @param cph the content-pane handler
	 * @param c the coordinate
	 * @param w the width of enemy
	 * @param h the height of enemy
	 */
	public Enemy(ContentPaneHandler cph, Coordinate c, int w, int h) {
		super(cph, c, w, h);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Distance to another soldier
	 * @param s the other soldier
	 * @return the distance to this soldier, in pixel units
	 */
	private double distanceTo(Soldier s) {
		// TODO Auto-generated method stub 
		return Math.sqrt(Math.pow(c.getCartX() - s.c.getCartX(), 2) + Math.pow(c.getCartY() - s.c.getCartY(), 2));
	}

	/**
	 * Determines if the soldier is within MIN_ATTACK_RADIUS
	 * @param target a target soldier
	 * @return if the soldier is within a reasonable attack radius
	 */
	private boolean withinAttackingRange(Soldier target) {
		// TODO Auto-generated method stub
		return (distanceTo(target) < MIN_ATTACK_RADIUS);
	}
	
	/**
	 * Draws the enemy
	 */
	@Override
	public void draw() {
		// TODO Auto-generated method stub
		try {
			//URL imgURL = Enemy.class.getResource("/assets/enemy/enemy128x128noshadow_" + (int)(Math.random()*7) + ".png");
			
			//File f = new File(imgURL.getFile().replaceAll("file:/", "").replaceAll("%20", " "));
			
			InputStream fstr = Enemy.class.getResourceAsStream("/assets/enemy/enemy128x128noshadow_" + (int)(Math.random()*7) + ".png");
			
			////System.out.println(f.toString());
			
			img = new AlphaImageLabel(fstr);
			
			//img = new AlphaImageLabel("src/assets/enemy/enemy128x128noshadow_" + (int)(Math.random()*7) + ".png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//img.setBorder(new LineBorder(Color.RED));
		
		opacity = 1f;
		
		cph.add(img, 
				World.W/2,
				World.H/2,
				c.getCartX(), 
				c.getCartY(), 
				false);
		
		healthLbl = new JProgressBar(JProgressBar.HORIZONTAL);
		healthLbl.setForeground(Color.ORANGE);
		healthLbl.setBackground(Color.white);
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
	}
	
	/**
	 * Affects the health. Dies if health under 0.
	 * @param i the dh the health is affected by
	 */
	@Override
	public void deltaHp(int i) {
		hp += i;
		
		if (hp <= 0) {
			hp = 0;
			alive = false;
			World.soldiers.remove(this);
			death();
		}
		
		//System.out.println(hp + " (taken " + i + " dmg)");
		
	}
	
	private int keyFrame = 0; //10 keyframes per routeFrame
	private final double MAX_KEYFRAME = 15.;
	private Coordinate previouslyFilled;
	private Timer atkAnim;
	/**
	 * Begins movement to target
	 */
	private void startMoveTo() {
		// TODO Auto-generated method stub
		//System.out.println(route);
		ActionListener task = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (route.size() <= 0) 
					stopMveTimer();
				else if (route.size() <= 1) {
					stopMveTimer();
					startAttackTo();
				}
				
				if (withinAttackingRange(target)) {
					stopMveTimer();
					startAttackTo();
				}
				
				if (keyFrame < MAX_KEYFRAME) {
					keyFrame++; 
					try {
						isoMove(previouslyFilled.getIsoX() + (keyFrame)*((route.get(routeFrame).getX() -previouslyFilled.getIsoX())/MAX_KEYFRAME),  
								previouslyFilled.getIsoY() + (keyFrame)*((route.get(routeFrame).getY() -previouslyFilled.getIsoY())/MAX_KEYFRAME));
					} catch (Exception ee) {}
					
				} else {
					routeFrame++;
					keyFrame = 0;
					previouslyFilled = new Coordinate(route.get(routeFrame-1).getX(), route.get(routeFrame-1).getY());
//					if (routeFrame < route.size()) {
//						isoMove(route.get(routeFrame).getX(), route.get(routeFrame).getY());
//					}
					if (routeFrame >= route.size()) {
						stopMveTimer();
						if (withinAttackingRange(target)) {
							startAttackTo();
						} else {
							initNewPathfinder(acquireTarget(World.getSoldiers()).c);
						}
					}
				}
				
			}

		};
		
		mveAnim = new Timer(20, task);
		mveAnim.start();
	}

	/**
	 * Initializes a new Pathfinder
	 * @param targetCoord a target coordinate
	 */
	private void initNewPathfinder(Coordinate targetCoord) {
		// TODO Auto-generated method stub

		if (mveAnim!= null) stopMveTimer();
		if (atkAnim!= null) stopAtkTimer();
		routeFrame = 0;
		keyFrame = 0;
		previouslyFilled = Coordinate.fromCart(c.getCartX(), c.getCartY());
		
		map = cph.getPartitionGenerator().getMap();
		pathfinder = new Pathfinder(map, 
				new SimpleCoordinate(c), 
				new SimpleCoordinate(targetCoord));
		
		route = pathfinder.solve();
		startMoveTo();
	}
	
	/**
	 * Returns this enemy
	 * @return this enemy
	 */
	private Soldier thisEnemy() {
		return this;
	}

	/**
	 * Attacks while within range of target.
	 */
	private void startAttackTo() {
		// TODO Auto-generated method stub
		if (atkAnim != null && atkAnim.isRunning()) return;
		
		ActionListener task = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (withinAttackingRange(target)) {
					
					if (!target.alive) {
						initNewPathfinder(acquireTarget(World.getSoldiers()).c);
						stopAtkTimer();
						return;
					}
					
					Point cam = new Point(cph.getPlayer().getImg().getLocation());
					
					Point obj = new Point(img.getLocation()); 
					//SwingUtilities.convertPointToScreen(pt, img); 
					
					//obj -> scrn
					int dx = obj.x - cam.x;
					int dy = obj.y - cam.y;
					
					obj.x = Launcher.GAME_WIDTH/2 + dx;
					obj.y = Launcher.GAME_HEIGHT/2 + dy;
					
					Point trg = new Point(target.getImg().getLocation()); 
					//SwingUtilities.convertPointToScreen(ptTarg, target.getImg()); 
					
					//trg -> scrn
					dx = trg.x - cam.x;
					dy = trg.y - cam.y;
					
					trg.x = Launcher.GAME_WIDTH/2 + dx;
					trg.y = Launcher.GAME_HEIGHT/2 + dy;

					//System.out.println(trg.x + ", " + trg.y);

					Coordinate scrnPos = Coordinate.fromCart(obj.getX(), obj.getY());
					Coordinate targPos = Coordinate.fromCart(trg.getX(), trg.getY());
					Coordinate actualThis = c;
					Coordinate actualCntrThis = Coordinate.fromCart(actualThis.getCartX() + img.getWidth()/2, actualThis.getCartY() + img.getHeight()/2);
					World.getQueueBullets().add(
							new Bullet(
									cph, 
									scrnPos, 
									targPos,
									actualCntrThis,
									thisEnemy(),
									20
							));
				} else {
					if (!target.alive) {
						initNewPathfinder(acquireTarget(World.getSoldiers()).c);
					} else {
						initNewPathfinder(target.c);	
					}

					stopAtkTimer();
				}
			}
		};
		
		atkAnim = new Timer(fitnessFireRate, task);
		atkAnim.start();
	}

	/**
	 * stops movement timer animation
	 */
	private void stopMveTimer() {
		// TODO Auto-generated method stub
		mveAnim.stop();
	}
	
	/**
	 *  stops attack animation
	 */
	private void stopAtkTimer() {
		atkAnim.stop();
	}

	/**
	 * starts death animation
	 */
	private void death() {
		// TODO Auto-generated method stub
		
		if (mveAnim != null && mveAnim.isRunning()) mveAnim.stop();
		if (atkAnim != null && atkAnim.isRunning()) atkAnim.stop();
		
		ActionListener task = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				opacity -= 0.05;
				if (opacity > 0.0) {
					img.setOpacity(opacity);
					c = Coordinate.fromCart(c.getCartX(), c.getCartY() - 1);
				}
				else stopTimer();
			}
		};
		
		deathAnimation = new Timer(100, task);
		deathAnimation.start();
	}

	/**
	 * stops death animation
	 */
	protected void stopTimer() {
		// TODO Auto-generated method stub
		deathAnimation.stop();
		cph.remove(img);
		cph.remove(healthLbl);
		if (World.soldiers.contains(this)) World.soldiers.remove(this);
	}

	/**
	 * Unimplemented animation cycle
	 */
	@Override
	public void animationCycle() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Unimplemented delta-set
	 */
	@Override
	public void deltaSet(double dx, double dy) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param i the iso-x tile
	 * @param j the iso-y tile
	 */
	@Deprecated
	public void isoMove(double i, double j) {
		c = new Coordinate(i, j);
	}
	
	/**
	 * Called by cph to update the enemy's position before repainting in loop.
	 */
	@Override
	public void forceMove() {
		// TODO Auto-generated method stub
		img.setLocation((int)c.getCartX(), (int)c.getCartY());
		
		healthLbl.setValue(hp);
		healthLbl.setLocation((int)c.getCartX(), (int)c.getCartY() - 10);
	}

	/**
	 * The JLabel image
	 */
	@Override
	public JLabel getImg() {
		// TODO Auto-generated method stub
		return img;
	}
	
	
}

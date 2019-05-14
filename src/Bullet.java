/**
 *Kailash Subramanian, Gallatin
 */
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 * Represents a fireball
 */
public class Bullet {
	private Coordinate from, to;
	private double speed, angle; //angle is radians
	private double cartX, cartY;
	private Soldier firedFrom;
	
	private JLabel img;
	private ContentPaneHandler cph;
	private int expectedDamage;
	public static final int STANDARD_DMG = 40, SPREAD_DMG = 20;
	private boolean isMoving;
	private boolean alreadyRemoved;
	
	/**
	 * Constructs a bullet
	 * @param cph the content-pane handler
	 * @param from the coordinate (point on screen)
	 * @param to the other coordinate (point on screen)
	 * @param actualPlayer actual player's relative coordinates to the content-pane
	 * @param firedFrom the soldier from which the bullet was fired from
	 * @param expectedDamage the damage done by the bullet
	 */
	public Bullet(ContentPaneHandler cph, Coordinate from, Coordinate to, Coordinate actualPlayer, Soldier firedFrom, int expectedDamage) {
		double toY = to.getCartY(); 
		double fromY = from.getCartY();
		double toX = to.getCartX();
		double fromX = from.getCartX();
		
		angle = Math.atan2(-(toY - fromY), toX - fromX); //from and to --> standard cartesian with bottom left as (0, 0), not top left.
		if (angle < 0) angle = Math.abs(angle);
		else angle = 2 * Math.PI - angle;
		angle = 360 - Math.toDegrees(angle);
		
		//System.out.println("from " + fromX + ", " + fromY + " and to " + toX + ", " + toY);
		//System.out.println(angle);
		
		
		speed = 15;
		cartX = actualPlayer.getCartX();
		cartY = actualPlayer.getCartY();
		this.cph = cph;
		this.firedFrom = firedFrom;
		this.expectedDamage = expectedDamage;
		
		isMoving = true;
		draw();
	}
	
	/**
	 * Draws the bullet
	 */
	public void draw() {
		// TODO Auto-generated method stub
		try {
			//URL imgURL = Bullet.class.getResource("/assets/projectile/fireball_12x12.gif");
			//File f = new File(imgURL.getFile().replaceAll("%20", " "));
			
			InputStream fsr = Bullet.class.getResourceAsStream("/assets/projectile/fireball_12x12.gif");
			
			//File f = new File("src/assets/projectile/fireball_12x12.gif");
			
			ImageIcon i = new ImageIcon(ImageIO.read(fsr));//f.toString());
			
			img = new JLabel(i);
			//img.setBorder(new LineBorder(Color.RED));
			cph.add(img, 
					12,
					12,
					cartX, 
					cartY, 
					false,
					1); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Moves the bullet
	 */
	public void forceStepMove() {
		// TODO Auto-generated method stub
		if (isMoving) {
			cartX += Math.round(Math.cos(Math.toRadians(angle))*speed);
			cartY -= Math.round(Math.sin(Math.toRadians(angle))*speed);
			
			if (CollisionManager.isWalkableBasic(img.getBounds(), Coordinate.fromCart(cartX, cartY), cph.getPartitionGenerator())) {
				Soldier collidedWith = CollisionManager.isSoldierHitBasic(img.getBounds(), Coordinate.fromCart(cartX, cartY), World.getSoldiers());
				if (collidedWith != null && collidedWith != firedFrom) {
					collidedWith.deltaHp(-expectedDamage);
					if (!collidedWith.alive) firedFrom.eliminationIncr();
					stopMove();
				} else {
					img.setLocation((int)cartX, (int)cartY);
				}
			} else {
				Soldier collidedWith = CollisionManager.isSoldierHitBasic(img.getBounds(), Coordinate.fromCart(cartX, cartY), World.getSoldiers());
				if (collidedWith != null) {
					collidedWith.deltaHp(-expectedDamage);
					stopMove();
				}
				stopMove();
			}
		}
	}

	/**
	 * Stops bullet movement
	 */
	private void stopMove() {
		// TODO Auto-generated method stub
		//System.out.println("stopped");
		isMoving = false;

		cph.remove(img);
		
		if (!cph.isRepainting()) {
			if (!alreadyRemoved) World.getQueueBullets().remove(this);
			alreadyRemoved = true;
		} else {
			forceStepMove();
		}
		
	}
}

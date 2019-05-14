/**
 *Kailash Subramanian, Gallatin
 */

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 * The World is where the calls to generate a map and add tiles to the UI occurs.
 */
public class World {
	private ContentPaneHandler cph;
	private Player pl;
	
	public static final int ROW = 20, COL = 20;
	
	//if you change from 256, you gotta enable scaling
	public static final int W = 256, H = 256;//512/4; 
	public static final int TOP_CARTX = W*2, TOP_CARTY = 0;

	public static ArrayList<Soldier> soldiers;
	public static HashMap<Coordinate, HashSet<Tile>> tiles;
	private static Queue<Bullet> queueBullets;
	public static Coordinate[][] cmap;
	
	/**
	 * Constructs a World
	 * @param cph content-pane handler
	 */ 
	public World(ContentPaneHandler cph) {
		this.cph = cph;
		this.soldiers = new ArrayList<>();
		cph.setSoldiers(soldiers);
		tiles = new HashMap<Coordinate, HashSet<Tile>>();
		queueBullets = new LinkedList<>();
		cmap = new Coordinate[ROW][COL];
		for (int i = 0; i < cmap.length; i++) for(int j = 0;j< cmap[i].length; j++) cmap[i][j] = new Coordinate(i , j);

		pl = new Player(cph, new Coordinate(2, 2));
		soldiers.add(pl);
		
		generate();
	//	uiAllTiles();
		
		new Thread(() -> {
			for (int i = 0; i < ROW; i++) {
				for (int j = 0; j < COL; j++) {
//					pl.isoMove(i, j);
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
	 		}
		}).start();
		
		
		
	}
	
	/**
	 * Generates a map
	 */
	public void generate() {
		//recursive space partitioning
		PartitionGenerator pag = new PartitionGenerator(ROW, COL, Setup.experimental);
		int[][] map = pag.generate();
		
		PartitionGenerator.print(map);
		
		cph.setPartitionGenerator(pag);
		cph.setPlayer(pl);

		for (int k = 0; k < Setup.players; k ++) {
			soldiers.add(new Enemy(cph, new Coordinate(1 + (Math.random()*(World.COL-1)), 1 + (Math.random()*(World.ROW-1)))));
		}
		
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				Coordinate c = cmap[i][j];
//				
//			}
//		}
		
		//if next tile has wall - draw next base then next wall then current base & current wall
		//https://imageresize.org/
		//https://www.pixilart.com/draw
		//https://onlinepngtools.com/create-transparent-png

		for (int i = ROW - 1; i >= 0; i--) {
			for (int j = COL - 1; j >= 0; j--) {
				System.out.print(i + "," + j + ": " + map[i][j] + "; ");
				Coordinate c = cmap[i][j];
				String[] tdir = {"N","E","W","S"};
				try {
						//wsne1234
					Tile wl = null;
					
					switch(map[i][j]) {
						case 1: wl = new Tile("stoneWallHalf_W", c); break;
						case 2: wl = new Tile("stoneWallHalf_S", c); break;
						case 3: wl = new Tile("stoneWallHalf_N", c); break;
						case 4: wl = new Tile("stoneWallHalf_E", c); break;
						//case 0: new Tile("planks_" + ((Math.random() > 0.5)? "N" : "E"), c); break;
					}
					
					Tile fl = (Math.random() > 0.9 && wl == null)? new Tile("stoneTile_" + tdir[(int) (Math.random()*4)], c) : 
						new Tile("stone_" + tdir[(int) (Math.random()*4)], c); 
					
					if (wl != null) uiTiles(wl, fl); else uiTiles(fl);			
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		Launcher.getLoadingFrame().setVisible(false);
		Launcher.getLoadingFrame().dispose();
	}
	
	/**
	 * Adds tiles to the user interface 
	 * @param c the tiles to place on the screen
	 */
	private void uiTiles(Tile... c) {
		for (Tile v : c) {
			try {
				ImageIcon i = new ImageIcon(ImageIO.read(v.getFsr()));
				//Image ii = i.getImage().getScaledInstance(W, H, Image.SCALE_SMOOTH);
				Rectangle size = new Rectangle(0, 256, 256, 256);
				//comment out //.getScaledInstance(...) if green lines appear. Also in Player!
			//	Image ii = cropImage(ImageIO.read(v.getFn()), size);
				BufferedImage ii = cropToBufferedImage(ImageIO.read(v.getFsr()), size);//.getScaledInstance(W, H, Image.SCALE_SMOOTH);
				//Image ii = cropImgIcon(v.getImgIcon(), size);//.getScaledInstance(W, H, Image.SCALE_SMOOTH);
				JLabel l = new JLabel(new ImageIcon(ii));
				//l.setBorder(new LineBorder(Color.BLACK));
				cph.add(l, 
						W,
						H,
						v.getC().getCartX(), 
						v.getC().getCartY(), 
						false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Places all tiles on the user interface. 
	 */
	private void uiAllTiles() {
		for (HashSet<Tile> c : tiles.values()) {
			for (Tile v : c) {
				try {
					ImageIcon i = new ImageIcon(ImageIO.read(v.getFsr()));
					//Image ii = i.getImage().getScaledInstance(W, H, Image.SCALE_SMOOTH);
					Rectangle size = new Rectangle(0, 256, 256, 256);
					//comment out //.getScaledInstance(...) if green lines appear. Also in Player!
	//				Image ii = cropImage(v.getFn(), size);//.getScaledInstance(W, H, Image.SCALE_SMOOTH);
	/**/			JLabel l = new JLabel(i /*new ImageIcon(ii)*/);
					//l.setBorder(new LineBorder(Color.BLACK));
					cph.add(l, 
							W,
							H,
							v.getC().getCartX(), 
							v.getC().getCartY(), 
							false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		

		Launcher.getLoadingFrame().setVisible(false);
		Launcher.getLoadingFrame().dispose();
//		Launcher.getLoadingFrame().dispatchEvent(
//				new WindowEvent(Launcher.getLoadingFrame(), 
//				WindowEvent.WINDOW_CLOSING));
	}
	
	/**
	 * returns the player
	 * @return the player
	 */
	public Player getPlayer() {
		return pl;
	}
	
	/**
	 * Crops a buffered image
	 * @param f the buffered image
	 * @param crop rectangular region
	 * @return an Image, cropped.
	 * @throws IOException if it couldn't be read
	 */
	public static Image cropImage(BufferedImage f, Rectangle crop) throws IOException {
		BufferedImage ret = new BufferedImage((int)crop.getWidth(), (int)crop.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
		ret.getGraphics().drawImage(f, 
				0, 0, 
				(int)crop.getWidth(), 
				(int)crop.getHeight(), 
				(int)crop.getX(), 
				(int)crop.getY(), 
				(int)(crop.getX()+crop.getWidth()), 
				(int)(crop.getY()+crop.getHeight()), null);
		return ret;
	}
	
	/**
	 * @param src
	 * @param rect
	 * @return
	 */
	public static BufferedImage cropToBufferedImage(BufferedImage src, Rectangle rect) {
		BufferedImage ret = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
		return ret; 
	}
	
	/**
	 * Crops an image icon
	 * @param icon the image icon
	 * @param crop rectangular region
	 * @return an Image, cropped
	 */
	public static Image cropImgIcon(ImageIcon icon, Rectangle crop) {
		Image image = icon.getImage();
		
		BufferedImage ret = new BufferedImage((int)crop.getWidth(), (int)crop.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
		ret.getGraphics().drawImage(image, 
				0, 0, 
				(int)crop.getWidth(), 
				(int)crop.getHeight(), 
				(int)crop.getX(), 
				(int)crop.getY(), 
				(int)(crop.getX()+crop.getWidth()), 
				(int)(crop.getY()+crop.getHeight()), null);
		return ret;
	}
	
	/**
	 * returns a HashMap of tiles
	 * @return the hash map of tiles
	 */
	public static HashMap<Coordinate, HashSet<Tile>> getTiles() {
		return tiles;
	}
	
	/**
	 * returns the list of soldiers, including Player
	 * @return the list of soldiers, including Player
	 */
	public static ArrayList<Soldier> getSoldiers() {
		return soldiers;
	}

	/**
	 * returns the queue of bullets
	 * @return the queueBullets
	 */
	public static Queue<Bullet> getQueueBullets() {
		return queueBullets;
	}

	/**
	 * Sets the queue of bullets
	 * @param queueBullets the queueBullets to set
	 */
	public void setQueueBullets(Queue<Bullet> queueBullets) {
		this.queueBullets = queueBullets;
	}
}

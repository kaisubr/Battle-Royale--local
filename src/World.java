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

public class World {
	private ContentPaneHandler cph;
	private Player pl;
	
	public static final int ROW = 10, COL = 10;
	
	//if you change from 256, you gotta enable scaling
	public static final int W = 256, H = 256;//512/4; 
	public static final int TOP_CARTX = W*2, TOP_CARTY = 0;

	public static ArrayList<Soldier> soldiers;
	public static HashMap<Coordinate, HashSet<Tile>> tiles;
	private static Queue<Bullet> queueBullets;
	public static Coordinate[][] cmap;
	
	public World(ContentPaneHandler cph) {
		this.cph = cph;
		this.soldiers = new ArrayList<>();
		cph.setSoldiers(soldiers);
		tiles = new HashMap<Coordinate, HashSet<Tile>>();
		queueBullets = new LinkedList<>();
		cmap = new Coordinate[ROW][COL];
		for (int i = 0; i < cmap.length; i++) for(int j = 0;j< cmap[i].length; j++) cmap[i][j] = new Coordinate(i , j);

		pl = new Player(cph, new Coordinate(2, 2));
		
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
	
	public void generate() {
		//recursive space partitioning
		PartitionGenerator pag = new PartitionGenerator(ROW, COL);
		int[][] map = pag.generate();
		
		PartitionGenerator.print(map);
		
		cph.setPartitionGenerator(pag);
		cph.setPlayer(pl);

		for (int k = 0; k < 5; k ++) {
			soldiers.add(new Enemy(cph, new Coordinate(Math.random() * World.COL, Math.random() * World.ROW)));
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
	 * @param t tile
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
	
	public Player getPlayer() {
		return pl;
	}
	
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
	
	public static BufferedImage cropToBufferedImage(BufferedImage src, Rectangle rect) {
		BufferedImage ret = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
		return ret; 
	}
	
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
	
	private boolean replaceTile(Tile t) {
		return true;
	}

	public static HashMap<Coordinate, HashSet<Tile>> getTiles() {
		return tiles;
	}
	
	public static ArrayList<Soldier> getSoldiers() {
		return soldiers;
	}

	/**
	 * @return the queueBullets
	 */
	public static Queue<Bullet> getQueueBullets() {
		return queueBullets;
	}

	/**
	 * @param queueBullets the queueBullets to set
	 */
	public void setQueueBullets(Queue<Bullet> queueBullets) {
		this.queueBullets = queueBullets;
	}
}

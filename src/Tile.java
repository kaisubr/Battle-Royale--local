/**
 *Kailash Subramanian, Gallatin
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * A Tile has a file name and a coordinate on the map
 */
public class Tile {
	
	private File fn;
	private Coordinate c;
	private Line2D ln;
	private boolean isWall;
	private boolean n, e, w, s;
	private InputStream fsr;
	private ImageIcon rsrc;
	private BufferedReader bfr;
	private String name;
	
	/**
	 * Constructs Tile from file and coordinate
	 * @param name file name
	 * @param where coordinate
	 * @throws URISyntaxException if file not read
	 */
	public Tile(String name, Coordinate where) throws URISyntaxException {
		
		
		//URL imgURL = /*Tile.class*/ getClass().getResource("/assets/tile/" + name + ".png");
		//fn = new File(imgURL.getFile().replaceAll("%20", " "));//
//		fn = new File(imgURL.toURI().toString());
//		//System.out.println(fn.toString());
//		 
		this.name = name;
		this.fsr = Tile.class.getResourceAsStream("/assets/tile/" + name + ".png");
		//setBfr(new BufferedReader(new InputStreamReader(fsr)));
		
		//ClassLoader cl = this.getClass().getClassLoader();
		//rsrc = new ImageIcon(cl.getResource("/assets/tile/" + name + ".png"));
		
		//fn = new File("src/assets/tile/" + name + ".png");
		////System.out.println(fn.getAbsolutePath());

		c = where;
		
		if (name.contains("Wall")) setWall(true);
		
		if (name.contains("_W")) w = true;
		else if (name.contains("_N")) n = true;
		else if (name.contains("_E")) e = true;
		else if (name.contains("_S")) s  = true;
		
		if (n) ln = new Line2D.Double(c.getCartX(), c.getCartY(), c.getCartX()+128, c.getCartY()+64);
		else if (e) ln = new Line2D.Double(c.getCartX()+128, c.getCartY()+64, c.getCartX(), c.getCartY()+128);
		else if (w) ln = new Line2D.Double(c.getCartX()-128, c.getCartY()+64, c.getCartX(), c.getCartY());
		else if (s) ln = new Line2D.Double(c.getCartX()-128, c.getCartY()+64, c.getCartX(), c.getCartY()+128);
		
		HashMap<Coordinate, HashSet<Tile>> all = World.getTiles();
		HashSet<Tile> tileCol = all.get(where);
		if (tileCol == null) {
			all.put(where, new HashSet<>());
		}

		all.get(where).add(this);
		
		//LinesComponent lc = new LinesComponent();
		//lc.addLine((int)ln.getX1(), (int)ln.getY1(), (int)ln.getX2(), (int)ln.getY2(), Color.BLACK);
//		ContentPaneHandler.add(
//				new JLabel((c.getCartX()+128) + ", " + (c.getCartY()+64) + " to " + c.getCartX() + ", " + (c.getCartY() + 128)),
//				c.getCartX(),
//				c.getCartY()
//				);
	}
	
	/**
	 * Constructs tile from file and coordiante
	 * Deprecated - Coordinates may hold more than 1 tile (i.e. walls)
	 * @param type file name
	 * @param where coordinate
	 * @param replaceIfExists to replace if a tile exists at a certain coordiante
	 */
	@Deprecated
	public Tile(String type, Coordinate where, boolean replaceIfExists) {
		
		if (replaceIfExists || World.getTiles().get(where) != null) {
			//URL imgURL = Tile.class.getResource("/assets/tile/" + name + ".png");
			//fn = new File(imgURL.getFile().replaceAll("%20", " "));
			this.fsr = Tile.class.getResourceAsStream("/assets/tile/" + type + ".png");
			//fn = new File("src/assets/tile/" + type + ".png");
//			File dir = new File("src/assets/");
//			
//			do {
//				fn = dir.listFiles()[new Random().nextInt(dir.listFiles().length)];
//			} while (!fn.isFile());
			
			c = where;
			
			/*World.getTiles().put(where, this);*/
		} else 
			throw new IllegalArgumentException("Tile occupied at " + where.getIsoX() + ", " + where.getIsoY());	
	}
	
	/**
	 * Returns a wall-line segment
	 * @return the Line2D version of segment
	 * @throws NullPointerException if the wall doesn't exist.
	 */
	public Line2D getWallLineSegment() throws NullPointerException {

		////System.out.println(c + "news:"+ n + "" + e +"" + w + "" +s);
		return ln;
	}
	
	/**
	 * return the File
	 * @return the File 
	 */
	public File getFn() {
		return fn;
	}

	/**
	 * return hte input stream
	 * @return hte input stream
	 */
	public InputStream getFsr() {
		return  Tile.class.getResourceAsStream("/assets/tile/" + name + ".png");
	}
	
	/**
	 * return the image icon
	 * @return the image icon
	 */
	public ImageIcon getImgIcon() {
		return rsrc;
	}

	/**
	 * return the coordinate
	 * @return the coordinate
	 */
	public Coordinate getC() {
		return c;
	}

	/**
	 * return if it's a wall
	 * @return if it's a wall
	 */
	public boolean isWall() {
		return isWall;
	}

	/**
	 * Sets if it's a wall
	 * @param isWall is it a wall?
	 */
	public void setWall(boolean isWall) {
		this.isWall = isWall;
	}

	/**
	 * Returns the BufferedReader
	 * @return the buffered reader
	 */
	public BufferedReader getBfr() {
		return bfr;
	}

	/**
	 * Sets a BufferedReader
	 * @param bfr the bfr to set
	 */
	public void setBfr(BufferedReader bfr) {
		this.bfr = bfr;
	}

}


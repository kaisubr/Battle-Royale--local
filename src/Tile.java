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
	
	public Tile(String name, Coordinate where) throws URISyntaxException {
		
		
		//URL imgURL = /*Tile.class*/ getClass().getResource("/assets/tile/" + name + ".png");
		//fn = new File(imgURL.getFile().replaceAll("%20", " "));//
//		fn = new File(imgURL.toURI().toString());
//		System.out.println(fn.toString());
//		
		this.name = name;
		this.fsr = Tile.class.getResourceAsStream("/assets/tile/" + name + ".png");
		//setBfr(new BufferedReader(new InputStreamReader(fsr)));
		
		//ClassLoader cl = this.getClass().getClassLoader();
		//rsrc = new ImageIcon(cl.getResource("/assets/tile/" + name + ".png"));
		
		//fn = new File("src/assets/tile/" + name + ".png");
		//System.out.println(fn.getAbsolutePath());

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
	
	public Line2D getWallLineSegment() throws NullPointerException {

		System.out.println(c + "news:"+ n + "" + e +"" + w + "" +s);
		return ln;
	}
	
	public File getFn() {
		return fn;
	}

	public InputStream getFsr() {
		return  Tile.class.getResourceAsStream("/assets/tile/" + name + ".png");
	}
	
	public ImageIcon getImgIcon() {
		return rsrc;
	}

	public Coordinate getC() {
		return c;
	}

	public boolean isWall() {
		return isWall;
	}

	public void setWall(boolean isWall) {
		this.isWall = isWall;
	}

	/**
	 * @return the bfr
	 */
	public BufferedReader getBfr() {
		return bfr;
	}

	/**
	 * @param bfr the bfr to set
	 */
	public void setBfr(BufferedReader bfr) {
		this.bfr = bfr;
	}

}

class LinesComponent extends JComponent{

	private static class Line{
	    final int x1; 
	    final int y1;
	    final int x2;
	    final int y2;   
	    final java.awt.Color color;
	
	    public Line(int x1, int y1, int x2, int y2, java.awt.Color color) {
	        this.x1 = x1;
	        this.y1 = y1;
	        this.x2 = x2;
	        this.y2 = y2;
	        this.color = color;
	    }               
	}
	
	private final LinkedList<Line> lines = new LinkedList<Line>();
	
	public void addLine(int x1, int x2, int x3, int x4) {
	    addLine(x1, x2, x3, x4, Color.BLACK);
	}
	
	public void addLine(int x1, int x2, int x3, int x4, Color color) {
	    lines.add(new Line(x1,x2,x3,x4, color));        
	    repaint();
	}
	
	public void clearLines() {
	    lines.clear();
	    repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    for (Line line : lines) {
	        g.setColor(line.color);
	        g.drawLine(line.x1, line.y1, line.x2, line.y2);
	        System.out.println("drawn line");
	    }
	}


}


/**
 *Kailash Subramanian, Gallatin
 */

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A JLabel with opacity (alpha value) option
 */
public class AlphaImageLabel extends JLabel {
	private float a = 1;
	private String fName = "";
	private BufferedImage img;
	
	/**
	 * Constructs an alpha-based image JLabel
	 * @param fn the File
	 * @throws IOException if file not found
	 */
	public AlphaImageLabel (File fn) throws IOException {
		//this.fName = fName;
		img = ImageIO.read(fn);
	}
	
	/**
	 * Constructs an alpha-based image JLabel
	 * @param fn the file's name
	 * @throws IOException if file not found
	 */
	public AlphaImageLabel (String fName) throws IOException {
		this.fName = fName;
		img = ImageIO.read(new File(fName));
	}
	
	/**
	 * Constructs an alpha-based image JLabel
	 * @param fsr the input stream (typically buffered input stream)
	 * @throws IOException if can't be read
	 */
	public AlphaImageLabel (InputStream fsr) throws IOException {
		img = ImageIO.read(fsr);
	}
	
	/**
	 * Constructs an alpha-based image JLabel
	 * @param ii an image icon
	 */
	public AlphaImageLabel(ImageIcon ii) {
		// TODO Auto-generated constructor stub
		img = new BufferedImage(ii.getIconWidth(), ii.getIconHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Sets alpha value
	 * @param alp alpha value
	 */
	public void setOpacity(float alp) {
		this.a = alp;
	}
	
	/**
	 * Paints the component
	 */
	@Override
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
 
        Graphics2D g2 = (Graphics2D) g;
        
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
        int x = (getWidth() - img.getWidth())/2;
        int y = (getHeight()- img.getHeight())/2;
        g2.drawRenderedImage(img, AffineTransform.getTranslateInstance(x, y));
        g2.setComposite(old);
    }
	
	/**
	 * Returns the size of img
	 * @return the dimensions of img
	 */
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(img.getWidth(), img.getHeight());
    }
}

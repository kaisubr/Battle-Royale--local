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

public class AlphaImageLabel extends JLabel {
	private float a = 1;
	private String fName = "";
	private BufferedImage img;
	
	public AlphaImageLabel (File fn) throws IOException {
		//this.fName = fName;
		img = ImageIO.read(fn);
	}
	
	public AlphaImageLabel (String fName) throws IOException {
		this.fName = fName;
		img = ImageIO.read(new File(fName));
	}
	
	public AlphaImageLabel (InputStream fsr) throws IOException {
		img = ImageIO.read(fsr);
	}
	
	public AlphaImageLabel(ImageIcon ii) {
		// TODO Auto-generated constructor stub
		img = new BufferedImage(ii.getIconWidth(), ii.getIconHeight(), BufferedImage.TYPE_INT_RGB);
	}

	public void setOpacity(float alp) {
		this.a = alp;
	}
	
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
	
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(img.getWidth(), img.getHeight());
    }
}

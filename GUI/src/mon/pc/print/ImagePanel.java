package mon.pc.print;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
    private BufferedImage image;
    
    public void load(String path) {
		try {
		    load(ImageIO.read(new File(path)));
		} catch (IOException ex) {}
    }
    
    public void load(File file) {
		try {
			load(ImageIO.read(file));
		} catch (IOException ex) {}
    }
    
    public void load(BufferedImage image) {
		this.image = image;
		this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		this.invalidate();
    	this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
    	if (image != null) {
    		g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    	}
    }
}

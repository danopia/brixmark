package mon.scan.pc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Input {

	public static void main(String[] args) throws NXTCommException, IOException {
		LiveView view = new LiveView();
		view.setVisible(true);
		
    	System.out.println("Factorying");
    	NXTComm conn = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
    	System.out.println("Connecting");
    	conn.open(new NXTInfo(NXTCommFactory.BLUETOOTH, "Mon Print Bed", "00:16:53:09:85:9D"));
    	System.out.println("Connected");
		
    	DataInputStream stream = new DataInputStream(conn.getInputStream());
    	
    	//int x = 0;
    	
    	while (true) {
    		int x = stream.readInt();
    		int y = stream.readInt();

    		int sign = stream.readByte();
    		
    		int r = stream.readInt();
    		int g = stream.readInt();
    		int b = stream.readInt();
    		
    		System.out.println(x + ", " + y + " (" + sign + ")\t Red: " + r + "\t Green: " + g + "\t Blue: " + b);
    		
    		Graphics2D graph = (Graphics2D) view.getGraphics();
            graph.setColor(new Color(r, g, b));
            Rectangle2D rect = new Rectangle2D.Float(x - (sign*5), y * 9 / 14, 6, 6);
            graph.fill(rect);
            
            x++;
    	}
	}

}

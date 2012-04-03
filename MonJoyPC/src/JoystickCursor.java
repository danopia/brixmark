import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;


public class JoystickCursor {
    public static void main (String[] args) throws NXTCommException, AWTException, IOException {
    	Robot robot = new Robot();

    	System.out.println("Factorying");
    	NXTComm conn = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
    	System.out.println("Connecting");
    	conn.open(new NXTInfo(NXTCommFactory.BLUETOOTH, "Mon Joy", "00:16:53:03:A0:8C"));
    	System.out.println("Connected");
    	DataInputStream stream = new DataInputStream(conn.getInputStream());
    	System.out.println("Stream open");
    	
    	float cursorX = Toolkit.getDefaultToolkit().getScreenSize().width/2;
    	float cursorY = Toolkit.getDefaultToolkit().getScreenSize().height/2;
    	boolean clicked = false;
    	
    	while (true) {
    		if (clicked != stream.readBoolean()) {
    			clicked = !clicked;
    			
    			if (clicked)
    				robot.mousePress(InputEvent.BUTTON1_MASK);
    			else
    				robot.mouseRelease(InputEvent.BUTTON1_MASK);
    		}
    		
    		int x = stream.readInt();
    		if (x < -2) {
    			cursorX -= Math.pow(((double)x + 2)/90, 2);
    			if (cursorX < 0) cursorX = 0;
    		} else if (x > 2) {
    			cursorX += Math.pow(((double)x + 2)/90, 2);
    			if (cursorX > Toolkit.getDefaultToolkit().getScreenSize().width) cursorX = Toolkit.getDefaultToolkit().getScreenSize().width;
    		}
    		
    		int y = stream.readInt();
    		if (y < -2) {
    			cursorY -= Math.pow(((double)y + 2)/90, 2);
    			if (cursorY < 0) cursorY = 0;
    		} else if (y > 2) {
    			cursorY += Math.pow(((double)y + 2)/90, 2);
    			if (cursorY > Toolkit.getDefaultToolkit().getScreenSize().height) cursorY = Toolkit.getDefaultToolkit().getScreenSize().height;
    		}
    		
    		robot.mouseMove((int)cursorX, (int)cursorY);
    	}
    }
}

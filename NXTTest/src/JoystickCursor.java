import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class JoystickCursor {
    public static void main (String[] args) throws InterruptedException {
    	System.out.println("LOLCODE");
    	Sound.playTone(400, 100);
    	Thread.sleep(100);
    	Sound.playTone(600, 100);
    	Thread.sleep(100);
    	Sound.playTone(800, 100);
        NXTConnection conn = Bluetooth.waitForConnection();
    	System.out.println("ROFLCOPTER");
    	Sound.playTone(800, 100);
    	Thread.sleep(100);
    	Sound.playTone(600, 100);
    	Thread.sleep(100);
    	Sound.playTone(400, 100);
        DataOutputStream dos = conn.openDataOutputStream();

    	TouchSensor ts = new TouchSensor(SensorPort.S4);
    	NXTRegulatedMotor motorX = Motor.C;
    	NXTRegulatedMotor motorY = Motor.A;
    	NXTRegulatedMotor motorC = Motor.B;
    	
    	motorX.flt();
    	motorY.flt();
		motorC.setSpeed(100);
    	
    	//boolean colorFloating = false;
    	boolean colorMoving = false;
    	int lastTaco = 0;
    	int sameFor = 0;
        
        while (Button.ESCAPE.isUp()) {
            try {
            	int offset = motorC.getTachoCount() % 60;
            	if (offset > 30) offset -= 60;
            	System.out.println(offset);
            	if (offset > -5 && offset < 5) {
            		motorC.flt();
            		colorMoving = false;
            	} else if (!colorMoving) {
            		if (motorC.getTachoCount() == lastTaco && sameFor == 15) {
            			Sound.beep();
            			//colorFloating = false;
            			colorMoving = true;
                		sameFor = 0;
            			motorC.rotate(-offset);
            			motorC.stop();
            			Thread.sleep(250);
                		motorC.flt();
            		} else if (lastTaco == motorC.getTachoCount()) {
            			sameFor += 1;
            		} else {
                		//colorFloating = true;
                		lastTaco = motorC.getTachoCount();
                		sameFor = 0;
            		}
            	}
            	
            	dos.writeBoolean(ts.isPressed());
				dos.writeInt(motorX.getTachoCount());
	            dos.writeInt(motorY.getTachoCount());
			} catch (IOException e) {
				System.out.println("Send failed" + (42/0));
			}
        }
    }
}

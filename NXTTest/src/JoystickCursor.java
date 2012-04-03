import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class JoystickCursor {
    public static void main (String[] args) {
    	System.out.println("LOLCODE");
        NXTConnection conn = Bluetooth.waitForConnection();
    	System.out.println("ROFLCOPTER");
        DataOutputStream dos = conn.openDataOutputStream();

    	TouchSensor ts = new TouchSensor(SensorPort.S4);
    	NXTRegulatedMotor motorX = Motor.C;
    	NXTRegulatedMotor motorY = Motor.B;
    	
    	motorX.flt();
    	motorY.flt();
        
        while (Button.ESCAPE.isUp()) {
            try {
            	dos.writeBoolean(ts.isPressed());
				dos.writeInt(motorX.getTachoCount());
	            dos.writeInt(motorY.getTachoCount());
			} catch (IOException e) {
				System.out.println("Send failed" + (42/0));
			}
        }
    }
}

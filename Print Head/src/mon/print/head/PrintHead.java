package mon.print.head;

import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import mon.slave.Slave;

public class PrintHead extends Slave {
	TouchSensor tsA = new TouchSensor(SensorPort.S1);
	TouchSensor tsB = new TouchSensor(SensorPort.S2);

	int stateA = 0, zeroA;
	int stateB = 0, zeroB;

	public static void main(String[] args) throws IOException, InterruptedException {
		PrintHead head = new PrintHead();
		
		do {
			head.run();
		} while (Button.ESCAPE.isUp());
	}

	public int getSlots()  { return 2; }
	public int getStates() { return 3; }

	public String[] getSlotNames()  { return new String[] {"Depressor", "Gripper"}; }
	public String[] getStateNames() { return new String[] {"Open", "Closed"};       }
	
	public void calibrate() {
		Motor.A.setSpeed(360);

		if (tsA.isPressed()) {
			Motor.A.rotate(-360);
		}
		
		Motor.A.forward();
		while (!tsA.isPressed()) {}
		Motor.A.stop();
		
		zeroA = Motor.A.getTachoCount();
		Motor.A.rotateTo(zeroA - 700);
		

		Motor.B.setSpeed(25);

		if (tsB.isPressed()) Motor.B.rotate(-30);
		Motor.B.forward();
		while (!tsB.isPressed()) {}
		Motor.B.stop();
		
		zeroB = Motor.B.getTachoCount();
		Motor.B.rotateTo(zeroB - 15);
	}

	public void setState(int slot, int newState) {
		if (slot == 0) {
			Motor.A.setSpeed(360);
			Motor.A.rotateTo(zeroA - ((newState == 2) ? 2550 : ((newState == 1) ? 1700 : 700)));
	
			sound(stateA, newState);
			stateA = newState;
		} else if (slot == 1) {
			Motor.B.setSpeed(150);
			Motor.B.rotateTo(zeroB - ((newState == 1) ? 60 : 15));

			sound(stateB, newState);
			stateB = newState;
		}
	}

	public NXTConnection connect() {
		return Bluetooth.waitForConnection(0, NXTConnection.PACKET);
	}
	/*{

	public static void main(String[] args) {
		Sound.beep();
		System.out.println("
		Calibrating");
		
		Motor.C.setSpeed(10);
		
		TouchSensor ts = new TouchSensor(SensorPort.S3);

		if (ts.isPressed()) Motor.C.rotate(20);
		
		Motor.C.backward();
		while (!ts.isPressed()) {}
		Motor.C.stop();

		Motor.C.setSpeed(100);
		Motor.C.rotate(20);
		
		boolean state = false;

		while (Button.ESCAPE.isUp()) {
			Sound.twoBeeps();
			System.out.println("Waiting for connection");
			RS485Connection conn = RS485.waitForConnection(0, NXTConnection.PACKET);
			DataInputStream dis = conn.openDataInputStream();
			DataOutputStream dos = conn.openDataOutputStream();
			System.out.println("Connected");
			Sound.playTone(750, 100);
	
			try {
				while (Button.ESCAPE.isUp()) {
					boolean newState;
					newState = dis.readBoolean();
					
					if (state != newState) {
						state = !state;
						if (state) {
							Sound.playTone(700, 5);
							System.out.println("Black pen down");
							Motor.C.rotate(55);
							Sound.playTone(800, 5);
						} else {
							Sound.playTone(800, 5);
							System.out.println("Black pen up");
							Motor.C.rotate(-55);
							Sound.playTone(700, 5);
						}
					}
					
					dos.writeBoolean(state);
					dos.flush();
				}
			} catch (IOException e) {}
		}
	}*/
}

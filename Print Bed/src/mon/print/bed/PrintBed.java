package mon.print.bed;

import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import mon.slave.Slave;

public class PrintBed extends Slave {
	TouchSensor tsA = new TouchSensor(SensorPort.S1);
	TouchSensor tsB = new TouchSensor(SensorPort.S2);
	TouchSensor tsC = new TouchSensor(SensorPort.S3);

	int stateBC = 0, zeroB, zeroC;
	int stateA = 0, zeroA;
	
	EStopTrigger trigger;

	public static void main(String[] args) throws IOException, InterruptedException {
		PrintBed bed = new PrintBed();
		
		do {
			bed.run();
		} while (Button.ESCAPE.isUp());
	}

	public int getSlots()  { return 2; }
	public int getStates() { return 3; }

	public String[] getSlotNames()  { return new String[] {"Page locks", "Marker magazine"}; }
	public String[] getStateNames() { return new String[] {"Open", "Loose", "Locked"}; }
	
	public void calibrate() {
		Motor.A.setSpeed(30);
		Motor.B.setSpeed(75);
		Motor.C.setSpeed(75);

		if (tsB.isPressed()) Motor.B.rotate(30, true);
		if (tsC.isPressed()) Motor.C.rotate(30, true);
		do {} while (Motor.B.isMoving() || Motor.C.isMoving());
		
		Motor.B.backward();
		Motor.C.backward();
		
		while (!tsB.isPressed() || !tsC.isPressed()) {
			if (tsB.isPressed()) Motor.B.stop(true);
			if (tsC.isPressed()) Motor.C.stop(true);
		}
		Motor.B.stop();
		Motor.C.stop();


		if (tsA.isPressed()) Motor.A.rotate(30);
		Motor.A.backward();
		while (!tsA.isPressed()) {}
		Motor.A.stop();

		
		zeroA = Motor.A.getTachoCount();
		zeroB = Motor.B.getTachoCount();
		zeroC = Motor.C.getTachoCount();

		Motor.A.rotateTo(zeroA + 20);
		Motor.B.rotateTo(zeroB + 10);
		Motor.C.rotateTo(zeroC + 10);
	}

	public void setState(int slot, int newState) {
		if (slot == 0) {
			Motor.B.setSpeed(50);
			Motor.C.setSpeed(50);
	
			Motor.B.rotateTo(zeroB + toAngleBC(newState), true);
			Motor.C.rotateTo(zeroC + toAngleBC(newState), true);
			do {} while (Motor.B.isMoving() || Motor.C.isMoving());

			sound(stateBC, newState);
			
			stateBC = newState;
		} else if (slot == 1) {
			Motor.A.setSpeed(100);
			
			Motor.A.rotateTo(zeroA + toAngleA(newState));

			sound(stateA, newState);
			stateA = newState;
		}
	}

	public NXTConnection connect() {
		return Bluetooth.waitForConnection(0, NXTConnection.PACKET);
	}
	
	public void ready() {
		if (trigger != null) {
			trigger.interrupt();
		}
		
		trigger = new EStopTrigger(dos);
		trigger.setDaemon(true);
		trigger.start();
	}
	
	protected int toAngleA(int newState) {
		switch (newState) {
			case 0: return   0;
			case 1: return 240;
			case 2: return 480;
		}
		return 0;
	}
	
	protected int toAngleBC(int newState) {
		switch (newState) {
			case 0: return 15;
			case 1: return 50;
			case 2: return 75;
		}
		return 0;
	}
}

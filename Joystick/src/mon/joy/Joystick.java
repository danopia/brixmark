package mon.joy;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class Joystick {
	BTConnection conn;
	DataInputStream dis;
	DataOutputStream dos;
	
	TouchSensor trigger;
	
	Joystick() {
		trigger = new TouchSensor(SensorPort.S4);
		
		conn = Bluetooth.connect(Bluetooth.getKnownDevice("Mon Print Arm"));
		conn.setIOMode(NXTConnection.PACKET);
		
		dis = conn.openDataInputStream();
		dos = conn.openDataOutputStream();
	}

	public static void main(String[] args) {
		new Joystick();
	}

}

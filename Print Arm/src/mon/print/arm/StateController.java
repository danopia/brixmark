package mon.print.arm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class StateController extends Thread {
	BTConnection conn;
	DataInputStream dis;
	DataOutputStream dos;
	
	String name;
	
	byte[] states = new byte[] {0, 0, 0, 0, 0};
	byte slotCount;
	byte stateCount;
	
	StateController(String name) {
		this.name = name;
	}
	
	public void run() {
    	System.out.println("Connecting to " + name);
		conn = Bluetooth.connect(Bluetooth.getKnownDevice(name));
		conn.setIOMode(NXTConnection.PACKET);
		
		dis = conn.openDataInputStream();
		dos = conn.openDataOutputStream();
    	System.out.println("NXT connected");
    	
    	// Request dimensions
    	try {
			dos.writeByte(3);
			dos.flush();
		} catch (IOException e1) {}

		try {
			while (true) {
				byte type = dis.readByte();
				switch (type) {
				case 1:
					byte slot = dis.readByte();
					byte state = dis.readByte();
					states[slot] = state;
					break;
					
				case 3:
					slotCount = dis.readByte();
					stateCount = dis.readByte();
					break;
					
				case 127:
					EStop.thread.activate = true;
					break;
				}
			}
		} catch (IOException e) {
			Sound.buzz();
			System.out.println("Comm dropped to " + name);
    	}
	}
	
	void setState(int i, int j) {
    	try {
    		dos.writeByte(1);
    		dos.writeByte(i);
    		dos.writeByte(j);
    		dos.flush();
    		
    		while (states[i] != j) {
    			Thread.sleep(15);
    		}
		} catch (IOException ex) {
		} catch (InterruptedException e) {
		}
	}
	
	void close() {
    	try {
    		dos.writeByte(2);
    		dos.flush();

    		dis.close();
    		dos.close();
    		conn.close();
		} catch (IOException ex) {}
	}
	
	void estop() {
    	try {
    		dos.writeByte(127);
    		dos.flush();
		} catch (IOException ex) {}
	}
}

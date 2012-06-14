package mon.slave;
	
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Motor;
import lejos.nxt.NXT;
import lejos.nxt.Sound;
import lejos.nxt.comm.NXTConnection;

public abstract class Slave implements Runnable {

	public abstract int getSlots();
	public abstract int getStates();
	public abstract void setState(int slot, int state);

	public abstract void calibrate();
	public abstract NXTConnection connect();
	
	NXTConnection conn;
	DataInputStream dis;
	protected DataOutputStream dos;
	
	boolean calibrated = false;
	
	public void sound(int oldState, int newState) {
		if (newState > oldState)
			soundUp();
		else if (newState == oldState)
			soundSame();
		else
			soundDown();
	}

	public void soundUp() {
		Sound.playTone(700, 75);
		
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {}
		
		Sound.playTone(800, 50);
	}
	
	public void soundDown() {
		Sound.playTone(800, 75);
		
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {}
		
		Sound.playTone(700, 50);
	}
	
	public void soundSame() {
		Sound.playTone(750, 75);
		
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {}
		
		Sound.playTone(750, 50);
	}
	
	public void ready() {}

	public void run() {
		CancelThread kill = new CancelThread();
		kill.setDaemon(true);	
		kill.start();
		
		Sound.beep();
		
		Button.ENTER.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				calibrated = false;
				Sound.twoBeeps();
			}

			public void buttonReleased(Button b) {}
		});

		while (Button.ESCAPE.isUp()) {
			try {
				Sound.twoBeeps();
				System.out.println("Comm pending");
				conn = connect();
				dis = conn.openDataInputStream();
				dos = conn.openDataOutputStream();
				System.out.println("Connected");
				Sound.playTone(750, 100);
				ready();
				
				if (!calibrated) {
					System.out.println("Calibrating");
					calibrate();
					calibrated = true;
				}
		
				while (Button.ESCAPE.isUp()) {
					switch (dis.readByte()) {
					case 1:
						doStateCommand();
						break;
						
					case 2:
						dis.close();
						dos.close();
						conn.close();
						break;
						
					case 3:
						dos.writeByte(3);
						dos.writeByte(getSlots());
						dos.writeByte(getStates());
						dos.flush();
						break;
						
					case 127:
						Motor.A.flt(true);
						Motor.B.flt(true);
						Motor.C.flt(true);
						
						try {
							Sound.buzz();
							Thread.sleep(15);
							
							for (int i = 1000;i>50;i-=50) {
								Sound.playTone(i, 100);
								Thread.sleep(25-(i/50));
							}
						} catch (InterruptedException e) {}
						
						NXT.shutDown();
					}
				}
			} catch (IOException e) {}
		}
	}
	
	void doStateCommand() throws IOException {
		int slot = dis.readByte();
		int state = dis.readByte();

		if (slot + 1 > getSlots()) return;
		if (state + 1 > getStates()) return;
		
		System.out.println("Slot " + slot + " to " + state);
		setState(slot, state);

		dos.writeByte(1);
		dos.writeByte(slot);
		dos.writeByte(state);
		dos.flush();
	}
}

package mon.print.arm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Sound;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class Printer implements Runnable {
	NXTConnection conn;
	DataInputStream dis;
	protected DataOutputStream dos;

	PrintField field;
	EStop eStop;
	
	int marker = -1;

	public static void main(String[] args) {
		new Printer().run();
	}
	
	public Printer() {
		field = new PrintField();
		
		eStop = new EStop(field);
		eStop.setDaemon(true);
		eStop.start();
	}
	
	public void ack(byte cmd) {
		try {
			dos.writeByte(126);
			dos.writeByte(cmd);
			dos.flush();
		} catch (IOException e) {}
	}

	public void run() {
		Sound.twoBeeps();
		System.out.println("PC comm pending");
		conn = Bluetooth.waitForConnection(0, NXTConnection.PACKET);
		dis = conn.openDataInputStream();
		dos = conn.openDataOutputStream();
		System.out.println("PC connected");
		Sound.playTone(950, 100);
		
		field.connect();
		
		try {
			while (true) {
				byte cmd = dis.readByte();
				System.out.println(cmd);
				switch (cmd) {
				
				case 1: // lock page; prep arm
					field.bed.setState(1, 0);
					field.bed.setState(0, 2);
					field.calibrate();
					field.moveYTo(450);
					break;
					
				case 2: // select colour
					field.head.setState(0, 0);
					
					int oldX = field.x;
					int oldY = field.y;
					
					if (marker >= 0) {
						field.moveYTo(450);
						field.moveXTo(marker * 270 - 20);
						field.moveYTo(0);

						field.head.gripOff();
						
						field.moveYTo(450);
					}
					
					marker = dis.readByte();
					
					if (marker >= 0) {
						field.moveYTo(450);
						field.moveXTo(marker * 270 - 20);
						field.moveYTo(0);

						field.head.gripOn();
						
						field.moveYTo(450);
					}

					field.moveYTo(oldY);
					field.moveXTo(oldX);
					
					break;
					
				case 3: // go to X,Y
					field.moveXTo(750 - dis.readInt());
					field.moveYTo(1000 + dis.readInt());
					break;
					
				case 4: // marker pressure
					field.head.setState(0, dis.readByte());
					break;
					
				case 5: // finish paper
					if (marker >= 0) {
						field.moveYTo(450);
						field.moveXTo(marker * 270 - 20);
						field.moveYTo(0);

						field.head.gripOff();
						
						field.moveYTo(450);
						
						marker = -1;
					}
					
					field.bed.setState(0, 0);
					break;
					
				case 6: // graceful exit
					field.close();
					break;
					
					
				case 127: // estop
					EStop.thread.activate = true;
					break;
				}
				
				ack(cmd);
			}
		} catch (IOException e) {
			
		}
		
		
		/*
		field.head.setState(1, 1);
		field.moveY( 2000);
		field.head.setState(0, 1);
		field.moveX(  200);
		field.moveY( -600);
		field.moveX( -200);
		field.moveY(  600);
		field.head.setState(0, 0);
		field.moveY(-2000);
		field.head.setState(1, 0);

		field.moveY(  450);
		field.moveX(  270);
		field.moveY( -450);
		*/
		
		/*
		double i = 0;
		while (Button.ESCAPE.isUp() && i < (Math.PI*2)) {
			double x = (int)(150*1.61*Math.cos(i)) + (field.width / 2);
			double y = (int)(150*1.00*Math.sin(i)) + (field.height / 2);
			field.moveTo((int)x, (int)y);

			i += 0.05;
		}
		*/
	}

}

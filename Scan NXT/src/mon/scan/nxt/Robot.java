package mon.scan.nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.ColorHTSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.Color;

public class Robot {
	NXTConnection conn;
	DataInputStream dis;
	DataOutputStream dos;
	
	ColorHTSensor cs;
	
	NXTRegulatedMotor LMotor, RMotor, xMotor;

	public static void main(String[] args) {
		while (Button.ESCAPE.isUp()) {
			try {
				Robot bot = new Robot();
				bot.run();
				bot.close();
			} catch (IOException e) {}
			
			Motor.A.flt(true);
			Motor.B.flt(true);
			Motor.C.flt(true);
		}
	}
	
	Robot() {
		Sound.beep();
		System.out.println("Waiting for connection");
		conn = Bluetooth.waitForConnection(0, NXTConnection.PACKET);
		dis = conn.openDataInputStream();
		dos = conn.openDataOutputStream();
		System.out.println("Connected");
		Sound.playTone(750, 100);
		
		cs = new ColorHTSensor(SensorPort.S1);

		xMotor = Motor.A;
		RMotor = Motor.B;
		LMotor = Motor.C;

		xMotor.setSpeed(135);
		RMotor.setSpeed(90);
		LMotor.setSpeed(90);
	}
	
	void run() throws IOException {
		int sign = 1;
		
		while (Button.ESCAPE.isUp()) {
			int y = RMotor.getTachoCount();
			
			xMotor.rotate(sign*720, true);
			
			while (Button.ESCAPE.isUp() && xMotor.isMoving()) {
				Color color = cs.getColor();

				dos.writeInt(xMotor.getTachoCount());
				dos.writeInt(y);
				dos.writeByte(sign);
				dos.writeInt(color.getRed());
				dos.writeInt(color.getGreen());
				dos.writeInt(color.getBlue());
				dos.flush();
			}

			sign *= -1;

			RMotor.rotate( 5, true);
			LMotor.rotate(-5, true);
			
			while (RMotor.isMoving() || LMotor.isMoving()) {}
		}
	}
	
	void runOld() throws IOException {
		RMotor.forward();
		LMotor.backward();
		
		while (Button.ESCAPE.isUp()) {
			Color color = cs.getColor();

			dos.writeInt(color.getRed());
			dos.writeInt(color.getGreen());
			dos.writeInt(color.getBlue());
			dos.flush();
		}

		RMotor.flt(true);
		LMotor.flt(true);
	}
	
	void close() throws IOException {
		dis.close();
		dos.close();
		conn.close();
	}
}

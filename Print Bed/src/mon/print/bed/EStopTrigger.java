package mon.print.bed;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class EStopTrigger extends Thread {
	DataOutputStream dos;
	
	TouchSensor button;
	
	EStopTrigger(DataOutputStream dos) {
		this.dos = dos;
	}
	
	public void run() {
		System.out.println("EStop ready");
		
		button = new TouchSensor(SensorPort.S4);
		try {
			while (!button.isPressed()) {
				Thread.sleep(15);
			}
			
			dos.writeByte(127);
			dos.flush();
		} catch (InterruptedException e) {
		} catch (IOException e) {
		}

		System.out.println("EStop down");
	}
}

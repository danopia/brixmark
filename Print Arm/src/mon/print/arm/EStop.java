package mon.print.arm;

import lejos.nxt.Motor;
import lejos.nxt.NXT;
import lejos.nxt.Sound;

public class EStop extends Thread {
	PrintField field;
	boolean activate = false;
	static EStop thread;
	
	EStop(PrintField field) {
		this.field = field;
	}
	
	public void run() {
		thread = this;
		
		System.out.println("EStop ready");

		try {
			while (!activate) {
				Thread.sleep(15);
			}
		} catch (InterruptedException e2) {}
		
		field.estop = true;
		
		Motor.A.flt(true);
		Motor.B.flt(true);
		Motor.C.flt(true);

		field.head.estop();
		field.bed.estop();
		
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

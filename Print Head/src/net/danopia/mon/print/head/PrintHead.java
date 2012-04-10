package net.danopia.mon.print.head;

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class PrintHead {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		Motor.B.setSpeed(180);
		Motor.C.setSpeed(180);
		
		double i = 0;
		double x = 300;
		double y = 0;
		
		while (Button.ESCAPE.isUp()) {
			i += 0.05;
			double newx = (int)(300*Math.sin(i));
			double newy = (int)(500*Math.cos(i));

			double velX = (newx - x) / 0.05;
			double velY = (newy - y) / 0.05;
			Motor.B.setSpeed((int) velX);
			Motor.C.setSpeed((int) velY);
			x = newx;
			y = newy;
			//do {} while (Motor.B.isMoving() || Motor.C.isMoving());
			//System.out.println((int)(50*Math.sin(i)) + (int)(50*Math.cos(i)));

			if (velX > 0)
				Motor.B.forward();
			else
				Motor.B.backward();

			if (velY > 0)
				Motor.C.forward();
			else
				Motor.C.backward();
			
			Thread.sleep(50);
		}
		
		Motor.B.stop();
		Motor.C.stop();
	}

}

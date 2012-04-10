package net.danopia.mon.printbed;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class PrintBed {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Motor.B.setSpeed(70);
		Motor.C.setSpeed(70);
		
		TouchSensor tsB = new TouchSensor(SensorPort.S2);
		TouchSensor tsC = new TouchSensor(SensorPort.S3);

		if (tsB.isPressed()) Motor.B.rotate(20, true);
		if (tsC.isPressed()) Motor.C.rotate(20, true);
		do {} while (Motor.B.isMoving() || Motor.C.isMoving());
		
		Motor.B.backward();
		Motor.C.backward();
		
		while (!tsB.isPressed() || !tsC.isPressed()) {
			if (tsB.isPressed()) Motor.B.stop();
			if (tsC.isPressed()) Motor.C.stop();
		}
		Motor.B.stop();
		Motor.C.stop();
		
		
		Motor.B.setSpeed(30);
		Motor.C.setSpeed(30);

		Button.waitForAnyPress();
		Motor.B.rotate(100, true);
		Motor.C.rotate(100, true);
		do {} while (Motor.B.isMoving() || Motor.C.isMoving());

		
		Motor.B.setSpeed(70);
		Motor.C.setSpeed(70);

		Button.waitForAnyPress();
		Motor.B.rotate(-25, true);
		Motor.C.rotate(-25, true);
		do {} while (Motor.B.isMoving() || Motor.C.isMoving());

		Button.waitForAnyPress();
		Motor.B.rotate(-75, true);
		Motor.C.rotate(-75, true);
		do {} while (Motor.B.isMoving() || Motor.C.isMoving());
		
		do {
			if (Button.LEFT.isDown()) {
				Motor.B.backward();
				Motor.C.backward();
				while (Button.LEFT.isDown()) {}
				Motor.B.stop();
				Motor.C.stop();
			}
			if (Button.RIGHT.isDown()) {
				Motor.B.forward();
				Motor.C.forward();
				while (Button.RIGHT.isDown()) {}
				Motor.B.stop();
				Motor.C.stop();
			}
		} while (Button.ESCAPE.isUp());
	}
}

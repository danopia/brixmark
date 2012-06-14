package mon.print.arm;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class PrintField {
	int width = 800, height = 3000;
	int x = 0, y = 0;
	int zeroYL = 0, zeroYR = 0, zeroX = 0;
	
	boolean estop = false;

	NXTRegulatedMotor xMotor  = Motor.A;
	NXTRegulatedMotor yMotorL = Motor.B;
	NXTRegulatedMotor yMotorR = Motor.C;
	
	TouchSensor xSensor = new TouchSensor(SensorPort.S3);
	TouchSensor ySensor = new TouchSensor(SensorPort.S1);

	PrintHead head = new PrintHead();
	PrintBed  bed  = new PrintBed();
	
	public void calibrate() {
		// up here so the markers don't get tangled when x calibrates
		yMotorL.setSpeed(720);
		yMotorR.setSpeed(720);
		if (ySensor.isPressed()) moveY(750);
		yMotorL.setSpeed(360);
		yMotorR.setSpeed(360);

		xMotor.setSpeed(90);
		if (xSensor.isPressed()) xMotor.rotate(-360);
		xMotor.forward();
		while (!xSensor.isPressed() && Button.ESCAPE.isUp()) {}
		xMotor.stop();

		zeroX  =  xMotor.getTachoCount();
		xMotor.rotateTo(zeroX + 20);

		yMotorL.backward();
		yMotorR.backward();
		while (!ySensor.isPressed() && Button.ESCAPE.isUp()) {}
		yMotorL.flt(true);
		yMotorR.flt(true);
		while (yMotorL.isMoving() || yMotorR.isMoving()) {};
		
		x = 0;
		y = 0;

		zeroYL = yMotorL.getTachoCount();
		zeroYR = yMotorR.getTachoCount();
		
		//moveX(width);
		
		/*try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {}*/
	}

	void connect() {
		bed.setDaemon(true);
		bed.start();
		while (bed.conn == null) {}
		
		head.setDaemon(true);
		head.start();
		while (head.conn == null) {}
	}
	
	void close() {
		bed.close();
		head.close();
	}
	
	void moveBy(double degreesX, double degreesY) {
		if (estop) return;
		
		while (xMotor.isMoving() || yMotorL.isMoving() || yMotorR.isMoving()) {};
		
		degreesX /= 1.61;
		
		boolean posX = (degreesX >= 0);
		boolean posY = (degreesY >= 0);

		degreesX = Math.abs(degreesX);
		degreesY = Math.abs(degreesY);
		
		//double distance = Math.sqrt(Math.pow(degreesX, 2) + Math.pow(degreesY, 2));
		double timedLength;
		double slope = (degreesY / degreesX);
		
		boolean toX = (degreesX > degreesY);
		if (toX) {
			xMotor.setSpeed((int)(90 * 1.61)); // * 1.61
			yMotorL.setSpeed((int)(90 * slope));
			yMotorR.setSpeed((int)(90 * slope));
			timedLength = degreesX;
			System.out.println("90   " + 90*slope);
		} else {
			xMotor.setSpeed((int)(90 * 1.61 / slope)); // * 1.61
			yMotorL.setSpeed(90);
			yMotorR.setSpeed(90);
			timedLength = degreesY;
			System.out.println(90*slope + "    90");
		}
		
		if (!posX)
			xMotor.backward();
		else
			xMotor.forward();
		
		if (!posY) {
			yMotorL.backward();
			yMotorR.backward();
		} else {
			yMotorL.forward();
			yMotorR.forward();
		}
		
		try {
			Thread.sleep((long)(timedLength / 90 * 1000));
		} catch (InterruptedException e) {}
		
		stop();
	}
	
	void moveX(int degrees) {
		if (estop) return;
		while (xMotor.isMoving()) {};
		xMotor.setSpeed(180);

		x += degrees;
		xMotor.rotateTo(-x + zeroX, true);
		while (xMotor.isMoving() && !estop) {};
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
	}
	
	void moveXTo(int degrees) {
		if (estop) return;
		while (xMotor.isMoving()) {};
		xMotor.setSpeed(180);

		x = degrees;
		xMotor.rotateTo(-x + zeroX, true);
		while (xMotor.isMoving() && !estop) {};
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
	}
	
	void moveY(int degrees) {
		if (estop) return;
		while (yMotorL.isMoving() || yMotorR.isMoving()) {};
		yMotorL.setSpeed(360);
		yMotorR.setSpeed(360);

		y += degrees;
		yMotorL.rotateTo(y + zeroYL, true);
		yMotorR.rotateTo(y + zeroYR, true);
		while ((yMotorL.isMoving() || yMotorR.isMoving()) && !estop) {};
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
	}
	
	void moveYTo(int degrees) {
		if (estop) return;
		while (yMotorL.isMoving() || yMotorR.isMoving()) {};
		yMotorL.setSpeed(360);
		yMotorR.setSpeed(360);

		y = degrees;
		yMotorL.rotateTo(y + zeroYL, true);
		yMotorR.rotateTo(y + zeroYR, true);
		while ((yMotorL.isMoving() || yMotorR.isMoving()) && !estop) {};
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
	}
	
	void moveTo(int newX, int newY) {
		if (estop) return;
		System.out.println(x + " " + y + ":" + newX + " " + newY);
		
		if (newX < 0) newX = 0;
		if (newY < 0) newY = 0;
		if (newX >= width) newX = width;
		if (newY >= height) newY = height;
		
		double velX = (newX - x) / 0.05;
		double velY = (newY - y) / 0.05;
		
		Motor.A.setSpeed((int) velX);
		Motor.B.setSpeed((int) velY);
		Motor.C.setSpeed((int) velY);
		
		if (velY > 0) {
			Motor.B.backward();
			Motor.C.backward();
		} else if (velY < 0) {
			Motor.B.forward();
			Motor.C.forward();
		} else {
			Motor.B.stop(true);
			Motor.C.stop(true);
		}

		if (velX > 0)
			Motor.A.forward();
		else if (velX < 0)
			Motor.A.backward();
		else
			Motor.A.stop(true);

		//double delayX = Math.abs(velX * (newX - x));
		//double delayY = Math.abs(velY * (newY - y));
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {}

		x = newX;
		y = newY;
	}
	
	void stop() {
		Motor.A.stop(true);
		Motor.B.stop(true);
		Motor.C.stop(true);
		Motor.A.stop();
		Motor.B.stop();
		Motor.C.stop();
	}
}

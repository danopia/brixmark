package mon.servo;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;

@SuppressWarnings("unused")
public class Servo {
	private NXTRegulatedMotor motor;
	private int zero = 0, minimum = 0, maximum = 360;
	private double ratio = 1;

	public Servo(NXTRegulatedMotor motor, int minimum, int maximum, double ratio) {
		this.motor = motor;
		this.minimum = minimum;
		this.maximum = maximum;
		this.ratio = ratio;
	}
	
	public void calibrateBy(TouchSensor ts, int speed) {
		motor.setSpeed(speed);
		if (ts.isPressed()) motor.rotate(-speed);
		
		zero = motor.getTachoCount();
	}
	
	
	protected void startRotate(int speed) {
		double position = (motor.getTachoCount() - zero) / ratio;
		
		motor.setSpeed(speed);
		if (speed > 0)
			motor.forward();
		else if (speed < 0)
			motor.backward();
		else
			motor.stop(true);
	}
	
	private double getPosition() {
		return (motor.getTachoCount() - zero) / ratio;
	}
}

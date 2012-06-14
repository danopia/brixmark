import lejos.nxt.Button;
import lejos.nxt.Motor;

public class BreakEverything {
	public static void main(String[] args) {
		Motor.B.setStallThreshold(360, 60000000);
		Motor.B.setSpeed(25);
		Motor.B.rotate(-45);
		
		Motor.A.setSpeed(90);
		while (Button.ESCAPE.isUp()) {
			if (Button.RIGHT.isDown()) {
				Motor.A.backward();
			} else if (Button.LEFT.isDown()) {
				Motor.A.forward();
			} else {
				Motor.A.stop(true);
			}
		}
	}
}

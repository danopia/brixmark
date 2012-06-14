import lejos.nxt.NXT;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;

public class SoundTest {

	public static void main(String[] args) throws InterruptedException {
		TouchSensor ts = new TouchSensor(SensorPort.S4);
		while (!ts.isPressed()) {}
		
		Sound.buzz();
		Thread.sleep(15);
		
		for (int i = 1000;i>50;i-=50) {
			Sound.playTone(i, 100);
			Thread.sleep(25-(i/50));
		}
		
		NXT.shutDown();
	}

}

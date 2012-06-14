package mon.slave;

import lejos.nxt.Button;
import lejos.nxt.NXT;

public class CancelThread extends Thread {
	public void run() {
		Button.ESCAPE.waitForPress();
		NXT.shutDown();
	}
}

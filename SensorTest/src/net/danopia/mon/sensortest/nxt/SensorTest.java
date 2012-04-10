package net.danopia.mon.sensortest.nxt;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorHTSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.Color;

public class SensorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RConsole.openAny(0);
		
		ColorHTSensor cs = new ColorHTSensor(SensorPort.S1);
		
		do {
			Color c = cs.getColor();
			System.out.println(c.getRed() + "  " + c.getGreen() + "  " + c.getBlue());
			RConsole.println(c.getRed() + "  " + c.getGreen() + "  " + c.getBlue());
		} while (Button.ESCAPE.isUp());
	}

}

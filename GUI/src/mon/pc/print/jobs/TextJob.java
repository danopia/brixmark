package mon.pc.print.jobs;

import mon.pc.print.Connection;

public class TextJob {
	String text;
	int x;
	
	public TextJob(String text) {
		this.text = text;
	}
	
	public void printTo(Connection comm) {
		comm.sendSimple(1);
		
	    comm.switchMarker(0);

	    x = 0;
		for (char letter : text.toUpperCase().toCharArray()) {
			drawLetter((Character)letter, comm);
		}
		
		comm.switchMarker(-1);
		comm.sendSimple(5);
	}
	
	protected void drawLetter(char letter, Connection comm) {
		switch (letter) {
		case 'D':
			comm.goTo(x,       0);
		    comm.markerPress(2);
		    comm.goTo(x+100,   0);
		    comm.goTo(x+100, 400);
		    comm.goTo(x,     400);
			comm.goTo(x,       0);
		    comm.markerPress(1);
		    x += 110;
		    break;

		case 'R':
			comm.goTo(x,     400);
		    comm.markerPress(2);
		    comm.goTo(x,       0);
		    comm.goTo(x+100,   0);
		    comm.goTo(x+100, 200);
		    comm.goTo(x,     200);
		    comm.goTo(x+100, 200);
		    comm.goTo(x+100, 400);
		    comm.markerPress(1);
		    x += 110;
		    break;

		case 'E':
			comm.goTo(x+100,   0);
		    comm.markerPress(2);
		    comm.goTo(x+100,   0);
		    comm.goTo(x,     200);
		    comm.goTo(x+80, 200);
		    comm.goTo(x,     200);
		    comm.goTo(x,     400);
		    comm.goTo(x+100, 400);
		    comm.markerPress(1);
		    x += 110;
		    break;

		case 'X':
			comm.goTo(x,       0);
		    comm.markerPress(2);
		    comm.goTo(x+100,   0);
		    comm.goTo(x+50,   0);
		    comm.goTo(x+50, 400);
		    comm.goTo(x+100, 400);
		    comm.goTo(x,     400);
		    comm.markerPress(1);
		    x += 110;
		    break;

		case 'L':
			comm.goTo(x,       0);
		    comm.markerPress(2);
		    comm.goTo(x,     400);
		    comm.goTo(x+50, 400);
		    comm.markerPress(1);
		    x += 60;
		    break;

		case 'H':
			comm.goTo(x,   0);
		    comm.markerPress(2);
			comm.goTo(x,   400);
			comm.goTo(x,   200);
			comm.goTo(x+100,   200);
			comm.goTo(x+100,   400);
		    comm.goTo(x+100,   0);
		    comm.markerPress(1);
		    x += 110;
		    break;

		case 'O':
			comm.goTo(x,   0);
		    comm.markerPress(2);
			comm.goTo(x,   400);
			comm.goTo(x+100,   400);
		    comm.goTo(x+100,   0);
			comm.goTo(x,   0);
		    comm.markerPress(1);
		    x += 110;
		    break;
		}
	}
}

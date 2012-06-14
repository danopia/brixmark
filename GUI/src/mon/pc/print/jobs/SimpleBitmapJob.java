package mon.pc.print.jobs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import mon.pc.print.Connection;
import mon.pc.print.Stroke;

public class SimpleBitmapJob {
	public HashMap<Integer, ArrayList<Stroke>> strokes = new HashMap<Integer, ArrayList<Stroke>>();
	
	public SimpleBitmapJob(int markers) {
    	for (int i = -1; i < markers; i++) {
    		strokes.put(i, new ArrayList<Stroke>());
    	}
	}

	public void printTo(Connection comm) {
		comm.sendSimple(1);
		
		for (int marker : strokes.keySet()) {
			if (marker < 0) break;
			
		    comm.switchMarker(marker);

			for (Stroke stroke : strokes.get(marker)) {
				comm.goTo(stroke.start*5, stroke.offset*20);
			    comm.markerPress(2);
				comm.goTo(stroke.end*5, stroke.offset*20);
			    comm.markerPress(1);
			}
		    comm.markerPress(0);
		}
		
		comm.switchMarker(-1);
		comm.sendSimple(6);
	}

	public void printTo(Graphics2D g) {
		for (int marker : strokes.keySet()) {
			if (marker < 0) break;
			
		    g.setColor((marker==2)?Color.BLUE : (marker==1)?Color.RED:Color.BLACK);

			for (Stroke stroke : strokes.get(marker)) {
				g.drawLine(stroke.start+100, stroke.offset+100, stroke.end+100, stroke.offset+100);
			}
		}
		g.dispose();
	}
}

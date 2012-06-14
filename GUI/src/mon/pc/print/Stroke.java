package mon.pc.print;

public class Stroke {
	public int marker, offset, start, end;
	
	Stroke(int marker, int offset, int start, int end) {
		this.marker = marker;
		this.offset = offset;
		this.start  = start;
		this.end    = end;
	}
}

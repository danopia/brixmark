package mon.scan.pc;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class LiveView extends JFrame {
    
    /**
     * Creates a new instance of Java2DFrame
     */
    public LiveView() {
    	super("BrixMark Scan Window");
    	
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    	this.setBounds(50, 50, 800, 800);
    }
}
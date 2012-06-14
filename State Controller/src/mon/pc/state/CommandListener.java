package mon.pc.state;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

public class CommandListener implements ActionListener {
	DataOutputStream stream;
	
	public CommandListener(DataOutputStream stream) {
    	this.stream = stream;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		StateButton button = ((StateButton) e.getSource());
		
    	try {
    		if (button.state == 127) {
	    		stream.writeByte(127);
		    	stream.flush();
    		} else {
	    		stream.writeByte(1);
				stream.writeByte(button.object);
		    	stream.writeByte(button.state);
		    	stream.flush();
    		}
		} catch (IOException ex) {}
	}

}

package mon.pc.state;

import java.awt.Color;

import javax.swing.JButton;

public class StateButton extends JButton {
	private static final long serialVersionUID = 116056954389048981L;
	
	public byte object, state;

	public StateButton(byte object, byte state) {
		super((state == 127) ? "E-Stop" : Byte.toString(state));
		
		if (state == 127) {
			this.setForeground(Color.WHITE);
			this.setBackground(Color.RED);
		}
		
		this.object = object;
		this.state = state;
	}
}

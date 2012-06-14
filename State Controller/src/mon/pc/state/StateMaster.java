package mon.pc.state;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

class Gui extends JFrame {
	private static final long serialVersionUID = -5033969259661710788L;

	public Gui(String s, NXTComm conn) throws IOException {
    	super(s);
    	JPanel bigpan = new JPanel();

    	DataInputStream dis = new DataInputStream(conn.getInputStream());
    	DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
    	CommandListener l = new CommandListener(dos);
    	
    	dos.writeByte(3);
    	dos.flush();

    	dis.readByte();
    	byte slots = dis.readByte();
    	byte states = dis.readByte();
    	
        bigpan.setLayout(new BoxLayout(bigpan, BoxLayout.PAGE_AXIS));
        
        for (byte obj = 0; obj < slots; obj++) {
            JPanel pan = new JPanel();
            pan.add(new JLabel("Object " + Byte.toString(obj)));
        	
            for (byte state = 0; state < states; state++) {
                JButton b = new StateButton(obj, state);
                b.addActionListener(l);
                pan.add(b);
            }
            bigpan.add(pan);
        }

        JButton b2 = new StateButton((byte) 127, (byte) 127);
        b2.addActionListener(l);
        bigpan.add(b2);
        
        add(bigpan);
    }
} //class Gui

public class StateMaster {
	public static void main(String[] args) throws NXTCommException, IOException, InterruptedException {
    	System.out.println("Factorying");
    	NXTComm conn = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
    	System.out.println("Connecting");
    	
    	//conn.open(new NXTInfo(NXTCommFactory.BLUETOOTH, "Mon Print Bed",  "00:16:53:09:85:9D"));
    	conn.open(new NXTInfo(NXTCommFactory.BLUETOOTH, "Mon Print Head", "00:16:53:07:E4:13"));
    	//conn.open(new NXTInfo(NXTCommFactory.BLUETOOTH, "Mon Print Arm",  "00:16:53:19:F6:0B"));
    	
    	System.out.println("Connected");

		Gui screen = new Gui("Mon State Control", conn);
		screen.setSize(300, 200);
		screen.setVisible(true);
		
		/*
    	System.out.println("0");

    	stream.writeByte(1);
    	stream.writeByte(0);
    	stream.writeByte(1);
    	stream.flush();

    	System.out.println("1");
    	Thread.sleep(25000);
    	System.out.println("2");stream

    	stream.writeByte(1);
    	stream.writeByte(0);
    	stream.writeByte(0);
    	stream.flush();
    	
    	System.out.println("3");

    	stream.writeByte(2);
    	stream.flush();
    	*/
	}

}

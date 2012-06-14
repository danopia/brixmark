package mon.pc.print;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Connection {
	NXTComm conn;
	DataInputStream dis;
	DataOutputStream dos;
	
	public void connect(String name, String addr) {
    	try {
        	System.out.println("Connecting to NXT");
        	
        	conn = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			conn.open(new NXTInfo(NXTCommFactory.BLUETOOTH, name, addr));
	    	dis = new DataInputStream (conn.getInputStream());
	    	dos = new DataOutputStream(conn.getOutputStream());
	    	
        	System.out.println("NXT connected");
		} catch (NXTCommException e) {}
	}
	
	public void sendSimple(int cmd) {
		try {
			dos.writeByte(cmd);
			dos.flush();

			dis.readByte();
			dis.readByte();
		} catch (IOException e) {}
	}
	
	public void switchMarker(int marker) {
		try {
			dos.writeByte(2);
			dos.writeByte(marker);
			dos.flush();

			dis.readByte();
			dis.readByte();
		} catch (IOException e) {}
	}
	
	public void goTo(int x, int y) {
		try {
			dos.writeByte(3);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.flush();

			dis.readByte();
			dis.readByte();
		} catch (IOException e) {}
	}
	
	public void markerPress(int level) {
		try {
			dos.writeByte(4);
			dos.writeByte(level);
			dos.flush();

			dis.readByte();
			dis.readByte();
		} catch (IOException e) {}
	}
	
	public void estop() {
		try {
			dos.writeByte(127);
			dos.flush();
			
			dos.close();
			dis.close();
			conn.close();
		} catch (IOException e) {}
	}
}

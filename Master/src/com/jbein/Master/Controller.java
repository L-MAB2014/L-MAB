package com.jbein.Master;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

public class Controller {
	private Model mModel;
	private View mView;
	
	Controller() {
		mModel = new Model();
		mView = new View(mModel);
		
		NXTConnector conn = new NXTConnector();
		
		conn.addLogListener(new NXTCommLogListener() {
			public void logEvent(String message) {
				System.out.println("BTSend Log.listener: "+message);
			}

			public void logEvent(Throwable throwable) {
				System.out.println("BTSend Log.listener - stack trace: ");
				throwable.printStackTrace();				
			}
		});
		
		boolean connected = conn.connectTo("btspp://");
		
		if (!connected) {
			System.err.println("Failed to connect to any NXT");
			System.exit(1);
		}
		
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		DataInputStream dis = new DataInputStream(conn.getInputStream());
		
		try {
			System.out.println("Sending Test-String: This is a String!");
			dos.writeChars("This is a String!");
			dos.flush();
		} 
		catch (IOException e) {
			System.out.println("IO Exception writing bytes:");
			System.out.println(e.getMessage());
		}
		
		try {
			dis.close();
			dos.close();
			conn.close();
		} catch (IOException ioe) {
			System.out.println("IOException closing connection:");
			System.out.println(ioe.getMessage());
		}		
	}
}

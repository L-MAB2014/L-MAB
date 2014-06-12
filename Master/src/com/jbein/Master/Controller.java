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
		String tmp = mModel.str;
		
		while(true) {
			if(!tmp.equals(mModel.str)) {
				btsend(mModel.str);
				tmp = mModel.str;
			}
		}
	}
	
	private void btsend(String sendMe) {
		NXTConnector connection = BTConnect("btspp://");
		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
		DataInputStream dis = new DataInputStream(connection.getInputStream());
				
		try {
			mView.lbSendInfo.setText("Send: " + sendMe);
			dos.writeUTF(sendMe);
			dos.flush();			
			
		} catch (IOException ioe) {
			System.out.println("IO Exception writing String:");
			System.out.println(ioe.getMessage());
		}
		
		try {
			mView.lbReceiveInfo.setText("Receive: " + dis.readUTF());
		} catch (IOException ioe) {
			System.out.println("IO Exception reading String:");
			System.out.println(ioe.getMessage());
		}
		
		try {
			dis.close();
			dos.close();
			connection.close();
		} catch (IOException ioe) {
			System.out.println("IOException closing connection:");
			System.out.println(ioe.getMessage());
		}
	}
	
	private NXTConnector BTConnect(String address) {
		NXTConnector connection = new NXTConnector();
		connection.addLogListener(new NXTCommLogListener() {
			public void logEvent(String message) {
				System.out.println("BTSend Log.listener: "+message);
			}

			public void logEvent(Throwable throwable) {
				System.out.println("BTSend Log.listener - stack trace: ");
				throwable.printStackTrace();	
			}		
		});
		boolean connected = connection.connectTo(address);
		if(!connected) {
			System.err.println("Failed to connect to any NXT");
			return null;
		}
		return connection;
	}

}
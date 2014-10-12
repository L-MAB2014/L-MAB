package com.marcel.TestMaster;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTConnector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

public class BT {
	
	
	private NXTConnector connection;
	private DataOutputStream dos;
	
	private String bt_Name;
	
	public DataInputStream dis;
	
	private IController controller;
	
	private InputChannel input;
	
	private boolean connect;
	
	
	BT(String bt_Name, IController controller)
	{
		this.bt_Name = bt_Name;
		this.controller = controller;
		this.input = new InputChannel();
		this.connect = false;
	}
	
	
	public boolean ConnectAgent()
	{
		try{
				this.connection = CreateAndConnect("btspp://"+this.bt_Name);
				
				if(this.connection != null)
				{
					this.dos = new DataOutputStream(connection.getOutputStream());			
					this.dis = new DataInputStream(connection.getInputStream());
					this.connect = true;
					input.start();
					return true;
				}else
				{
					this.connect = false;
					return false;
				}
			
		}catch(Exception e)
		{
			controller.InputConsole(bt_Name+": "+e);
			this.connect = false;
			return false;
		}
	}
	
	private void ResetForNewConnect()
	{
		this.connection = null;
		this.dos = null;
		this.dis= null;
		this.connect = false;
		this.input = new InputChannel();
	}
	
	private static NXTConnector CreateAndConnect(String address) {
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
	
	public void CloseAgent()
	{
		try {
				this.connect = false;
				this.connection.close();
				this.dis.close();
				this.dos.close();
				this.input.stop();
				
				
		} catch (Exception e) {
			
			controller.InputConsole(bt_Name+": "+e);
		}finally
		{
			this.ResetForNewConnect();
		}
		
	}
	
	public boolean SendMessage(String message)
	{
		try {
				dos.write(message.getBytes());
				dos.flush();
				
				return true;
				
		} catch (Exception e) {
			
			controller.InputConsole(bt_Name+": "+e);
			return false;
		}
	}
	
	public String MessageFromBot()
	{
		try {
				String message= "";
				char c = (char) this.dis.readByte();
				while(c != '#' )
				{					
					 message += c;
				     c = (char) this.dis.readByte();
				}	
												
				return message;
				
		} catch (Exception e) {
			
			controller.InputConsole(bt_Name+ ": " +e);
			return null;
		}
	}
	
	public boolean isConnect()
	{
		return this.connect;
	}
	
	private class InputChannel extends Thread {
		
		public void run()
		  {	 
			     
			try {
				controller.InputConsole(bt_Name+": Input-Cahnnel gestartet");
				 	while(connect)
				 	{
				 		String message = MessageFromBot();
				 		if(message != null && message != "")
				 		{
				 			controller.InputConsole(message);
				 			//Verwalten?!?
				 		}			
				 	
				 	}
				 	
			 } catch (Exception e) {
					
				 controller.InputConsole(bt_Name+": "+e);
			 }
				controller.InputConsole(bt_Name+": Input-Cahnnel beendet");
						
		  }		
		
	}

}

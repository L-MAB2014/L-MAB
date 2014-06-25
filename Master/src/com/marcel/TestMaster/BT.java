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
	public DataInputStream dis;
	
	
	BT()
	{
		
	}
	
	
	public boolean ConnectAgent()
	{
		try{
				this.connection = CreateAndConnect("btspp://");
				
				if(this.connection != null)
				{
					this.dos = new DataOutputStream(connection.getOutputStream());			
					this.dis = new DataInputStream(connection.getInputStream());
					
					return true;
				}else
				{
					return false;
				}
			
		}catch(Exception e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	private void ResetForNewConnect()
	{
		this.connection = null;
		this.dos = null;
		this.dis= null;
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
				connection.close();
				dis.close();
				dos.close();
				
		} catch (Exception e) {
			
			System.out.println(e);
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
			
			System.out.println(e);
			return false;
		}
	}
	
	public String MessageFromAgent()
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
			
			System.out.println(e);
			return null;
		}
	}
	
	public boolean isConnect()
	{
		return connection.connectTo("btspp://");
	}
	

}

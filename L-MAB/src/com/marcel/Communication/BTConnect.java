package com.marcel.Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BTConnect {
	
	private final int disconnect_time = 5000;
		
	
	private BTConnection btc;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private InputChannel input;
	
	private OutputChannel output;
	
	private boolean isConnect;
	
	private OrderManagement manager;
	
	BTConnect(OrderManagement manager)
	{
		this.isConnect = false;
		this.manager = manager;
	}
	
	
	public boolean Connection()		
	{
		try{
			LCD.drawString("WAITE..", 0, 1);
			this.input = new InputChannel();
		 	this.output= new OutputChannel();
			this.btc = Bluetooth.waitForConnection();
			this.dis = btc.openDataInputStream();
			this.dos = btc.openDataOutputStream();
			LCD.drawString("VERBUNDEN", 0, 1);
			this.input.start();
		//	this.output.start();
			
			this.isConnect = true;
			
			return true;
			
		}catch(Exception e)
		{
			this.Close();			
			return false;
		}
		
		
	}
	
	public void Close()
	{
		try {
			this.input.CloseInput();
		//	this.output.CloseInput();
			this.dos.close();
			this.dis.close();
			this.btc.close();
			
			this.ResetForNewStart();
		} catch (IOException e1) {
			
			//Fehlermeldung
			//"Fehler mein schlieﬂen der Verbindung";
		}
	}
	
	private void ResetForNewStart()
	{
		this.input = null;
		this.output = null;
		this.btc = null;
		this.dis = null;
		this.dos = null;
	}
	
	private void FullMessage(String message)
	{							
		this.manager.addOrder(Protokoll.StringToMessage(message));

	}
	
	public synchronized void SendPosition(String m)
	{
		try{
			m += "#";
			dos.write(m.getBytes());
			dos.flush();
			
		}catch(Exception e)
		{
			
		}
	}
	
	
	private class InputChannel extends Thread {
	
		private String message= "";
		
		private boolean disconnect;
		
		InputChannel ()
		{
			disconnect = false;
		}
		
		public void run()
		  {	 
			     
			try {
				 long time_last_input = System.currentTimeMillis();
				 
				 
				  LCD.drawString("INPUT START", 0, 4);
			//		while(((System.currentTimeMillis()-time_last_input) < disconnect_time) && !this.disconnect )
				  while(!this.disconnect )
				    {
						while(dis.available() != 0 && !this.disconnect)
						{
							
							char c = (char) dis.readByte();
							
							if(c != '+')
							{
								if(c == '#')
								{
									FullMessage(message);
									message= "";
								}else
								{
									message+=c;
								}
							} 
						}
			    	}
					LCD.drawString("INPUT ENDE", 0, 4);
					
				
			 } catch (Exception e) {
					
					
			 }
			
			
		  }
		
		public void CloseInput()
		{
			this.disconnect=true;
		}
	}
	
	
	private class OutputChannel extends Thread {
					
		private boolean disconnect;
		
		OutputChannel()
		{
			disconnect = false;
		}
		
		public void run()
		  {	 
			     
			try {
				 long lastsend = System.currentTimeMillis();
				 				 
					while(!this.disconnect )
				    {
						if((System.currentTimeMillis()-lastsend)>5000)
						{
							String m1 = "HUNTER#";

							dos.write(m1.getBytes());
							dos.flush();
						
							lastsend = System.currentTimeMillis();
						}
			    	}
				
			 } catch (Exception e) {
					
				 LCD.drawString(""+e, 0, 1);
			 }
			
			
		  }
		
		public void CloseInput()
		{
			this.disconnect=true;
		}
	}

}

package com.jbein.BTReceive;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BTReceive {
	public static void main(String [] args)  throws Exception {
        String showMe = "";
        
		while(true) {
			LCD.clear();
			LCD.drawString(showMe, 0, 1);
			LCD.refresh();
			
	        BTConnection btc = Bluetooth.waitForConnection();
			DataInputStream dis = btc.openDataInputStream();
			DataOutputStream dos = btc.openDataOutputStream();
			
			showMe = dis.readUTF();
			LCD.drawString(showMe, 0, 1);
			LCD.refresh();
			
			dos.writeUTF(showMe);
			dos.flush();
			
			dis.close();
			dos.close();
			btc.close();
			Thread.sleep(500);
		}
	}
}

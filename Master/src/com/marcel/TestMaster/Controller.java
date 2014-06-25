package com.marcel.TestMaster;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Controller {
	
	private View view;
	private BT bt;
	private InputChannel input;
	
	private boolean connect;
	
	Controller()
	{
		this.view = new View();
		this.view.addBTNConnectListener(new addBTNConnectListener());
		this.view.addBTNSendListener(new addBTNSendListener());
		
		this.bt = new BT();
		
		this.input = new InputChannel();
		
		this.connect =false;
	}
	
	public Message GetStoreSelection()
	{
		if(this.view.store_blue.isSelected())
			return new Message("S","1");
		else if(this.view.store_green.isSelected())
			return new Message("S","2");
		else if(this.view.store_yellow.isSelected())
			return new Message("S","3");
		else
			return null;
	}
	
	
	public Message GetExitSelection() 
    {					
		if(this.view.exit_orange.isSelected())
			return new Message("E","1");
		else if(this.view.exit_pink.isSelected())
			return new Message("E","2");
		else
			return null;		
		
    } 
	
		
    
    class addBTNConnectListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	
        	view.btn_connect.setEnabled(false);
        	if(!connect)
	        {	
	        	view.InputDialog("Verbindungsaufbau wird gestartet");
	        		        	
	        	if(bt.ConnectAgent())
	        	{
	        		connect = true;
	        		view.SetModus(true);
	        		input.start();
	        		view.InputDialog("Verbindung hergestellt!");
	        	}else
	        	{
	        		view.InputDialog("KEINE Verbindung");
	        	}
	        		        		        	
	        }else
	        {
	        	view.InputDialog("Verbindung trennen");
	        	connect = false;
	        	bt.CloseAgent();
	        	view.SetModus(false);
	        	view.InputDialog("Verbindung getrennt");
	        	
	        }
        	view.btn_connect.setEnabled(true);
	        
	       
        }
    }
    
    class addBTNSendListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	
        	Message store = GetStoreSelection();
        	Message exit = GetExitSelection();
        	        	
        	if(store != null && exit != null)
        	{
        		List <Message> list = new ArrayList<Message>();
        		list.add(store);
        		list.add(exit);
        		
        		String message= Protokoll.MessageToString(list);
		    	if(bt.SendMessage(message))
		    		view.InputDialog("Nachricht "+message+" wurde erfolgreich gesendet");
		    	else
		    		view.InputDialog("Fehler beim senden der Nachricht "+message);
        	}else
        		view.InputDialog("Lager und Ausgang wählen!");
        	
        }
    }
    
    private class InputChannel extends Thread {
    			
		public void run()
		  {	 
			     
			try {
					System.out.println("Input-Cahnnel gestartet");
				 	while(connect)
				 	{
				 		String message = bt.MessageFromAgent();
				 		if(message != null && message != "")
				 		{
				 			view.InputDialog(message);
				 		}			
				 	
				 	}
				 	
			 } catch (Exception e) {
					
				 System.out.println(e);
			 }
			System.out.println("Input-Cahnnel beendet");
						
		  }		
		
	}
    
    
    public static void main(String [] args)
	{
		new Controller();
	}

}

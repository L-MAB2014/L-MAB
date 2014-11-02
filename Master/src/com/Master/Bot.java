package com.Master;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


public class Bot implements IBot{
	
	/**
	 * Controller für informationen
	 */
	private IController controller;
	
	/**
	 * Bluetooth-Objekt für die Verbindung
	 */
	private BT bt;
	
	/**
	 * Verbindungsstatus
	 */
	private boolean connect;
	
	private boolean puffer_info;
	
	
	private boolean inPuffer;
	
	/**
	 * Gibt an ob sich der Bot in einer Warteschlange befindet
	 */
	private boolean inWaitList;
	
	/**
	 * Name des Bots
	 */
	private String bt_Name;	

	/**
	 * Order-Verwaltung
	 */
	private OrderManagement order_management;
	
	/**
	 * Status
	 */
	private String status;
	
	/**
	 * Ziel des Bots
	 */
	private String target;
	
	/**
	 * Aktuelle Position
	 */
	private String checkpoint;
	
	/**
	 * Letzte Position
	 */
	private String last_checkpoint;
	
	/**
	 * Nächste Position
	 */
	private String next_checkpoint;
	
	
	/**
	 * Message zum Senden nachdem der Bot 
	 * aus der Warteschlange dran kommt und 
	 * zum Bot sendet 
	 */
	private Message m_waitList;
	
	/**
	 * ID des Bots
	 */
	private int id;
	
	/**
	 * Park/Start- Position des Bots
	 */
	private String park_position;
	
		
	
	Bot(IController controller, String bt_Name, int id, String park)
	{
		this.id = id;
		this.park_position = park;
		
		this.controller = controller;		
		this.bt_Name = bt_Name;
		this.bt = new BT(this.bt_Name, this.controller, this);
		this.order_management = new OrderManagement();
		this.status = "Nicht Verbunden";
		this.target = "-";
		this.checkpoint = "-";
		this.last_checkpoint = "-";
		
		this.connect = false;
		this.inWaitList = false;
		this.puffer_info = false;
		this.inPuffer = false;
	}
	
	/**
	 * Baut eine Verbindung zum Roboter auf
	 * @return
	 */
	public boolean Connect()
	{

    	if(!connect)
        {	
    		this.controller.InputConsole("Verbindungsaufbau wird gestartet");
        		        	
        	if(bt.ConnectAgent())
        	{
        		connect = true;
        		this.controller.InputConsole("Verbindung hergestellt!");
        		this.status = "Verbunden";
        		this.InfoUpdate();
        		return true;
        	}else
        	{
        		this.controller.InputConsole("KEINE Verbindung");
        	}
        		        		        	
        }else
        {
        	this.controller.InputConsole("Verbindung trennen");
        	connect = false;
        	bt.CloseAgent();
        	this.controller.InputConsole("Verbindung getrennt");        	
        }
    	
    	return false;

	}
	
	/**
	 * Nimmt eine neue Order entegen und sendet sie zum Roboter
	 * @param order Neue order des Bots
	 */
	public void NewOrder(Order order)
	{
		this.order_management.add(order);
		String message= Protokoll.OrderToString(order);
		this.InfoUpdate();
		
    	if(bt.SendMessage(message))
    		this.controller.InputConsole("Nachricht "+message+" wurde erfolgreich gesendet");
    	else
    		this.controller.InputConsole("Fehler beim senden der Nachricht "+message);
		
	}
	
	/**
	 * Updatet die Position des Bots
	 * @param m Message mit der neuen Position
	 */
	private void UpdateCheckpoint(String check, String next_check)
	{
		this.last_checkpoint = this.checkpoint;
		this.checkpoint = check;
		this.next_checkpoint = next_check;
		this.controller.UpdateMap(this.checkpoint, this.last_checkpoint, this.bt_Name);   	 	
	}
	
	/**
	 * Updatet die Position des Bots
	 * @param m Message mit der neuen Position
	 */
	private void Entrance(String check)
	{
		this.last_checkpoint = this.checkpoint;
		this.checkpoint = check;
		this.controller.UpdateMap(this.checkpoint, this.last_checkpoint, this.bt_Name);  
		controller.InputConsole((this.bt_Name+" bereit zum Laden"));
		bt.SendMessage(Protokoll.MessageToString((new Message (MasterData.code_Conintue, MasterData.code_Load)))); 
	}
	
	
	/**
	 * Überprüft ob der Bot weiterfahren kann und sendet dies dann dem Roboter zu
	 */
	private void CheckAndSendForContinue()
	{
		if(this.controller.CheckForConintue(checkpoint, next_checkpoint, this))
		{			    	
			controller.InputConsole((this.bt_Name+": Checkpoint:"+ this.next_checkpoint +" Freigeben"));
			if(this.puffer_info)	
			{
				List<Message> message = new ArrayList<Message>();
				message.add((new Message (MasterData.code_Conintue, this.next_checkpoint)));
				message.add((new Message (MasterData.code_Reserved, MasterData.code_Conintue)));
				
				bt.SendMessage(Protokoll.MessageToString(message));  
				this.puffer_info = false;
			}
			else
				bt.SendMessage(Protokoll.MessageToString((new Message (MasterData.code_Conintue, this.next_checkpoint))));   
			controller.InputConsole((this.bt_Name+ "Freigabe gesendet"));
		}else
		{
			controller.InputConsole((this.bt_Name+": Checkpoint:"+ this.next_checkpoint +" nicht Freigeben"));
			this.inWaitList = true;
			this.m_waitList = new Message (MasterData.code_Conintue, this.next_checkpoint);
		}		
	}
	
	
	/**
	 * Führt die Aktionen weiter bevor der Bot in die Warteschlange kam
	 * @return
	 */
	public boolean ContinueAfterWaitList()
	{
		if( this.inWaitList && this.m_waitList != null)
		{
			controller.InputConsole((this.bt_Name+": Checkpoint:"+ this.next_checkpoint +" Freigeben"));
			bt.SendMessage(Protokoll.MessageToString(this.m_waitList));   
			controller.InputConsole((this.bt_Name+ "Freigabe gesendet"));
			
			this.inWaitList = false;
			this.m_waitList = null;
			
			return true;
		}else if(this.inPuffer)
		{
			bt.SendMessage(Protokoll.MessageToString((new Message (MasterData.code_Reserved, MasterData.code_Conintue))));
			this.inPuffer = false;
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.Master.IBot#HandleMessageInput(java.lang.String)
	 */
	public void HandleMessageInput(String message)
	{
		if(message != null && message != "")
 		{
			controller.InputConsole(this.bt_Name + " (Eingang) : "+ message );
			
			List<Message> m = Protokoll.StringToMessage(message);
 			
 			if (m.size() == 2)
 			{
 				 Message m1 = m.get(0);
 				 Message m2= m.get(1);
 				 
 				 if(m1.getKey().equals(MasterData.code_Checkpoint) && m2.getKey().equals(MasterData.code_NextCheckpoint))
 				 {
 					this.UpdateCheckpoint(m1.getValue(), m2.getValue());
 					this.CheckAndSendForContinue();
 				 }
 				 else if(m1.getKey().equals(MasterData.code_Checkpoint) && m2.getKey().equals(MasterData.code_PostionLoad))
 				 {
 					if(m1.getValue().equals(m2.getValue()))
 					{
 						this.Entrance(m1.getValue());
 					}
 				 }else if(m1.getKey().equals(MasterData.code_FinishLoad) && m2.getKey().equals(MasterData.code_NextCheckpoint))
 				 {
 					if(m1.getValue().equals(this.checkpoint))
 					{
 						this.next_checkpoint = m2.getValue();
 						this.CheckAndSendForContinue();
 					}
 				 }  								
 			}else if(m.size() == 3)
 			{
 				Message m1 = m.get(0);
				Message m2= m.get(1);
				Message m3= m.get(2);
				
				if(m1.getKey().equals(MasterData.code_Checkpoint) && m2.getKey().equals(MasterData.code_NextCheckpoint) && m3.getKey().equals(MasterData.code_TestTarget))
				 {
					if(controller.TestEntranceForPuffer(m3.getValue()))  // Prüfen ob Lager belegt ist
					{
						if(controller.EntranceReserved(m3.getValue()))	// Lager Reservieren!	
						{
							this.UpdateCheckpoint(m1.getValue(), m2.getValue());
							this.puffer_info = true;
							this.CheckAndSendForContinue();
							this.InfoUpdate();
							return;
						}			
					}
					
					if(controller.IsNextPufferFree(m1.getValue())) // Prüfen ob ein nächtser Puffer frei ist
					{
						this.UpdateCheckpoint(m1.getValue(), m2.getValue());						
						this.CheckAndSendForContinue();
						this.InfoUpdate();
						return;
					}else
					{
						controller.ToPufferAndSetOnWaitList(m1.getValue(), m3.getValue());						
						bt.SendMessage(Protokoll.MessageToString((new Message (MasterData.code_ToPuffer, this.bt_Name))));  					
						this.inPuffer = true;
					}

				 }
				
				
 			}			
 			this.InfoUpdate();
 		}
	}
	
	
	/**
	 * 
	 */
	public void SendParkPosition()
	{
		bt.SendMessage(Protokoll.MessageToString((new Message (MasterData.code_ParkPosition, this.park_position)))); 
		controller.InputConsole((this.bt_Name+": Park Position "+ this.park_position +" gesendet"));
	}
		
	
	/**
	 * Updatet die Informationen des Bots in der Benutzeroberfläche (Tabelle)
	 */
	public void InfoUpdate()
	{
		this.controller.UpdateTable(this.id, new String []{this.bt_Name,this.status, this.target, this.checkpoint, ""+this.order_management.size()});
	}
	
	/**
	 * Gibt den Verbindungsstatus des Bots an
	 * @returnVerbindungsstatus
	 */
	public boolean isConnect()
	{
		return this.bt.isConnect();
	}
	
	/**
	 * Gibt den Namen das Bots an
	 * @return Bot-Name
	 */
	public String getBt_Name() {
		return bt_Name;
	}
	
	/**
	 * Gibt den Namen des Checkpoints an wo der Bot sich befindet.
	 * @return Checkpoint
	 */
	public String getCheckpoint() {
		return checkpoint;
	}

}

package com.marcel.TestMaster;

public class Bot {
	
	private IController controller;
	
	private BT bt;
	
	private boolean connect;
	
	private String bt_Name;	

	private OrderManagement order_management;
	
	private String status;
	
	private String target;
	
	private String position;
	
	private int id;
	
		
	
	Bot(IController controller, String bt_Name, int id)
	{
		this.id = id;
		
		this.controller = controller;		
		this.bt_Name = bt_Name;
		this.bt = new BT(this.bt_Name, this.controller);
		this.order_management = new OrderManagement();
		this.status = "Nicht Verbunden";
		this.target = "-";
		this.position = "-";
				
		this.connect = false;
		
	}
	
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
	
	public void NewOrder(Order order)
	{
		this.order_management.add(order);
		String message= Protokoll.MessageToString(order);
		this.InfoUpdate();
		
    	if(bt.SendMessage(message))
    		this.controller.InputConsole("Nachricht "+message+" wurde erfolgreich gesendet");
    	else
    		this.controller.InputConsole("Fehler beim senden der Nachricht "+message);
		
	}
	
	
	public void InfoUpdate()
	{
		this.controller.UpdateTable(this.id, new String []{this.bt_Name,this.status, this.target, this.position, ""+this.order_management.size()});
	}
	
	public boolean isConnect()
	{
		return this.bt.isConnect();
	}
	
	public String getBt_Name() {
		return bt_Name;
	}

}

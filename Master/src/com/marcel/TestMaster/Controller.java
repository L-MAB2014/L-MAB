package com.marcel.TestMaster;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Controller implements IController {
	
	private View2 view;
	
	private List<Bot> bots;
	
	private Controller controller;
	
	private int order_ID;
	
			
	Controller()
	{
		this.controller = this;
		this.order_ID =0;
		this.view = new View2();
		this.view.addControlBotListener(new ControlBotListener());
		this.view.addNewBotListener(new NewBotListener());
		this.view.addConrtolOrderListener(new ControlOrderListener());
		this.view.addNewOrderListener(new NewOrderListener());
		this.view.addStoppListener(new StoppListener());
		
		this.bots = new ArrayList<Bot>();
							
		
	}
		
	public void InputConsole( String text)
	{
		view.InputDialog(text);
	}
	 
	
	public void UpdateTable( int row, String [] text)
	{
		view.UpdateTable(row, text);
	}
	
	public boolean ExitsBot (String name)
	{
		for(int i = 0 ; i < this.bots.size(); ++i)
		{
			if(name.equals(this.bots.get(i).getBt_Name()))
				return true;
		}
		return false;
	}
	
    class ControlBotListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        	InputConsole("Der Bots-Verwalten-Button wurde betätigt");
        }
    }
    
    class NewBotListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        	String bot_name = view.GetBotName();
        	
        	if(bot_name.trim().length() != 0)
        	{
	        	if(!ExitsBot(bot_name))
	        	{
        			Bot bot = new Bot(controller,bot_name, bots.size());
        			view.InputTable(new String [] {""+bots.size(),bot_name,"","","",""});
        			
	        		if(bot.Connect())
	        		{	        			
	            		bot.InfoUpdate();
	        			bots.add(bot);
	        			
	        		}else
	        		{
	        			view.DeleteFromTable(bots.size());
	        			InputConsole("Fehlermeldung konnte keine Verbindung hergestellt werden");
	        			//Fehlermeldung konnte keine Verbindung hergestellt werden
	        		}
	        	}else
	        	{
	        		InputConsole("Bot mit dem Namen existiert bereits!");
	        	}
        	}else
        	{
        		InputConsole("Fehlermeldung : Keine Eingabe");
        		//Fehlermeldung : Keine Eingabe
        	}
        	
        	
        }
    }
    
    class ControlOrderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        	InputConsole("Der Aufträge-Verwalten-Button wurde betätigt");
        }
    }
    
    class NewOrderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        	int store = view.GetStoreSelection();
        	int exit = view.GetExitSelection();
        	        	        	
        	if(store != 0 && exit != 0)
        	{
        		int id = order_ID++;
        		     		
        		Order new_order = new Order(id,store,exit);
        		
        		if(bots.size() >0)
        		{
        			bots.get(0).NewOrder(new_order);
        		}else
        		{
        			view.InputDialog("Keine Bots vorhanden");
        		}
        		
        	}else
        		view.InputDialog("Lager und Ausgang wählen!");
        	       
        }
    }
    
    class StoppListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        	InputConsole("Der Stopp-Button wurde betätigt");
        }
    }
         
    public static void main(String [] args)
	{
		new Controller();
	}

}

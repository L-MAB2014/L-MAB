package com.Master;

import com.Simulator.Simulator;
import com.View.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.*;

public class Controller implements IController, IStockInput {

	/**
	 * logger
	 */
	private static org.apache.log4j.Logger logger = LogManager.getLogger("Controller");
	
    /**
     * Benutzeroberfläche
     */
    private View view;

    /**
     * Liste der enthaltenen Bots
     */
    private List<Bot> bots;

    /**
     * Speichert sich selbst als Objekt ab
     */
    private Controller controller;
    
    private Simulator simulator;

    /**
     * Enthüllt alle Checkpoints des Lagers
     */
    private HashMap<String, Checkpoint> checkpoints;
    
    /**
     * Enthüllt alle Ein und Ausg�nge des Lagers
     */
    private HashMap<String, Stock> stocks;

    //TODO Kann später Weg
    private int order_ID;

    //TODO Kann später Weg
    private int counter;

    Controller() {
        this.controller = this;
        this.order_ID = 0;
        this.counter = 0;
        this.view = new View();
        
        logger.info("Oberflaeche wird ge�ffnet und und die Actionlistener gesetzt");
        
        this.view.addNewBotListener(new NewBotListener());
        this.view.addNewOrderListener(new NewOrderListener());
        this.view.addStoppListener(new StoppListener());

        this.view.addSimulationStartListener(new SimulationStartListener());
        this.view.addSimulationOeffnenListener(new SimulationOeffnenListener());
        this.view.addSimulationSpeichernListener(new SimulationSpeichernListener());
        this.view.addSimulationEinstellungenListener(new SimulationEinstellungenListener());
        
        logger.info("Checkpoints (Map) werden Initialisiert");
        this.checkpoints = CreatCheckpoints.InitializeCheckpoints();
        this.bots = new ArrayList<Bot>();
        
        this.stocks = new HashMap<String, Stock>();
        
        this.stocks.put("PL1", new Stock(this.checkpoints.get("PL1")));
        this.stocks.put("PL2", new Stock(this.checkpoints.get("PL2")));
        this.stocks.put("PL3", new Stock(this.checkpoints.get("PL3")));
        
        this.stocks.put("PU1", new Stock(this.checkpoints.get("PU1")));
        this.stocks.put("PU2", new Stock(this.checkpoints.get("PU2")));
        
        this.simulator = new Simulator(this);
    }

    public static void main(String[] args) {
    	logger.info("Mastrer(Controller) wird gestartet!");
        new Controller();
    }

    /* (non-Javadoc)
     * @see com.Master.IController#InputConsole(java.lang.String)
     */
    public void InputConsole(String text) {
    	logger.info("Der Text  "+ text + " wurde in die Console eingef�gt");
    	view.InputDialog(text);
    }

    /* (non-Javadoc)
     * @see com.Master.IController#UpdateTable(int, java.lang.String[])
     */
    public void UpdateTable(int row, String[] text) {
        view.UpdateBotTable(row, text);
    }

    /* (non-Javadoc)
     * @see com.Master.IController#UpdateMap(java.lang.String, java.lang.String, java.lang.String)
     */
    public void UpdateMap(String checkpoint, String last_checkpoint, String bot_name) {
    	logger.info("Der Bot "+ bot_name + " wird auf der grafischen Oberflaeche von Checkpunkt"+ last_checkpoint +" auf "+checkpoint+" gesetzt");
        view.UpdateCheckpoint(checkpoint, last_checkpoint, bot_name);
    }

    /**
     * ÜberprÜft ob ein Bots schon mit dem übergebenen-Namen existiert
     *
     * @param name Name des Bots
     * @return Ergebnis der überprüfung
     */
    public boolean ExitsBot(String name) {
        for (int i = 0; i < this.bots.size(); ++i) {
            if (name.equals(this.bots.get(i).getBt_Name()))
                return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.Master.IController#CheckForContinue(java.lang.String)
     */
    public synchronized boolean CheckForContinue(String checkpoint, String next_checkpoint, Bot bot) {
        Checkpoint check = checkpoints.get(checkpoint);

        if (check != null) {
            Checkpoint checknext = check.getNext_WayCheckpoint();
            Checkpoint checknext2 = check.getNext_OtherCheckpoint();

            if (checknext != null && checknext.getName().equals(next_checkpoint)) {
                return this.EditCheckpointsForContinue(check, checknext, bot);
            } else if (checknext2 != null && checknext2.getName().equals(next_checkpoint)) {
                return this.EditCheckpointsForContinue(check, checknext2, bot);
            } else {
                logger.info("Der n�chste Checkpunkt von "+ bot.getBt_Name() + " (" + next_checkpoint + ") ist nicht vorhanden");
            }

        } else {
        	logger.info("Der aktuelle Checkpunkt von "+ bot.getBt_Name() + " (" + next_checkpoint + ") ist nicht vorhanden");
        }
        return false;
    }

    public synchronized void SetOnWaitList(String check, Bot bot) {
    	
    	Checkpoint checkpoint = checkpoints.get(check);
    	
    	if(checkpoint != null)
    	{
    		logger.info("Bot "+ bot.getBt_Name() +" wird in die Warteschlange von Checkpunkt "+ checkpoint.getName()+"  gesetzt");
    		checkpoint.setBotOnWaitList(bot);
    	}
    }

    private synchronized boolean EditCheckpointsForContinue(Checkpoint checkpoint, Checkpoint next_checkpoint, Bot bot) {
    	logger.info("Bot "+ bot.getBt_Name() +" pr�ft ob Checkpoint "+ next_checkpoint.getName()+"  gesperrt ist");
    	
    	if (!next_checkpoint.isClosed()) {
    		logger.info("Bot "+ bot.getBt_Name() +" sperrt den  Checkpoint "+ next_checkpoint.getName());
        	next_checkpoint.setClosedBot(bot);
        	this.view.UpdateClosedpoint(next_checkpoint.getName(), true);
        	logger.info("Warteschlange des Checkpunkts  "+ checkpoint.getName() + " von Bot "+ bot.getBt_Name() +" wird ueberprueft");
           
        	if (checkpoint.isBotInWaitList()) {
            	logger.info("Bots in Warteschlange des Checkpunkts  "+ checkpoint.getName());
                
//            	if (checkpoint.isStoreOrExit()) {
//            		
//                    checkpoint.setClosedBot(null);
//                    checkpoint.setReservedBot(null);
//                    
//                    this.view.UpdateClosedpoint(checkpoint.getName(), false);
//                    Bot waitBot = checkpoint.getFirstOnWaitList();
//                    
//                    if(!waitBot.IsinPuffer())
//                    {
//                    	Checkpoint puffer = checkpoints.get(waitBot.GetPuffer());
//                    	if(puffer != null)
//                    		puffer.setReservedBot(null);
//                    }
//                    
//                    checkpoint.setReservedBot(waitBot);
//                    logger.info("Bots "+waitBot.getBt_Name()+" wird aus der Warteschlange des Checkpunkts  "+ checkpoint.getName()+ "geholt!");
//                    waitBot.ContinueAfterWaitList();
//                    logger.info("Checkpunkts  "+ checkpoint.getName()+" f�r Bot "+waitBot.getBt_Name()+" freigeben");
//                    
//                } else {
                	
                    WorkOffWaitList w = new WorkOffWaitList(checkpoint);
                    w.start();
 //               }
            } else {
            	logger.info("Checkpoint "+ next_checkpoint.getName()+ " wird enstperrt (Keiner in der Warteschlange, letzter Bot: "+bot.getBt_Name()+")");
                
            	checkpoint.setClosedBot(null);
                checkpoint.setReservedBot(null);
                
                this.view.UpdateClosedpoint(checkpoint.getName(), false);
            }
    	
            return true;
            
        } else {
           
        	if (next_checkpoint.setBotOnWaitList(bot)) {
               
        		logger.info("Bot "+ bot.getBt_Name() +" ist in der Warteschlange fuer den  Checkpoint "+ next_checkpoint.getName());
            } else {
            	return EditCheckpointsForContinue(checkpoint, next_checkpoint, bot);
            }
        }

        return false;
    }

    public synchronized Checkpoint NextFreePuffer() {
        Checkpoint puffer1 = checkpoints.get("PF1");
        Checkpoint puffer2 = checkpoints.get("PF2");
        Checkpoint puffer3 = checkpoints.get("PF3");
        
        if(!puffer3.isReserved() && !puffer3.isClosed())
        	return puffer3;
        else if(!puffer2.isReserved() && !puffer2.isClosed())
        	return puffer2;
        else if(!puffer1.isReserved() && !puffer1.isClosed())
        	return puffer1;
        else
        	return null;
    }
    
    public synchronized boolean TestEntranceForPuffer(String entrance, Bot bot) {
        Checkpoint check = checkpoints.get(entrance);
        if ((check.isClosed() || check.isReserved()) && !check.BotHaveClosesOrReserved(bot))
        {
        	logger.info("Checkpoint "+ check.getName()+" ist belegt oder reserviert");
        	return false;
        }
        logger.info("Checkpoint "+ check.getName()+" ist nicht belegt oder reserviert");
        return true;
    	

    }

    public synchronized boolean CheckpointReserved(String entrance, Bot bot) {
        Checkpoint check = checkpoints.get(entrance);

        if (!check.isReserved() || check.BotHaveClosesOrReserved(bot) ) {       	
        	view.UpdateClosedpoint(entrance, true);
            check.setReservedBot(bot);
            logger.info("Checkpoint "+ check.getName()+" wurde reserviert");
            return true;
        }
        logger.info("Checkpoint "+ check.getName()+" konnte nicht reserviert werden, da dieser schon reserviert ist");
        return false;
    }
    
    public synchronized void ObjektToStock(String id, String stock, String target)
    {
    	Order order = new Order(id,stock,target);
    	if (counter >= bots.size()) {
		    counter = 0;
		}
		    	
    	Bot bot = null;
    	if(bots.size() > 0 )
    	{
    		bot = this.bots.get(counter);
    		counter++;
	    	bot.NewOrder(order);
    	}
    	
    	Stock s = stocks.get(stock);
    	s.addOrder(order);
    	
    	view.InputStoreTable(s.getName(), id, target, bot != null ? bot.getBt_Name():"-");
    	
    	
    }
    
    public synchronized void OrderLoad(Order order)
    {
    	try{
    		view.DeleteFirstStoreTable(order.getStore_place(), order.getId());
    	}catch(Exception e)
    	{
    		logger.info("OrderLoad : "+e);
    	}
    }
    
    public synchronized void OrderUnload(Order order, Bot bot)
    {
    	try{
    		view.InputExitTable(order.getExit_place(), order.getId(), order.getExit_place(), bot.getBt_Name());
		}catch(Exception e)
		{
			logger.info("OrderUnload : "+e);
		}
    }
    
    public void Stop()
    {
    	try{
    		for(int i =0; i < this.bots.size();++i)
    		{
    			this.bots.get(i).Stop();
    		}
    		
    	}catch(Exception e)
    	{
    		logger.info("Stopp : "+e);
    	}
    }
    
    
    
    
    
//    /**
//     * ActionListener zum Betätigen des Bots Verwalten-Buttons
//     */
//    class ControlBotListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//       	
//        	logger.info("Der Button 'Bots Verwalten' wurde betaetigt");
//        }
//    }

    /**
     * ActionListener zum Betätigen des Neuen Bots -Buttons
     */
    class NewBotListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        	logger.info("Der Button 'Neuer Bot' wurde betaetigt");
        	
            String bot_name = view.GetBotName();

            if (bot_name.trim().length() != 0) {
                if (!ExitsBot(bot_name)) {
                	logger.info("Ein Bot mit dem  Namen "+ bot_name+" wird angelegt");
                    Bot bot = new Bot(controller, bot_name, bots.size(), ("P" + (bots.size() + 1)));
                    view.InputBotTable(new String[]{"" + bots.size(), bot_name, "", "", "", ""});
                    logger.info("Eine Verbindung zum  Bot  "+ bot_name+" wird aufgebaut");
                    if (bot.Connect()) {
                    	logger.info("Verbindung zum  Bot  "+ bot_name+" erfolgreich hergestellt");
                        bot.InfoUpdate();
                        logger.info("Parkposition wird zum  Bot  "+ bot_name+" gesendet");
                        bot.SendParkPosition();
                        bots.add(bot);
                        logger.info("Bot  "+ bot_name+" erfolgreich integriert");

                    } else {
                        view.DeleteFromTable(bots.size());
                        logger.info("Es konnte keine Verbindung zum Bot  "+ bot_name+" aufgebaut werden");
                    }
                } else {
                	logger.info("Ein Bot mit dem eingebenen Namen "+ bot_name+" existerit bereits");
                }
            } else {
            	logger.info("Es wurde kein Bot Name eingetragen!");
            }
        }
    }

//    /**
//     * ActionListener zum Betätigen des Auftrags Verwaltungs-Buttons
//     */
//    class ControlOrderListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//        	logger.info("Der Button 'Auftrags Verwaltung' wurde betaetigt");
//        }
//    }

    /**
     * ActionListener zum Betaetigen des Neuer Auftrag-Buttons
     */
    class NewOrderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	
        	logger.info("Der Button 'Neuer Auftrag' wurde betaetigt");
        	
            int store = view.GetStoreSelection();
            int exit = view.GetExitSelection();
                       
            if (store != 0 && exit != 0) {
//                int id = order_ID++;
//                logger.info("Es wurde das Lager "+store +" und der Ausgang"+ exit+" ausgew�hlt!");
//                Order new_order = new Order(id, store, exit);
//
//                if (bots.size() > 0) {
//                    if (counter >= bots.size()) {
//                        counter = 0;
//                    }
//                    bots.get(counter).NewOrder(new_order);
//                    counter++;
//                } else {
//                    view.InputDialog("Keine Bots vorhanden");
//                }
            } else {
            	logger.info("Es wurde kein Lager und/oder Ausgang  ausgew�hlt!");
            }
        }
    }

    /**
     * ActionListener zum Betätigen des Stopp/Buttons
     */
    class StoppListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	logger.info("Der Button 'Stop' wurde betaetigt");
        	
        	Stop();            
        }
    }

    /**
     * ActionListener zum Betätigen des Menüteintrags "Starten"
     */
    class SimulationStartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	logger.info("Der Menueeintrag Simulation -> Start wurde betaetigt");
        	
        	simulator.CreatSimulateData();
        	simulator.StartSimulat();
        }
    }

    /**
     * ActionListener zum Betätigen des Menüteintrags "Öffnen"
     */
    class SimulationOeffnenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	logger.info("Der Menueeintrag Simulation -> Oeffnen wurde betaetigt");
        }
    }

    /**
     * ActionListener zum Betätigen des Menüteintrags "Speichern"
     */
    class SimulationSpeichernListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	logger.info("Der Menueeintrag Simulation -> Speichern wurde betaetigt");
        }
    }

    /**
     * ActionListener zum Betätigen des Menüteintrags "Einstellungen"
     */
    class SimulationEinstellungenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {        	
        	logger.info("Der Menueeintrag Simulation -> Einstellungen wurde betaetigt");
        }
    }

//    /**
//     * Klasse welche den Nachrichteneingang der Bluetooth-Verbindung händelt
//     */
//    private class WorkOffWaitList extends Thread {
//        Checkpoint check;
//        WorkOffWaitList(Checkpoint cp) {
//            this.check = cp;
//        }
//
//        /* (non-Javadoc)
//         * @see java.lang.Thread#run()
//         */
//        public void run() {
//        	logger.info("WorkOffWaitList  fuer den Checkpunkt "+check.getName()+" gestartet");
//            boolean b = true;
//            while (check.isBotInWaitList() && b) {
//            	
//                Bot waitBot = check.getFirstOnWaitList();
//                check.setClosedBot(waitBot);
//                logger.info(" Bot "+ waitBot.getBt_Name()+" wird aus der Warteschlange von Checkpunkt "+check.getName()+" geholt");
//                check = checkpoints.get(waitBot.getCheckpoint());                
//                b = waitBot.ContinueAfterWaitList();
//                
//                logger.info("Ueberpruefung  ob sich Bots in der Warteschlange von Checkpunkt "+check.getName()+" befinden");
//                
//                if (check.isStoreOrExit()) {
//                	if(check.isBotInWaitList())
//                	{
//	                    check.setClosedBot(null);
//	                    check.setReservedBot(null);
//	                    
//	                    view.UpdateClosedpoint(check.getName(), false);
//	                    waitBot = check.getFirstOnWaitList();
//	                    
//	                    if(!waitBot.IsinPuffer() && waitBot.HavePufferReserved())
//	                    {
//	                    	Checkpoint puffer = checkpoints.get(waitBot.GetPuffer());
//	                    	if(puffer != null)
//	                    		puffer.setReservedBot(null);
//	                    }
//	                    check.setReservedBot(waitBot);
//	                    b = waitBot.ContinueAfterWaitList();
//	                    logger.info("Checkpunkts  "+ check.getName()+" f�r Bot "+waitBot.getBt_Name()+" freigeben");
//	                    
//	                    b = false;
//                	}
//                }
//                
//            }
//            logger.info("Keine Bots an Checkpunkt "+check.getName() + ", deswegen  wird er entsperrt");
//            
//            check.setClosedBot(null);
//            check.setReservedBot(null);
//            view.UpdateClosedpoint(check.getName(), false);
//            
//            logger.info("WorkOffWaitList  beendet");
//        }
//    }
    
    /**
     * Klasse welche den Nachrichteneingang der Bluetooth-Verbindung händelt
     */
    private class WorkOffWaitList extends Thread {
        Checkpoint check;
        WorkOffWaitList(Checkpoint cp) {
            this.check = cp;
        }

        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        public void run() {
        	logger.info("WorkOffWaitList  fuer den Checkpunkt "+check.getName()+" gestartet");
            boolean b = true;
            while (check.isBotInWaitList() && b) {
            	
                Bot waitBot = check.getFirstOnWaitList();
                logger.info(" Bot "+ waitBot.getBt_Name()+" wird aus der Warteschlange von Checkpunkt "+check.getName()+" geholt");
                
                
                if(check.isStoreOrExit())
                {
                	check.setClosedBot(null);
                    check.setReservedBot(null);
                    
                    if(!waitBot.IsinPuffer() && waitBot.HavePufferReserved())
                    {
                    	Checkpoint puffer = checkpoints.get(waitBot.GetPuffer());
                    	if(puffer != null)
                    		puffer.setReservedBot(null);
                    }
                    
                    check.setReservedBot(waitBot);
                }else
                {
                	check.setClosedBot(waitBot);
                }           
                
                check = checkpoints.get(waitBot.getCheckpoint()); 
                b = waitBot.ContinueAfterWaitList();
                              
                
                
            }
            logger.info("Keine Bots in der Warteschlange von Checkpunkt "+check.getName() + ", deswegen  wird er entsperrt");
            
            check.setClosedBot(null);
            check.setReservedBot(null);
            view.UpdateClosedpoint(check.getName(), false);
            
            logger.info("WorkOffWaitList  beendet");
        }
    }
}

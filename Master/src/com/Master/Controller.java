package com.Master;

import com.Simulator.Simulator;
import com.View.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.*;

public class Controller implements IController, IStockInput {

	/**
	 * logger
	 */
	private static org.apache.log4j.Logger logger = LogManager.getLogger("Controller");
	
    /**
     * Benutzeroberfl√§che
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
     * Enth√ºllt alle Checkpoints des Lagers
     */
    private HashMap<String, Checkpoint> checkpoints;
    
    /**
     * Enth√ºllt alle Eing‰nge des Lagers
     */
    private Map<String, Stock> stocks;
    
    /**
     * Enth√ºllt alle Ausg‰nge des Lagers
     */
    private Map<String, Exit> exits;





    Controller() {
        this.controller = this;

        this.view = new View();
        
        logger.info("Oberflaeche wird geˆffnet und und die Actionlistener gesetzt");
        
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
        
        this.exits = new HashMap<String, Exit>();
        
        this.exits.put("PU1", new Exit(this.checkpoints.get("PU1")));
        this.exits.put("PU2", new Exit(this.checkpoints.get("PU2")));
        
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
    	logger.info("Der Text  "+ text + " wurde in die Console eingef¸gt");
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
     * √úberpr√úft ob ein Bots schon mit dem √ºbergebenen-Namen existiert
     *
     * @param name Name des Bots
     * @return Ergebnis der √ºberpr√ºfung
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
                logger.info("Der n‰chste Checkpunkt von "+ bot.getBt_Name() + " (" + next_checkpoint + ") ist nicht vorhanden");
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
    	logger.info("Bot "+ bot.getBt_Name() +" pr¸ft ob Checkpoint "+ next_checkpoint.getName()+"  gesperrt ist");
    	
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
//                    logger.info("Checkpunkts  "+ checkpoint.getName()+" f¸r Bot "+waitBot.getBt_Name()+" freigeben");
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

    public synchronized boolean ExitReserved(String exit, Bot bot) {
        
    	logger.info("Checkpoint "+ this.checkpoints.get(exit).getName()+" soll von "+bot.getBt_Name()+" reserviert");
    	Exit ex = this.exits.get(exit);
    
        if(ex != null)
        {
        	String nextID = ex.getNextDelivery(); 
        	logger.info("Ausgang erwartet als n‰chstes "+ nextID+" und der Bot hat  "+bot.getOrderID() +" geladen");
        	if(nextID.equals(bot.getOrderID()))
        	{
        		view.UpdateClosedpoint(exit, true);
                this.checkpoints.get(exit).setReservedBot(bot);
                logger.info("Checkpoint "+ this.checkpoints.get(exit).getName()+" wurde reserviert");
                return true;
        	}else
        	{
        		logger.info("Checkpoint "+ exit+" konnte nicht reserviert werden, da "+ex.getNextDelivery()+" erwartet wird und nicht "+bot.getOrderID()+" als n‰chstes");
        	}
        }else
        {
        	logger.info("Exit -Checkpoint "+ exit+"  nicht vorhanden");
        }

        return false;
    }
    
    public synchronized boolean StoreReserved(String store, Bot bot) {
    	Stock ex = this.stocks.get(store);
        
        if(ex != null)
        {
        	if(ex.getNextDelivery().equals(bot.getOrderID()))
        	{
        		view.UpdateClosedpoint(store, true);
                this.checkpoints.get(store).setReservedBot(bot);
                logger.info("Checkpoint "+ this.checkpoints.get(store).getName()+" wurde reserviert");
                return true;
        	}else
        	{
        		logger.info("Checkpoint "+ store+" konnte nicht reserviert werden, da "+ex.getNextDelivery()+" erwartet wird und nicht "+bot.getOrderID()+" als n‰chstes");
        	}
        }else
        {
        	logger.info("Exit -Checkpoint "+ store+"  nicht vorhanden");
        }

        return false;
    }
    
    public synchronized boolean CheckpointReserved(String check, Bot bot) {
        Checkpoint checkpoint = checkpoints.get(check);     
        
        if (!checkpoint.isReserved() || checkpoint.BotHaveClosesOrReserved(bot) ) {       	
        	view.UpdateClosedpoint(check, true);
            checkpoint.setReservedBot(bot);
            logger.info("Checkpoint "+ checkpoint.getName()+" wurde reserviert");
            return true;
        }
        logger.info("Checkpoint "+ checkpoint.getName()+" konnte nicht reserviert werden, da dieser schon reserviert ist");
        return false;
    }
    
    public synchronized void ObjektToStock(String id, String stock, String target)
    {
    	Order order = new Order(id,stock,target);

		    	
    	Bot bot = null;
    	for (int i = 0; i < bots.size() && bot == null ; ++i )
    	{
    		Bot b = this.bots.get(i);
    		if(!b.HaveOrder())
    		{
    			bot = b;
    		}
    	}
    	
    	Stock s = stocks.get(stock);
    	s.addOrder(order);
    	
    	if(bot != null)
    	{
    		order.SetBot(bot);
    		bot.NewOrder(order);
    	}
    	
    	view.InputStoreTable(s.getName(), id, target, bot != null ? bot.getBt_Name():"-");
    	
    	
    }
    
    public synchronized void OrderLoad(Order order)
    {
    	try{
    		Stock st = this.stocks.get(order.getStore_place());
    		st.removeOrder(order);
    		
    		Exit ex = this.exits.get(order.getExit_place());
    		ex.OrderonWay(order.getId());
    		
    		view.DeleteFirstStoreTable(order.getStore_place(), order.getId());
    	}catch(Exception e)
    	{
    		logger.info("OrderLoad : "+e);
    	}
    }
    
    public synchronized void OrderUnload(Order order, Bot bot)
    {
    	try{
    		Exit ex = this.exits.get(order.getExit_place());    		
    		ex.addOrder(order);
    		
    		view.InputExitTable(order.getExit_place(), order.getId(), order.getExit_place(), bot.getBt_Name());
		}catch(Exception e)
		{
			logger.info("OrderUnload : "+e);
		}
    }
    
    public synchronized Order NextOrderForBot()
    {
	    try{
	    	List<Stock> s = new ArrayList<Stock>(this.stocks.values());
	    	Collections.sort(s, new StokComperator() );
	    	Stock first = s.get(0);
	    	
	    	return first.getNextFreeOrder();
	    }catch(Exception e)
	    {
	    	logger.info("NextOrderForBot : "+e);
	    	return null;
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
//     * ActionListener zum Bet√§tigen des Bots Verwalten-Buttons
//     */
//    class ControlBotListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//       	
//        	logger.info("Der Button 'Bots Verwalten' wurde betaetigt");
//        }
//    }

    /**
     * ActionListener zum Bet√§tigen des Neuen Bots -Buttons
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
//     * ActionListener zum Bet√§tigen des Auftrags Verwaltungs-Buttons
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
//                logger.info("Es wurde das Lager "+store +" und der Ausgang"+ exit+" ausgew‰hlt!");
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
            	logger.info("Es wurde kein Lager und/oder Ausgang  ausgew‰hlt!");
            }
        }
    }

    /**
     * ActionListener zum Bet√§tigen des Stopp/Buttons
     */
    class StoppListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	logger.info("Der Button 'Stop' wurde betaetigt");
        	
        	Stop();            
        }
    }

    /**
     * ActionListener zum Bet√§tigen des Men√ºteintrags "Starten"
     */
    class SimulationStartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	logger.info("Der Menueeintrag Simulation -> Start wurde betaetigt");
        	
        	simulator.CreatSimulateData();
        	
        	exits.get("PU1").setOrderList(simulator.getOrdertoExit1());
        	exits.get("PU2").setOrderList(simulator.getOrdertoExit2());
        	
        	simulator.StartSimulat();
        }
    }

    /**
     * ActionListener zum Bet√§tigen des Men√ºteintrags "√ñffnen"
     */
    class SimulationOeffnenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	logger.info("Der Menueeintrag Simulation -> Oeffnen wurde betaetigt");
        }
    }

    /**
     * ActionListener zum Bet√§tigen des Men√ºteintrags "Speichern"
     */
    class SimulationSpeichernListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	logger.info("Der Menueeintrag Simulation -> Speichern wurde betaetigt");
        }
    }

    /**
     * ActionListener zum Bet√§tigen des Men√ºteintrags "Einstellungen"
     */
    class SimulationEinstellungenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {        	
        	logger.info("Der Menueeintrag Simulation -> Einstellungen wurde betaetigt");
        }
    }


    
    /**
     * Klasse welche den Nachrichteneingang der Bluetooth-Verbindung h√§ndelt
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
            while (check != null && check.isBotInWaitList() && b) {
            	
            	check.setClosedBot(null);
                check.setReservedBot(null);
                
                if(check.isStore() || check.isExit())
                {
                	logger.info("WorkOffWaitList Checkpunkt "+check.getName()+" handelt es sich um ein Lager oder Ausgang");
                	List<Bot> checkWaitList = check.getWaitList();
                	Bot waitBot = null;
                	
                	if(check.isStore())
                	{
                		logger.info("WorkOffWaitList Checkpunkt "+check.getName()+" handelt es sich um ein Lager");
                		Stock st = stocks.get(check.getName());
                		String id = st.getNextDelivery();
                		
                		logger.info("WorkOffWaitList Checkpunkt "+check.getName()+" artikel "+id+" muss als n‰chstes abgeholt werden");
                		for(int i = 0; (i < checkWaitList.size()) && (waitBot == null); ++i)
                		{
                			Bot bot = checkWaitList.get(i);
                			logger.info("WorkOffWaitList Checkpunkt "+check.getName()+"  Bot "+bot.getBt_Name()+" befindet sich in der Wartreschlange");
                			if(id.equals(bot.getOrderID()))
                			{
                				logger.info("WorkOffWaitList Checkpunkt "+check.getName()+" Bot "+ bot.getBt_Name()+" hat den Auftrag f¸r Artikel "+id);
                				waitBot = bot;
                				checkWaitList.remove(bot);
                			}else
                			{
                				logger.info("WorkOffWaitList Checkpunkt "+check.getName()+" Bot "+ bot.getBt_Name()+" hat NICHT den Auftrag f¸r Artikel "+id);
                			}
                		}
                	}else if(check.isExit())
                	{
                		logger.info("WorkOffWaitList Checkpunkt "+check.getName()+" handelt es sich um einen Ausgang");
                		Exit ex = exits.get(check.getName());
                		String id = ex.getNextDelivery();
                		
                		logger.info("WorkOffWaitList Checkpunkt "+check.getName()+" artikel "+id+" muss als n‰chstes geliefert werden");
                		
                		for(int i = 0; (i < checkWaitList.size()) && (waitBot == null); ++i)
                		{
                			Bot bot = checkWaitList.get(i);
                			logger.info("WorkOffWaitList Checkpunkt "+check.getName()+"  Bot "+bot.getBt_Name()+" befindet sich in der Wartreschlange");
                			if(id.equals(bot.getOrderID()))
                			{
                				logger.info("WorkOffWaitList Checkpunkt "+check.getName()+" Bot "+ bot.getBt_Name()+" hat den Auftrag f¸r Artikel "+id);
                				waitBot = bot;
                				checkWaitList.remove(bot);
                			}else
                			{
                				logger.info("WorkOffWaitList Checkpunkt "+check.getName()+" Bot "+ bot.getBt_Name()+" hat NICHT den Auftrag f¸r Artikel "+id);
                			}
                		}
                	}
                	                    
                    
                    if(waitBot != null)
                    {
                    	logger.info(" Bot "+ waitBot.getBt_Name()+" wird aus der Warteschlange von Checkpunkt "+check.getName()+" geholt"); 
                    	if(!waitBot.IsinPuffer() && waitBot.HavePufferReserved())
		                {
		                	Checkpoint puffer = checkpoints.get(waitBot.GetPuffer());
		                	if(puffer != null)
		                		puffer.setReservedBot(null);
		                }
                    	check.setReservedBot(waitBot);
                    	check = checkpoints.get(waitBot.getCheckpoint()); 
                        b = waitBot.ContinueAfterWaitList();
                    	
                    }else if(waitBot == null)
                    {
                    	logger.info("Kein Passender Bot in der  Warteschlange von Checkpunkt "+check.getName()); 
                    	
                    	b = false;
                    	check = null;                    	
                    }

                }else
                {
                	Bot waitBot = check.getFirstOnWaitList();
                    logger.info(" Bot "+ waitBot.getBt_Name()+" wird aus der Warteschlange von Checkpunkt "+check.getName()+" geholt");                    
                	
                	check.setClosedBot(waitBot);
                	
                	check = checkpoints.get(waitBot.getCheckpoint()); 
                    b = waitBot.ContinueAfterWaitList();
                }                   
                
            }
            
            
            if(check != null)
            {
            	logger.info("Keine Bots in der Warteschlange von Checkpunkt "+check.getName() + ", deswegen  wird er entsperrt");
            	check.setClosedBot(null);
                check.setReservedBot(null);
                view.UpdateClosedpoint(check.getName(), false);
            }
            
            
            logger.info("WorkOffWaitList  beendet");
        }
    }
    
    public class StokComperator implements Comparator <Stock>
    {
    	@Override
    	public int compare(Stock s1, Stock s2)
    	{
    		int pos = 0;    		
    		
    		while (true)
    		{
    			Order o1 = s1.GiveOrderByPosition(pos);
        		Order o2 = s2.GiveOrderByPosition(pos);
        		
        		if(o1 == null && o2 != null)
        			return 1;
        		
        		if(o1 != null && o2 == null)
        			return -1;		
       		
        		if(o1 == null && o2 == null)
        			return 0;
        		
        		if(o1.HaveBot() && !o2.HaveBot())
        			return 1;
        		
        		if(!o1.HaveBot() && o2.HaveBot())
    				return -1;	
        		
        		if(!o1.HaveBot() && !o2.HaveBot())
        		{        			        			
        			Exit e1 = exits.get(o1.getExit_place());
        			Exit e2 = exits.get(o2.getExit_place());
        			
        			int pos1 = e1.OrderPostion(o1.getId());
        			int pos2 = e2.OrderPostion(o2.getId());;
        			
        			if(pos1 > pos2)
            			return 1;
            		
        			if(pos1 < pos2)
            			return -1;		
           		
        			if(pos1 == pos2)
        			{
        				int sum1 = s1.SumObjekts();
        				int sum2 = s2.SumObjekts();
        				
        				if(sum1 > sum2)
                			return 1;
                		
            			if(sum1 < sum2)
                			return -1;		
               		
            			if(sum1 == sum2)
            				return 0;
        			}
        		}
	
        		++pos;
        		
    		}
    	}
    	
    	
    	
    	
    }
}

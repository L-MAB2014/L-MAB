package com.Master;

import com.View.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class Controller implements IController {

	/**
	 * logger
	 */
	private static Logger logger = Logger.getAnonymousLogger();
	
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

    /**
     * Enthüllt alle Checkpoints des Lagers
     */
    private HashMap<String, Checkpoint> checkpoints;

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
        
        this.view.addControlBotListener(new ControlBotListener());
        this.view.addNewBotListener(new NewBotListener());
        this.view.addConrtolOrderListener(new ControlOrderListener());
        this.view.addNewOrderListener(new NewOrderListener());
        this.view.addStoppListener(new StoppListener());

        this.view.addSimulationStartListener(new SimulationStartListener());
        this.view.addSimulationOeffnenListener(new SimulationOeffnenListener());
        this.view.addSimulationSpeichernListener(new SimulationSpeichernListener());
        this.view.addSimulationEinstellungenListener(new SimulationEinstellungenListener());
        
        logger.info("Checkpoints (Map) werden Initialisiert");
        this.checkpoints = CreatCheckpoints.InitializeCheckpoints();
        this.bots = new ArrayList<Bot>();
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
        view.UpdateTable(row, text);
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
    public boolean CheckForContinue(String checkpoint, String next_checkpoint, Bot bot) {
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

    public void SetOnWaitList(String check, Bot bot) {
    	
    	Checkpoint checkpoint = checkpoints.get(check);
    	
    	if(checkpoint != null)
    	{
    		logger.info("Bot "+ bot.getBt_Name() +" wird in die Warteschlange von Checkpunkt "+ checkpoint.getName()+"  gesetzt");
    		checkpoint.setBotOnWaitList(bot);
    	}
    }

    private boolean EditCheckpointsForContinue(Checkpoint checkpoint, Checkpoint next_checkpoint, Bot bot) {
    	logger.info("Bot "+ bot.getBt_Name() +" pr�ft ob Checkpoint "+ next_checkpoint.getName()+"  gesperrt ist");
    	
    	if (!next_checkpoint.isClosed()) {
    		logger.info("Bot "+ bot.getBt_Name() +" sperrt den  Checkpoint "+ next_checkpoint.getName());
        	next_checkpoint.setClosed(true);
        	this.view.UpdateClosedpoint(next_checkpoint.getName(), true);
        	logger.info("Warteschlange des Checkpunkts  "+ checkpoint.getName() + " von Bot "+ bot.getBt_Name() +" wird ueberprueft");
           
        	if (checkpoint.isBotInWaitList()) {
            	logger.info("Bots in Warteschlange des Checkpunkts  "+ checkpoint.getName());
                
            	if (checkpoint.isReserved()) {
                    checkpoint.setClosed(false);
                    this.view.UpdateClosedpoint(checkpoint.getName(), false);
                    Bot waitBot = checkpoint.getFirstOnWaitList();
                    
                    if(!waitBot.IsinPuffer() && waitBot.HavePufferReserved())
                    {
                    	Checkpoint puffer = checkpoints.get(waitBot.GetPuffer());
                    	if(puffer != null)
                    		puffer.setReserved(false);
                    }
                    
                    waitBot.ContinueAfterWaitList();
                    logger.info("Checkpunkts  "+ checkpoint.getName()+" f�r Bot "+waitBot.getBt_Name()+" freigeben");
                    
                } else {
                    WorkOffWaitList w = new WorkOffWaitList(checkpoint);
                    w.start();
                }
            } else {
            	logger.info("Checkpoint "+ next_checkpoint.getName()+ " wird enstperrt (Bot: "+bot.getBt_Name()+")");
                checkpoint.setClosed(false);
                checkpoint.setReserved(false);
                this.view.UpdateClosedpoint(checkpoint.getName(), false);
            }
            return true;
            
        } else {
           
        	if (!next_checkpoint.setBotOnWaitList(bot)) {
               
            	if (!next_checkpoint.isBotInWaitList() && !next_checkpoint.isClosed()) {
                    checkpoint.setClosed(false);   
                    logger.info("Checkpoint "+ next_checkpoint.getName()+ " wird enstperrt (Bot: "+bot.getBt_Name()+")");
                    next_checkpoint.setClosed(true);
                    logger.info("Bot "+ bot.getBt_Name() +" sperrt den  Checkpoint "+ next_checkpoint.getName());
                    
                    this.view.UpdateClosedpoint(checkpoint.getName(), false);
                    this.view.UpdateClosedpoint(next_checkpoint.getName(), true);
                    
                    return true;
                }
            } else {
            	logger.info("Bot "+ bot.getBt_Name() +" ist in der Warteschlange fuer den  Checkpoint "+ next_checkpoint.getName());
            }
        }

        return false;
    }

    public Checkpoint NextFreePuffer() {
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
    
    public boolean TestEntranceForPuffer(String entrance) {
        Checkpoint check = checkpoints.get(entrance);
        if (check.isClosed() || check.isReserved())
        {
        	logger.info("Checkpoint "+ check.getName()+" ist belegt oder reserviert");
        	return false;
        }
        logger.info("Checkpoint "+ check.getName()+" ist nicht belegt oder reserviert");
        return true;
    	

    }

    public boolean CheckpointReserved(String entrance) {
        Checkpoint check = checkpoints.get(entrance);

        if (!check.isReserved()) {       	
        	view.UpdateClosedpoint(entrance, true);
            check.setReserved(true);
            logger.info("Checkpoint "+ check.getName()+" wurde reserviert");
            return true;
        }
        logger.info("Checkpoint "+ check.getName()+" konnte nicht reserviert werden, da dieser schon reserviert ist");
        return false;
    }


    /**
     * ActionListener zum Betätigen des Bots Verwalten-Buttons
     */
    class ControlBotListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
       	
        	logger.info("Der Button 'Bots Verwalten' wurde betaetigt");
        }
    }

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
                    view.InputTable(new String[]{"" + bots.size(), bot_name, "", "", "", ""});
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

    /**
     * ActionListener zum Betätigen des Auftrags Verwaltungs-Buttons
     */
    class ControlOrderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	logger.info("Der Button 'Auftrags Verwaltung' wurde betaetigt");
        }
    }

    /**
     * ActionListener zum Betaetigen des Neuer Auftrag-Buttons
     */
    class NewOrderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	
        	logger.info("Der Button 'Neuer Auftrag' wurde betaetigt");
        	
            int store = view.GetStoreSelection();
            int exit = view.GetExitSelection();
                       
            if (store != 0 && exit != 0) {
                int id = order_ID++;
                logger.info("Es wurde das Lager "+store +" und der Ausgang"+ exit+" ausgew�hlt!");
                Order new_order = new Order(id, store, exit);

                if (bots.size() > 0) {
                    if (counter >= bots.size()) {
                        counter = 0;
                    }
                    bots.get(counter).NewOrder(new_order);
                    counter++;
                } else {
                    view.InputDialog("Keine Bots vorhanden");
                }
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
            
        }
    }

    /**
     * ActionListener zum Betätigen des Menüteintrags "Starten"
     */
    class SimulationStartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	logger.info("Der Menueeintrag Simulation -> Start wurde betaetigt");
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
                b = waitBot.ContinueAfterWaitList();
                check = checkpoints.get(waitBot.getCheckpoint());
                logger.info("Ueberpruefung  ob sich Bots in der Warteschlange von Checkpunkt "+check.getName()+" befinden");
            }
            logger.info("Keine Bots an Checkpunkt "+check.getName() + ", deswegen  wird er entsperrt");
            check.setClosed(false);
            view.UpdateClosedpoint(check.getName(), false);
            
            logger.info("WorkOffWaitList  beendet");
        }
    }
}

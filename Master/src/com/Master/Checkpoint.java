package com.Master;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.log4j.LogManager;


/**
 * @author Marcel Reich
 * Checkpunkt
 *
 */
public class Checkpoint {

	/**
	 * logger
	 */
	private static org.apache.log4j.Logger logger = LogManager.getLogger("Checkpoint");
	
    /**
     * Name des Checkpunktes
     */
    private String name;

    /**
     * Nächster Checkpunkt auf dem normalen Weg
     */
    private Checkpoint next_WayCheckpoint;

    /**
     * Nächstes mÃ¼gliches Lager, Parkplatz oder Puffer
     */
    private Checkpoint next_OtherCheckpoint;

    /**
     * Vorheriger Checkpunkt auf dem normalen Weg
     */
    private Checkpoint previous_WayCheckpoint;

    /**
     * Mögliches vorheriges Lager, Parkplatz oder Puffer
     */
    private Checkpoint previous_OtherCheckpoint;

    /**
     * Beinhaltet den Bot der den Checkpoint reserviert 
     */
    private Bot reserved;

    /**
     * Beinhaltet den Bot der den Checkpoint belegt/sperrt 
     */
    private Bot closed;

    /**
     * Warteliste für den Checkpoint
     */
    private List<Bot> waiting_List;
        
    /**
     *  Ist der Checkpunkt ein Eingang
     */
    private boolean isStore;
    
    /**
     * Ist der Checkpunkt ein Ausgang
     */
    private boolean isExit;

    /**
     * Konstruktor 
     * @param name
     * Name des Checkpunktes
     * @param store
     * Ist der Checkpunkt ein Eingang
     * @param exit
     * Ist der Checkpunkt ein Ausgang
     */
    Checkpoint(String name, boolean store, boolean exit) {
        this.name = name;
        this.isStore = store;
        this.isExit = exit;

        this.waiting_List = new ArrayList<Bot>();

        this.closed = null;
        this.reserved = null;

        this.next_WayCheckpoint = null;
        this.next_OtherCheckpoint = null;
        this.previous_WayCheckpoint = null;
        this.previous_OtherCheckpoint = null;
    }
    
    /**
     * Ist der Checkpunkt ein Eingang
     * @return
     * Eingang oder nicht
     */
    public boolean isStore()
    {
    	return this.isStore;
    }

    /**
     * Ist der Checkpunkt ein Ausgang
     * @return
     * Ausgang oder nicht
     */
    public boolean isExit()
    {
    	return this.isExit;
    }
    
    /**
     * Setzt die nächsten Checkpunkte des Checkpunktes
     *
     * @param next_WayCheckpoint   NÃ¤chster Checkpunkt der sich auf dem Weg befindet
     * @param next_OtherCheckpoint NÃ¤chster Checkpunkt der als Lager, Puffer oder Parkplatz dient
     */
    public void setNextCheckpoints(Checkpoint next_WayCheckpoint, Checkpoint next_OtherCheckpoint) {
        this.next_WayCheckpoint = next_WayCheckpoint;
        this.next_OtherCheckpoint = next_OtherCheckpoint;
    }

    /**
     * Setzt die vorherigen Checkpunkte des Checkpunktes
     *
     * @param previous_WayCheckpoint   Vorheriger Checkpunkt der sich auf dem Weg befindet
     * @param previous_OtherCheckpoint Vorheriger Checkpunkt der als Lager, Puffer oder Parkplatz diente
     */
    public void setPeviousCheckpoints(Checkpoint previous_WayCheckpoint, Checkpoint previous_OtherCheckpoint) {
        this.previous_WayCheckpoint = previous_WayCheckpoint;
        this.previous_OtherCheckpoint = previous_OtherCheckpoint;
    }

    /**
     * Gibt an ob der Checkpoint reserviert ist oder nicht!
     *
     * @return
     */
    public synchronized boolean isReserved() {
    	logger.info(" Abfrage- Reservierung Checkpoint "+ this.name+ " : ="+ 
    			(this.reserved == null ? "NULL" : this.reserved.getBt_Name()));
    	
        return this.reserved != null;
    }


    /**
     * Reserviert den Checkpunkt
     * @param bot
     * Bot welcher den Checkpunkt reserviert
     */
    public synchronized void setReservedBot(Bot bot) {
    	logger.info("Checkpoint "+ this.name+ " : Reservierung (ALT)="+ 
    			(this.reserved == null ? "NULL" : this.reserved.getBt_Name())  +" ## Reservierung (NEU)="
    	+ (bot==null ? "NULL" : bot.getBt_Name()));
        this.reserved = bot;
    }

    /**
     * Gibt an ob der Checkpoint gesperrt ist oder nicht!
     *
     * @return
     * Gesperrt oder nicht
     */
    public boolean isClosed() {
    	logger.info(" Abfrage-Belegung Checkpoint "+ this.name+ " : ="+ 
    			(this.closed == null ? "NULL" : this.closed.getBt_Name()));
        return this.closed != null;
    }


    /**
     * Sperrt oder entsperrt den Checkpunkt
     * @param bot
     * Bot welcher den Checkpunkt sperrt (Null = entsperrt)
     */
    public void setClosedBot(Bot bot) {
    	logger.info("Checkpoint "+this.name+ " : Belegung (ALT)="+ 
    			(this.closed == null ? "NULL":this.closed.getBt_Name())  +" ### Belegung (NEU)="
    	+ (bot == null ? "NULL" :bot.getBt_Name()));
        this.closed = bot;
    }

    /**
     * Gibt den Namen des Checkunktes an
     *
     * @return
     * name des Checkpunktes
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt den nächsten Checkpunkt auf dem Weg zurück
     *
     * @return 
     * nächster Checkpunkt(Weg)
     */
    public Checkpoint getNext_WayCheckpoint() {
        return next_WayCheckpoint;
    }

    /**
     * Gibt den nächsten Checkpunkt Parkplatz & Puffer usw. zurÃ¼ck
     *
     * @return 
     * nächster Checkpunkt(Anderer)
     */
    public Checkpoint getNext_OtherCheckpoint() {
        return next_OtherCheckpoint;
    }

    /**
     * Gibt den vorheriger Checkpunkt auf dem Weg zurÃ¼ck
     *
     * @return 
     * vorheriger Checkpunkt(Weg)
     */
    public Checkpoint getPrevious_WayCheckpoint() {
        return previous_WayCheckpoint;
    }

    /**
     * Gibt den vorheriger Checkpunkt Parkplatz & Puffer usw. zurÃ¼ck
     *
     * @return vorheriger Checkpunkt(Anderer)
     */
    public Checkpoint getPrevious_OtherCheckpoint() {
        return previous_OtherCheckpoint;
    }

    /**
     * Gibt den ersten Bot aus der Warteliste zurÃ¼ck
     *
     * @return Erster Bot der Warteliste
     */
    public Bot getFirstOnWaitList() {
    	
        Bot bot = this.waiting_List.remove(0);
        
        logger.info("Checkpoint-Warteschlange ersten herausnehmen :"+ this.name+ "  "+ 
    			bot.getBt_Name()  +" ## Warteschlangegesamt="+this.waiting_List.size());
        
        return bot;
    }

    /**
     * Setzt einen Bot in die Warteliste
     *
     * @param bot Zu setzender Bot
     * @return Ob der Bot in die Warteliste gesetzt wurde oder nciht
     */
    public boolean setBotOnWaitList(Bot bot) {
        if (this.isBotInWaitList() || this.isClosed() || this.isReserved()|| this.isStore || this.isExit) {
            this.waiting_List.add(bot);
            logger.info("Checkpoint-Warteschlange NEU :"+ this.name+ "  "+ 
        			bot.getBt_Name()  +" ## Warteschlangegesamt="+this.waiting_List.size());
        
            return true;
        }

        return false;
    }

    /**
     * Überprüt ob sich ein Bot in der Warteliste befindet
     *
     * @return
     * Resultat ob sich ein Bot in der Warteschleife befindet
     */
    public boolean isBotInWaitList() {
        return this.waiting_List.size() == 0 ? false : true;
    }
    
    /**
     * Überprüfung odb der übergebene Bot den Checkpunkt gesperrt oder reserviert hat
     * @param bot
     * ZU überprüfender Bot
     * @return
     * Resultat ob der Bot den Checkpoint gesperrt oder reseviert hat
     */
    public boolean BotHaveClosesOrReserved(Bot bot)
     {
        if(this.closed != null && this.closed == bot)
        	return true;
        
        if(this.reserved != null && this.reserved == bot)
        	return true;
        
    	return false;
    }
    
    
    /**
     * Übergibt die Warteliste
     * @return
     * Warteliste des Bots
     */
    public List<Bot> getWaitList()
    {
    	
    	return this.waiting_List;
    }
}

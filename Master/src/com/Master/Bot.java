package com.Master;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.log4j.LogManager;

/**
 * @author Marcel Reich
 * Bot schnittstelle zum Roboter(Symbolisiert ihn)
 */
public class Bot implements IBot {

	/**
	 * logger
	 */
	private static org.apache.log4j.Logger logger = LogManager.getLogger("Bot");
	
	/**
     * Controller für Informationen
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

    /**
     * ob der Bot in einem Puffer muss
     */
    private boolean puffer_modus;
    
    /**
     * Ob sich der Bot im Puffer befindet oder nicht
     */
    private boolean inPuffer;
    
    /**
     * Ob ein Puffer reserviert wurde oder nicht
     */
    private boolean puffer_reserved;

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
     * Order welche der Bot bearbeitet
     */
    private Order order;

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
     * Zugeteielter Puff (falls notwendig)
     */
    private String puffer;


    /**
     * Message zum Senden nachdem der Bot
     * aus der Warteschlange dran kommt und
     * zum Bot sendet
     */
    private List<Message> m_waitList;
       
    /**
     * Text wenn sich der Bot in der Warteschlange befindet
     */
    private List<Message> m_waitListCheckpoint;
    
    /**
     * ID des Bots
     */
    private int id;

    /**
     * Park/Start- Position des Bots
     */
    private String park_position;

    Bot(IController controller, String bt_Name, int id, String park) {
        this.id = id;
        this.park_position = park;
       
        this.controller = controller;
        this.bt_Name = bt_Name;
        logger.info("Fuer Bot "+bt_Name+" wird ein BT-Objekt angelegt");
        this.bt = new BT(this.bt_Name, this.controller, this);
        logger.info("Fuer Bot "+bt_Name+" wurde ein BT-Objekt angelegt");
       
        this.order_management = new OrderManagement();
        this.order = null;
        
        this.m_waitList = new ArrayList<Message>();
        this.m_waitListCheckpoint = new ArrayList<Message>();
        
        this.status = "Nicht Verbunden";
        this.target = "-";
        this.checkpoint = this.park_position;
        this.last_checkpoint = "-";
        this.puffer = "-";
        
        this.puffer_modus = false;
        this.puffer_reserved = false;
        this.connect = false;
        this.inWaitList = false;
        this.inPuffer = false;
    }

    /**
     * Baut eine Verbindung zum Roboter auf
     *
     * @return Resultat ob eine Verbindung aufgebaut werden konnte oder nicht
     */
    public boolean Connect() {

        if (!connect) {
        	logger.info("Verbindungsaufbau  für Bot "+bt_Name+" wird gestartet");
            this.controller.InputConsole("Verbindungsaufbau wird gestartet");

            if (bt.ConnectAgent()) {
                connect = true;
                logger.info("Verbindung zum Bot "+bt_Name+" hergestellt");
                this.controller.InputConsole("Verbindung hergestellt!");
                this.status = "Verbunden";
                this.InfoUpdate();
                this.controller.UpdateMap(this.checkpoint, null, this.bt_Name);
                return true;
            } else {
                this.controller.InputConsole("KEINE Verbindung");
            }

        } else {
        	logger.info("Verbindung zum Bot "+bt_Name+" wird getrennt");
            this.controller.InputConsole("Verbindung trennen");
            connect = false;
            bt.CloseAgent();
            logger.info("Verbindung zum Bot "+bt_Name+" trennt");
            this.controller.InputConsole("Verbindung getrennt");
        }

        return false;

    }

  
    /**
     * Nimmte eine neu Order entgegen
     * @param order Neue Order
     */
    public void NewOrder(Order order) {
        if(this.order == null)
        {
        	
        	if(order != null)
        	{
        		this.order = order;
        		String message = Protokoll.OrderToString(order);
			    this.InfoUpdate();
			
			    if (bt.SendMessage(message))
			        {
			    	logger.info("Die Nachricht "+message+" für Bot "+bt_Name+" wurde erfolgreich gesendet");	
			    	this.controller.InputConsole("Nachricht " + message + " wurde erfolgreich gesendet");
			        }
			    else
			    {
			    	logger.info("Die Nachricht "+message+" für Bot "+bt_Name+" konnte nicht gesendet werden");
			    	this.controller.InputConsole("Fehler beim senden der Nachricht " + message);
			    }
        	}else
            {
            	logger.info("Bot "+bt_Name+"hat keine eine neue Aufgabe erhalten");
            }
        }else
        {
        	logger.error("Bot "+bt_Name+" wurde eine neue Aufgabe ("+order.getId()+") zugewisen, obwohl ider noch eine hat"+this.order.getId()+")");
        }
		    
    }
    
    /**
     * Updatet die Posizion des Bots
     * @param check Aktueller Checkpunkt
     * @param next_check nächster Checkpunkt
     */
    private void UpdateCheckpoint(String check, String next_check) {
    	logger.info("Bot "+bt_Name+" setzt seine aktuelle Position auf den Checkpunkt  "+check +" und seinenaechste auf "+next_check);
        this.last_checkpoint = this.checkpoint;
        this.checkpoint = check;
        this.next_checkpoint = next_check;
        this.controller.UpdateMap(this.checkpoint, this.last_checkpoint, this.bt_Name);
    }



    /**
     * Bot befindet sich im Einang und bekommt die mittgeteilt das dieser aufladen darf
     * @param check Checkpunkt
     */
    private void Entrance(String check) {
        this.last_checkpoint = this.checkpoint;
        this.checkpoint = check;
        logger.info("Position des Bots "+bt_Name+" wird auf "+check+" gesetzt");
        this.controller.UpdateMap(this.checkpoint, this.last_checkpoint, this.bt_Name);
        controller.InputConsole((this.bt_Name + " bereit zum Laden"));
        logger.info("Bot "+bt_Name+" bekommt die genehmigung aufzuladen");
        bt.SendMessage(Protokoll.MessageToString((new Message(MasterData.code_Continue, MasterData.code_Load))));
    }


    /**
     * Überprüft ob der Bot weiterfahren kann und sendet dies dann dem Roboter zu
     */
    private void CheckAndSendForContinue() {
        if (this.controller.CheckForContinue(checkpoint, next_checkpoint, this)) {
            controller.InputConsole((this.bt_Name + ": Checkpoint:" + this.next_checkpoint + " Freigeben"));
            logger.info("Bot "+bt_Name+" wird die genehmigung für Checkpunkt "+next_checkpoint+" gesendet");
            bt.SendMessage(Protokoll.MessageToString((new Message(MasterData.code_Continue, this.next_checkpoint))));
 
        } else {
        	logger.info("Bot "+bt_Name+" bekommt  Checkpunkt "+next_checkpoint+" nicht freigegeben");
            controller.InputConsole((this.bt_Name + ": Checkpoint:" + this.next_checkpoint + " nicht Freigeben"));
            this.inWaitList = true;
            this.m_waitListCheckpoint.add(new Message(MasterData.code_Continue, this.next_checkpoint));
        }
    }


    /**
     * Fährt die Aktionen weiter bevor der Bot in die Warteschlange kam
     *
     * @return
     * Resultat ob der Bot aus der Warteschlange geholt wurde oder nicht
     */
    public boolean ContinueAfterWaitList() {
        if (this.inWaitList && this.m_waitList != null) {
        	logger.info("Bot "+bt_Name+" wird aus der Warteschlange geholt und bekommt die genehmigung für Checkpunkt "+next_checkpoint);
            controller.InputConsole((this.bt_Name + ": Checkpoint:" + this.next_checkpoint + " Freigeben"));
            bt.SendMessage(Protokoll.MessageToString(this.m_waitListCheckpoint));


            this.inWaitList = false;
            this.m_waitListCheckpoint.clear();

            return true;
        } else if (this.puffer_modus) {
        	logger.info("Bot "+bt_Name+" wird aus der Warteschlange und dem Puffer geholt und bekommt die genehmigung weiter zu fahren ");
        	controller.InputConsole((this.bt_Name + ": Checkpoint:" + this.next_checkpoint + " Freigeben"));
            bt.SendMessage(Protokoll.MessageToString(this.m_waitList));

        	
        	this.puffer_modus = false;
        	this.puffer_reserved = false;
        	this.inPuffer = false;
        	this.puffer = "-";
        	this.m_waitList.clear();
            return true;
        }else
        {
        	logger.error("Bot "+bt_Name+" wurde aus der Warteschleife geholt obwohl sich dier nicht in einer befidnet ");
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.Master.IBot#HandleMessageInput(java.lang.String)
     */
    public void HandleMessageInput(String message) {
    	//Analysiert den Nachrichten eingang
        try
        {
        	if (message != null && message != "") {
                controller.InputConsole(this.bt_Name + " (Eingang) : " + message);
                logger.info("Von Bot "+bt_Name+" ist folgende nachrciht eingegangen: "+message);
                List<Message> m = Protokoll.StringToMessage(message);

                if (m.size() == 2) {
                    Message m1 = m.get(0);
                    Message m2 = m.get(1);

                    if (m1.getKey().equals(MasterData.code_Checkpoint) && m2.getKey().equals(MasterData.code_NextCheckpoint)) {
                    	logger.info("Nachricht von Bot "+bt_Name+" das sich dieser auf "+m1.getValue()+" befindet und nach "+m2.getValue()+" will" );
                    	this.UpdateCheckpoint(m1.getValue(), m2.getValue());
                        this.CheckAndSendForContinue();
                    } else if (m1.getKey().equals(MasterData.code_Checkpoint) && (m2.getKey().equals(MasterData.code_PostionLoad ) || m2.getKey().equals(MasterData.code_PostionUnload ))) {
                    	logger.info("Nachricht von Bot "+bt_Name+" das sich dieser auf "+m1.getValue()+" befindet und auf  "+m2.getValue()+" auf oder abladen will" );
                        if (m1.getValue().equals(m2.getValue())) {
                            this.Entrance(m1.getValue());
                        }
                    } else if (m1.getKey().equals(MasterData.code_FinishLoad) && m2.getKey().equals(MasterData.code_NextCheckpoint)) {
                    	logger.info("Nachricht von Bot "+bt_Name+" das  dieser fertig mit aufladen  ist und nach "+m2.getValue()+" will" );
                        this.next_checkpoint = m2.getValue();
                        this.controller.OrderLoad(this.order);
                        this.CheckAndSendForContinue();
                        
                    }else if (m1.getKey().equals(MasterData.code_FinishUnload) && m2.getKey().equals(MasterData.code_NextCheckpoint)) {
                    	logger.info("Nachricht von Bot "+bt_Name+" das dieser fertig  ist mit abladen   und nach "+m2.getValue()+" will" );
                        this.next_checkpoint = m2.getValue();
                        this.controller.OrderUnload(this.order, this);
                        this.order_management.add(this.order);
                        
                        this.CheckAndSendForContinue();
                                                
                        Order o = this.controller.NextOrderForBot();                       
                        
                        this.order = null;
                        if(o != null)
                    	{
                        	o.SetBot(this);
                    		this.NewOrder(o);                    
                    	}
                        
                    }else if (m1.getKey().equals(MasterData.code_Checkpoint) && m2.getKey().equals(MasterData.code_ParkPosition)) {
                    	logger.info("Nachricht von Bot "+bt_Name+" das sich dieser auf "+m1.getValue()+" befindet und parkt" );
                    	this.Parking();
                        
                    }else if (m1.getKey().equals(MasterData.code_Checkpoint) && m2.getKey().equals(MasterData.code_Puffer)) {
                    	logger.info("Nachricht von Bot "+bt_Name+" das sich dieser auf "+m1.getValue()+" befindet und im Puffer wartet" );
                    	this.inPuffer = true;
                    	this.last_checkpoint = this.checkpoint;
                    	this.checkpoint = this.puffer; 
                    	controller.UpdateMap(checkpoint, last_checkpoint, this.bt_Name);
                    }
                } else if (m.size() == 3) {
                    Message m1 = m.get(0);
                    Message m2 = m.get(1);
                    Message m3 = m.get(2);

                    if (m1.getKey().equals(MasterData.code_Checkpoint) && m2.getKey().equals(MasterData.code_Puffer) && m3.getKey().equals(MasterData.code_TestTarget)) {
                        
                    	logger.info("Nachricht von Bot "+bt_Name+" das sich dieser auf "+m1.getValue()+" befindet und nach dem Ausgang  "+m3.getValue()+" will" );
                    	if (controller.TestEntranceForPuffer(m3.getValue(), this)) { // PrÃ¼fen ob Lager belegt ist
                            if (controller.ExitReserved(m3.getValue(),this)) { // Lager Reservieren!
                            	
                            	List<Message> list = new ArrayList<Message>();
                            	list.add((new Message(MasterData.code_Continue, m1.getValue())));
                            	list.add((new Message(MasterData.code_Reserved, m3.getValue())));

                                bt.SendMessage(Protokoll.MessageToString(list));                        	
                                return;
                            }
                        }

                        this.controller.SetOnWaitList(m3.getValue(), this) ;            
                    	Checkpoint p = controller.NextFreePuffer();
                    	
                    	
                    	if(p != null)
                    	{
                    		List<Message> list = new ArrayList<Message>();
                        	list.add((new Message(MasterData.code_Continue, m1.getValue())));
                        	list.add((new Message(MasterData.code_Puffer, p.getName())));
                        	
                        	this.controller.CheckpointReserved(p.getName(), this);
                        	this.puffer_modus = true;
                        	this.puffer_reserved = true;
                        	this.puffer = p.getName();

                            bt.SendMessage(Protokoll.MessageToString(list));
                            
                            list.clear();
                        	list.add((new Message(MasterData.code_Continue, m1.getValue())));
                        	list.add((new Message(MasterData.code_Reserved, m3.getValue())));
                        	
                        	this.m_waitList = list;
                    	}

                        return;                    
                    }else if(m1.getKey().equals(MasterData.code_Checkpoint) && m2.getKey().equals(MasterData.code_ToPark) && m3.getKey().equals(MasterData.code_TestTarget)) {
                    
                    	logger.info("Nachricht von Bot "+bt_Name+" das sich dieser auf "+m1.getValue()+" befindet und nach dem Eingang  "+m3.getValue()+" will" );
                    	List<Message> list = new ArrayList<Message>();
                    	
                    	if (controller.TestEntranceForPuffer(m3.getValue(), this)) { // PrÃ¼fen ob Lager belegt ist
                            if (controller.StoreReserved(m3.getValue(), this)) { // Lager Reservieren!
                            	
                            	
                            	list.add((new Message(MasterData.code_Continue, m1.getValue())));
                            	list.add((new Message(MasterData.code_Reserved, m3.getValue())));

                                bt.SendMessage(Protokoll.MessageToString(list));                        	
                                return;
                            }
                        }
                    	
                    	this.controller.SetOnWaitList(m3.getValue(), this) ;  
                    	this.puffer_modus = true;	
                    	
                    	list.add((new Message(MasterData.code_Continue, m1.getValue())));
                    	list.add((new Message(MasterData.code_ToPark, this.park_position)));
                    	bt.SendMessage(Protokoll.MessageToString(list));
                    	
                    	
                    	list.clear();
                    	list.add((new Message(MasterData.code_Continue, m1.getValue())));
                    	list.add((new Message(MasterData.code_Reserved, m3.getValue())));
                    	
                    	this.m_waitList = list;
                    }
                }
                this.InfoUpdate();
            }
        }catch (Exception e)
        {
        	logger.info("Bot "+bt_Name+" Execption-HandleMessageInput "+ e);
        }
    	
    }
    
    /**
     * Sendet dem Bot das Stopp-Signal
     */
    public void Stop()
    {
    	bt.SendMessage(Protokoll.MessageToString((new Message(MasterData.code_STOP, MasterData.STOP_CODE))));
    	this.bt.CloseAgent();
    }
    
    /**
     * Parkmodus des Bots (Bot hat geparkt)
     */
    public void Parking()
    {
    	this.last_checkpoint = this.checkpoint;
    	this.checkpoint = this.park_position; 
    	logger.info("Bot "+bt_Name+"  befindet sich in der  Park-Position "+ park_position);
    	controller.UpdateMap(checkpoint, last_checkpoint, this.bt_Name);
    }

    /**
     * Sendet dem Bot das dieser Parken soll
     */
    public void SendParkPosition() {
        bt.SendMessage(Protokoll.MessageToString((new Message(MasterData.code_ParkPosition, this.park_position))));
        logger.info("Bot "+bt_Name+" wurde die Park-Position "+ park_position+" gesendet");
        controller.InputConsole((this.bt_Name + ": Park Position " + this.park_position + " gesendet"));
    }

    /**
     * Updatet die Informationen des Bots in der Benutzeroberfläche (Tabelle)
     */
    public void InfoUpdate() {
    	String auftrag = this.order != null ? this.order.getId() : "-";
        this.controller.UpdateTable(this.id, new String[]{this.bt_Name, this.status, this.target, this.checkpoint, auftrag});
    }
    
    
    /**
     * Gitb an ob der Bot einen Order hat,w eclhe er bearbeiten muss oder nicht
     * @return
     * Hat der Bot eine Order
     */
    public boolean HaveOrder()
    {
    	return (order != null);
    }
    
    /**
     * Gibt die ID des zurzeit bearbeiten Order zurückgibt
     * @return
     * Order ID
     */
    public String getOrderID()
    {
    	return this.order.getId();
    }
    
    /**
     * Gibt den Verbindungsstatus des Bots an
     *
     * @return Verbindungsstatus
     */
    public boolean isConnect() {
        return this.bt.isConnect();
    }

    /**
     * Gibt den Namen das Bots an
     *
     * @return Bot-Name
     */
    public String getBt_Name() {
        return bt_Name;
    }

    /**
     * Gibt den Namen des Checkpoints an wo der Bot sich befindet.
     *
     * @return Checkpoint
     */
    public String getCheckpoint() {
        return checkpoint;
    }
    
    /**
     * Gibt an ob sich der Bot im Puffer befindet
     * @return
     * Bot in Puffer oder nicht
     */
    public boolean IsinPuffer()
    {
    	return this.inPuffer;
    }
    
    /**
     * Gibt an ob der Bot einen Puffer reserviert hat
     * @return
     * Puffer von Bot reserviert
     */
    public boolean HavePufferReserved()
    {
    	return this.puffer_reserved;
    }
    
    /**
     * Übergibt den Puffer Namen
     * @return
     * Name des zugeteilten Puffers
     */
    public String GetPuffer()
    {
    	return this.puffer;
    }

}

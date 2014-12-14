package com.Master;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Bot implements IBot {

	/**
	 * logger
	 */
	private static Logger logger = Logger.getAnonymousLogger();
	
	/**
     * Controller fÃ¼r Informationen
     */
    private IController controller;

    /**
     * Bluetooth-Objekt fÃ¼r die Verbindung
     */
    private BT bt;

    /**
     * Verbindungsstatus
     */
    private boolean connect;


    private boolean puffer_modus;
    
    private boolean inPuffer;
    
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
     * NÃ¤chste Position
     */
    private String next_checkpoint;
    
    
    private String puffer;


    /**
     * Message zum Senden nachdem der Bot
     * aus der Warteschlange dran kommt und
     * zum Bot sendet
     */
    private List<Message> m_waitList;
    
    
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
     * @return
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
     * Nimmt eine neue Order entegen und sendet sie zum Roboter
     *
     * @param order Neue order des Bots
     */
    public void NewOrder(Order order) {
        this.order_management.add(order);
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
    }

    /**
     * Updatet die Position des Bots
     */
    private void UpdateCheckpoint(String check, String next_check) {
    	logger.info("Bot "+bt_Name+" setzt seine aktuelle Position auf den Checkpunkt  "+check +" und seinenaechste auf "+next_check);
        this.last_checkpoint = this.checkpoint;
        this.checkpoint = check;
        this.next_checkpoint = next_check;
        this.controller.UpdateMap(this.checkpoint, this.last_checkpoint, this.bt_Name);
    }

    /**
     * Updatet die Position des Bots
     */
    private void Entrance(String check) {
        this.last_checkpoint = this.checkpoint;
        this.checkpoint = check;
        logger.info("Position des Bots "+bt_Name+" wird auf "+check+" gesetzt");
        this.controller.UpdateMap(this.checkpoint, this.last_checkpoint, this.bt_Name);
        controller.InputConsole((this.bt_Name + " bereit zum Laden"));
        logger.info("Bot "+bt_Name+" bekommt die genehmigung auf- oder abzuladen");
        bt.SendMessage(Protokoll.MessageToString((new Message(MasterData.code_Continue, MasterData.code_Load))));
    }


    /**
     * ÃœberprÃ¼ft ob der Bot weiterfahren kann und sendet dies dann dem Roboter zu
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
     * FÃ¼hrt die Aktionen weiter bevor der Bot in die Warteschlange kam
     *
     * @return
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
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.Master.IBot#HandleMessageInput(java.lang.String)
     */
    public void HandleMessageInput(String message) {
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
                    this.controller.OrderLoad(this.order_management.getById(m1.getValue()));
                    this.CheckAndSendForContinue();
                    
                }else if (m1.getKey().equals(MasterData.code_FinishUnload) && m2.getKey().equals(MasterData.code_NextCheckpoint)) {
                	logger.info("Nachricht von Bot "+bt_Name+" das dieser fertig  ist mit abladen   und nach "+m2.getValue()+" will" );
                    this.next_checkpoint = m2.getValue();
                    this.controller.OrderUnload(this.order_management.getById(m1.getValue()), this);
                    this.CheckAndSendForContinue();
                    
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
                        if (controller.CheckpointReserved(m3.getValue(),this)) { // Lager Reservieren!
                        	
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
                        if (controller.CheckpointReserved(m3.getValue(), this)) { // Lager Reservieren!
                        	
                        	
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
    }
    
    
    public void Parking()
    {
    	this.last_checkpoint = this.checkpoint;
    	this.checkpoint = this.park_position; 
    	logger.info("Bot "+bt_Name+"  befindet sich in der  Park-Position "+ park_position);
    	controller.UpdateMap(checkpoint, last_checkpoint, this.bt_Name);
    }

    /**
     *
     */
    public void SendParkPosition() {
        bt.SendMessage(Protokoll.MessageToString((new Message(MasterData.code_ParkPosition, this.park_position))));
        logger.info("Bot "+bt_Name+" wurde die Park-Position "+ park_position+" gesendet");
        controller.InputConsole((this.bt_Name + ": Park Position " + this.park_position + " gesendet"));
    }

    /**
     * Updatet die Informationen des Bots in der BenutzeroberflÃ¼che (Tabelle)
     */
    public void InfoUpdate() {
        this.controller.UpdateTable(this.id, new String[]{this.bt_Name, this.status, this.target, this.checkpoint, "" + this.order_management.size()});
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
    
    public boolean IsinPuffer()
    {
    	return this.inPuffer;
    }
    
    public boolean HavePufferReserved()
    {
    	return this.puffer_reserved;
    }
    
    public String GetPuffer()
    {
    	return this.puffer;
    }

}

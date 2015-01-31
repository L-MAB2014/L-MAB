package com.marcel.Communication;


import lejos.nxt.*;
import lejos.util.Delay;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcel Reich
 *
 */
/**
 * @author Marcel
 *
 */
public class Roboter implements IRoboter {

    /**
     * Wert zur PID-Rechnung
     */
    float tp = 350;
    
    /**
     * Wert zur PID-Rechnung
     */
    float light;
    
    /**
     * Wert zur PID-Rechnung
     */
    float high;
    
    /**
     * Wert zur PID-Rechnung
     */
    float low;
    
    /**
     * Wert zur PID-Rechnung
     */
    float offset;
    
    /**
     * Wert zur PID-Rechnung
     */
    float error;
    
    /**
     * Wert zur PID-Rechnung
     */
    float turn;
    
    /**
     * Wert zur PID-Rechnung
     */
    float kp = 15;
    
    /**
     * layout
     */
    private CheckpointList map;
    
    /**
     * Parkposition
     */
    private Checkpoint park_Position;
    
    /**
     * Akutelle Psotion des Roboters
     */
    private Checkpoint position;
    
    /**
     * Puffer Postion des Puffes (falls notwendig)
     */
    private Checkpoint puffer_position;
    
    /**
     * Farbsensor
     */
    private ColorSensor colorSensor;
    
    /**
     * Lichtsensor
     */
    private LightSensor lightSensor;
    
    /**
     * Rechter-Antriebsmotor
     */
    private NXTRegulatedMotor rightMotor;
    
    /**
     * Linker Antriebs-Motor
     */
    private NXTRegulatedMotor leftMotor;
    
    /**
     * Verbindungsobjekt
     */
    private BTConnect bt;
    
    /**
     * Auftragsmanager
     */
    private OrderManagement manager;
    
    /**
     * Ob der Roboter warten muss(Aus Antwort/Freigabe des Leitsandes)
     */
    private boolean wait;
    
    /**
     * Zeit seit dem letzten Checkpoints
     */
    private long last_checkpoint;
    
    /**
     * Ob das Pogramm beendet werden soll
     */
    private boolean IsEnding;

    /**
     * Ob sich der Roboter im Parkmodus befndet
     */
    private boolean IsParking;

    /**
     * Ob dem Roboter ein Parkplatz zugewiesen wurde
     */
    private boolean InitializePosition;
    
    /**
     * Erlaubnis Ware abzuholen
     */
    private boolean licence_store;
    
    /**
     * Erlaubnis Ware abzuliefern
     */
    private boolean licence_exit;
    
    /**Ob der Roboter zum Puffer muss oder nciht
     * 
     */
    private boolean toPuffer;
   
    /**
     * Ob sicher Roboter im Puffer befindet oder nicht
     */
    private boolean inPuffer;
    
    /**
     * Ob der Roboter die Hälfte der Strecke erreicht hat
     */
    private boolean way_check;

    Roboter() {
        this.map = CreatCheckpoints.InitializeCheckpoints();
        this.IsEnding = false;
        this.wait = false;
        this.InitializePosition = false;

        this.IsParking = true;

        this.last_checkpoint = 0;
        this.manager = new OrderManagement();

        this.bt = new BTConnect(this);

        this.colorSensor = new ColorSensor(SensorPort.S1);
        this.lightSensor = new LightSensor(SensorPort.S2);
        
        this.lightSensor.setFloodlight(true);

        this.rightMotor = new NXTRegulatedMotor(MotorPort.C);
        this.leftMotor = new NXTRegulatedMotor(MotorPort.A);

        this.rightMotor.setSpeed(tp);
        this.leftMotor.setSpeed(tp);

        Button.ESCAPE.addButtonListener(new ButtonListener() {
            public void buttonPressed(Button b) {
                IsEnding = true;
                System.exit(0);
            }

            public void buttonReleased(Button b) {
            }
        });
        this.drive();
    }

    public static void main(String[] args) {
        Roboter r = new Roboter();
    }

    /**
     * Zuweisung
     */
    private void InitializeStart() {
        LCD.clear();
        LCD.drawString("Auf Zuweisung warten!", 0, 1);

        while (!this.InitializePosition) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }
    }

    /**
     * Startvorgang des Roboters
     */
    private void drive() {
        try {

            init();


            this.IsEnding = false;

            if (this.bt.Connection()) {
                this.InitializeStart();

                while (!this.IsEnding) {

                    LCD.drawString("Auftrï¿½ge: " + this.manager.size() + "    ", 0, 4);

                    if (this.manager.size() > 0) {
                        Order order = this.manager.GiveFirstOrder();
                        LCD.clear();

                        LCD.drawString("AuftragNr.: " + order.getId(), 0, 4);
                        LCD.drawString("Lager: " + order.getStore_place(), 0, 5);
                        LCD.drawString("Ausgang: " + order.getExit_place(), 0, 6);
                        LCD.drawString("Modus:   Start ", 0, 7);

                        this.MakeOrder(order);

                        LCD.clear();
                    } else {
                        this.leftMotor.stop(true);
                        this.rightMotor.stop(true);
                    }
                }
                this.bt.Close();
            }
        } catch (Exception e) {

        }

    }

    /**
     * Initialiseren der Sensorwerte
     */
    void init() {

        LCD.drawString("HELL: ", 0, 0);

        Button.waitForAnyPress();

        high = this.lightSensor.getLightValue();

        LCD.drawString("Hell: " + high, 0, 0);

        Button.waitForAnyPress();
        LCD.clear();

        LCD.drawString("Dunkel:", 0, 0);

        Button.waitForAnyPress();

        low = this.lightSensor.getLightValue();

        LCD.drawString("Dunkel: " + low, 0, 0);

        Button.waitForAnyPress();

        LCD.clear();

        offset = (high + low) / 2;

        LCD.drawString("Mitte: " + offset, 0, 0);

        LCD.drawString("Init Fertig", 0, 2);
        Button.waitForAnyPress();
        LCD.clear();
    }

    /**
     * Order Abarbeiten
     * @param order Abzuarbeitene Order
     */
    private void MakeOrder(Order order) {
        Checkpoint check_store = map.getCheckpoint(order.getStore_place());
        Checkpoint check_exit = map.getCheckpoint(order.getExit_place());

        boolean isLoaded = false;
        boolean left_curve = false;
        
        this.way_check = true;
        
        this.licence_store  = false;
        this.licence_exit = false;
        this.toPuffer = false;
        this.inPuffer = false;

       this.Start_Modus(check_store);
        

        Checkpoint nextCheckWay = position.getNext_WayCheckpoint();
        Checkpoint nextCheckOther = position.getNext_OtherCheckpoint();
        
        boolean roundEnd = false;

        while (!roundEnd) {
            
		  if(this.IsOnPuffer() && !this.licence_exit){			  
			  this.PufferModus(nextCheckOther, check_exit);
			  
		  } else if (!isLoaded && nextCheckOther != null && nextCheckOther == check_store) {
                this.EntranceModus(nextCheckOther, nextCheckWay, order.getId(), true);
                isLoaded = true;

                this.position = nextCheckWay;
                nextCheckWay = position.getNext_WayCheckpoint();
                nextCheckOther = position.getNext_OtherCheckpoint();

            } else if (isLoaded && nextCheckOther != null && nextCheckOther == check_exit) {
                this.EntranceModus(nextCheckOther, nextCheckWay, order.getId(), false);
                isLoaded = false;

                this.position = nextCheckWay;
                nextCheckWay = position.getNext_WayCheckpoint();
                nextCheckOther = position.getNext_OtherCheckpoint();

            }

		  
		  	this.CheckForContinue(new Message(RoboterData.code_Checkpoint, position.getName()),
                    new Message(RoboterData.code_NextCheckpoint, nextCheckWay.getName()));                       

           

            this.fahreZu(this.position.getNext_WayCheckpoint().getColor());


            this.position = nextCheckWay;
            nextCheckWay = position.getNext_WayCheckpoint();
            nextCheckOther = position.getNext_OtherCheckpoint();

            if (nextCheckOther != null && nextCheckOther == this.park_Position) {
                roundEnd = true;
            }
                       

        }

        if (manager.size() == 0) {
            this.Parking();
        }


    }
    
   
    
    
    /**
     * Managen des Parken 
     */
    private void Parking() {
        LCD.drawString("PARKEN!! ", 0, 2);

        this.rechts90();
        
        this.CheckForContinue(new Message(RoboterData.code_Checkpoint, position.getName()),
                new Message(RoboterData.code_NextCheckpoint, this.park_Position.getName()));

        
        this.fahreZu(this.park_Position.getColor());
        this.turn180();

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        this.position = this.park_Position;

        
        List<Message> message = new ArrayList<Message>();
        message.add(new Message(RoboterData.code_Checkpoint, position.getName()));
        message.add(new Message(RoboterData.code_ParkPosition, position.getName()));

        this.bt.SendPosition(Protokoll.MessageToString(message));

        this.IsParking = true;


    }

    /**
     * Herausfahren aus einem Parkplatz
     */
    private void Unparking() {
        this.CheckForContinue(new Message(RoboterData.code_Checkpoint, position.getName()),
                new Message(RoboterData.code_NextCheckpoint, position.getNext_WayCheckpoint().getName()));

        this.fahreZu(this.position.getNext_WayCheckpoint().getColor());

        this.position = position.getNext_WayCheckpoint();

        this.rechts45();
        this.IsParking = false;
    }

    /**
     * Positon melden und um Freigabe für den Nächsten Checkppoint beim Leitstand bitten 
     * @param check Aktuelle Positon
     * @param next_check Nächster Checkpunkt
     */
    private void CheckForContinue(Message check, Message next_check) {
        List<Message> message = new ArrayList<Message>();
        message.add(check);
        message.add(next_check);

        this.bt.SendPosition(Protokoll.MessageToString(message));
        this.WaiteForOk();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        
        this.last_checkpoint = System.currentTimeMillis();
    }

    /**
     * Zuweisung der Parkposition
     * @param message Parkposition
     */
    private void SetParkPosition(Message message) {
        Checkpoint check = map.getCheckpoint(message.getValue());
        LCD.drawString("PP: " + message.getValue(), 0, 2);
        if (check != null) {
            this.park_Position = check;
            this.position = check;

            this.InitializePosition = true;
        }
    }

    /**
     * Zu nächsten Farbe farhen
     * @param farbe Farbe zu der gefahren werden soll
     */
    private void fahreZu(int farbe) {

        leftMotor.forward();
        rightMotor.forward();

        while (!this.IsEnding) {
            light = this.lightSensor.getLightValue();
            int f = this.colorSensor.getColorID();

            if ((System.currentTimeMillis() - this.last_checkpoint) > 1000) {

                if (f == farbe) {
                    Sound.playTone(500, 500, 100);
                    return;
                }
            }

            if (light >= high)
                light = high;
            if (light <= low)
                light = low;

            error = light - offset;

            turn = (kp * error);


            if (turn >= tp)
                turn = tp - 20;
            if (turn <= -tp)
                turn = -tp + 20;

            LCD.drawString("Licht: " + light, 0, 1);
            LCD.drawString("Links: " + (tp + turn), 0, 2);
            LCD.drawString("Rechts: " + (tp - turn), 0, 3);


            leftMotor.setSpeed(tp - turn);
            rightMotor.setSpeed(tp + turn);
        }

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

    }

    /**
     * Auf ein Ok des Leitstands warten
     */
    private void WaiteForOk() {
        this.wait = false;
        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        while (!this.wait) {
        }
    }

    /**Managet den Roboter innhaler eines Ein- oder Ausgangs
     * @param entrance Eingang
     * @param exit	Ausgang (zum herausfahren aus dem lager)
     * @param order_id Order ID
     * @param status Status Auf- oder Abladen
     */
    private void EntranceModus(Checkpoint entrance, Checkpoint exit, String order_id, boolean status) {
        try {
            
        	this.rechts90();
        	
        	this.CheckForContinue(new Message(RoboterData.code_Checkpoint, position.getName()),
                    new Message(RoboterData.code_NextCheckpoint, entrance.getName()));            

            this.fahreZu(entrance.getColor());

            this.CheckForContinue(new Message(RoboterData.code_Checkpoint, entrance.getName()),
                    new Message((status ? RoboterData.code_PostionLoad : RoboterData.code_PostionUnload), entrance.getName()));

            this.position = entrance;

            this.leftMotor.stop(true);
            this.rightMotor.stop(true);

            Sound.playTone(500, 500, 100);
            Thread.sleep(5000);
            Sound.playTone(500, 500, 100);

            List<Message> message = new ArrayList<Message>();
            message.add(new Message((status ? RoboterData.code_FinishLoad : RoboterData.code_FinishUnload), order_id));
            message.add(new Message(RoboterData.code_NextCheckpoint, exit.getName()));

            this.links120();
            this.bt.SendPosition(Protokoll.MessageToString(message));
            this.WaiteForOk();

            this.fahreZu(exit.getColor());


        } catch (Exception e) {

        }
    }

    
    /**
     * Managet den Start des Roboters
     * @param check_store Ziel-Eingang des Roboters 
     */
    private void Start_Modus(Checkpoint check_store)
    {
    	List<Message> message = new ArrayList<Message>();
        message.add(new Message(RoboterData.code_Checkpoint, this.position.getName()));
        message.add(new Message(RoboterData.code_ToPark, this.park_Position.getName()));
        message.add(new Message(RoboterData.code_TestTarget, check_store.getName()));
   	
        this.bt.SendPosition(Protokoll.MessageToString(message));
        this.WaiteForOk();

        if(this.licence_store)
        {
        	if (this.IsParking) {
                this.Unparking();
            }
        }else
        {
        	if (!this.IsParking) {
                this.Parking();
            }
        	
        	while (!this.licence_store) {
	        	try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
        	
        	this.Unparking();
        }
        
    }
    
    /**
     * Managet den Puffer-Modus des Roboters
     * @param puffer Zugewiesener Puffer
     * @param target Ziel-Ausgang
     */
    private void PufferModus(Checkpoint puffer, Checkpoint target)
    {        
    	if(this.puffer_position == null)
    	{
    		List<Message> message = new ArrayList<Message>();
	         message.add(new Message(RoboterData.code_Checkpoint, position.getName()));
	         message.add(new Message(RoboterData.code_Puffer, puffer.getName()));
	         message.add(new Message(RoboterData.code_TestTarget, target.getName()));
	    	
	         this.bt.SendPosition(Protokoll.MessageToString(message));
	         this.WaiteForOk();
    	}
    	
    	if(!this.licence_exit && (puffer == this.puffer_position) && this.toPuffer)
    	{
    		
    		this.CheckForContinue(new Message(RoboterData.code_Checkpoint, this.position.getName()),
                    new Message(RoboterData.code_NextCheckpoint, puffer.getName()));
    		
    		this.rechts90();
	        this.fahreZu( puffer.getColor());
	        this.turn180();
	        
	        List<Message> message = new ArrayList<Message>();
	         message.add(new Message(RoboterData.code_Checkpoint, puffer.getName()));
	         message.add(new Message(RoboterData.code_Puffer, puffer.getName()));
	         
	         this.bt.SendPosition(Protokoll.MessageToString(message));
	        
	        this.inPuffer = true;
	        
	        this.leftMotor.stop(true);
	        this.rightMotor.stop(true);

	        while (!this.licence_exit) {
	        	try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	        
	        this.CheckForContinue(new Message(RoboterData.code_Checkpoint, puffer.getName()),
                    new Message(RoboterData.code_NextCheckpoint, position.getName())); 
	        
	        this.fahreZu(position.getColor());

	        this.rechts45();   
	        this.toPuffer = false;
	        this.puffer_position = null;
    	}
    	    	
    }
    
    /**
     * 90 Grad Drehung nach Links
     */
    void links90() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        rightMotor.rotate(450);
    }
    
    /**
     * 120 Grad Drehung nach Links
     */
    void links120() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        rightMotor.rotate(550);
    }
    
    
    /**
     * 90 Grad Drehung nach Rechts
     */
    void rechts90() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        leftMotor.rotate(400);
    }

    /**
     * 45 Grad Drehung nach links
     */
    void links45() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        rightMotor.rotate(360);
    }
    
    /**
     * 45 Grad Drehung nach Rechts
     */
    void rechts45() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        leftMotor.rotate(360);
    }

    /**
     * 180 Grad Drehung
     */
    void turn180() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        leftMotor.backward();
        rightMotor.forward();

        Delay.msDelay(1800);


    }
    
    
    /**
     * Überprüft ob man sich auf kreuzung zum Puffer befindet 
     * @return
     * Ergebnis
     */
    private boolean IsOnPuffer()
    {
    	String check = this.position.getName();
    	return (check.equals("C1") || check.equals("C2") || check.equals("C3"));
    }

    /* (non-Javadoc)
     * @see com.marcel.Communication.IRoboter#InputMessage(java.lang.String)
     */
    public void InputMessage(String message) {
        LCD.drawString("N: " + message, 0, 0);
        List<Message> list = Protokoll.StringToMessage(message);

        if (list.size() == 1) {

            if (list.get(0).getKey().equals(RoboterData.code_Continue)) {
                this.wait = true;

            } else if (list.get(0).getKey().equals(RoboterData.code_ParkPosition)) {
                this.SetParkPosition(list.get(0));            
            }else if (list.get(0).getKey().equals(RoboterData.code_STOP)) {
            	if(list.get(0).getValue().equals(RoboterData.STOP_CODE))
            		System.exit(0);
            }
            	
            
        } else if (list.size() == 2) {
        	Message m1 = list.get(0);
            Message m2 = list.get(1);

            if (m1.getKey().equals(RoboterData.code_Continue) && m2.getKey().equals(RoboterData.code_Reserved)) {
            	
            	if(this.way_check)
            		this.licence_store = true;
            	else
            		this.licence_exit = true;
            	
            	this.way_check = !this.way_check;
            	this.wait = true;
            }else if (m1.getKey().equals(RoboterData.code_Continue) && m2.getKey().equals(RoboterData.code_Puffer)) 
            {
            	LCD.drawString("PUFFER -->" + m2.getValue(), 0, 1);
            	// Muss in den Puffer fahren
            	this.puffer_position = this.map.getCheckpoint(m2.getValue());
            	
            	if(this.puffer_position != null)
            	{          	 
            		this.toPuffer = true;
            		this.wait = true;
            	}
            }else if (m1.getKey().equals(RoboterData.code_Continue) && m2.getKey().equals(RoboterData.code_ToPark))
            {           	
            	this.wait = true;
            }
        
        }else if (list.size() == 3) {
            this.manager.addOrder(list);
        } else {
            //Fehlerbehbung
        }
    }
}

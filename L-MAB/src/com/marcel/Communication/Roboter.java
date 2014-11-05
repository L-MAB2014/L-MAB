package com.marcel.Communication;


import lejos.nxt.*;
import lejos.util.Delay;

import java.util.ArrayList;
import java.util.List;

public class Roboter implements IRoboter {

    float tp = 300;
    float light;
    float high;
    float low;
    float offset;
    float error;
    float turn;
    float kp_init = (1.3f / 185);
    float kp = 0;
    private CheckpointList map;
    private Checkpoint park_Position;
    private Checkpoint position;
    
    private Checkpoint puffer_position;
    
    private ColorSensor colorSensor;
    private NXTRegulatedMotor rightMotor;
    private NXTRegulatedMotor leftMotor;
    private BTConnect bt;
    private OrderManagement manager;
    private boolean wait;
    private long last_checkpoint;
    private boolean IsEnding;

    private boolean IsParking;

    private boolean InitializePosition;
    
    private boolean licence_store;
    private boolean licence_exit;
    
    private boolean toPuffer;
    private boolean inPuffer;

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

    private void drive() {
        try {

            init();


            this.IsEnding = false;

            if (this.bt.Connection()) {
                this.InitializeStart();

                while (!this.IsEnding) {

                    LCD.drawString("Auftr�ge: " + this.manager.size() + "    ", 0, 4);

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

    void init() {

        LCD.drawString("HELL: ", 0, 0);

        Button.waitForAnyPress();

        high = colorSensor.getLightValue();

        LCD.drawString("Hell: " + high, 0, 0);

        Button.waitForAnyPress();
        LCD.clear();

        LCD.drawString("Dunkel:", 0, 0);

        Button.waitForAnyPress();

        low = colorSensor.getLightValue();

        LCD.drawString("Dunkel: " + low, 0, 0);

        Button.waitForAnyPress();

        LCD.clear();

        offset = (high + low) / 2;
        kp = kp_init * offset;
        LCD.drawString("Mitte: " + offset, 0, 0);

        LCD.drawString("Init Fertig", 0, 2);
        Button.waitForAnyPress();
        LCD.clear();
    }

    private void MakeOrder(Order order) {
        Checkpoint check_store = map.getCheckpoint("PL" + order.getStore_place());
        Checkpoint check_exit = map.getCheckpoint("PU" + order.getExit_place());

        boolean isLoaded = false;
        boolean left_curve = false;
        
        this.licence_store  = false;
        this.licence_exit = false;
        this.toPuffer = false;
        this.inPuffer = false;

        if (this.IsParking) {
            this.Unparking();
        }

        Checkpoint nextCheckWay = position.getNext_WayCheckpoint();
        Checkpoint nextCheckOther = position.getNext_OtherCheckpoint();

        boolean b = true;

        while (nextCheckOther == null || nextCheckOther != this.park_Position || b) {
            
		  if(this.IsOnPuffer() && !this.licence_exit){			  
			  this.PufferModus(nextCheckOther, check_exit);
			  
		  } else if (!isLoaded && nextCheckOther != null && nextCheckOther == check_store) {
                this.EntranceModus(nextCheckOther, nextCheckWay);
                isLoaded = true;
                left_curve = true;

                this.position = nextCheckWay;
                nextCheckWay = position.getNext_WayCheckpoint();
                nextCheckOther = position.getNext_OtherCheckpoint();

            } else if (isLoaded && nextCheckOther != null && nextCheckOther == check_exit) {
                this.EntranceModus(nextCheckOther, nextCheckWay);
                isLoaded = false;
                left_curve = true;

                this.position = nextCheckWay;
                nextCheckWay = position.getNext_WayCheckpoint();
                nextCheckOther = position.getNext_OtherCheckpoint();

            }

            this.CheckForContinue(new Message(RoboterData.code_Checkpoint, position.getName()),
                    new Message(RoboterData.code_NextCheckpoint, nextCheckWay.getName()));                       

            if (left_curve) {
                left_curve = false;
                this.links45();
            }

            this.fahreZu(this.position.getNext_WayCheckpoint().getColor());


            this.position = nextCheckWay;
            nextCheckWay = position.getNext_WayCheckpoint();
            nextCheckOther = position.getNext_OtherCheckpoint();

            if (b)
                b = false;

        }

        if (manager.size() == 0) {
            this.Parking();
        }


    }

    private void Parking() {
        LCD.drawString("PARKEN!! ", 0, 2);

        this.CheckForContinue(new Message(RoboterData.code_Checkpoint, position.getName()),
                new Message(RoboterData.code_NextCheckpoint, this.park_Position.getName()));

        this.links90();
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

    private void Unparking() {
        this.CheckForContinue(new Message(RoboterData.code_Checkpoint, position.getName()),
                new Message(RoboterData.code_NextCheckpoint, position.getNext_WayCheckpoint().getName()));

        this.fahreZu(this.position.getNext_WayCheckpoint().getColor());

        this.position = position.getNext_WayCheckpoint();

        this.links90();
        this.IsParking = false;
    }

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

    private void SetParkPosition(Message message) {
        Checkpoint check = map.getCheckpoint(message.getValue());
        LCD.drawString("PP: " + message.getValue(), 0, 2);
        if (check != null) {
            this.park_Position = check;
            this.position = check;

            this.InitializePosition = true;
        }
    }

    private void fahreZu(int farbe) {

        leftMotor.forward();
        rightMotor.forward();

        while (!this.IsEnding) {
            light = colorSensor.getLightValue();
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


            leftMotor.setSpeed(tp + turn);
            rightMotor.setSpeed(tp - turn);
        }

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

    }

    private void WaiteForOk() {
        this.wait = false;
        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        while (!this.wait) {
        }
    }

    private void EntranceModus(Checkpoint entrance, Checkpoint exit) {
        try {
            this.CheckForContinue(new Message(RoboterData.code_Checkpoint, position.getName()),
                    new Message(RoboterData.code_NextCheckpoint, entrance.getName()));

            this.links90();

            this.fahreZu(entrance.getColor());

            this.CheckForContinue(new Message(RoboterData.code_Checkpoint, entrance.getName()),
                    new Message(RoboterData.code_PostionLoad, entrance.getName()));

            this.position = entrance;

            this.leftMotor.stop(true);
            this.rightMotor.stop(true);

            Sound.playTone(500, 500, 100);
            Thread.sleep(5000);
            Sound.playTone(500, 500, 100);

            List<Message> message = new ArrayList<Message>();
            message.add(new Message(RoboterData.code_FinishLoad, this.position.getName()));
            message.add(new Message(RoboterData.code_NextCheckpoint, exit.getName()));

            this.rechts90();
            this.bt.SendPosition(Protokoll.MessageToString(message));
            this.WaiteForOk();

            this.fahreZu(exit.getColor());


        } catch (Exception e) {

        }
    }

    
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
    		
    		this.links90();
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

	        this.links90();   
	        this.toPuffer = false;
	        this.park_Position = null;
    	}
    	    	
    }
    
    void links90() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        rightMotor.rotate(450);
    }
    void rechts90() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        leftMotor.rotate(400);
    }

    void links45() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        rightMotor.rotate(220);
    }

    void turn180() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        leftMotor.backward();
        rightMotor.forward();

        Delay.msDelay(2000);


    }
    
    
    private boolean IsOnPuffer()
    {
    	String check = this.position.getName();
    	return (check.equals("C1") || check.equals("C2") || check.equals("C3"));
    }

    public void InputMessage(String message) {
        LCD.drawString("N: " + message, 0, 0);
        List<Message> list = Protokoll.StringToMessage(message);

        if (list.size() == 1) {

            if (list.get(0).getKey().equals(RoboterData.code_Continue)) {
                this.wait = true;

            } else if (list.get(0).getKey().equals(RoboterData.code_ParkPosition)) {
                this.SetParkPosition(list.get(0));            
            }
            
        } else if (list.size() == 2) {
        	Message m1 = list.get(0);
            Message m2 = list.get(1);

            if (m1.getKey().equals(RoboterData.code_Continue) && m2.getKey().equals(RoboterData.code_Reserved)) {
            	
            	 // Hat die Lizens f�r das Ausgangslager erhalten!
            	 this.licence_exit = true;
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
            }
        
        }else if (list.size() == 3) {
            this.manager.addOrder(list);
        } else {
            //Fehlerbehbung
        }
    }
}

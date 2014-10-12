package com.marcel.Communication;



import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.Color;

public class Roboter {
	
	private ColorSensor colorSensor;
	
	private UltrasonicSensor ultra ;
	
	private NXTRegulatedMotor rightMotor;
	private NXTRegulatedMotor leftMotor;
	
	private BTConnect bt;
	
	private ThreadColorSensor thread_color;
	
	private ThreadUltraSensor thread_ultra;
	
	private OrderManagement manager;
	
	
	private int tp = 150;		
	private int light;			
	private int high;		
	private int low;		
	private int offset;		
	private int turn;		
	private int kp;		
	private int error;
	
	private int counter;
	
	private long last_checkpoint;
	
	private boolean IsExit;
	
	private boolean IsEnding;
	
	
	
	Roboter()
	{
		
		this.IsEnding = false;
		this.IsExit = false;
		this.counter = 0;		
		this.last_checkpoint = 0;
		
		this.manager = new OrderManagement();
		
		this.bt = new BTConnect(this.manager);
		
		this.colorSensor = new ColorSensor(SensorPort.S1);
		this.ultra = new UltrasonicSensor(SensorPort.S2);
		
		this.rightMotor = new NXTRegulatedMotor(MotorPort.C);
		this.leftMotor = new NXTRegulatedMotor(MotorPort.A);	
		
		this.rightMotor.setAcceleration(3000);
		this.leftMotor.setAcceleration(3000);	        
   
		this.rightMotor.setSpeed(tp);
		this.leftMotor.setSpeed(tp);			
		
		this.thread_color = new ThreadColorSensor(this.colorSensor);
		this.thread_ultra = new ThreadUltraSensor(this.ultra);	
		
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
	
	private void drive()
	{
		try{
			this.thread_color.StartThreadSensor();
			this.thread_ultra.StartThreadSensor();
			
			init();
			
			
			this.IsEnding = false;
			
			if(this.bt.Connection())
			{
				while(!this.IsEnding)
				{		

					LCD.drawString("Aufträge: "+ this.manager.size()+"    ", 0, 4);
					LCD.drawString("Abstand: "+ this.thread_ultra.GetAverageDistance()+"    ", 0, 5);
					LCD.drawString("Farbe: "+ this.thread_color.ColorString()+"    ", 0, 6);
					LCD.drawString("Licht: "+ this.thread_color.getLightID()+"    ", 0, 7);
					
					if(this.manager.size() > 0)
					{
						Order order = this.manager.GiveFirstOrder();
						LCD.clear();
						
						LCD.drawString("AuftragNr.: "+ order.getId(), 0, 4);
						LCD.drawString("Lager: "+  order.getStore_place(), 0, 5);
						LCD.drawString("Ausgang: "+ order.getExit_place(), 0, 6);
						LCD.drawString("Modus:   Start ", 0, 7);
												
						this.MakeOrder(order);
						
						this.leftMotor.stop(true);
						this.rightMotor.stop(true);	
						
						LCD.clear();
						
					}
				}
				this.bt.Close();
			}
		}catch(Exception e)
		{
			
		}finally
		{
			this.thread_color.StopThreadSensor();
			this.thread_ultra.StopThreadSensor();
		}

	}
	
	
	void init(){
		
		 
		LCD.drawString("HELL: ", 0, 0);
	
		Button.waitForAnyPress();
			 
		high = this.thread_color.getLightID();
	 
        LCD.drawString("Hell: "+ high, 0, 0);			
     
        Button.waitForAnyPress();    
        LCD.clear();    
   
        LCD.drawString("Dunkel:", 0, 0);
     
        Button.waitForAnyPress();
         
        low = this.thread_color.getLightID();		
     
        LCD.drawString("Dunkel: "+ low, 0, 0);
    
        Button.waitForAnyPress();
     
        LCD.clear();
         
        offset = (high + low)/2;       
       
        LCD.drawString("Mitte: "+ offset, 0, 0);
      
        kp = tp / (high-offset);        
        if(kp<1)
        	kp=1;
                
        LCD.drawString("KP: "+ kp, 0, 1);
        LCD.drawString("Init Fertig", 0, 2); 
        Button.waitForAnyPress();
   	    LCD.clear();   	    		
	}
	
	private void MakeOrder(Order order)
	{
		LCD.drawString("Modus:   FAHRT ", 0, 7);
		int target_store = 1;
		int target_exit  = 1;
		
		if(order.getStore_place() == 2)
			target_store=3;
		if(order.getStore_place() == 3)
			target_store=5;		
		if(order.getExit_place() == 2)
			target_exit=3;
		
		for(int i = 0 ; i < target_store; ++i)
		{
			this.fahreZu(Color.RED);
			++this.counter; 			
  			Sound.playTone(500,500,100); 
  			//LCD.drawString("Checkpoint :"+counter, 0, 7);
  			this.bt.SendPosition("Checkpoint "+ counter + " erreicht");
		}
		
		this.bt.SendPosition("Lagereingang erreicht");
		LCD.drawString("Checkpoint:  EINGANG", 0, 7);
		this.EntranceModus(true);
		
		
		LCD.drawString("GELB", 0, 7);
		this.fahreZu(Color.YELLOW);
		Sound.playTone(500,500,100); 
		this.bt.SendPosition("Gelber Checkpoint erreicht");
		//UMSTELLUNG
		
		for(int i = 0 ; i < target_exit; ++i)
		{
			this.fahreZu(Color.RED);	
			++this.counter; 			
  			Sound.playTone(500,500,100); 
  			//LCD.drawString("Checkpoint :"+counter, 0, 7);
  			this.bt.SendPosition("Checkpoint "+ counter + " erreicht");
		}
		
		this.EntranceModus(false);
		
		this.fahreZu(Color.YELLOW);
		this.bt.SendPosition("Gelber Checkpoint erreicht");		
		this.fahreZu(Color.BLACK);
		counter =0;
		
	}
	
	
	
	private void fahreZu(int t){
		
		leftMotor.forward();
		rightMotor.forward();		
	
		int farbe = this.thread_color.getColorID();
		
		while(!this.IsEnding)
		{			
		  	if((System.currentTimeMillis()-this.last_checkpoint )>2000)			
		  	{	if((farbe=this.thread_color.getColorID()) == t)
				{
		  			this.last_checkpoint = System.currentTimeMillis();	
			  		return;
				}
		  	}
			
			light = this.thread_color.getLightID();    
		
        	if(light>high)
        		light=high;
        	if(light<low)
        		light=low;
         	
        	error=light-offset;
        
        	turn = kp * error;
         	
            if(turn>100)
            	turn=100;
            if(turn<-100)
            	turn=-100;           
        
            leftMotor.setSpeed(tp + turn);
            rightMotor.setSpeed(tp - turn);
            
                	
		}	 					
	}
	
	
	private void EntranceModus(boolean modus)
	{
		try{
			this.leftMotor.stop(true);
			this.rightMotor.stop(true);	
			this.rightMotor.rotate(400);
			this.fahreZu(Color.RED);
			this.bt.SendPosition("Lager erreicht");
			this.leftMotor.stop(true);
			this.rightMotor.stop(true);	
			this.bt.SendPosition("Aufladen/Abladen");
			LCD.drawString("STOP LAGER", 0, 7);
			Sound.playTone(500,500,100); 
			Thread.sleep(5000);
			Sound.playTone(500,500,100); 
			this.bt.SendPosition("Aufladen/Abladen beendet");
			LCD.drawString("WEITER LAGER", 0, 7);
     		leftMotor.rotate(400);
			this.fahreZu(Color.RED);
			Sound.playTone(500,500,100); 
			this.bt.SendPosition("Lagerausgang erreicht");
			LCD.drawString("LAGER AUSGANG", 0, 7);
			rightMotor.rotate(270);	
			
		}catch(Exception e)
		{
			
		}
	}
	
	
	public static void main (String[] args) {
		Roboter r = new Roboter();
	}
}

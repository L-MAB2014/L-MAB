package com.marcel.Communication;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.robotics.Color;


/**
 * @author Marcel Reich
 *
 */
public class ThreadColorSensor extends Thread {
	
	
	/**
	 * 
	 */
	private ColorSensor colorSensor;
	
	
	/**
	 * 
	 */
	private int colorID;
	
	/**
	 * 
	 */
	private int lightID;
	
	
	/**
	 * 
	 */
	private boolean threadRun;
	
	
	/**
	 * 
	 */
	private boolean threadEnds;
	
	
	
	/**
	 * @param cs
	 */
	ThreadColorSensor (ColorSensor cs)
	{
		this.colorSensor = cs;
		this.colorID = -99;
		this.lightID =0;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run()
	  {	 
		
		try {
				this.threadRun = true;
				this.threadEnds = false;
				
				while( !this.threadEnds )
			    {
	    			this.colorID = this.colorSensor.getColorID();
	    			this.lightID = this.colorSensor.getLightValue();
					this.sleep(20);
		    	}
				
				LCD.drawString("ENDE", 0, 3); 
				
		 } catch (Exception e) {
				
				
		 }
		
		this.threadRun = false;	
		
	  }
	
	
	/**
	 * 
	 */
	public void StartThreadSensor()
	{	   		
		this.start();					
	}
	
	/**
	 * 
	 */
	public void StopThreadSensor()
	{	   		
		this.threadEnds = true;				
	}
	
	
	/**
	 * @return
	 */
	public boolean IsRunThreadSensor()
	{
		return this.threadRun;
	}
    
	
	/**
	 * @return
	 */
	public int getColorID()
	{
		return this.colorID;
	}
	
	/**
	 * @return
	 */
	public int getLightID()
	{
		return this.lightID;
	}
	
	/**
	 * @return
	 */
	public String ColorString()
	{
		if(this.colorID != -99)	
		{
			switch(this.colorID)		
			{
				case Color.BLACK :  return "Schwarz"; 
				case Color.GREEN :  return "Gruen";  
				case Color.WHITE :  return "Weiss"; 
				case Color.PINK :   return "Pink"; 
				case Color.RED :   return  "Rot"; 
				case Color.GRAY :  return "Grau"; 
				case Color.CYAN :  return "Cyab"; 
				case Color.ORANGE :  return "Orange";  
				case Color.YELLOW :  return "Gelb";
				case Color.NONE :  return  "Keine "; 
				default : return "Fehler"; 
			}
		}else
		{
			return "Noch nicht definiert";
		}
	}

}

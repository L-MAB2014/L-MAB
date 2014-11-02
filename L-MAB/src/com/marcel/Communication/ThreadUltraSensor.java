package com.marcel.Communication;
import java.util.ArrayList;
import java.util.List;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.UltrasonicSensor;




/**
 * @author Marcel
 *
 */
public class ThreadUltraSensor extends Thread {
	
	
	/**
	 * 
	 */
	private UltrasonicSensor ultraSensor;
	
	
	private List<Integer> measurements;
	
	private final int numberOfMeasurements = 10;
	private final int mindistance = 10;
	private int distanceAverage;
	private  double correction ; 
	
	
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
	ThreadUltraSensor (UltrasonicSensor us)
	{
		this.ultraSensor = us;
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run()
	  {	 
		
		try {
				this.threadRun = true;
				this.threadEnds = false;
				this.measurements = new ArrayList <Integer>();	
				this.correction = 1.0;
				
				while( !this.threadEnds )
			    {
	    			
					this.AnalysisDistance( this.ultraSensor.getDistance());
				//	this.sleep(20);
		    	}
				
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
	public int GetAverageDistance()
	{
		return this.distanceAverage;
	}
	
	
	/**
	 * @return
	 */
	public double GetCoorection()
	{
		return this.correction;
	}
	
	/**
	 * @return
	 */
	private int CountAverage()
	{
		int sum = 0;
		
		int max;
		int min;
		
		if( this.measurements.get(0) <= this.measurements.get(1))
		{
			max = this.measurements.get(1);
			min = this.measurements.get(0);
		}else
		{
			max = this.measurements.get(0);
			min = this.measurements.get(1);
		}
								
		for(int i = 2; i < this.measurements.size() ;++i)
		{
			if( this.measurements.get(i) > max)
			{
				sum += max;
				max = this.measurements.get(i);
				
			}else if ( this.measurements.get(i) < min)
			{
				sum += min;
				min = this.measurements.get(i);
			}else
			{
				sum += this.measurements.get(i);
			}
		}
		
		return (sum / ( this.numberOfMeasurements - 2));		
	}
	
	
	/**
	 * @param newDistance
	 * @return
	 */
	private void AnalysisDistance(int newDistance)
	{
		
		this.measurements.add(newDistance);
		
		if(this.measurements.size() > this.numberOfMeasurements)
		{
			this.measurements.remove(0);
		}
		
		if(this.measurements.size() >= 5)
		{
			this.distanceAverage = this.CountAverage();
			
			if(this.distanceAverage < 20 && this.distanceAverage >= 10)
			{
				this.correction = ((double)(this.distanceAverage - 10))/10;
			}else
			{
				this.correction = 1.0;
			}			
				
		}
	
	}
}

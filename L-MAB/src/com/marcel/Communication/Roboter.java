package com.marcel.Communication;



import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Roboter {
	
	private ColorSensor colorSensor;
	
	private UltrasonicSensor ultra ;
	
	private NXTRegulatedMotor rightMotor;
	private NXTRegulatedMotor leftMotor;
	
	private BTConnect bt;
	
	private ThreadColorSensor thread_color;
	
	private ThreadUltraSensor thread_ultra;
	
	private OrderManagement manager;
	
	Roboter()
	{
		this.manager = new OrderManagement();
		
		this.bt = new BTConnect(this.manager);
		
		this.colorSensor = new ColorSensor(SensorPort.S1);
		this.ultra = new UltrasonicSensor(SensorPort.S2);
		
		this.rightMotor = Motor.A;
		this.leftMotor = Motor.C;
		
		this.thread_color = new ThreadColorSensor(this.colorSensor);
		this.thread_ultra = new ThreadUltraSensor(this.ultra);						
		
		this.drive();
	}
	
	private void drive()
	{
		try{
			this.thread_color.StartThreadSensor();
			this.thread_ultra.StartThreadSensor();
			
			if(this.bt.Connection())
			{
				while(!Button.ESCAPE.isDown())
				{		
					LCD.drawString("Farbe: "+ this.thread_color.ColorString()+"    ", 0, 6);
					LCD.drawString("Abstand: "+ this.thread_ultra.GetAverageDistance()+"    ", 0, 5);
					LCD.drawString("Aufträge: "+ this.manager.size()+"    ", 0, 4);
					
					if(this.manager.size() > 0)
					{
						this.leftMotor.setSpeed((int)(100* thread_ultra.GetCoorection()));
						this.leftMotor.forward();
						this.rightMotor.setSpeed((int)(100* thread_ultra.GetCoorection()));
						this.rightMotor.forward();
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
	
	
	public static void main (String[] args) {
		Roboter r = new Roboter();
	}
}

package com.marcel.Communication;

import lejos.nxt.Button;

public class Roboter {
	
	Roboter()
	{
		this.drive();
	}
	
	private void drive()
	{
		BTConnect bt = new BTConnect();
		if(bt.Connection())
		{
			while(!Button.ESCAPE.isDown())
			{				
			}
			bt.Close();
		}
	}
	
	
	public static void main (String[] args) {
		Roboter r = new Roboter();
	}
}

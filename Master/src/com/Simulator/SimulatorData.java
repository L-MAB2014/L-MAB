package com.Simulator;

public class SimulatorData {
	
	public final static char key_PU1 = 'X';
	public final static char key_PU2 = 'Y';
	public final static char key_Timeout = 'T';
	public final static int number_packages = 10;
	
	public static String KeyToStock(char key)
	{
		return (key==key_PU1  ? "PU1": "PU2");
	}
}

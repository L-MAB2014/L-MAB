package com.Simulator;

/**
 * @author Marcel Reich
 *
 */
public class SimulatorData {
	
	/**
	 * Schl�ssel f�r Auftr�ge f�r den ersten Ausgang
	 */
	public final static char key_PU1 = 'X';
	
	/**
	 * Schl�ssel f�r Auftr�ge f�r den zweiten Ausgang
	 */
	public final static char key_PU2 = 'Y';
	
	/**
	 * Schl�ssel f�r Tiemouts
	 */
	public final static char key_Timeout = 'T';
	
	/**
	 * Wie viele Paket soller erzeugt werden
	 */
	public final static int number_packages = 10;
	
	/**
	 * Schl�ssel zu Ausgnag zuordnung
	 * @param key Schl�ssel
	 * @return Ausgang
	 */
	public static String KeyToStock(char key)
	{
		return (key==key_PU1  ? "PU1": "PU2");
	}
}

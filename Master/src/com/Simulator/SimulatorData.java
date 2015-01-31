package com.Simulator;

/**
 * @author Marcel Reich
 *
 */
public class SimulatorData {
	
	/**
	 * Schlüssel für Aufträge für den ersten Ausgang
	 */
	public final static char key_PU1 = 'X';
	
	/**
	 * Schlüssel für Aufträge für den zweiten Ausgang
	 */
	public final static char key_PU2 = 'Y';
	
	/**
	 * Schlüssel für Tiemouts
	 */
	public final static char key_Timeout = 'T';
	
	/**
	 * Wie viele Paket soller erzeugt werden
	 */
	public final static int number_packages = 10;
	
	/**
	 * Schlüssel zu Ausgnag zuordnung
	 * @param key Schlüssel
	 * @return Ausgang
	 */
	public static String KeyToStock(char key)
	{
		return (key==key_PU1  ? "PU1": "PU2");
	}
}

package com.Simulator;

/**
 * @author Marcel Reich
 * Eine Order ensteht aus einem StockObjekt (key1 = X , key2 = 1 , Order = X-1)  
 */
public class StockObjekt {
	

	/**
	 * Schlüssel
	 */
	private char key1;
	
	/**
	 * Wert
	 */
	private int key2;
	
	/**
	 * Konstruktor
	 * @param k1 Schlüssel
	 * @param k2 ID
	 */
	StockObjekt(char k1, int k2)
	{
		this.key1=k1;
		this.key2 = k2;
	}
	
	public char getKey1() {
		return key1;
	}

	public int getKey2() {
		return key2;
	}
	
	public String getObjekt() {
		return key1 + "-" + key2 ;
	}
	
	@Override
	public String toString() {
		return key1 + "-" + key2 ;
	}

}

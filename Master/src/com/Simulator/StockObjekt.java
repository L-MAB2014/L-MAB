package com.Simulator;

public class StockObjekt {
	

	private char key1;
	
	private int key2;
	
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

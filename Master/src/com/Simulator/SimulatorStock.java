package com.Simulator;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jan Bein und Marcel Reich
 * Spiegelt einen Eingang wieder zu wem die Order zugetelt weden
 */
public class SimulatorStock {
	
	/**
	 * Liste mit den Aufträgen
	 */
	private List<StockObjekt> objekts;
	
	/**
	 * name des Eingangs
	 */
	private String name;
	
	/**
	 * Konstruktor
	 * @param name Name des Iengangs
	 */
	public SimulatorStock(String name) {
		
		this.name = name;
		this.objekts = new ArrayList<StockObjekt>();
	}		

	public String toString() {
		String ret = "";
		for(int i = 0; i < objekts.size() ; ++i)
			ret = ret + objekts.get(i).toString() + " ";
		return ret;
	}
	
	public void addObjekt(StockObjekt obj) {
		objekts.add(obj);
	}
	
	public StockObjekt getNextObjekt()
	{
		if(objekts.size() != 0)
			return objekts.remove(0);
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public void print() {
		for(int i = 0; i < objekts.size() ; ++i)
			System.out.print(objekts.get(i).toString()+ "\t");
	}
	
	public void clear() {
		objekts.clear();
	}
}

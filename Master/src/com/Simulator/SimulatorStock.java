package com.Simulator;

import java.util.ArrayList;
import java.util.List;


public class SimulatorStock {
	
	private List<StockObjekt> objekts;
	
	private String name;
	
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
	
//	public void addFront(String key1, int key2) {
//		objekts.add(new StockObjekt(key1,key2));
//	}
//
//	public void addBack(String key1, int key2) {
//		
//		objekts.add(0, new StockObjekt(key1,key2));
//	}
	
	public void print() {
		for(int i = 0; i < objekts.size() ; ++i)
			System.out.print(objekts.get(i).toString()+ "\t");
	}
	
	public void clear() {
		objekts.clear();
	}
}

package com.Master;

import java.util.ArrayList;
import java.util.List;


public class Stock {
	
	private List<Order> objekts;
	
	private Checkpoint checkpoint;
	
	public Stock(Checkpoint checkpoint) {
		
		this.checkpoint = checkpoint;
		this.objekts = new ArrayList<Order>();
	}		

	
	public void addOrder(Order order) {
		objekts.add(order);
	}
	
	public boolean removeOrder(Order order)
	{
		return objekts.remove(order);
	}
	
	public Order getFirstObjekt()
	{
		if(objekts.size() != 0)
			return objekts.remove(0);
		return null;
	}
	
	public String getName() {
		return this.checkpoint.getName();
	}
	
	
	public void clear() {
		objekts.clear();
	}
}


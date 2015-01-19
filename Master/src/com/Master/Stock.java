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
		
	public String getName() {
		return this.checkpoint.getName();
	}
	
	public String getNextDelivery()
	{
		return this.objekts.get(0).getId();
	}
	
	public Order GiveFirstOrder()
	{
		return objekts.size() != 0 ?this.objekts.get(0): null;
	}
	
	public Order GiveOrderByPosition(int pos )
	{
		return objekts.size() > pos ? this.objekts.get(pos): null;
	}
	
	public int SumObjekts()
	{
		return this.objekts.size();
	}
	
	public Order getNextFreeOrder()
	{
		for(int i =0; i < objekts.size(); ++i)
		{
			Order o = objekts.get(i);
			if(!o.HaveBot())
				return o;
		}
		return null;
	}
	
	public void clear() {
		objekts.clear();
	}
}


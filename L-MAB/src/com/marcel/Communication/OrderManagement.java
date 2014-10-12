package com.marcel.Communication;

import java.util.ArrayList;
import java.util.List;



public class OrderManagement extends ArrayList<Order> {
	
	
	public void addOrder(List<Message> message)
	{
		int id = Integer.parseInt(message.get(0).getValue());
		int store  = Integer.parseInt(message.get(1).getValue());
		int exit  = Integer.parseInt(message.get(2).getValue());
		this.add(new Order(id, store, exit));
	}
	
	
	public Order GiveFirstOrder()
	{
		return this.remove(0);
	}

}

package com.marcel.TestMaster;

import java.util.ArrayList;
import java.util.List;



public class OrderManagement extends ArrayList<Order> {
	
	
	public void addOrder(int id, int store , int exit)
	{
		this.add(new Order(id, store, exit));
	}
	
	
//	public Order GiveFirstOrder()
//	{
//		return this.remove(0);
//	}

}

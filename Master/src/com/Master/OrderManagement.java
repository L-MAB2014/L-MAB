package com.Master;

import java.util.ArrayList;

public class OrderManagement extends ArrayList<Order> {

    /**
     * Legt ein neues Order/Objekt an und speichert es
     *
     * @param id    ID der Order
     * @param store Lager der Order
     * @param exit  Ziel der Order
     */
    public void addOrder(String id, String store, String exit) {
        this.add(new Order(id, store, exit));
    }
    
    public Order getById(String id)
    {
    	for (int i = 0 ; i< this.size(); i++)
    	{
    		Order order = this.get(i);
    		
    		if(order.getId().equals(id))
    		{
    			return order;
    		}
    				
    	}
    	
    	return null;
    }

//	public Order GiveFirstOrder() //TODO ???
//	{
//		return this.remove(0);
//	}
}

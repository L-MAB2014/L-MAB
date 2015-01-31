package com.Master;

import java.util.ArrayList;

/**
 * @author Marcel Reich
 * Liste zum Speichern von Order
 */
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
    
    /**
     * Übergibt anhand einer ID die Passender Order
     * @param id
     * ID der zu sucheneden Oder
     * @return
     * Order passen zu der ID
     */
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

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
    public void addOrder(int id, int store, int exit) {
        this.add(new Order(id, store, exit));
    }

//	public Order GiveFirstOrder() //TODO ???
//	{
//		return this.remove(0);
//	}
}

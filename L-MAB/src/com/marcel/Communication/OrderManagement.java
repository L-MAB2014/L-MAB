package com.marcel.Communication;

import java.util.ArrayList;
import java.util.List;


public class OrderManagement extends ArrayList<Order> {


    public void addOrder(List<Message> message) {
        String id = message.get(0).getValue();
        String  store = message.get(1).getValue();
        String  exit = message.get(2).getValue();
        this.add(new Order(id, store, exit));
    }


    public Order GiveFirstOrder() {
        return this.remove(0);
    }

}

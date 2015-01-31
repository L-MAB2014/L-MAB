package com.marcel.Communication;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Marcel Reich
 * Verwaltet die Aufträge
 *
 */
public class OrderManagement extends ArrayList<Order> {


    /**
     * Nimmte eine neue Order auf
     * @param message Message mit den Informationen für eine neue Order
     */
    public void addOrder(List<Message> message) {
        String id = message.get(0).getValue();
        String  store = message.get(1).getValue();
        String  exit = message.get(2).getValue();
        this.add(new Order(id, store, exit));
    }


    /**
     * Gibt die Nächste order zurück und löscht sie aus der Liste
     * @return Order
     */
    public Order GiveFirstOrder() {
        return this.remove(0);
    }

}

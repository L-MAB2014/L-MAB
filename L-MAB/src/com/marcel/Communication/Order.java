package com.marcel.Communication;

public class Order {

    private int id;

    private int store_place;

    private int exit_place;

    Order(int id, int store, int exit) {
        this.id = id;
        this.exit_place = exit;
        this.store_place = store;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_place() {
        return store_place;
    }

    public void setStore_place(int store_place) {
        this.store_place = store_place;
    }

    public int getExit_place() {
        return exit_place;
    }

    public void setExit_place(int exit_place) {
        this.exit_place = exit_place;
    }

}

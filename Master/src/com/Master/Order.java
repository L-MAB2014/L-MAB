package com.Master;

/**
 *
 *
 */
public class Order {

    /**
     * ID der Order
     */
    private String id;

    /**
     * Lager wo die Lieferung abgeholt werden soll
     */
    private String store_place;

    /**
     * Ziel wo die Lieferung abgeliefert werden soll
     */
    private String exit_place;

    Order(String id, String store, String exit) {
        this.id = id;
        this.exit_place = exit;
        this.store_place = store;
    }

    /**
     * Gibt die ID der Order zurück
     *
     * @return ID der Order
     */
    public String getId() {
        return id;
    }

    /**
     * Setzt die ID der Order
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gibt das Lager der Order zurück
     *
     * @return Lager
     */
    public String getStore_place() {
        return store_place;
    }

    /**
     * Setzt das Lager der Order
     *
     * @param store_place
     */
    public void setStore_place(String store_place) {
        this.store_place = store_place;
    }

    /**
     * Gibt das Ziel der Order zurück
     *
     * @return Ziel
     */
    public String getExit_place() {
        return exit_place;
    }

    /**
     * Setzt das Ziel der Order
     *
     * @param exit_place
     */
    public void setExit_place(String exit_place) {
        this.exit_place = exit_place;
    }

}

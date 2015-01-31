package com.Master;


/**
 * @author Marcel Reich
 * Order oder Paket
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
    
    /**
     * Bot welcher die Order tranportiert
     */
    private Bot bot;

    /**
     * Kontsruktor des Order-Objektes
     * @param id ID der Order
     * @param store	Eingang der Order
     * @param exit Ausgang der Order
     */
    Order(String id, String store, String exit) {
    	this.id = id;
        this.exit_place = exit;
        this.store_place = store;
        this.bot = null;
    }
    
    /**
     * Überprüft ob die Order einem Bot zugeteilt ist oder nicht
     * @return
     * Resulat od bide Order zugeteilt ist oder nicht
     */
    public boolean HaveBot()
    {
    	return (bot != null);
    }
    
    /**
     * Weißt der Order einen Bot zu
     * @param bot Zuzuweisender Bot
     */
    public void SetBot(Bot bot)
    {
    	this.bot = bot;
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
     * @param id ID der Order
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gibt das Lager der Order zurück
     *
     * @return Lager Lager der Order
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
     * @return Ziel Ziel/Ausgang der Order
     */
    public String getExit_place() {
        return exit_place;
    }

    /**
     * Setzt das Ziel der Order
     *
     * @param exit_place  Zusetzendes Ziel/Ausgang der Order
     */
    public void setExit_place(String exit_place) {
        this.exit_place = exit_place;
    }

}

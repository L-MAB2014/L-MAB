package com.Master;

/**
 * 
 *
 */
public class Order {
	
	/**
	 * ID der Order
	 */
	private int id;
	
	/**
	 * Lager wo die Lieferung abgeholt werden soll
	 */
	private int store_place;
	
	/**
	 * Ziel wo die Lieferung abgeliefert werden soll
	 */
	private int exit_place;
	
	Order(int id, int store, int exit)
	{
		this.id = id;
		this.exit_place = exit;
		this.store_place = store;
	}
	
	/**
	 * Gibt die ID der Order zurück
	 * @return ID der ORder
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setzt die ID der Order
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gibt das Lager der Order zurück
	 * @return Lager
	 */
	public int getStore_place() {
		return store_place;
	}

	/**
	 * Setzt das Lager der Order
	 * @param store_place
	 */
	public void setStore_place(int store_place) {
		this.store_place = store_place;
	}

	/**
	 * Gibt das Ziel der  Order zurück
	 * @return Ziel
	 */
	public int getExit_place() {
		return exit_place;
	}

	/**
	 * Setzt das Ziel der Order
	 * @param exit_place
	 */
	public void setExit_place(int exit_place) {
		this.exit_place = exit_place;
	}

}

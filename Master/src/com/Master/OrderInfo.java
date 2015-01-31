package com.Master;

/**
 * @author Marcel Reich
 *
 * Informationen zu einer Order, für das Ausgangslager 
 */
public class OrderInfo {
	
	/**
	 * ID der Order
	 */
	private String id;
	
	/**
	 * Beinhaltet den Wert, ob die Order aus dem Lager abgeholt wurde
	 */
	private boolean onWay;
	
	/**
	 * Konsturktor
	 * @param id
	 * ID der ORder
	 */
	public OrderInfo(String id)
	{
		this.id = id;
		this.onWay = false;
	}

	/**
	 * Übergibt die ID der Order/paket
	 * @return
	 * ID der Order
	 */
	public String getId() {
		return id;
	}

	/**
	 * Übergibt die Information ob die Order aus dem Lager abgeholt wurde oder nicht
	 * @return
	 * Abgeholt oder nicht
	 */
	public boolean isOnWay() {
		return onWay;
	}

	/**
	 * Setzt die Information ob die Order aus dem Lager abgeholt wurde oder nicht
	 * @param onWay
	 * Paket abgeholt oder nicht
	 */
	public void setOnWay(boolean onWay) {
		this.onWay = onWay;
	}
	

	
	
}

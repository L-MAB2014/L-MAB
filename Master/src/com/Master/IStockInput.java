package com.Master;

/**
 * @author Marcel Reich
 *Interface zum einfügen der Aufträge
 */
public interface IStockInput {

	/**
	 * Fügt neue Aufträge (Order) ein
	 * @param id
	 * ID der Order
	 * @param stock
	 * Eingang der Order
	 * @param target
	 * Ausgang der Oder
	 */
	abstract public void ObjektToStock(String id, String stock, String target);
}

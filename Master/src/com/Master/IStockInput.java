package com.Master;

/**
 * @author Marcel Reich
 *Interface zum einf�gen der Auftr�ge
 */
public interface IStockInput {

	/**
	 * F�gt neue Auftr�ge (Order) ein
	 * @param id
	 * ID der Order
	 * @param stock
	 * Eingang der Order
	 * @param target
	 * Ausgang der Oder
	 */
	abstract public void ObjektToStock(String id, String stock, String target);
}

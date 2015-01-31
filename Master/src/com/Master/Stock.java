package com.Master;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Marcel Reich
 * Stock ist ein Eingang von dem Waren abgeholt werden können
 */
public class Stock {
	
	/**
	 * Liste der abzuholenden Waren/Paketen
	 */
	private List<Order> objekts;
	
	/**
	 * Checkpunkt des Einganges
	 */
	private Checkpoint checkpoint;
	
	public Stock(Checkpoint checkpoint) {
		
		this.checkpoint = checkpoint;
		this.objekts = new ArrayList<Order>();
	}		

	
	/**
	 * @param order
	 */
	public void addOrder(Order order) {
		objekts.add(order);
	}
	
	/**
	 * Löschen/Entfernen einer Order
	 * @param order
	 * Zu löschende Order
	 * @return
	 * Resulatat der Löschung
	 */
	public boolean removeOrder(Order order)
	{
		return objekts.remove(order);
	}
		
	/**
	 * Übergibt den Name des Eingangs
	 * @return
	 * name des Einganges
	 */
	public String getName() {
		return this.checkpoint.getName();
	}
	
	/**
	 * Übergibt die ID des zu nächst abzuholende Paketes
	 * @return
	 * ID-des Paktes welches als nächstes abgeholt werden muss
	 */
	public String getNextDelivery()
	{
		return this.objekts.get(0).getId();
	}
	
	/**
	 * Übergibt die nächst mögliche  Order
	 * @return
	 * Nächst mögliche Order
	 */
	public Order GiveFirstOrder()
	{
		return objekts.size() != 0 ?this.objekts.get(0): null;
	}
	
	/**
	 * Übergibt eine Order welche sich an einer bestimmten Position befidnent 
	 * @param pos
	 * Position der Order
	 * @return
	 * Order welche sich an der angebenen Position befindet
	 */
	public Order GiveOrderByPosition(int pos )
	{
		return objekts.size() > pos ? this.objekts.get(pos): null;
	}
	
	/**
	 * Übergibt die Anzhal der Pakte des einganges
	 * @return
	 * Anzhal der Pakte im Eingang
	 */
	public int SumObjekts()
	{
		return this.objekts.size();
	}
	
	/**
	 * Gibt das erste Paket in der Rheinfolge an, welches keinen Bot zugeordnert wurde 
	 * @return
	 * Erste Order ohne zuordnung zu einem Bot
	 */
	public Order getNextFreeOrder()
	{
		for(int i =0; i < objekts.size(); ++i)
		{
			Order o = objekts.get(i);
			if(!o.HaveBot())
				return o;
		}
		return null;
	}
	
	/**
	 * Entfernt alle Pakte des Eingangs
	 */
	public void clear() {
		objekts.clear();
	}
}


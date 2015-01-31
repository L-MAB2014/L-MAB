package com.Master;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Marcel Reich
 * Stock ist ein Eingang von dem Waren abgeholt werden k�nnen
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
	 * L�schen/Entfernen einer Order
	 * @param order
	 * Zu l�schende Order
	 * @return
	 * Resulatat der L�schung
	 */
	public boolean removeOrder(Order order)
	{
		return objekts.remove(order);
	}
		
	/**
	 * �bergibt den Name des Eingangs
	 * @return
	 * name des Einganges
	 */
	public String getName() {
		return this.checkpoint.getName();
	}
	
	/**
	 * �bergibt die ID des zu n�chst abzuholende Paketes
	 * @return
	 * ID-des Paktes welches als n�chstes abgeholt werden muss
	 */
	public String getNextDelivery()
	{
		return this.objekts.get(0).getId();
	}
	
	/**
	 * �bergibt die n�chst m�gliche  Order
	 * @return
	 * N�chst m�gliche Order
	 */
	public Order GiveFirstOrder()
	{
		return objekts.size() != 0 ?this.objekts.get(0): null;
	}
	
	/**
	 * �bergibt eine Order welche sich an einer bestimmten Position befidnent 
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
	 * �bergibt die Anzhal der Pakte des einganges
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


package com.Master;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;

/**
 * @author Marcel REich
 * Ausgang/Ziel der Order
 *
 */
public class Exit {

	/**
	 * logger
	 */
	private static org.apache.log4j.Logger logger = LogManager.getLogger("Checkpoint");
	
	/**
	 * Liste mit den Abgelieferten Ordern
	 */
	private List<Order> objekts;
	
	/**
	 * Checkpunkt des Augsnags
	 */
	private Checkpoint checkpoint;
	
	/**
	 * LIste mit den zulieferden Ordern
	 */
	private List<OrderInfo> orderList;
	
	/**
	 * Konstruktor
	 * @param checkpoint
	 * Checkpunkt des Ausganges
	 */
	public Exit(Checkpoint checkpoint) {
		
		this.checkpoint = checkpoint;
		this.objekts = new ArrayList<Order>();
	}		

	/**
	 * Nimmt die Liste den zu liefernden Ordern auf 
	 * @param orders
	 * Liste der zu liefernden Ordern
	 */
	public void setOrderList(List<OrderInfo> orders)
	{		
		this.orderList = orders;
		
		logger.info("Zugewiesende Liste mit "+orderList.size()+" Aufträgen  für den Ausgang "+this.checkpoint.getName() );
	}
	
	/**
	 * Überprüft ob die übergebene Order-ID ist, welche als nächstes geliefert werden uss
	 * @param id Zu überprüfende ID
	 * @return Resultat ob die ID als nächstes geliefert werden muss oder nicht
	 */
	public boolean onFirstPosition(String id)
	{
		return id.equals(this.orderList.get(0).getId());
	}
	
	
	/**
	 * Nimmt eine Order auf (wenn sie abgeladen wurde im Ausgang)
	 * @param order Einzufügene Order
	 */
	public void addOrder(Order order) {
		
		this.orderList.remove(0);
		this.objekts.add(order);
	}	
	
	/**
	 * Enfernt die Order welche als nächstes bnötigt wird/wurde
	 * @return
	 * Enterfete Order ID
	 */
	public String DeleteFirstFromOrderList()
	{
		OrderInfo order = this.orderList.remove(0);
		logger.info("Benötigte Lieferung "+order.getId()+" von "+this.checkpoint.getName()+" gelöscht" );
		return order.getId();
	}
	
	/** Übergibt die ID der als nächst benötigten Order
	 * @return
	 * ID der Order, welche als nächstens benötigt wird
	 */
	public String getNextDelivery()
	{
		logger.info("Zugewiesende Liste mit "+orderList.size()+" Aufträgen  für den Ausgang "+this.checkpoint.getName() );
		return this.orderList.get(0).getId();
	}
	
	/**
	 * Übergibt den Namen des Ausgang
	 * @return Name des Ausgangs
	 */
	public String getName() {
		return this.checkpoint.getName();
	}
	
	/**
	 * Überprüft an welcher stelle eine Order in den Ausgang geliefert werden muss
	 * @param id ID der Order
	 * @return  Prosiotn der Order
	 */
	public int OrderPostion(String id)
	{
		int sum = 0;
		for(int i =0; i < orderList.size(); ++i)
		{
			OrderInfo oi = orderList.get(i);
			if(id.equals(oi.getId()))
				return sum;
			
			if(orderList.get(i).isOnWay())
				sum+=1;
			else
				sum+=2;
		}
				
		logger.error("(OrderPostion) Auftrag "+id+" wird nicht im ausgang"+ this.getName()+ "erwartet");
		return sum;
	}
	
	/**
	 * Setzt bei einer OrderInformation den Wert das sie aus dem Eingang abgeholt wurde
	 * @param id ID der Order
	 */
	public void OrderonWay(String id)
	{
		for(int i =0; i < orderList.size(); ++i)
		{
			OrderInfo oi = orderList.get(i);
			if(id.equals(oi.getId()))
			{
				oi.setOnWay(true);
				return;
			}
			

		}
	}
	
	/**
	 * Entfertn alle Order aus dem Ausgang
	 */
	public void clear() {
		objekts.clear();
	}
}

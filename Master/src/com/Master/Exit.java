package com.Master;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;

public class Exit {

	/**
	 * logger
	 */
	private static org.apache.log4j.Logger logger = LogManager.getLogger("Checkpoint");
	
	private List<Order> objekts;
	
	private Checkpoint checkpoint;
	
	private List<OrderInfo> orderList;
	
	public Exit(Checkpoint checkpoint) {
		
		this.checkpoint = checkpoint;
		this.objekts = new ArrayList<Order>();
	}		

	public void setOrderList(List<OrderInfo> orders)
	{		
		this.orderList = orders;
		
		logger.info("Zugewiesende Liste mit "+orderList.size()+" Aufträgen  für den Ausgang "+this.checkpoint.getName() );
	}
	
	public boolean onFirstPosition(String id)
	{
		return id.equals(this.orderList.get(0).getId());
	}
	
	
	public void addOrder(Order order) {
		
		this.orderList.remove(0);
		this.objekts.add(order);
	}	
	
	public String DeleteFirstFromOrderList()
	{
		OrderInfo order = this.orderList.remove(0);
		logger.info("Benötigte Lieferung "+order.getId()+" von "+this.checkpoint.getName()+" gelöscht" );
		return order.getId();
	}
	
	public String getNextDelivery()
	{
		logger.info("Zugewiesende Liste mit "+orderList.size()+" Aufträgen  für den Ausgang "+this.checkpoint.getName() );
		return this.orderList.get(0).getId();
	}
	
	public String getName() {
		return this.checkpoint.getName();
	}
	
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
	
	public void clear() {
		objekts.clear();
	}
}

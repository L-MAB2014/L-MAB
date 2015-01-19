package com.Master;

public class OrderInfo {
	
	private String id;
	private boolean onWay;
	
	public OrderInfo(String id)
	{
		this.id = id;
		this.onWay = false;
	}

	public String getId() {
		return id;
	}

	public boolean isOnWay() {
		return onWay;
	}

	public void setOnWay(boolean onWay) {
		this.onWay = onWay;
	}
	

	
	
}

package com.Simulator;

import java.util.ArrayList;
import java.util.List;

import com.Master.IStockInput;
import com.Master.OrderInfo;

public class Simulator {
	private  SimulatorStock mStockA;
	private  SimulatorStock mStockB;
    private  SimulatorStock mStockC;
    
    private  SimulatorThread tStockA;
    private  SimulatorThread tStockB;
    private  SimulatorThread tStockC;
    
    
    private List<OrderInfo> OrdertoExit1;

	private List<OrderInfo> OrdertoExit2;
    
    private IStockInput input;
    
    
    public int mXCount;
    public int mYCount;

	public Simulator(IStockInput input) {
		
		this.input = input;
		
		this.OrdertoExit1 = new ArrayList<OrderInfo>();
		this.OrdertoExit2 = new ArrayList<OrderInfo>();
		
		mStockA = new SimulatorStock("PL1");
		mStockB = new SimulatorStock("PL2");
		mStockC = new SimulatorStock("PL3");
		mXCount = 0;
		mXCount = 0;	
	}
		
	private StockObjekt getNextPackage() {
		boolean left = (Math.random() < 0.5); 
		StockObjekt so;
	//	if(true)
		if(left)
		{
			so =  new StockObjekt(SimulatorData.key_PU2, mXCount++ );
			this.OrdertoExit2.add(new OrderInfo(so.toString()));
		}else
		{
			so = new StockObjekt(SimulatorData.key_PU1, mYCount++);
			this.OrdertoExit1.add(new OrderInfo(so.toString()));
		}
		
		return so;
	}
	
	public void CreatSimulateData() {
		
		mStockA.addObjekt(new StockObjekt(SimulatorData.key_Timeout,((int) (Math.random() * 10))));
		mStockB.addObjekt(new StockObjekt(SimulatorData.key_Timeout,((int) (Math.random() * 10))));
		mStockC.addObjekt(new StockObjekt(SimulatorData.key_Timeout,((int) (Math.random() * 10))));
		
		
		for(int i=0; i<SimulatorData.number_packages; i++) {
			int tmp = (int) (Math.random() * 3);
			if(tmp == 0) 
			{
				mStockA.addObjekt(getNextPackage());
				mStockA.addObjekt(new StockObjekt(SimulatorData.key_Timeout,((int) (Math.random() * 20))));
			}
			else if(tmp == 1) 
			{
				mStockB.addObjekt(getNextPackage());
				mStockB.addObjekt(new StockObjekt(SimulatorData.key_Timeout,((int) (Math.random() * 20))));
			}
			else
			{
				mStockC.addObjekt(getNextPackage());
				mStockC.addObjekt(new StockObjekt(SimulatorData.key_Timeout,((int) (Math.random() * 20))));
			}
		}
	}
	
	public void StartSimulat()
	{
		this.tStockA = new SimulatorThread(this.mStockA, this.input);
		this.tStockB = new SimulatorThread(this.mStockB, this.input);
		this.tStockC = new SimulatorThread(this.mStockC, this.input);
		
		this.tStockA.start();
		this.tStockB.start();
		this.tStockC.start();
	}
	
	
	public void reset() {
		mXCount = 0;
		mYCount = 0;
		mStockA.clear();
		mStockB.clear();
		mStockC.clear();
	}
	
	public void Stopp()
	{
		this.tStockA.stop();
		this.tStockB.stop();
		this.tStockC.stop();
	}
	
    public List<OrderInfo> getOrdertoExit1() {
		return OrdertoExit1;
	}

	public List<OrderInfo> getOrdertoExit2() {
		return OrdertoExit2;
	}
	
	
//	public static void main(String[] args) {
//		Simulator cs = new Simulator();
//		
//		cs.CreatSimulateData();
//		
//		System.out.println("Eingang A (" + cs.mStockA + ")");
//
//		System.out.println("Eingang B (" + cs.mStockB + ")");
//
//		System.out.println("Eingang C (" + cs.mStockC + ")");
//
//	}
}

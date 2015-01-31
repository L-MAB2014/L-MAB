package com.Simulator;

import java.util.ArrayList;
import java.util.List;

import com.Master.IStockInput;
import com.Master.OrderInfo;

/**
 * @author Jan Bein und Marcel Reich
 * Simulator Klasse
 */
public class Simulator {
	
	/**
	 * Eingang 1
	 */
	private  SimulatorStock mStockA;
	
	/**
	 * Eingang 2
	 */
	private  SimulatorStock mStockB;
	
    /**
     * Eingang 3
     */
    private  SimulatorStock mStockC;
    
    /**
     * Thread zum Pushen von Aufträgen für Eingang 1
     */
    private  SimulatorThread tStockA;
    
    /**
     * Thread zum Pushen von Aufträgen für Eingang 2
     */
    private  SimulatorThread tStockB;
    
    /**
     * Thread zum Pushen von Aufträgen für Eingang 3
     */
    private  SimulatorThread tStockC;
       
    /**
     * Liste mit den Aufträgen in welcher Reihenfolge sie von Ausgang1 erwartet werden
     */
    private List<OrderInfo> OrdertoExit1;

	/**
	 * Liste mit den Aufträgen in welcher Reihenfolge sie von Ausgang2 erwartet werden
	 */
	private List<OrderInfo> OrdertoExit2;
    
    /**
     * Schnittstelle zum Controller
     */
    private IStockInput input;
       
    /**
     * Anzahl Aufträge für Eingang 1
     */
    public int mXCount;
    
    /**
     * Anzahl Aufträge für Eingang 1
     */
    public int mYCount;

	/**
	 * @param input
	 */
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
		
	/**
	 * Erzeugt einen Auftrag für einen Eingang
	 * @return
	 * Neuer Auftrag für ein Eingang
	 */
	private StockObjekt getNextPackage() {
		boolean left = (Math.random() < 0.5); 
		StockObjekt so;
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
	
	/**
	 * Erstelt die Aufträge und Teil sie zu
	 */
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
	
	/**
	 * Startet die Simualtor-Threads, welche die Aufträge dem Controller übergben
	 */
	public void StartSimulat()
	{
		this.tStockA = new SimulatorThread(this.mStockA, this.input);
		this.tStockB = new SimulatorThread(this.mStockB, this.input);
		this.tStockC = new SimulatorThread(this.mStockC, this.input);
		
		this.tStockA.start();
		this.tStockB.start();
		this.tStockC.start();
	}
	
	
	/**
	 * Resetet die Informationen
	 */
	public void reset() {
		mXCount = 0;
		mYCount = 0;
		mStockA.clear();
		mStockB.clear();
		mStockC.clear();
	}
	
	/**
	 * Stoppt die Eingangs-Threads
	 */
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
	
}

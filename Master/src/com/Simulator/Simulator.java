package com.Simulator;

import com.Master.IStockInput;

public class Simulator {
	private  SimulatorStock mStockA;
	private  SimulatorStock mStockB;
    private  SimulatorStock mStockC;
    
    private  SimulatorThread tStockA;
    private  SimulatorThread tStockB;
    private  SimulatorThread tStockC;
    
    private IStockInput input;
    
    
    public int mXCount;
    public int mYCount;

	public Simulator(IStockInput input) {
		
		this.input = input;
		
		mStockA = new SimulatorStock("PL1");
		mStockB = new SimulatorStock("PL2");
		mStockC = new SimulatorStock("PL3");
		mXCount = 0;
		mXCount = 0;	
	}
		
	private StockObjekt getNextPackage() {
		boolean left = (Math.random() < 0.5); 
		return left ? new StockObjekt(SimulatorData.key_PU2, mXCount++ ): new StockObjekt(SimulatorData.key_PU1, mYCount++);
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
				mStockA.addObjekt(new StockObjekt(SimulatorData.key_Timeout,((int) (Math.random() * 40))));
			}
			else if(tmp == 1) 
			{
				mStockB.addObjekt(getNextPackage());
				mStockB.addObjekt(new StockObjekt(SimulatorData.key_Timeout,((int) (Math.random() * 40))));
			}
			else
			{
				mStockC.addObjekt(getNextPackage());
				mStockC.addObjekt(new StockObjekt(SimulatorData.key_Timeout,((int) (Math.random() * 40))));
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

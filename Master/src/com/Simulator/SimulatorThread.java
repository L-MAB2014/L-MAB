package com.Simulator;

import java.util.logging.Logger;

import com.Master.IStockInput;


public class SimulatorThread extends Thread {
	
	private SimulatorStock stock;
	
	private IStockInput input;
	
	
	/**
	 * logger
	 */
	private static Logger logger = Logger.getAnonymousLogger();
	
	SimulatorThread(SimulatorStock s, IStockInput input)
	{
		this.stock = s; 
		this.input = input;
	}
	
	public void run() {

        try {
        	logger.info("Thread-StartStock: "+this.stock.getName());
        	StockObjekt sb = this.stock.getNextObjekt();
        	       	
            while (sb != null) {
                
            	if(sb.getKey1() == SimulatorData.key_Timeout)
            	{
            		try{
            			 Thread.sleep((sb.getKey2()*1000));
            		}catch(Exception e)
            		{
            			logger.info(this.stock.getName()+"-SimulatorThread-run() : "+e);
            		}
            	}else
            	{
            		this.input.ObjektToStock(sb.toString(), this.stock.getName(), SimulatorData.KeyToStock(sb.getKey1()));
            	}
            	            	
            	sb = stock.getNextObjekt();           	            	
            }
           
        } catch (Exception e) {

        	logger.info("SimulatorThread -run():"+e);
        }
        logger.info("Thread-Ende Stock "+this.stock.getName());


    }

}

package com.Simulator;

import org.apache.log4j.LogManager;

import com.Master.IStockInput;


/**
 * @author Marcel Reich
 * Thread kümmert sich um eine Eingang und pusht jenach Zeit angebae die Pakte dem Controller zu
 */
public class SimulatorThread extends Thread {
	
	/**
	 *  Eingang
	 */
	private SimulatorStock stock;
	
	/**
	 * Schnittstelle zum Controller
	 */
	private IStockInput input;
	
	
	/**
	 * logger
	 */
	private static org.apache.log4j.Logger logger = LogManager.getLogger("Controller");
	
	/**
	 * Konstruktor 
	 * @param s Eingang
	 * @param input Schnittstelle
	 */
	SimulatorThread(SimulatorStock s, IStockInput input)
	{
		this.stock = s; 
		this.input = input;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
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

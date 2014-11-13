package lmab.jbein.StockSimulator;

public class Model {
	Stock mStockA;
	Stock mStockB;
	Stock mStockC;
	int mXCount;
	int mYCount;
	Integer mElements;
	
	public Model() {
		mStockA = new Stock();
		mStockB = new Stock();
		mStockC = new Stock();
		mXCount = 0;
		mXCount = 0;
		mElements = 50;
		fillStocks(mElements);
	}
		
	private String getNextPackage() {
		boolean left = (Math.random() < 0.5); 
		return left ? "X-" + mXCount++ : "Y-" + mYCount++;
	}
	
	private void fillStocks(int packages) {
		for(int i=0; i<packages; i++) {
			int tmp = (int) (Math.random() * 3);
			if(tmp == 0) 
				{
					mStockA.addBack(getNextPackage());
					mStockA.addBack("T-"+(int) (Math.random() * 40));
				}
			else if(tmp == 1) 
				{
					mStockB.addBack(getNextPackage());
					mStockB.addBack("T-"+(int) (Math.random() * 40));
				}
			else
				{
					mStockC.addBack(getNextPackage());
					mStockC.addBack("T-"+(int) (Math.random() * 40));
				}
		}
	}
	
	public void setElements(int elements) {
		mElements = elements;
	}
	
	public void reset() {
		mXCount = 0;
		mYCount = 0;
		mStockA = new Stock();
		mStockB = new Stock();
		mStockC = new Stock();
		fillStocks(mElements);
	}
}

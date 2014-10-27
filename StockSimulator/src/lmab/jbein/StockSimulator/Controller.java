package lmab.jbein.StockSimulator;

public class Controller {
	Model mModel;
	View mView;
	
	public Controller() {
		mModel = new Model();
		mView = new View(mModel);
	}
}

package com.Master;

import com.View.StockView;

public class StockController {
	StockModel mModel;
	StockView mView;

	public StockController() {
		mModel = new StockModel();
		mView = new StockView(mModel);
	}
}

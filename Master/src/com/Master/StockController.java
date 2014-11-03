package com.Master;

import com.View.ViewStocksimulator;

public class StockController {
	StockModel mModel;
	ViewStocksimulator mView;

	public StockController() {
		mModel = new StockModel();
		mView = new ViewStocksimulator(mModel);
	}
}

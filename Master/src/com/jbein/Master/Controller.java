package com.jbein.Master;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

public class Controller {
	private Model mModel;
	private View mView;
	
	Controller() {
		mModel = new Model();
		mView = new View(mModel);
	}
	
}

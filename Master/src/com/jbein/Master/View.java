package com.jbein.Master;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

public class View extends JFrame {
	private Model mModel;
	private JMenuBar mbMenu;
	private JMenu mFile;
	private JMenuItem miExit;
	private JButton btnSend;
	private JTextField tfSend;
	public JLabel lbInfo, lbBot, lbSendInfo, lbReceiveInfo;
		
	View(Model model) {
		super("L-MAB Master v1.0");
		mModel = model;
		setLocation(100, 100);
		setSize(200, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(0,1));
		setElements();
		setJMenuBar(mbMenu);
		setResizable(false);
		setVisible(true);
	}
	
	private void setElements() {
		mbMenu = new JMenuBar();
		mFile = new JMenu("File");
		miExit = new JMenuItem("Exit");
		miExit.setMnemonic(KeyEvent.VK_Q);
		miExit.addActionListener(new ClosingListener());
		mFile.add(miExit);
		mbMenu.add(mFile);
		
		tfSend = new JTextField(10);
		tfSend.setSize(30, 100);
		
		lbInfo = new JLabel("");
		lbInfo.setVisible(true);
		
		lbBot = new JLabel("");
		lbBot.setVisible(true);
		
		lbSendInfo = new JLabel("Send: ");
		lbSendInfo.setVisible(true);
		
		lbReceiveInfo = new JLabel("Receive: ");
		lbReceiveInfo.setVisible(true);
		
		btnSend = new JButton("Send");
		btnSend.setSize(30, 100);
		btnSend.addActionListener(new BTSendListener());
		
		add(tfSend);
		add(btnSend);
		add(lbSendInfo);
		add(lbReceiveInfo);
	}
	
    class ClosingListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	System.exit(0);
        }
    }
    
    class BTSendListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	mModel.str = tfSend.getText();
        }
    }
}

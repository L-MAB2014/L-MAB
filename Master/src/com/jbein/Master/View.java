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
	private JLabel lbInfo, lbBot;
		
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
		
		btnSend = new JButton("Send");
		btnSend.setSize(30, 100);
		btnSend.addActionListener(new BTSendListener());
		
		add(tfSend);
		add(btnSend);
		add(lbInfo);
		add(lbBot);
	}
	
    class ClosingListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	System.exit(0);
        }
    }
    
    class BTSendListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
//        	String sendMe = tfSend.getText();
        	mModel.str = tfSend.getText();
        	btsend();
        }
    }
    
	private void btsend() {
		NXTConnector conn = new NXTConnector();
		conn.addLogListener(new NXTCommLogListener() {
			public void logEvent(String message) {
				System.out.println("BTSend Log.listener: "+message);
				
			}

			public void logEvent(Throwable throwable) {
				System.out.println("BTSend Log.listener - stack trace: ");
				throwable.printStackTrace();	
			}		
		});
		// Connect to any NXT over Bluetooth
		boolean connected = conn.connectTo("btspp://");
	
		if (!connected) {
			System.err.println("Failed to connect to any NXT");
			System.exit(1);
		}
		
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		DataInputStream dis = new DataInputStream(conn.getInputStream());
				
		String sendMe = mModel.str;
		try {
			System.out.println(sendMe);
			System.out.println("Sending " + sendMe);
			dos.writeUTF(sendMe);
			dos.flush();			
			
		} catch (IOException ioe) {
			System.out.println("IO Exception writing String:");
			System.out.println(ioe.getMessage());
		}
		
		try {
			System.out.println("Received -> " + dis.readUTF());
		} catch (IOException ioe) {
			System.out.println("IO Exception reading String:");
			System.out.println(ioe.getMessage());
		}
		
		try {
			dis.close();
			dos.close();
			conn.close();
		} catch (IOException ioe) {
			System.out.println("IOException closing connection:");
			System.out.println(ioe.getMessage());
		}
	}
}

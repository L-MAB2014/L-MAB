package com.marcel.TestMaster;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class View extends JFrame{
	ButtonGroup grp_exit;
	ButtonGroup grp_store;
	
	JCheckBox store_blue;
	JCheckBox store_green;
	JCheckBox store_yellow;
	
	JCheckBox exit_orange;
	JCheckBox exit_pink;
	
	JPanel panel_connect;
	JPanel panel_selection;
	JPanel panel_selection_store;
	JPanel panel_selection_exit;
	
	JButton btn_connect;
	JButton btn_send;
	
	JTextArea dialog;
	
	View()
	{
		super("Master");
		setLayout(new GridLayout(4,1));
		
		this.panel_connect = new JPanel();
		this.panel_selection = new JPanel();
		this.panel_selection_store = new JPanel();
		this.panel_selection_exit = new JPanel();
		
		this.panel_connect.setLayout(new GridLayout(1,1));			
		this.panel_selection.setLayout(new GridLayout(1,2));
		this.panel_selection_store.setLayout(new GridLayout(4,1));
		this.panel_selection_exit.setLayout(new GridLayout(3,1));

						
		this.btn_connect = new JButton("Verbindung aufbauen");		
		this.btn_send = new JButton("Auftrag senden!");

		
		this.dialog = new JTextArea();
		
		this.panel_connect.add(this.btn_connect);
		
		this.grp_store = new ButtonGroup();
		this.store_blue = new JCheckBox("Blaues Lager",false);
		this.store_green = new JCheckBox("Grünes Lager",false);
		this.store_yellow = new JCheckBox("Gelbes Lager",false);
		
		this.grp_store.add(this.store_blue); this.grp_store.add(this.store_green); this.grp_store.add(this.store_yellow);
		
		this.panel_selection_store.add(new JLabel("Lager"));
		this.panel_selection_store.add(this.store_blue);
		this.panel_selection_store.add(this.store_green);
		this.panel_selection_store.add(this.store_yellow);
		
		this.grp_exit = new ButtonGroup();
		this.exit_orange = new JCheckBox("Orangener Ausgang",false);
		this.exit_pink = new JCheckBox("Pinker Ausgang",false);
		
		this.grp_exit.add(this.exit_orange); this.grp_exit.add(this.exit_pink);
		
		this.panel_selection_exit.add(new Label("Ausgang"));
		this.panel_selection_exit.add(this.exit_orange);
		this.panel_selection_exit.add(this.exit_pink);
		
		this.panel_selection.add(this.panel_selection_store);
		this.panel_selection.add(this.panel_selection_exit);
		
		this.add(this.panel_connect);
		this.add(this.panel_selection);
		
		this.add(this.btn_send);
		this.add(this.dialog);
		
		this.SetModus(false);
		

		pack();
		setVisible(true);
	}
	
	
	public void SetModus(boolean modus)
	{
		this.btn_send.setEnabled(modus);
		this.dialog.setEnabled(modus);
		this.store_blue.setEnabled(modus);
		this.store_green.setEnabled(modus);
		this.store_yellow.setEnabled(modus);
		this.exit_orange.setEnabled(modus);
		this.exit_pink.setEnabled(modus);
		
		if(modus)
		{
			btn_connect.setText("Verbindung trennen");
		}else
		{
			btn_connect.setText("Verbindung aufbauen");
		}
	}
	
	
	
	public synchronized void InputDialog(String text)
	{
		this.dialog.insert(text+"\n", 0);
	}
	
	public void addBTNConnectListener(ActionListener listenForCalcButton)
	{
			         
		btn_connect.addActionListener(listenForCalcButton);
		         
	 }
		
	public void addBTNSendListener(ActionListener listenForCalcButton)
	{
			         
		btn_send.addActionListener(listenForCalcButton);
		         
	 }
	
	 

	
}

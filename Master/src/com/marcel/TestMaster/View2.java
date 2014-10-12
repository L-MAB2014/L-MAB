package com.marcel.TestMaster;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class View2 extends JFrame{
	
	private ButtonGroup grp_exit;
	private ButtonGroup grp_store;
	
	private JRadioButton store_1;
	private JRadioButton store_2;
	private JRadioButton store_3;
	
	private JRadioButton exit_1;
	private JRadioButton exit_2;
		
	private JTable bot_Table;
	
	private JTextField bot_Name;
	
	private JButton btn_newBot;
	private JButton btn_newOrder;
	
	private JButton btn_controlBot;
	private JButton btn_controlOrder;
	
	private JButton btn_stopp;
		
	private JTextArea console;
	
	View2()
	{
		setResizable(false);
		
		getContentPane().setLayout(null);
		this.setSize(1200,680);
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(View2.class.getResource("/com/marcel/TestMaster/map.png")));
		lblNewLabel.setBounds(20, 10, 730, 400);
		getContentPane().add(lblNewLabel);
		
		bot_Table = new JTable();	
		bot_Table.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"Nr.", "Name", "Status", "Ziel", "Standpunkt", "Auftr\u00E4ge"
			}
		));
		bot_Table.getColumnModel().getColumn(0).setPreferredWidth(27);
		bot_Table.getColumnModel().getColumn(1).setPreferredWidth(91);
		bot_Table.getColumnModel().getColumn(2).setResizable(false);
		bot_Table.getColumnModel().getColumn(2).setMinWidth(80);
		bot_Table.getColumnModel().getColumn(2).setMaxWidth(100);
		bot_Table.getColumnModel().getColumn(3).setPreferredWidth(80);
		bot_Table.getColumnModel().getColumn(3).setMaxWidth(80);
		bot_Table.getColumnModel().getColumn(4).setMinWidth(24);
		bot_Table.getColumnModel().getColumn(4).setMaxWidth(80);
		bot_Table.getColumnModel().getColumn(5).setMinWidth(12);
		bot_Table.getColumnModel().getColumn(5).setMaxWidth(55);
		bot_Table.setBounds(760, 10, 420, 400);
		
		JScrollPane scrollTable = new JScrollPane(bot_Table);
		scrollTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		scrollTable.setBounds(760, 10, 420, 400);
		getContentPane().add(scrollTable);
		
		
	    this.console = new JTextArea();
	    this.console.setEditable(false);
	    this.console.setBounds(30, 443, 700, 180);
		
		JScrollPane scrollPane = new JScrollPane(this.console);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		scrollPane.setBounds(20, 433, 730, 180);
		getContentPane().add(scrollPane);
		
		this.btn_newOrder= new JButton("Neuer Auftrag");
		this.btn_newOrder.setBounds(760, 524, 146, 37);
		getContentPane().add(this.btn_newOrder);
		
		this.btn_controlOrder = new JButton("Auftr\u00E4ge  verwalten");
		this.btn_controlOrder.setBounds(760, 572, 146, 37);
		getContentPane().add(this.btn_controlOrder);
		
		this.btn_newBot = new JButton("Neuer Bot");
		this.btn_newBot.setBounds(916, 524, 147, 37);
		getContentPane().add(btn_newBot);
		
		this.btn_controlBot = new JButton("Bot verwalten");
		this.btn_controlBot.setBounds(916, 572, 147, 37);
		getContentPane().add(btn_controlBot);
		
		this.btn_stopp = new JButton("STOPP");
		this.btn_stopp.setBounds(1078, 433, 102, 167);
		getContentPane().add(btn_stopp);
				
		this.bot_Name = new JTextField();
		this.bot_Name.setBounds(916, 494, 147, 20);
		getContentPane().add(this.bot_Name);
		this.bot_Name.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(921, 480, 46, 14);
		getContentPane().add(lblName);
		
		JLabel lblLager = new JLabel("Lager:");
		lblLager.setBounds(760, 439, 46, 14);
		getContentPane().add(lblLager);
		
		JLabel lblAusfahrt = new JLabel("Ausfahrt:");
		lblAusfahrt.setBounds(760, 480, 46, 14);
		getContentPane().add(lblAusfahrt);
		
		this.store_1 = new JRadioButton("L1");
		this.store_1.setBounds(760, 455, 39, 23);
		getContentPane().add(this.store_1);
		
		this.store_2 = new JRadioButton("L2");
		this.store_2.setBounds(801, 455, 39, 23);
		getContentPane().add(this.store_2);
		
		this.store_3 = new JRadioButton("L3");
		this.store_3.setBounds(838, 455, 46, 23);
		getContentPane().add(this.store_3);
		
		this.grp_store = new ButtonGroup();
		this.grp_store.add(this.store_1); this.grp_store.add(this.store_2); this.grp_store.add(this.store_3);
		
		this.exit_1 = new JRadioButton("E1");
		this.exit_1.setBounds(760, 493, 39, 23);
		getContentPane().add(this.exit_1);
		
		this.exit_2 = new JRadioButton("E2");
		this.exit_2.setBounds(801, 493, 39, 23);
		getContentPane().add(this.exit_2);
			
		this.grp_exit = new ButtonGroup();
		this.grp_exit.add(this.exit_1); this.grp_exit.add(this.exit_2);
						
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnVorbereitung = new JMenu("Vorbereitung?!?");
		menuBar.add(mnVorbereitung);
		
		JMenuItem menuItem = new JMenuItem("1.1");
		mnVorbereitung.add(menuItem);
		
		JMenuItem menuItem_1 = new JMenuItem("1.2");
		mnVorbereitung.add(menuItem_1);
		
		JMenuItem menuItem_2 = new JMenuItem("1.3");
		mnVorbereitung.add(menuItem_2);
		setVisible(true);
		

//		
//		for(int i = 0 ; i< 100; ++i)
//		{
//			((DefaultTableModel)bot_Table.getModel()).addRow(new Object[]{""+i, "A", "B", "C", "D", "Q"});
//		}
//		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for(int i = 0 ; i< 100; ++i)
//		{
//			bot_Table.setValueAt((100-i),i,0) ;
//		}
				
	}
	
	public synchronized void InputDialog(String text)
	{
		this.console.append(text+"\n");
		this.console.setCaretPosition(this.console.getDocument().getLength());
	}
	
	public synchronized void InputTable( String [] text)
	{
		((DefaultTableModel)bot_Table.getModel()).addRow(text);
	}
	
	public synchronized void DeleteFromTable( int row)
	{
		this.bot_Table.remove(row);
	}
	
	public synchronized void UpdateTable( int row, String [] text)
	{
		if(bot_Table.getValueAt(row, 1).toString() ==  text[0])
		{
			for(int i = 1; i < text.length;++i)
			{
				bot_Table.setValueAt(text[i],row,i+1) ;
			}
		}

	}
	
	public void addControlBotListener(ActionListener listerButton)
	{			         
		this.btn_controlBot.addActionListener(listerButton);		         
	 }
		
	public void addNewBotListener(ActionListener listerButton)
	{			         
		this.btn_newBot.addActionListener(listerButton);		         
	 }
	
	public void addConrtolOrderListener(ActionListener listerButton)
	{			         
		this.btn_controlOrder.addActionListener(listerButton);		         
	 }
	
	public void addNewOrderListener(ActionListener listerButton)
	{			         
		this.btn_newOrder.addActionListener(listerButton);		         
	 }
	public void addStoppListener(ActionListener listerButton)
	{			         
		this.btn_stopp.addActionListener(listerButton);		         
	 }
	
	public int GetStoreSelection()
	{
		if(this.store_1.isSelected())
			//return new Message("S","1");
			return 1;
		else if(this.store_2.isSelected())
			return 2;
		else if(this.store_3.isSelected())
			return 3;
		else
			return 0;
	}
		
	public int GetExitSelection() 
    {					
		if(this.exit_1.isSelected())
			return 1;
		else if(this.exit_2.isSelected())
			return 2;
		else
			return 0;				
    } 
	
	public String GetBotName()
	{
		return this.bot_Name.getText();
	}
	
	
	
}

package com.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.logging.Logger;

public class View extends JFrame {

	/**
	 * logger
	 */
	private static Logger logger = Logger.getAnonymousLogger();
	
    JMenuItem menuStart;
    JMenuItem menuOeffnen;
    JMenuItem menuSpeichern;
    JMenuItem menuEinstellungen;
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
    private HashMap<String, JLabel> checkmap;
    HashMap<String, JRadioButton> closedmap;
    public View() {
        setResizable(false);

        getContentPane().setLayout(null);
        this.setSize(1200, 680);

        this.setClosedPoints();
        this.setCheckpoints();
               
        JLabel lblNewLabel = new JLabel(new ImageIcon(View.class.getResource("/com/View/map.png")));
        //lblNewLabel.setIcon(new ImageIcon(View.class.getResource("/com/View/map.png")));
        lblNewLabel.setBounds(20, 10, 730, 419);
        getContentPane().add(lblNewLabel);

        bot_Table = new JTable();
        bot_Table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "Nr.", "Name", "Status", "Ziel", "Standpunkt", "Aufträge"
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

        this.btn_newOrder = new JButton("Neuer Auftrag");
        this.btn_newOrder.setBounds(760, 524, 146, 37);
        getContentPane().add(this.btn_newOrder);

        this.btn_controlOrder = new JButton("Aufträge  verwalten");
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

        this.bot_Name = new JTextField("hunter2");
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
        this.grp_store.add(this.store_1);
        this.grp_store.add(this.store_2);
        this.grp_store.add(this.store_3);

        this.exit_1 = new JRadioButton("E1");
        this.exit_1.setBounds(760, 493, 39, 23);
        getContentPane().add(this.exit_1);

        this.exit_2 = new JRadioButton("E2");
        this.exit_2.setBounds(801, 493, 39, 23);
        getContentPane().add(this.exit_2);

        this.grp_exit = new ButtonGroup();
        this.grp_exit.add(this.exit_1);
        this.grp_exit.add(this.exit_2);

        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnVorbereitung = new JMenu("Simulation");
        menuBar.add(mnVorbereitung);

        menuStart = new JMenuItem("Starten");
        mnVorbereitung.add(menuStart);

        menuOeffnen = new JMenuItem("Öffnen");
        mnVorbereitung.add(menuOeffnen);

        menuSpeichern = new JMenuItem("Speichern");
        mnVorbereitung.add(menuSpeichern);

        menuEinstellungen = new JMenuItem("Einstellungen");
        mnVorbereitung.add(menuEinstellungen);

        setVisible(true);
    }

    public synchronized void InputDialog(String text) {
        this.console.append(text + "\n");
        this.console.setCaretPosition(this.console.getDocument().getLength());
    }

    public synchronized void InputTable(String[] text) {
        ((DefaultTableModel) bot_Table.getModel()).addRow(text);
    }

    public synchronized void DeleteFromTable(int row) {
        this.bot_Table.remove(row);
    }

    public synchronized void UpdateTable(int row, String[] text) {
        if (bot_Table.getValueAt(row, 1).toString() == text[0]) {
            for (int i = 1; i < text.length; ++i) {
                bot_Table.setValueAt(text[i], row, i + 1);
            }
        }
    }

    public void addControlBotListener(ActionListener listerButton) {
        this.btn_controlBot.addActionListener(listerButton);
    }

    public void addNewBotListener(ActionListener listerButton) {
        this.btn_newBot.addActionListener(listerButton);
    }

    public void addConrtolOrderListener(ActionListener listerButton) {
        this.btn_controlOrder.addActionListener(listerButton);
    }

    public void addNewOrderListener(ActionListener listerButton) {
        this.btn_newOrder.addActionListener(listerButton);
    }

    public void addStoppListener(ActionListener listerButton) {
        this.btn_stopp.addActionListener(listerButton);
    }

    public void addSimulationStartListener(ActionListener listerButton) {
        this.menuStart.addActionListener(listerButton);
    }

    public void addSimulationOeffnenListener(ActionListener listerButton) {
        this.menuOeffnen.addActionListener(listerButton);
    }

    public void addSimulationSpeichernListener(ActionListener listerButton) {
        this.menuSpeichern.addActionListener(listerButton);
    }

    public void addSimulationEinstellungenListener(ActionListener listerButton) {
        this.menuEinstellungen.addActionListener(listerButton);
    }

    public int GetStoreSelection() {
        if (this.store_1.isSelected())
            //return new Message("S","1");
            return 1;
        else if (this.store_2.isSelected())
            return 2;
        else if (this.store_3.isSelected())
            return 3;
        else
            return 0;
    }

    public int GetExitSelection() {
        if (this.exit_1.isSelected())
            return 1;
        else if (this.exit_2.isSelected())
            return 2;
        else
            return 0;
    }

    public String GetBotName() {
        return this.bot_Name.getText();
    }

    public synchronized void UpdateCheckpoint(String checkpoint, String last_checkpoint, String bot_name) {
	      		
       if(checkpoint != null)
 	   {	
    		JLabel label = this.checkmap.get(checkpoint);
	
	        if (label != null) {
	            label.setText(bot_name);
	        }
 	   }

	   if(last_checkpoint != null)
	   { 	
		   JLabel label = this.checkmap.get(last_checkpoint);
	
	        if (label != null) {
	            if (bot_name.equals(label.getText()))
	                label.setText("");
	        }
	   }
    }
    
    public synchronized void UpdateClosedpoint(String checkpoint, boolean b) {
        JRadioButton radio = this.closedmap.get(checkpoint);

        if (radio != null) {
            radio.setSelected(b);
        }
    }

    private void setCheckpoints() {
        this.checkmap = new HashMap<String, JLabel>();

        JLabel lblCp4 = new JLabel("");
        lblCp4.setBounds(161, 127, 46, 14);
        getContentPane().add(lblCp4);
        checkmap.put("CP4", lblCp4);

        JLabel lblCp3 = new JLabel("");
        lblCp3.setBounds(161, 189, 46, 14);
        getContentPane().add(lblCp3);
        checkmap.put("CP3", lblCp3);

        JLabel lblCp2 = new JLabel("");
        lblCp2.setBounds(161, 248, 46, 14);
        getContentPane().add(lblCp2);
        checkmap.put("CP2", lblCp2);

        JLabel lblCp1 = new JLabel("");
        lblCp1.setBounds(161, 310, 46, 14);
        getContentPane().add(lblCp1);
        checkmap.put("CP1", lblCp1);

        //--------------------------------------------------
        JLabel lblp4 = new JLabel("");
        lblp4.setBounds(61, 111, 46, 14);
        getContentPane().add(lblp4);
        checkmap.put("P4", lblp4);

        JLabel lblp3 = new JLabel("");
        lblp3.setBounds(61, 168, 46, 14);
        getContentPane().add(lblp3);
        checkmap.put("P3", lblp3);

        JLabel lblp2 = new JLabel("");
        lblp2.setBounds(61, 229, 46, 14);
        getContentPane().add(lblp2);
        checkmap.put("P2", lblp2);

        JLabel lblp1 = new JLabel("");
        lblp1.setBounds(61, 290, 46, 14);
        getContentPane().add(lblp1);
        checkmap.put("P1", lblp1);

        //-----------------------------------------------
        JLabel lbls6 = new JLabel("");
        lbls6.setBounds(561, 113, 46, 14);
        getContentPane().add(lbls6);
        checkmap.put("S6", lbls6);

        JLabel lbls5 = new JLabel("");
        lbls5.setBounds(493, 113, 46, 14);
        getContentPane().add(lbls5);
        checkmap.put("S5", lbls5);

        JLabel lbls4 = new JLabel("");
        lbls4.setBounds(409, 113, 46, 14);
        getContentPane().add(lbls4);
        checkmap.put("S4", lbls4);

        JLabel lbls3 = new JLabel("");
        lbls3.setBounds(341, 113, 46, 14);
        getContentPane().add(lbls3);
        checkmap.put("S3", lbls3);

        JLabel lbls2 = new JLabel("");
        lbls2.setBounds(257, 113, 46, 14);
        getContentPane().add(lbls2);
        checkmap.put("S2", lbls2);

        JLabel lbls1 = new JLabel("");
        lbls1.setBounds(190, 113, 46, 14);
        getContentPane().add(lbls1);
        checkmap.put("S1", lbls1);

        //---------------------------------------------
        JLabel lblpl3 = new JLabel("");
        lblpl3.setBounds(550, 18, 46, 14);
        getContentPane().add(lblpl3);
        checkmap.put("PL3", lblpl3);

        JLabel lblpl2 = new JLabel("");
        lblpl2.setBounds(396, 18, 46, 14);
        getContentPane().add(lblpl2);
        checkmap.put("PL2", lblpl2);

        JLabel lblpl1 = new JLabel("");
        lblpl1.setBounds(250, 18, 46, 14);
        getContentPane().add(lblpl1);
        checkmap.put("PL1", lblpl1);

        //---------------------------------------------
        JLabel lblc3 = new JLabel("");
        lblc3.setBounds(561, 272, 46, 14);
        getContentPane().add(lblc3);
        checkmap.put("C3", lblc3);

        JLabel lblc2 = new JLabel("");
        lblc2.setBounds(561, 214, 46, 14);
        getContentPane().add(lblc2);
        checkmap.put("C2", lblc2);

        JLabel lblc1 = new JLabel("");
        lblc1.setBounds(561, 151, 46, 14);
        getContentPane().add(lblc1);
        checkmap.put("C1", lblc1);

        //---------------------------------------------
        JLabel lblpf3 = new JLabel("");
        lblpf3.setBounds(680, 260, 46, 14);
        getContentPane().add(lblpf3);
        checkmap.put("PF3", lblpf3);

        JLabel lblpf2 = new JLabel("");
        lblpf2.setBounds(680, 196, 46, 14);
        getContentPane().add(lblpf2);
        checkmap.put("PF2", lblpf2);

        JLabel lblpf1 = new JLabel("");
        lblpf1.setBounds(680, 133, 46, 14);
        getContentPane().add(lblpf1);
        checkmap.put("PF1", lblpf1);

        //---------------------------------
        JLabel lble4 = new JLabel("");
        lble4.setBounds(245, 322, 46, 14);
        getContentPane().add(lble4);
        checkmap.put("E4", lble4);

        JLabel lble3 = new JLabel("");
        lble3.setBounds(310, 322, 46, 14);
        getContentPane().add(lble3);
        checkmap.put("E3", lble3);

        JLabel lble2 = new JLabel("");
        lble2.setBounds(435, 322, 46, 14);
        getContentPane().add(lble2);
        checkmap.put("E2", lble2);

        JLabel lble1 = new JLabel("");
        lble1.setBounds(505, 322, 46, 14);
        getContentPane().add(lble1);
        checkmap.put("E1", lble1);

        //----------------------------------
        JLabel lblpu2 = new JLabel("");
        lblpu2.setBounds(296, 403, 46, 14);
        getContentPane().add(lblpu2);
        checkmap.put("PU2", lblpu2);

        JLabel lblpu1 = new JLabel("");
        lblpu1.setBounds(491, 403, 46, 14);
        getContentPane().add(lblpu1);
        checkmap.put("PU1", lblpu1);
    }
    
    private void setClosedPoints() {
    	
    	this.closedmap = new HashMap<String, JRadioButton>();
    	
    	 JRadioButton radioP1 = new JRadioButton();
    	 radioP1.setBounds(78, 310, 21, 14);
    	 radioP1.setBackground(Color.WHITE);
    	 radioP1.setEnabled(false);
         getContentPane().add(radioP1);
         closedmap.put("P1",radioP1);
         
         JRadioButton radioP2 = new JRadioButton();
    	 radioP2.setBounds(78, 247, 21, 14);
    	 radioP2.setBackground(Color.WHITE);
    	 radioP2.setEnabled(false);
         getContentPane().add(radioP2);
         closedmap.put("P2",radioP2);
         
         JRadioButton radioP3 = new JRadioButton();
    	 radioP3.setBounds(78, 188, 21, 14);
    	 radioP3.setBackground(Color.WHITE);
    	 radioP3.setEnabled(false);
         getContentPane().add(radioP3);
         closedmap.put("P3",radioP3);
         
         JRadioButton radioP4 = new JRadioButton();
    	 radioP4.setBounds(78, 127, 21, 14);
    	 radioP4.setBackground(Color.WHITE);
    	 radioP4.setEnabled(false);
         getContentPane().add(radioP4);
         closedmap.put("P4",radioP4);
         
         //----------------------------------------------
         
         JRadioButton radioPF1 = new JRadioButton();
    	 radioPF1.setBounds(640, 151, 21, 14);
    	 radioPF1.setBackground(Color.WHITE);
    	 radioPF1.setEnabled(false);
         getContentPane().add(radioPF1);
         closedmap.put("PF1",radioPF1);
         
         JRadioButton radioPF2 = new JRadioButton();
    	 radioPF2.setBounds(640, 213, 21, 14);
    	 radioPF2.setBackground(Color.WHITE);
    	 radioPF2.setEnabled(false);
         getContentPane().add(radioPF2);
         closedmap.put("PF2",radioPF2);
         
         JRadioButton radioPF3 = new JRadioButton();
    	 radioPF3.setBounds(640, 271, 21, 14);
    	 radioPF3.setBackground(Color.WHITE);
    	 radioPF3.setEnabled(false);
         getContentPane().add(radioPF3);
         closedmap.put("PF3",radioPF3);
         
       //----------------------------------------------
        
         JRadioButton radioCP1 = new JRadioButton();
    	 radioCP1.setBounds(127, 340, 21, 14);
    	 radioCP1.setBackground(Color.WHITE);
    	 radioCP1.setEnabled(false);
         getContentPane().add(radioCP1);
         closedmap.put("CP1",radioCP1);
         
         JRadioButton radioCP2 = new JRadioButton();
    	 radioCP2.setBounds(127, 280, 21, 14);
    	 radioCP2.setBackground(Color.WHITE);
    	 radioCP2.setEnabled(false);
         getContentPane().add(radioCP2);
         closedmap.put("CP2",radioCP2);
         
         JRadioButton radioCP3 = new JRadioButton();
    	 radioCP3.setBounds(127, 221, 21, 14);
    	 radioCP3.setBackground(Color.WHITE);
    	 radioCP3.setEnabled(false);
         getContentPane().add(radioCP3);
         closedmap.put("CP3",radioCP3);
         
         JRadioButton radioCP4 = new JRadioButton();
    	 radioCP4.setBounds(127, 160, 21, 14);
    	 radioCP4.setBackground(Color.WHITE);
    	 radioCP4.setEnabled(false);
         getContentPane().add(radioCP4);
         closedmap.put("CP4",radioCP4);
         
   //----------------------------------------------
         
         JRadioButton radioC1 = new JRadioButton();
    	 radioC1.setBounds(601, 120, 21, 14);
    	 radioC1.setBackground(Color.WHITE);
    	 radioC1.setEnabled(false);
         getContentPane().add(radioC1);
         closedmap.put("C1",radioC1);
         
         JRadioButton radioC2 = new JRadioButton();
    	 radioC2.setBounds(601, 182, 21, 14);
    	 radioC2.setBackground(Color.WHITE);
    	 radioC2.setEnabled(false);
         getContentPane().add(radioC2);
         closedmap.put("C2",radioC2);
         
         JRadioButton radioC3 = new JRadioButton();
    	 radioC3.setBounds(601, 242, 21, 14);
    	 radioC3.setBackground(Color.WHITE);
    	 radioC3.setEnabled(false);
         getContentPane().add(radioC3);
         closedmap.put("C3",radioC3);
         
       //----------------------------------------------
         
         JRadioButton radioS1 = new JRadioButton();
    	 radioS1.setBounds(140, 93, 21, 14);
    	 radioS1.setBackground(Color.WHITE);
    	 radioS1.setEnabled(false);
         getContentPane().add(radioS1);
         closedmap.put("S1",radioS1);
         
         JRadioButton radioS2 = new JRadioButton();
    	 radioS2.setBounds(210, 93, 21, 14);
    	 radioS2.setBackground(Color.WHITE);
    	 radioS2.setEnabled(false);
         getContentPane().add(radioS2);
         closedmap.put("S2",radioS2);
         
         JRadioButton radioS3 = new JRadioButton();
    	 radioS3.setBounds(285, 93, 21, 14);
    	 radioS3.setBackground(Color.WHITE);
    	 radioS3.setEnabled(false);
         getContentPane().add(radioS3);
         closedmap.put("S3",radioS3);
         
         JRadioButton radioS4 = new JRadioButton();
    	 radioS4.setBounds(360, 93, 21, 14);
    	 radioS4.setBackground(Color.WHITE);
    	 radioS4.setEnabled(false);
         getContentPane().add(radioS4);
         closedmap.put("S4",radioS4);
         
         JRadioButton radioS5 = new JRadioButton();
    	 radioS5.setBounds(435, 93, 21, 14);
    	 radioS5.setBackground(Color.WHITE);
    	 radioS5.setEnabled(false);
         getContentPane().add(radioS5);
         closedmap.put("S5",radioS5);
         
         JRadioButton radioS6 = new JRadioButton();
    	 radioS6.setBounds(515, 93, 21, 14);
    	 radioS6.setBackground(Color.WHITE);
    	 radioS6.setEnabled(false);
         getContentPane().add(radioS6);
         closedmap.put("S6",radioS6);
         
 //----------------------------------------------
         
         JRadioButton radioE1 = new JRadioButton();
    	 radioE1.setBounds(550, 340, 21, 14);
    	 radioE1.setBackground(Color.WHITE);
    	 radioE1.setEnabled(false);
         getContentPane().add(radioE1);
         closedmap.put("E1",radioE1);
         
         JRadioButton radioE2 = new JRadioButton();
    	 radioE2.setBounds(455, 340, 21, 14);
    	 radioE2.setBackground(Color.WHITE);
    	 radioE2.setEnabled(false);
         getContentPane().add(radioE2);
         closedmap.put("E2",radioE2);
         
         JRadioButton radioE3 = new JRadioButton();
    	 radioE3.setBounds(355, 340, 21, 14);
    	 radioE3.setBackground(Color.WHITE);
    	 radioE3.setEnabled(false);
         getContentPane().add(radioE3);
         closedmap.put("E3",radioE3);
         
         JRadioButton radioE4 = new JRadioButton();
    	 radioE4.setBounds(265, 340, 21, 14);
    	 radioE4.setBackground(Color.WHITE);
    	 radioE4.setEnabled(false);
         getContentPane().add(radioE4);
         closedmap.put("E4",radioE4);
 //----------------------------------------------
         
         JRadioButton radioPU1 = new JRadioButton();
    	 radioPU1.setBounds(473, 365, 21, 14);
    	 radioPU1.setBackground(Color.WHITE);
    	 radioPU1.setEnabled(false);
         getContentPane().add(radioPU1);
         closedmap.put("PU1",radioPU1);
         
         JRadioButton radioPU2 = new JRadioButton();
    	 radioPU2.setBounds(280, 365, 21, 14);
    	 radioPU2.setBackground(Color.WHITE);
    	 radioPU2.setEnabled(false);
         getContentPane().add(radioPU2);
         closedmap.put("PU2",radioPU2);
         
 //----------------------------------------------
         
         JRadioButton radioPL1 = new JRadioButton();
    	 radioPL1.setBounds(195, 58, 21, 14);
    	 radioPL1.setBackground(Color.WHITE);
    	 radioPL1.setEnabled(false);
         getContentPane().add(radioPL1);
         closedmap.put("PL1",radioPL1);
         
         JRadioButton radioPL2 = new JRadioButton();
    	 radioPL2.setBounds(343, 58, 21, 14);
    	 radioPL2.setBackground(Color.WHITE);
    	 radioPL2.setEnabled(false);
         getContentPane().add(radioPL2);
         closedmap.put("PL2",radioPL2);
         
         JRadioButton radioPL3 = new JRadioButton();
    	 radioPL3.setBounds(500, 58, 21, 14);
    	 radioPL3.setBackground(Color.WHITE);
    	 radioPL3.setEnabled(false);
         getContentPane().add(radioPL3);
         closedmap.put("PL3",radioPL3);
    }
}

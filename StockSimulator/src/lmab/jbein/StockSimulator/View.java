package lmab.jbein.StockSimulator;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class View extends JFrame {
	private static final long serialVersionUID = 6839619030110044237L;
	public View(Model model) {
    	super("L-MAB StockSimulator v1.0.0");
    	mModel = model;
    	setSize(1200, 350);
    	setLocation(50, 50);
    	setResizable(false);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
		setVisible(true);
    }
                          
    private void initComponents() {
        mPnlStock 		= new JPanel();
        mLblStockA 		= new JLabel();
        mLblStockB	 	= new JLabel();
        mLblStockC 		= new JLabel();
        mPnlControl 	= new JPanel();
        mLblElements 	= new JLabel();
        mLblXCount 		= new JLabel();
        mLblYCount 		= new JLabel();
        mSldMaxElements = new JSlider();
        mMenuBar 		= new JMenuBar();
        mMenFile 		= new JMenu();
        mMenHelp 		= new JMenu();

        mLblStockA.setText("Stock A: " + mModel.mStockA);
        mLblStockA.setMaximumSize(new Dimension(50, 500));
        mLblStockB.setText("Stock B: " + mModel.mStockB);
        mLblStockB.setMaximumSize(new Dimension(50, 500));
        mLblStockC.setText("Stock C: " + mModel.mStockC);
        mLblStockC.setMaximumSize(new Dimension(50, 500));

        GroupLayout mPnlStockLayout = new GroupLayout(mPnlStock);
        mPnlStock.setLayout(mPnlStockLayout);
        mPnlStockLayout.setHorizontalGroup(
            mPnlStockLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mPnlStockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mPnlStockLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(mLblStockA)
                    .addComponent(mLblStockB)
                    .addComponent(mLblStockC))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mPnlStockLayout.setVerticalGroup(
            mPnlStockLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mPnlStockLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mLblStockA)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mLblStockB)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mLblStockC, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mLblElements.setText("Elements:");
        mLblXCount.setText("X: " + mModel.mXCount);
        mLblYCount.setText("Y: " + mModel.mYCount);

        mSldMaxElements.setForeground(new java.awt.Color(0, 0, 0));
        mSldMaxElements.setMajorTickSpacing(5);
        mSldMaxElements.setMinimum(10);
        mSldMaxElements.setMinorTickSpacing(1);
        mSldMaxElements.setPaintLabels(true);
        mSldMaxElements.setPaintTicks(true);
        mSldMaxElements.setSnapToTicks(true);
        mSldMaxElements.setToolTipText("Set number of elements");
        mSldMaxElements.setValue(55);
        mSldMaxElements.addChangeListener(new SliderListener());

        GroupLayout mPnlLayout = new GroupLayout(mPnlControl);
        mPnlControl.setLayout(mPnlLayout);
        mPnlLayout.setHorizontalGroup(
            mPnlLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mPnlLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(mSldMaxElements, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mPnlLayout.createSequentialGroup()
                        .addGroup(mPnlLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(mLblElements, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addGroup(mPnlLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(mPnlLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(mLblXCount)
                                    .addComponent(mLblYCount))))
                        .addGap(10, 10, 10)
                        .addGap(0, 434, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mPnlLayout.setVerticalGroup(
            mPnlLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mSldMaxElements, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mPnlLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(mPnlLayout.createSequentialGroup()
                        .addComponent(mLblElements)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mLblYCount)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mLblXCount)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        mMISave = new JMenuItem("Save");
		mMISave.addActionListener(new MenuListener());
		mMISave.setMnemonic('S');
		mMISave.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
		mMIQuit = new JMenuItem("Quit");
		mMIQuit.addActionListener(new MenuListener());
		mMIQuit.setMnemonic('Q');
		mMIQuit.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.Event.CTRL_MASK));
		mMIAbout = new JMenuItem("About");
		mMIAbout.addActionListener(new MenuListener());
		mMIAbout.setMnemonic('A');
		mMIAbout.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.Event.CTRL_MASK));
        mMenFile.setText("File");
        mMenFile.add(mMISave);
        mMenFile.add(mMIQuit);
        mMenuBar.add(mMenFile);
        mMenHelp.setText("Help");
        mMenHelp.add(mMIAbout);
        mMenuBar.add(mMenHelp);
        setJMenuBar(mMenuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(mPnlControl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mPnlStock, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mPnlStock, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(mPnlControl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

	class MenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent object) {
			if(object.getSource() == mMIQuit) {
				View.this.dispose();
			}
			else if(object.getSource() == mMISave) {
				final String fileName = "Stock.txt";
		        FileDialog fdgSave = new FileDialog(new JFrame(), "Sequenz speichern unter...", FileDialog.SAVE);
		        fdgSave.setFilenameFilter(new FilenameFilter() {
		            public boolean accept(File dir, String name) {
		                return name.endsWith(".txt");
		            }
		        });
		        fdgSave.setFile(fileName);
		        fdgSave.setVisible(true);
		        String path = fdgSave.getDirectory() + fdgSave.getFile();
				FileWriter writer;
				File file = new File(path);
				
				try {
					writer = new FileWriter(file ,false);
					writer.write("Stock A { " + mModel.mStockA + "}");
				    writer.write(System.getProperty("line.separator"));
					writer.write("Stock B { " + mModel.mStockB + "}");
				    writer.write(System.getProperty("line.separator"));
					writer.write("Stock C { " + mModel.mStockC + "}");
				    writer.write(System.getProperty("line.separator"));
				    writer.flush();
				    writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
		}
	}
	
	class SliderListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			mModel.setElements(new Integer(mSldMaxElements.getValue()));
			mModel.reset();
	        mLblStockA.setText("Stock A: " + mModel.mStockA);
	        mLblStockB.setText("Stock B: " + mModel.mStockB);
	        mLblStockC.setText("Stock C: " + mModel.mStockC);
			mLblXCount.setText("X: " + mModel.mXCount);
			mLblYCount.setText("Y: " + mModel.mYCount);
		}
	}
    
	private Model mModel;
    private JMenuItem mMISave, mMIQuit, mMIAbout;
    private JMenu mMenFile, mMenHelp;
    private JMenuBar mMenuBar;
    private JPanel mPnlControl, mPnlStock;
    private JLabel mLblElements, mLblStockA, mLblStockB, mLblStockC, mLblXCount, mLblYCount;
    private JSlider mSldMaxElements;
}


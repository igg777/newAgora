package system.rdf.ui;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextPane;

import javax.swing.WindowConstants;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import javax.swing.SwingUtilities;


public class UINoConnectionToEnter extends javax.swing.JFrame {
	private JTextPane infoTextPane;
	private JButton btnCancel;
	private JButton btnEnter;
	private GraphEd app;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UINoConnectionToEnter inst = new UINoConnectionToEnter();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public UINoConnectionToEnter() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			{
				infoTextPane = new JTextPane();
				infoTextPane.setText("No connection with database.Press enter to access to the system\nwithout connection or Cancel to exit. If you enter without\nconection you only can model, not save.");
				infoTextPane.setBackground(new java.awt.Color(212,208,200));
				infoTextPane.setEditable(false);
				infoTextPane.setFont(new java.awt.Font("Tahoma",0,12));
			}
			{
				btnEnter = new JButton();
				btnEnter.setText("Enter");
				btnEnter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnEnterActionPerformed(evt);
					}
				});
			}
			{
				btnCancel = new JButton();
				btnCancel.setText("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCancelActionPerformed(evt);
					}
				});
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap(22, 22)
				.add(infoTextPane, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
				.add(17)
				.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
				    .add(GroupLayout.BASELINE, btnEnter, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .add(GroupLayout.BASELINE, btnCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(17, 17));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.add(thisLayout.createParallelGroup()
				    .add(GroupLayout.LEADING, infoTextPane, 0, 329, Short.MAX_VALUE)
				    .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
				        .add(78)
				        .add(btnEnter, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
				        .add(15)
				        .add(btnCancel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
				        .add(86)))
				.addContainerGap());
			pack();
			this.setSize(355, 170);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setLocationRelativeTo(Component c){
		super.setLocationRelativeTo(c);
		this.app = (GraphEd) c;
	}
	
	public JButton getBtnEnter() {
		return btnEnter;
	}
	
	public JButton getBtnCancel() {
		return btnCancel;
	}
	
	private void btnEnterActionPerformed(ActionEvent evt) {
		if(app != null){
			app.setVisible(true);
		}
	}
	
	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
		if(app != null){
			app.exit();
		}
	}

}

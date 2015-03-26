package system.rdf.ui;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;


public class AboutPanel extends javax.swing.JFrame {
	private JLabel developerLabel;
	private JLabel yoLabel;
	private JLabel irlanLabel;
	private JLabel productVersionLabel;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				AboutPanel inst = new AboutPanel();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public AboutPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			this.setTitle("About Creators");
			{
				developerLabel = new JLabel();
				GroupLayout developerLabelLayout = new GroupLayout((JComponent)developerLabel);
				developerLabel.setLayout(null);
				developerLabel.setText("Developers:");
				developerLabelLayout.setVerticalGroup(developerLabelLayout.createSequentialGroup());
				developerLabelLayout.setHorizontalGroup(developerLabelLayout.createSequentialGroup());
			}
			{
				yoLabel = new JLabel();
				yoLabel.setText("Armando Carracedo Velázquez");
				yoLabel.setFont(new java.awt.Font("Tahoma",1,16));
			}
			{
				irlanLabel = new JLabel();
				irlanLabel.setText("Irlán Grangel");
			}
			{
				productVersionLabel = new JLabel();
				productVersionLabel.setText("SCIManager v. 0.1");
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap(22, 22)
				.addComponent(productVersionLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(18)
				.addComponent(developerLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(yoLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(irlanLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(65, 65));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(thisLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(developerLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
				        .addGap(255))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(productVersionLabel, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
				        .addGap(224))
				    .addGroup(thisLayout.createSequentialGroup()
				        .addPreferredGap(developerLabel, yoLabel, LayoutStyle.ComponentPlacement.INDENT)
				        .addGroup(thisLayout.createParallelGroup()
				            .addComponent(yoLabel, GroupLayout.Alignment.LEADING, 0, 313, Short.MAX_VALUE)
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addComponent(irlanLabel, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
				                .addGap(246))))));
			pack();
			this.setSize(297, 165);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
